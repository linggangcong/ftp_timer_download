package util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by SAM on 2018/5/7.
 */
public class FileCheckUtil {    //把验证文件有效性的功能,嵌入ftp下载功能类之中.
    //private static FTPClient ftpClient;
    private static Logger logger= Logger.getLogger(FileCheckUtil.class);

    //验证ftp服务器内数据集文件夹的文件
    public  static  boolean checkFile(String startDate,String endDate ,FTPClient ftpClient){       //  /20180507
        List<String> dateList=TimeUtil.getDateList(startDate,endDate);
        Boolean haveDirectory=false;
        Boolean haveWorngNum=false;
        Boolean hasPosMoney=false;
        Boolean hasPosProduct=false;
        Boolean hasStoreFile=false;
        Boolean hasProductFile=false;
        for(String dataStr : dateList){
            try{

                FTPFile[] ftpFiles= ftpClient.listFiles("/");
                for (int i = 0; i < ftpFiles.length; i++) {
                    String directoryStr=ftpFiles[i].getName();
                    if (directoryStr.equals(dataStr)) {              //当天日期拼成的数据文件夹的字符串。 20180104
                        System.out.println(ftpFiles[i].getName() + ",服务器存在当天文件夹");
                        logger.info(ftpFiles[i].getName() + ",服务器存在当天文件夹");
                        haveDirectory= true;
                        break;
                    }
                }
               /* if(!haveDirectory){
                    System.out.println(dataStr + ",服务器没有当天文件夹");
                    Mail.mailToMe("服务器没有当天的文件夹，下载失败");
                    return false;
                }*/
                String ftpDir="/"+dataStr;
                FTPFile[] ftpDateFiles= ftpClient.listFiles(ftpDir);
                if(ftpDateFiles.length !=15){
                    //System.out.println("number of files is wrong ");
                    logger.warn("number of files is wrong ");
                    haveWorngNum=true;
                }
                for (int i = 0; i < ftpDateFiles.length; i++) {
                    String fileName=ftpDateFiles[i].getName();
                    if(fileName.contains("金额流水")) hasPosMoney=true;
                    if(fileName.contains("实物流水"))hasPosProduct=true;
                    if(fileName.contains("安达便利门店"))hasStoreFile=true;
                    if(fileName.contains("商品档案表")) hasProductFile=true;
                }
                if (hasPosMoney||hasPosProduct||hasStoreFile||hasProductFile){              //当天日期拼成的数据文件夹的字符串。 20180104
                    //System.out.println("金额流水，实物流水,安达便利门店，商品档案表 文件名存在  验证通过 ");
                    logger.info("金额流水，实物流水,安达便利门店，商品档案表 文件名存在  验证通过 ");
                }else{
                    //System.out.println("金额流水，实物流水,安达便利门店，商品档案表;" +dataStr+ "文件夹有文件缺失");
                    logger.error("金额流水，实物流水,安达便利门店，商品档案表;" +dataStr+ "文件夹有文件缺失");
                    return  false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(!haveDirectory||haveWorngNum||!hasPosMoney||!hasPosProduct||!hasStoreFile||!hasProductFile){
            logger.error("文件夹校验未通过");
            return  false;

        }else return true;
    }

}
