package com.elab.data.dts.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dts数据订阅属性
 *
 * @author ： liukx
 * @time ： 2020/9/22 - 17:03
 */
@ConfigurationProperties(prefix = "spring.dts")
public class DTSProperties {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 消费组id [dts->数据消费列表->消费组ID]
     */
    private String sidName;
    /**
     * 消费组名称[dts->数据消费列表->消费组名称]
     */
    private String groupName;
    /**
     * kafka topic配置 [dts->订阅配置->基本信息->订阅Topic]
     */
    private String kafkaTopic;
    /**
     * kafka 连接地址 [dts->订阅配置->网络->公私网]
     */
    private String kafkaBrokerUrlName;
    /**
     * 启动位点 : 这里表示消费的起点,如果是第一次启动则有效.
     */
    private String initialCheckpointName;
    /**
     * 如果在启动时强制使用配置检查点。在检查点重置
     */
    private String useConfigCheckpointName = "false";
    /**
     * 容灾能力 assign 表示单机 subscribe表示主备
     */
    private String subscribeModeName;
    /**
     * 批量拉取的最大数量
     */
    private Integer maxPollRecords = 500;

    /**
     * 拉取的超时时间
     */
    private Integer maxPollIntervalMs = 30000;
    /**
     * 包括的数据库信息
     */
    private Map<String, List<String>> includeDataInfo;
    /**
     * 排除的数据库信息
     */
    private Map<String, List<String>> excludeDataInfo;

    /**
     * 排除表的改变字段信息,注意的是这里使用的是List.containsAll方法,也就是说只过滤containsAll方法返回为true的情况
     */
    private Map<String, List<String>> excludeTableChangeField;

    /**
     * 表名,分区号。将特定的表名落入特定分区
     */
    private Map<String, Integer> tablePartitionMap = new HashMap<>();

    public Map<String, Integer> getTablePartitionMap() {
        return tablePartitionMap;
    }

    public void setTablePartitionMap(Map<String, Integer> tablePartitionMap) {
        this.tablePartitionMap = tablePartitionMap;
    }

    public Map<String, List<String>> getExcludeTableChangeField() {
        return excludeTableChangeField;
    }

    public void setExcludeTableChangeField(Map<String, List<String>> excludeTableChangeField) {
        this.excludeTableChangeField = excludeTableChangeField;
    }

    public Integer getMaxPollIntervalMs() {
        return maxPollIntervalMs;
    }

    public void setMaxPollIntervalMs(Integer maxPollIntervalMs) {
        this.maxPollIntervalMs = maxPollIntervalMs;
    }

    public String getUsername() {
        return username;
    }

    public Integer getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(Integer maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSidName() {
        return sidName;
    }

    public void setSidName(String sidName) {
        this.sidName = sidName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getKafkaBrokerUrlName() {
        return kafkaBrokerUrlName;
    }

    public void setKafkaBrokerUrlName(String kafkaBrokerUrlName) {
        this.kafkaBrokerUrlName = kafkaBrokerUrlName;
    }

    public String getInitialCheckpointName() {
        return initialCheckpointName;
    }

    public void setInitialCheckpointName(String initialCheckpointName) {
        this.initialCheckpointName = initialCheckpointName;
    }

    public String getUseConfigCheckpointName() {
        return useConfigCheckpointName;
    }

    public void setUseConfigCheckpointName(String useConfigCheckpointName) {
        this.useConfigCheckpointName = useConfigCheckpointName;
    }

    public String getSubscribeModeName() {
        return subscribeModeName;
    }

    public void setSubscribeModeName(String subscribeModeName) {
        this.subscribeModeName = subscribeModeName;
    }

    public Map<String, List<String>> getIncludeDataInfo() {
        return includeDataInfo;
    }

    public void setIncludeDataInfo(Map<String, List<String>> includeDataInfo) {
        this.includeDataInfo = includeDataInfo;
    }

    public Map<String, List<String>> getExcludeDataInfo() {
        return excludeDataInfo;
    }

    public void setExcludeDataInfo(Map<String, List<String>> excludeDataInfo) {
        this.excludeDataInfo = excludeDataInfo;
    }
}
