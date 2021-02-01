package com.elab.data.dts.recordprocessor;


import com.elab.data.dts.common.*;
import com.elab.data.dts.formats.avro.Record;
import com.elab.data.dts.recordgenerator.OffsetCommitCallBack;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * ETl记录处理器（解析DTS avro格式数据并对其反序列化）
 */
public class EtlRecordProcessor implements Runnable, Closeable {
    private static final Logger log = LoggerFactory.getLogger(EtlRecordProcessor.class);
    private final OffsetCommitCallBack offsetCommitCallBack;
    private volatile Checkpoint commitCheckpoint;
    private WorkThread commitThread;
    private final LinkedBlockingQueue<ConsumerRecord> toProcessRecord; //存储待处理记录
    private final AvroDeserializer fastDeserializer;
    private final Context context;
    private final Map<String, RecordListener> recordListeners = new HashMap<>();
    private volatile boolean existed = false;

    public EtlRecordProcessor(OffsetCommitCallBack offsetCommitCallBack, Context context) {
        this.offsetCommitCallBack = offsetCommitCallBack;
        this.toProcessRecord = new LinkedBlockingQueue<>(512);
        fastDeserializer = new AvroDeserializer();
        this.context = context;
        commitCheckpoint = new Checkpoint(null, -1, -1, "-1");
        commitThread = getCommitThread();
        commitThread.start();
    }

    public boolean offer(long timeOut, TimeUnit timeUnit, ConsumerRecord record) {
        try {
            return toProcessRecord.offer(record, timeOut, timeUnit);
        } catch (Exception e) {
            log.error("EtlRecordProcessor: offer record failed, record[" + record + "], cause " + e.getMessage(), e);
            return false;
        }
    }


    @Override
    public void run() {
        while (!existed) {
            ConsumerRecord<byte[], byte[]> toProcess = null;
            Record record = null;
            int fetchFailedCount = 0;
            try {
                while (null == (toProcess = toProcessRecord.peek()) && !existed) {
                    Util.sleepMS(5);
                    fetchFailedCount++;
                    if (fetchFailedCount % 1000 == 0) {
                        log.info("EtlRecordProcessor: haven't receive records from generator for  5s");
                    }
                }
                if (existed) {
                    return;
                }
                fetchFailedCount = 0;
                final ConsumerRecord<byte[], byte[]> consumerRecord = toProcess;
                record = fastDeserializer.deserialize(consumerRecord.value());
                //log.debug("EtlRecordProcessor: meet [{}] record type", record.getOperation());
                for (RecordListener recordListener : recordListeners.values()) {
                    recordListener.consume(new UserRecord(new TopicPartition(consumerRecord.topic(), consumerRecord.partition()), consumerRecord.offset(), record, new UserCommitCallBack() {
                        @Override
                        public void commit(TopicPartition tp, Record commitRecord, long offset, String metadata) {
                            commitCheckpoint = new Checkpoint(tp, commitRecord.getSourceTimestamp(), offset, metadata);
                        }
                    }));
                }
                toProcessRecord.poll();
            } catch (Exception e) {
                log.error("EtlRecordProcessor: process record failed, raw consumer record [" + toProcess + "], parsed record [" + record + "], cause " + e.getMessage(), e);
                existed = true;
            }
        }
    }

    // 用户定义如何来提交
    private void commit() {
        if (null != offsetCommitCallBack) {
            if (commitCheckpoint.getTopicPartition() != null && commitCheckpoint.getOffset() != -1) {
                log.debug("commit record with checkpoint {}", commitCheckpoint);
                offsetCommitCallBack.commit(commitCheckpoint.getTopicPartition(), commitCheckpoint.getTimeStamp(),
                        commitCheckpoint.getOffset(), commitCheckpoint.getInfo());
            }
        }
    }

    public void registerRecordListener(String name, RecordListener recordListener) {
        Util.require(null != name && null != recordListener, "null value not accepted");
        recordListeners.put(name, recordListener);
    }

    @Override
    public void close() {
        this.existed = true;
        commitThread.stop();
    }

    private WorkThread getCommitThread() {
        WorkThread workThread = new WorkThread(new Runnable() {
            @Override
            public void run() {
                while (!existed) {
                    Util.sleepMS(5000);
                    commit();
                }
            }
        });
        return workThread;
    }

}
