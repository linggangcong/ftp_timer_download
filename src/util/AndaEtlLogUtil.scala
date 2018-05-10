/*
package util

import model.PropertiesValueModel

/**
  * etl 日志管理工具
  */
object AndaEtlLogUtil {
  private final val LOG_TO_SQL_SERVER_ON_OR_OFF = PropertiesValueModel.getMyjLogToSqlServerOpenOrNot()



  def myjEtlLogProduce(dataDay: String, msg: String, logLevel: String, logType: String,
                       logToSqlServerOpen: Boolean = LOG_TO_SQL_SERVER_ON_OR_OFF): Unit = {
    if (logToSqlServerOpen) {   //开启功能。
      val etlLogModel: EtlLogModel = EtlLogModel(dataDay, msg, _banner = "R10003",
        _logType = logType, _logLevel = logLevel)

      val sql = "insert into leo_etl_log(data_day,banner,msg,log_level,timestamp,host_address,hostname,log_type)" +
        s"values(?,?,?,?,?,?,?,?)"
      val params = Array[String](etlLogModel.day, etlLogModel.banner, etlLogModel.msg, etlLogModel.logLevel,
        etlLogModel.timestamp, etlLogModel.IP, etlLogModel.hostname, etlLogModel.logType)
      SqlServerUtil.insert(sql, params)
    }
  }

  def produceEtlMyjSuccessLog(dataDay: String, msg: String): Unit = {
    myjEtlLogProduce(dataDay, msg, LogLevelModel.SUCCESS_LOG, LogTypeModel.ETL_LOG)
  }

  def produceEtlMyjInfoLog(dataDay: String, msg: String): Unit = {
    myjEtlLogProduce(dataDay, msg, LogLevelModel.INFO_LOG, LogTypeModel.ETL_LOG)
  }

  def produceEtlMyjWarnLog(dataDay: String, msg: String): Unit = {
    myjEtlLogProduce(dataDay, msg, LogLevelModel.WARN_LOG, LogTypeModel.ETL_LOG)
  }

  def produceEtlMyjErrorLog(dataDay: String, msg: String): Unit = {
    myjEtlLogProduce(dataDay, msg, LogLevelModel.ERROR_LOG, LogTypeModel.ETL_LOG)
  }
}
*/
