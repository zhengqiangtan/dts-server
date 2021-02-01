package com.elab.data.dts.recordgenerator;

import java.util.Properties;

public interface ConsumerWrapFactory {
    ConsumerWrap getConsumerWrap(Properties properties);

    class KafkaConsumerWrapFactory implements ConsumerWrapFactory {
        @Override
        public ConsumerWrap getConsumerWrap(Properties properties) {
            return new ConsumerWrap.DefaultConsumerWrap(properties);
        }
    }
}
