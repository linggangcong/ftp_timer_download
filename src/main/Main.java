package main;


import download.FtpDownload;
import org.apache.log4j.Logger;
import util.HdfsUtil;
import util.SqlServerUtil;
import util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public  class Main {    //从日志获取日期。
    static util.PropertiesUtil propertiesUtil = new util.PropertiesUtil();
    private static Logger logger = Logger.getLogger(Main.class);
    // static Properties properties = new Properties();   //全局变量
    public static void main(String[] args) {   //拉取式操作，从日志获取最近日期。
        System.out.println("started to download  myj pos data daily and new store&&product...");
        logger.info("started to download  myj pos data daily and new store&&product...");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        cal.add(Calendar.DATE, -1);
        String yesterday =sdf.format(cal.getTime());


        //最近的错误数据天
        String sqlFindFailure="select data_day from dbo.leo_etl_log where banner='R10003' and log_type='error'";
        List<String> LatestFailureDayList = SqlServerUtil.getLatestDay( sqlFindFailure,"data_day");   //确定只有一天是错误。
        String latestFailureDay= TimeUtil.getLatestDay(LatestFailureDayList);
        //没有错误天，找最近的成功天。

       //缺失某一天，不可能。 某一天数据缺失，报错。并不执行后面的天数。
        String sqlFindSuccess="select data_day from dbo.leo_etl_log where banner='R10003' and log_type='success'";   //最近的成功日期，之后的一天。
        List<String> LatestSuccessDayList =SqlServerUtil.getLatestDay(sqlFindSuccess,"data_day");
        String latestSuccessDay= TimeUtil.getLatestDay(LatestSuccessDayList);   //20180505

        //日期格式20180510
        if(latestFailureDay!=null){
            FtpDownload.startDownload(latestFailureDay, yesterday);   //增加日期选择。
                //FtpDownload.startDownload(latestFailureDay, "20180511");
            HdfsUtil.moveToHdfs(latestFailureDay, yesterday);
            //newStore.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),storeOutPath);
            //newProduct.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),productOutPath);
        }else if(latestSuccessDay!=null){
            Date latestSuccessDate=null;
            try{
                latestSuccessDate = sdf.parse(latestSuccessDay );
            }catch (Exception e){
                e.printStackTrace();
            }
            cal.setTime(latestSuccessDate);
            cal.add(Calendar.DAY_OF_MONTH, +1);
            Date Plus1Date =cal.getTime();
            String LatestSuccessDayPlus1=sdf.format(Plus1Date);
            FtpDownload.startDownload(LatestSuccessDayPlus1, yesterday);   //增加日期选择。
            HdfsUtil.moveToHdfs(LatestSuccessDayPlus1, yesterday);
            //newStore.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),storeOutPath);
            //newProduct.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),productOutPath);
        }else{
            System.exit(1);
        }

    }
}

    /*public static void main(String[] args) {   //周末判断操作，定时依靠azkaban。
        System.out.println("started to download  myj pos data daily and new store&&product...");
        logger.info("started to download  myj pos data daily and new store&&product...");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //String basicDirectory = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime()); //今天
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime()); //昨天
        cal.add(Calendar.DATE, -1);
        String daybeforeYesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime()); //前天
        cal.add(Calendar.DATE, -1);
        String today3Minus = new SimpleDateFormat("yyyyMMdd").format(cal.getTime()); //
        System.out.println(yesterday);
        System.out.println(daybeforeYesterday);
        cal.add(Calendar.DATE, +3);      //复原日历日期到今天。

        Boolean isSunday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        Boolean isSaturday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
        if (isSunday || isSaturday) {
            logger.info("今天是周末，不下载");
            System.exit(1);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            logger.info("今天是周一，下载三天的数据");
            FtpDownload.startDownload(today3Minus, yesterday);   //增加日期选择。
            //newStore.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),storeOutPath);
            //newProduct.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),productOutPath);
            HdfsUtil.moveToHdfs(today3Minus, yesterday);
        } else {
            logger.info("今天不是周末，也不是周一，正常检验昨天的数据文件夹 ，产生昨天的新商品和新店铺。下载昨天数据到hdfs");    //2个字段
            //FtpDownload.startDownload( "20180423" ,"20180425");
            FtpDownload.startDownload(yesterday, yesterday);    //包含文件验证功能，下载之前。
            //newStore.startWork(daybeforeYesterday,yesterday,propertiesUtil.getProperty("new_input_basicPath"),storeOutPath);
            //newProduct.startWork(daybeforeYesterday,yesterday,propertiesUtil.getProperty("new_input_basicPath"),productOutPath);
            HdfsUtil.moveToHdfs(yesterday, yesterday);
        }
    }
}
*/

