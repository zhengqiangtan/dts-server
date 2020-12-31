package com.elab.data.dts.utils;


import org.junit.Assert;
import org.junit.Test;

/**
 * @Module 数据解析工具
 * @Description 解析所有表名
 * @Author Administrator
 * @Date 2020/10/20 14:45
 */
public class DataUtils {

    @Test
    public void testTable() {
        String binLog = "ALTER TABLE `marketing_db_prod`.`banner` \n" +
                "MODIFY COLUMN `title` varchar(53) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题' AFTER `id`";
        parseTableName(binLog);
    }

    @Test
    public void testIndex() {
        String indexSQL = "ALTER TABLE `c_report_log` \n" +
                "ADD INDEX `idx_pub`(`brand_id`, `house_id`, `user_id`, `report_id`, `report_time`, `status`) USING BTREE";
        parseTableName(indexSQL);
    }

    @Test
    public void testAlterTable() {
        String sql = "alter table customer_material modify intention_layout varchar(100) COMMENT '意向房型'";
        parseTableName(sql);
    }

    @Test
    public void test() {
        String sql = "CREATE TABLE `c_user_extend`  (\\r\\n  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',\\r\\n  `brand_id` int(11) NOT NULL COMMENT '集团id',\\r\\n  `house_id` int(11) NOT NULL COMMENT '项目id',\\r\\n  `user_id` int(11) NOT NULL COMMENT '用户id',\\r\\n  `dic_id` int(11) NOT NULL COMMENT '字典表id',\\r\\n  `dic_data_id` int(11) NOT NULL COMMENT '字典数据表id',\\r\\n  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',\\r\\n  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 1 表示正常 -1 表示失效',\\r\\n  `creator` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',\\r\\n  `created` datetime NULL DEFAULT NULL COMMENT '创建时间',\\r\\n  `updator` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',\\r\\n  `updated` datetime NULL DEFAULT NULL COMMENT '更新时间',\\r\\n  PRIMARY KEY (`id`) USING BTREE\\r\\n) ENGINE = InnoDB AUTO_INCREMENT = 254 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact";
        parseTableName(sql);
    }

    @Test
    public void testDrop() {
        String sql = "DROP TABLE IF EXISTS `dm_dictionary` /* generated by server */";
        parseTableName(sql);
    }

    @Test
    public void testDropIndex() {
        String sql = "ALTER TABLE `marketing_db`.`t_test` \n" +
                "DROP INDEX `ps`";
        parseTableName(sql);
    }

    @Test
    public void testTruncateTable() {
        String sql = "truncate table app_dm_data_center_mrk_data_qr_code_offline_d";
        parseTableName(sql);
    }

    @Test
    public void testTableName() {
//        String sql = "ALTER TABLE `t_dm_deal_contract` MODIFY COLUMN `params` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口参数' AFTER `stake_holder_mobile`";
//        String sql = "ALTER TABLE t_transaction_information MODIFY COLUMN `name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名' AFTER `mobile`";
        String sql = "ALTER TABLE `t_transaction_information`\n" +
                "MODIFY COLUMN `name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名' AFTER `mobile`;\n";
        parseTableName(sql);
    }

    @Test
    public void testTableName2() {
        String sql = "alter table c_report_log  MODIFY COLUMN `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',\n" +
                "MODIFY COLUMN `report_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '报备添加的备注'";
        parseTableName(sql);
    }


    @Test
    public void testCreateTable() {
        String sql = "CREATE TABLE IF NOT EXISTS app_dm_data_center_mrk_data_qr_code_offline_d\n" +
                "(\n" +
                "   id                INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增ID',\n" +
                "   report_d          DATETIME                        COMMENT '统计日期',   -- 参与时间:年月日\n" +
                "   brand_id          INT(4)                          COMMENT '归属集团',\n" +
                "   house_id          INT(11)                         COMMENT '归属项目',\n" +
                "   house_name        VARCHAR(50)                     COMMENT '项目名称',\n" +
                "   terminal          INT(2)                          COMMENT '平台ID',\n" +
                "\t\t\t\t\t\t\t\t\t                 \n" +
                "   qr_code_id        INT(11)                         COMMENT '二维码ID',   -- 对应qr_code 表中的  id 主键  和 雪莹确认\n" +
                "   qr_code_name      VARCHAR(100)                    COMMENT '二维码名称',   -- 对应qr_code 表中的  instruction  和 殳佳确认\n" +
                "   mobile            VARCHAR(20)                     COMMENT '手机号', \n" +
                "   qr_code_url       VARCHAR(500)                    COMMENT '二维码链接', \n" +
                "   created           DATETIME                        COMMENT '活动创建时间',\n" +
                "   creator           VARCHAR(30)                     COMMENT '运营创建人',\n" +
                "   view_pv           INT(11)                         COMMENT '浏览量',\n" +
                "   view_uv           INT(11)                         COMMENT '浏览人数',\n" +
                "   new_cust_num      INT(11)                         COMMENT '新增获客',\n" +
                "   get_call_num      INT(11)                         COMMENT '着陆页面获电数',\n" +
                "   new_get_call      INT(11)                         COMMENT '着陆页面新增获电',\n" +
                "   \n" +
                "   type              INT(2)                          COMMENT '数据类型：用于区分数据是实时的还是离线的(0-代表实时，1-代表离线)',\n" +
                "   status            INT(2)                          COMMENT '状态：1  有效,-1  无效',  \n" +
                "   insert_time       DATETIME                        COMMENT '入库时间',\n" +
                "   PRIMARY KEY (id)\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='二维码运营活动数据统计表'";
        parseTableName(sql);
    }

    private void parseTableName(String sql) {
        String tableName = DataParseUtils.getDDLTableName(sql);
        System.out.println(tableName);
        Assert.assertTrue(tableName != null);
    }

}
