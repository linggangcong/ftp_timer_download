package download;

import ftp.Ftp;
import util.PropertiesUtil;
import util.TimeUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by SAM on 2018/4/28.
 */
public class FtpDownload {
    static PropertiesUtil propertiesUtil=new PropertiesUtil();
    //执行的内容
    public static  void startDownload(String startDate ,String endDate ) {
        Ftp ftp = new Ftp(propertiesUtil.getProperty("FtpIP"), 21, propertiesUtil.getProperty("username"), propertiesUtil.getProperty("password"));
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
