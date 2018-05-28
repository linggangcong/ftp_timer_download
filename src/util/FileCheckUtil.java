package util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by SAM on 2018/5/7.
 */
public class FileCheckUtil {    //把验证文件有效性的功能,嵌入ftp下载功能类之中.
    //private static FTPClient ftpClient;
    private static Logger logger= Logger.getLogger(FileCheckUtil.class);
    static PropertiesUtil propertiesUtil=new PropertiesUtil();

    //验证ftp服务器内数据集文件夹的文件
    public  static  boolean checkFile(String startDate,String endDate ,FTPClient ftpClient){       //  /20180507
        List<String> dateList=TimeUtil.getDateList(startDate,endDate);
        Boolean haveDirectory=false;
        Boolean haveWorngNum=false;
        Boolean hasPosMoney=false;
        Boolean hasPosProduct=false;
        Boolean hasStoreFile=false;
        Boolean hasProductFile=false;
        //Ftp ftp = new Ftp(propertiesUtil.getProperty("FtpIP"), 21, propertiesUtil.getProperty("username"), propertiesUtil.getProperty("password"));
        for(String dateStr : dateList){
            try{

                ftpClient.setControlEncoding("GBK");   //解决问题。GBK
                FTPFile[] ftpFiles= ftpClient.listFiles("/");   //获取文件列表
                for (int i = 0; i < ftpFiles.length; i++) {
                    String directoryStr=ftpFiles[i].getName();
                    if (directoryStr.equals(dateStr)) {                 //当天日期拼成的数据文件夹的字符串。 20180104
                        //System.out.println(ftpFiles[i].getName() + ",服务器存在当天文件夹");
                        logger.info(ftpFiles[i].getName() + ",服务器存在当天文件夹");
                        //AndaEtlLogUtil.produceEtlAndaInfoLog(dateStr, "服务器存在当天文件夹");
                        haveDirectory= true;
                        break;   //跳出循环
                    }
                }
                if(!haveDirectory){
                    System.out.println(dateStr + ",服务器没有当天文件夹");
                    //AndaEtlLogUtil.produceEtlAndaErrorLog(dateStr, "服务器没有当天文件夹");
                    //Mail.mailToMe("服务器没有当天的文件夹，下载失败");

                }
                String ftpDir="/"+dateStr;
                FTPFile[] ftpDateFiles= ftpClient.listFiles(ftpDir);
                if(ftpDateFiles.length !=15){
                    //System.out.println("number of files is wrong ");
                    logger.warn("number of files is wrong ");
                    //AndaEtlLogUtil.produceEtlAndaErrorLog(dateStr, "当天文件夹内文件总数量不正确");
                    haveWorngNum=true;
                }
                for (int i = 0; i < ftpDateFiles.length; i++) {
                    String fileName=ftpDateFiles[i].getName();
                    if(fileName.contains("金额流水")) hasPosMoney=true;
                    if(fileName.contains("实物流水")){
                        hasPosProduct=true;
                        String filePath=propertiesUtil.getProperty("ftp_default_dir")+dateStr+"/"+fileName;  // //192.168.0.193/ftp/RetailData/Anda/20180510/实物流水2018.05.10.txt
                        checkLineNum( dateStr, filePath, ftpClient);     //读取其中一个ftp文件，计算行数   在linux下，只能通过ftp方式读取windows文件。
                    }
                    if(fileName.contains("安达便利门店"))hasStoreFile=true;
                    if(fileName.contains("商品档案表")) hasProductFile=true;
                }
                if (hasPosMoney||hasPosProduct||hasStoreFile||hasProductFile){              //当天日期拼成的数据文件夹的字符串。 20180104
                    //System.out.println("金额流水，实物流水,安达便利门店，商品档案表 文件名存在  验证通过 ");
                    logger.info("金额流水，实物流水,安达便利门店，商品档案表 文件名存在  验证通过 ");
                    //AndaEtlLogUtil.produceEtlAndaInfoLog(dateStr, "金额流水，实物流水,安达便利门店，商品档案表 文件名存在  验证通过 ");
                }else{
                    //System.out.println("金额流水，实物流水,安达便利门店，商品档案表;" +dataStr+ "文件夹有文件缺失");
                    logger.error("金额流水，实物流水,安达便利门店，商品档案表;" +dateStr+ "文件夹有文件缺失");
                    //AndaEtlLogUtil.produceEtlAndaErrorLog(dateStr, "金额流水，实物流水,安达便利门店，商品档案表 文件夹有重要文件缺失");
                }
                //ftpClient = null;
            }catch (Exception e){
                e.printStackTrace();
            }
            //ftp.ftpLogOut();
        }
        if(!haveDirectory || haveWorngNum||!hasPosMoney||!hasPosProduct||!hasStoreFile||!hasProductFile){
            logger.error("日期段内，文件夹（是否存在文件夹，文件夹内文件数量，重要文件是否存在）校验未通过");
            //AndaEtlLogUtil.produceEtlAndaErrorLog(dataStr, "文件夹校验未通过");
            return  false;
        }else return true;
    }


    public static void checkLineNum(String dataDay ,String filePath ,FTPClient ftpClient ) {      //读取其中一个ftp文件，输出文件行数。new 程序都读不到（之前读193本地。）
        int lineNum= 0;
        InputStream in = null;
        try {
            String fileName= filePath.substring(filePath.indexOf("/"));    //从路径中截取文件名。
            //ftpClient.changeWorkingDirectory(filePath);
            in = ftpClient.retrieveFileStream(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
           // String data = null;
            try {
                while ((br.readLine()) != null) {
                    lineNum++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            logger.error("in为空，不能读取。");
        }
        System.out.println("ftp服务器中该文件总行数为"+lineNum);
        AndaEtlLogUtil.produceEtlAndaInfoLog(dataDay, filePath + ":ftp服务器中该文件总行数为"+lineNum);     //也是日期
    }


}
