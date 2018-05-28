package util;

import model.EtlLogModel;
import model.LogLevelModel;
import model.LogTypeModel;
import model.PropertiesValueModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by SAM on 2018/5/9.
 */
public class AndaEtlLogUtil {

    private static final boolean LOG_TO_SQL_SERVER_ON_OR_OFF = PropertiesValueModel.getMyjLogToSqlServerOpenOrNot();

    public static  void andaEtlLogProduce( String dataDay ,String msg , String logLevel , String logType  ){
        boolean logToSqlServerOpen = LOG_TO_SQL_SERVER_ON_OR_OFF;
        if (logToSqlServerOpen) {   //开启功能。
            EtlLogModel etlLogModel= new EtlLogModel(dataDay, msg, "R10003", logType,logLevel);  //一个log对  logLevel = "info";  5个

            String sql = "insert into leo_etl_log(data_day,banner,msg,log_level,timestamp,host_address,hostname,log_type,worker) values(?,?,?,?,?,?,?,?,?)";
            List<String> params= Arrays.asList(etlLogModel.getDay(), etlLogModel.getBanner(), etlLogModel.getMsg(),
                    etlLogModel.getLogLevel(), etlLogModel.getTimestamp(), etlLogModel.getIP(), etlLogModel.getHostname(), etlLogModel.getLogType() ,etlLogModel.getWorker());
            SqlServerUtil.insert(sql, params);
        }
    }

    /**
     * anda   etl处理过程中产生success级别的日志
     *
     * @param dataDay
     * @param msg
     */
    public static void produceEtlAndaSuccessLog(String dataDay, String msg) {
        andaEtlLogProduce(dataDay, msg, LogLevelModel.getSUCCESS_LOG(), LogTypeModel.getETL_LOG());  //4个
    }

    public static void produceEtlAndaInfoLog(String dataDay, String msg) {
        andaEtlLogProduce(dataDay, msg, LogLevelModel.getINFO_LOG(), LogTypeModel.getETL_LOG());
    }

    public static void produceEtlAndaWarnLog(String dataDay, String msg){
        andaEtlLogProduce(dataDay, msg, LogLevelModel.getWARN_LOG(), LogTypeModel.getETL_LOG());
    }

    public static  void produceEtlAndaErrorLog(String dataDay, String msg){
        andaEtlLogProduce(dataDay, msg, LogLevelModel.getERROR_LOG(), LogTypeModel.getETL_LOG());
    }
}
