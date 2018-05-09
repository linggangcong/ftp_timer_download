package main;

import download.FtpDownload;
import newstoreanditem.NewProduct;
import newstoreanditem.NewStore;
import util.HdfsUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public  class Main {
     static  util.PropertiesUtil propertiesUtil=new util.PropertiesUtil();
    // static Properties properties = new Properties();   //全局变量

    public static void main(String[] args) {
        System.out.println("started to download  myj pos data daily and new store&&product...");
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
                    /*String productOutPath=basicOutputPath+"R10003_ANDA_new_product.csv";
                    String storeOutPath=basicOutputPath+"R10003_ANDA_new_store.csv";*/
                    String productOutPath= propertiesUtil.getProperty("new_output_basicPath_product")+propertiesUtil.getProperty("new_product_default_name");                                       //basicOutputPath+"\\NEWITEM\\"+"R10003_ANDA_new_product.csv";
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
                        System.out.println("今天是周末，不下载");
                        //System.exit(1);
                    } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        System.out.println("今天是周一，下载三天的数据");
                        FtpDownload.startDownload(today3Minus,yesterday );   //增加日期选择。
                        //newStore.startWork(today3Minus,yesterday,basicInputPath,storeOutPath);
                        //newProduct.startWork(today3Minus,yesterday,basicInputPath,productOutPath);
                        HdfsUtil.moveToHdfs(today3Minus,yesterday);
                    } else {
                        System.out.println("今天不是周末，也不是周一，正常检验昨天的数据文件夹 ，产生昨天的新商品和新店铺。下载昨天数据到hdfs");
                        //FtpDownload.startDownload( "20180423" ,"20180425");
                        //FileCheckUtil.checkFile("/" );    //ftp默认路径

                        //newStore.startWork(daybeforeYesterday,yesterday,propertiesUtil.getProperty("new_input_basicPath"),storeOutPath);

                        //newProduct.startWork(daybeforeYesterday,yesterday,propertiesUtil.getProperty("new_input_basicPath"),productOutPath);

                        FtpDownload.startDownload( yesterday ,yesterday);

                        //HdfsUtil.moveToHdfs(yesterday ,yesterday);
                    }
                }
            }, time, 1000*60*60*24 );   //这里设定将延时每天固定执行  部署：1000*60*60*24
        }
}

