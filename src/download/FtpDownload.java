package download;

import ftp.Ftp;
import org.apache.log4j.Logger;
import util.FileCheckUtil;
import util.PropertiesUtil;
import util.TimeUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by SAM on 2018/4/28.
 */
public class FtpDownload {
    static PropertiesUtil propertiesUtil=new PropertiesUtil();
    private static Logger logger= Logger.getLogger(FtpDownload.class);
    //执行的内容
    public static  void startDownload(String startDate ,String endDate ) {
        Ftp ftp = new Ftp(propertiesUtil.getProperty("FtpIP"), 21, propertiesUtil.getProperty("username"), propertiesUtil.getProperty("password"));
        logger.info("开始登录ftp...");
       // AndaEtlLogUtil.produceEtlAndaInfoLog("2018-05-11", "开始登录ftp...");
        ftp.ftpLogin();
        FileCheckUtil.checkFile(startDate , endDate , ftp.getFtpClient());
        ftp.ftpLogin();
        //下载文件夹
        try {
            // 字符串--date --字符串
           List<String> dataStrList= TimeUtil.getDateList(startDate ,endDate);   //20180507
            for(String dataStr : dataStrList){      //批量下载文件夹
                String ftpDirectoryFinal ="/" +dataStr;
                ftp.downLoadDirectory(propertiesUtil.getProperty("local_daily_dir"), ftpDirectoryFinal);  //   /home/etl/samgao/anda_daily/   和 /20180401   ftp下载文件夹
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ftp.ftpLogOut();
    }

}
