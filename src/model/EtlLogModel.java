package model;


import util.NetUtil;
import util.TimeUtil;

/**
 * Created by SAM on 2018/5/10.
 */
public class EtlLogModel {
    String day;
    String msg;
    String logType;
    String banner;
    String logLevel;
    String timestamp=TimeUtil.getNowDateTimeStamp();
    String  IP=NetUtil.getHostAddress();
    String  hostname=NetUtil.getHostName();

    public EtlLogModel(String day ,String msg ,String banner , String logLevel,String logType) {
        this.day =day;
        this.banner =banner;
        this.msg = msg;
        this.logType =logType;
        logLevel = "info";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