/*public  class Main {     //定时任务操作
     static  util.PropertiesUtil propertiesUtil=new util.PropertiesUtil();
     private static Logger logger= Logger.getLogger(Main.class);
    // static Properties properties = new Properties();   //全局变量

    public static void main(String[] args) {
        System.out.println("started to download  myj pos data daily and new store&&product...");
        logger.info("started to download  myj pos data daily and new store&&product...");
        timer4();
    }
        public static void timer4() {
            Calendar   cal   =   Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(propertiesUtil.getProperty("Calendar.HOUR_OF_DAY")));  // 定时
            cal.set(Calendar.MINUTE, Integer.parseInt(propertiesUtil.getProperty("Calendar.MINUTE")));
            cal.set(Calendar.SECOND, 0);

            Date time = cal.getTime();
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    //执行的内容
                    NewProduct newProduct=new NewProduct();
                    NewStore newStore=new NewStore();
                    //String basicInputPath="C:\\Users\\SAM\\Desktop\\test\\本地虚拟机测试\\新产品代码测试\\";   //店铺测试版 ，输入地址
                    //String basicInputPath="\\\\Drfssh001\\ftp\\RetailData\\Anda\\";     //  \\\\192.168.0.193E:\FTP\RetailData\Anda\   \\Drfssh001\ftp\RetailData\Anda
                    //String basicOutputPath="C:\\Users\\SAM\\Desktop\\test\\本地虚拟机测试\\新产品代码测试\\";
                    //String basicOutputPath="\\\\Drfssh001\\DataReal_Data\\DAILY";
                    *//*String productOutPath=basicOutputPath+"R10003_ANDA_new_product.csv";
                    String storeOutPath=basicOutputPath+"R10003_ANDA_new_store.csv";*//*
                    String productOutPath= propertiesUtil.getProperty("new_output_basicPath_product")+propertiesUtil.getProperty("new_product_default_name");                                        //basicOutputPath+"\\NEWITEM\\"+"R10003_ANDA_new_product.csv";
                    String storeOutPath= propertiesUtil.getProperty("new_output_basicPath_store")+propertiesUtil.getProperty("new_store_default_name");                                           //basicOutputPath+"\\NEWSTORE\\"+"R10003_ANDA_new_store.csv";

                    Calendar cal=Calendar.getInstance();
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
                    //String basicDirectory = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime()); //今天
                    cal.add(Calendar.DATE,   -1);
                    String yesterday = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime()); //昨天
                    cal.add(Calendar.DATE,   -1);
                    String daybeforeYesterday = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime()); //前天
                    cal.add(Calendar.DATE,   -1);
                    String today3Minus = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime()); //
                    System.out.println(yesterday);
                    System.out.println(daybeforeYesterday);
                    cal.add(Calendar.DATE, +3);      //复原日历日期到今天。

                    Boolean isSunday =cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
                    Boolean isSaturday =cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
                    if (isSunday||isSaturday) {
                        logger.info("今天是周末，不下载");
                        //System.exit(1);
                    } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        logger.info("今天是周一，下载三天的数据");
                        FtpDownload.startDownload(today3Minus,yesterday );   //增加日期选择。
                        //newStore.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),storeOutPath);
                        //newProduct.startWork(today3Minus,yesterday,propertiesUtil.getProperty("new_input_basicPath"),productOutPath);
                        HdfsUtil.moveToHdfs(today3Minus,yesterday);
                    } else {
                        logger.info("今天不是周末，也不是周一，正常检验昨天的数据文件夹 ，产生昨天的新商品和新店铺。下载昨天数据到hdfs");    //2个字段
                        //FtpDownload.startDownload( "20180423" ,"20180425");
                        FtpDownload.startDownload( yesterday ,yesterday);    //包含文件验证功能，下载之前。
                        //newStore.startWork(daybeforeYesterday,yesterday,propertiesUtil.getProperty("new_input_basicPath"),storeOutPath);
                        //newProduct.startWork(daybeforeYesterday,yesterday,propertiesUtil.getProperty("new_input_basicPath"),productOutPath);
                        HdfsUtil.moveToHdfs(yesterday ,yesterday);
                    }
                }
            }, time, 1000*60*60*24 );   //这里设定将延时每天固定执行  部署：1000*60*60*24
        }
}*/

