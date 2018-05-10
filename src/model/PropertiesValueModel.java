package model;

import util.PropertiesUtil;

/**
 * Created by SAM on 2018/5/10.
 */
public class PropertiesValueModel {
    static PropertiesUtil propertiesUtil=new PropertiesUtil();
    /**
     * 获取anda etl流程中日志是否输出到sql server。
     * 如果配置错误，默认返回false
     *
     * @return true or false
     */
    public static boolean  getMyjLogToSqlServerOpenOrNot(){
        boolean switchh =Boolean.valueOf(propertiesUtil.getProperty("anda.etl.log.to.sql_server")) ;
        return  switchh;
    }
}
