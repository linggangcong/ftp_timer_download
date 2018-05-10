package model;

/**
 * Created by SAM on 2018/5/10.
 */
public class LogLevelModel {
    final static String   INFO_LOG = "info";
    final static String SUCCESS_LOG = "success";
    final static String ERROR_LOG = "error";
    final static String WARN_LOG = "warn";

    public static  String getINFO_LOG() {
        return INFO_LOG;
    }

    public static String getSUCCESS_LOG() {
        return SUCCESS_LOG;
    }

    public static  String getERROR_LOG() {
        return ERROR_LOG;
    }

    public static String getWARN_LOG() {
        return WARN_LOG;
    }
}
