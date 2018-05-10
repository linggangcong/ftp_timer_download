package model;

/**
 * Created by SAM on 2018/5/10.
 */
public class LogTypeModel {
    //etl日志
    final static String ETL_LOG = "1";
    //ftp下载数据日志
    final static String  FTP_LOG = "2";
    //统计job的日志
    final  static String STATISTICS_LOG = "3";
    //计算job的日志
    final static  String  COMPUTE_LOG = "4";

    public static String getETL_LOG() {
        return ETL_LOG;
    }

    public static String getFTP_LOG() {
        return FTP_LOG;
    }

    public static String getSTATISTICS_LOG() {
        return STATISTICS_LOG;
    }

    public static String getCOMPUTE_LOG() {
        return COMPUTE_LOG;
    }
}
