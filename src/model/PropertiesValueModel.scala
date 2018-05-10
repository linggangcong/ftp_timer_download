/*package model

import util.PropertiesUtil


/**
  * properties文件中键值对，值得数据模型类
  */
public class PropertiesValueModel {
  static PropertiesUtil propertiesUtil=new PropertiesUtil();
  /**
    * 获取美宜佳etl流程中日志是否输出到sql server。
    * 如果配置错误，默认返回false
    *
    * @return true or false
    */
  def getMyjLogToSqlServerOpenOrNot(): Boolean = {
    try {
      val switch = propertiesUtil.("anda.etl.log.to.sql_server").toBoolean
      switch
    }
  }
}*/
