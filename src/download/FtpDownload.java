package download;

import ftp.Ftp;
import main.Main;
import org.apache.log4j.Logger;
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
        MyjEtlLogUtil.produceEtlMyjErrorLog(dataRealDate, "美宜佳流水数据目录：${flowDataPath} 不存在，程序退出！");
        ftp.ftpLogin();

        //连接登录之后，验证ftp文件有效性。
        Ftp.checkDirectory(startDate,endDate ,ftp.getFtpClient());

        //下载文件夹
        try {
            //ftp.downLoadDirectory("C:\\MYJ_pos_data", directory);
            //ftp.downLoadDirectory("C:\\MYJ_pos_data", "/20180401");
            // 字符串--date --字符串
           List<String> dataStrList= TimeUtil.getDateList(startDate ,endDate);   //20180507
            for(String dataStr : dataStrList){
                String ftpDirectoryFinal ="/" +dataStr;
                ftp.downLoadDirectory(propertiesUtil.getProperty("local_daily_dir"), ftpDirectoryFinal);  //   /home/etl/samgao/anda_daily/   和 /20180401
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ftp.ftpLogOut();
    }

}
