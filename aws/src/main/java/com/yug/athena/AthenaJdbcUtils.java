package com.yug.athena;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Properties;

/**
 * @author gy
 * @version 1.0
 * @description AWS Athena JDBC Utils
 * @date 2023/1/6 14:40
 */
@Slf4j
public class AthenaJdbcUtils {

    public static void query() throws Exception {
        Connection connection = buildAthenaConnection();
        Statement ps = connection.createStatement();
        ResultSet res = ps.executeQuery("SELECT * FROM \"fisk\".\"parquet_ods_di_asddd\" limit 10;");
        while (res.next()) {
            String id = res.getString(1),
                    timeStamp = res.getString(2),
                    fiCreatetime = res.getString(3),
                    fiUpdatetime = res.getString(4),
                    fidataBatchCode = res.getString(5),
                    diAsdddkey = res.getString(6);
            System.out.printf("%s, %s, %s, %s, %s, %s%n", id, timeStamp, fiCreatetime, fiUpdatetime, fidataBatchCode, diAsdddkey);
        }
    }

    public static void createTable() throws Exception {
        Connection connection = buildAthenaConnection();
        Statement ps = connection.createStatement();
        boolean res = ps.execute("CREATE EXTERNAL TABLE `fisk.parquet_ods_di_asdddd`( " +
                "  `id` int, " +
                "  `time_stamp` varchar(50), " +
                "  `fi_createtime` varchar(50), " +
                "  `fi_updatetime` varchar(50), " +
                "  `fidata_batch_code` varchar(50), " +
                "  `di_asdddkey` varchar(50)) " +
                " ROW FORMAT SERDE " +
                "  'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe' " +
                "STORED AS INPUTFORMAT " +
                "  'org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat' " +
                "OUTPUTFORMAT " +
                "  'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'" +
                "LOCATION " +
                "  's3://fiskbucket/fisk/nifi/parquet_ods_di_asddd' " +
                "TBLPROPERTIES ( " +
                "  'classification'='parquet', " +
                "  'transient_lastDdlTime'='1670922608')");
        System.out.printf("创建 Athena 表结束");
    }


    private static Connection buildAthenaConnection(){
        Properties properties = new Properties();
        properties.put("User", "AKIAV7Q2QC55HJBRGMYK");
        properties.put("Password", "0Y6Nc1KlDTRniU2GNVuMISzgQtqGSertL4hpmmml");
        properties.put("S3OutputLocation", "s3://fiskbucket/fisk/nifi/");
        properties.put("Workgroup", "primary");
        properties.put("MaxQueryExecutionPollingInterval", "20");

        try {
            Class.forName("com.simba.athena.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("加载 Athena JDBC Driver 报错.", e);
        }
        try {
            return DriverManager.getConnection(
                    "jdbc:awsathena://AwsRegion=cn-north-1;EndpointOverride=athena.cn-north-1.amazonaws.com.cn:443;",
                    properties);
        } catch (SQLException e) {
            log.error("创建 Athena JDBC 连接报错.", e);
            return null;
        }
    }
}
