package com.elab.data.dts.utils;


import org.junit.Test;

/**
 * @Module TODO
 * @Description TODO
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

    private void parseTableName(String sql) {
        String tableName = DataParseUtils.getDDLTableName(sql);
        System.out.println(tableName);

    }

}
