package ftp;
/**
 * Created by SAM on 2018/1/4.
 */

import mail.Mail;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import util.FileCheckUtil;
import util.PropertiesUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;

public class Ftp {
    private static Logger logger= Logger.getLogger(Ftp.class);
    private FTPClient ftpClient;
    private String strIp;
    private int intPort;
    private String user;
    private String password;
    static PropertiesUtil propertiesUtil=new PropertiesUtil();


    public Ftp(String strIp, int intPort, String user, String Password) {
        this.strIp = strIp;
        this.intPort = intPort;
        this.user = user;
        this.password = Password;
        this.ftpClient = new FTPClient();
    }

    public boolean ftpLogin() {
        boolean isLogin = false;
        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());

        this.ftpClient.setControlEncoding("UTF-8");   //乱码解决
        this.ftpClient.configure(ftpClientConfig);
        try {
            if (this.intPort > 0) {
                this.ftpClient.connect(this.strIp, this.intPort);
            } else {
                this.ftpClient.connect(this.strIp);
            }
            // FTP服务器连接回答
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                //logger.error("登录FTP服务失败！");
                System.out.println("登录FTP服务失败！"); //连接确认。
                logger.info("登录FTP服务失败！");
                return isLogin;
            }

            this.ftpClient.login(this.user, this.password);
            // 设置传输协议
            this.ftpClient.enterLocalPassiveMode();
            this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //logger.info("恭喜" + this.user + "成功登陆FTP服务器");
            System.out.println("恭喜" + this.user + "成功登陆FTP服务器");
            logger.info("恭喜" + this.user + "成功登陆FTP服务器");
            isLogin = true;
        } catch (Exception e) {
            e.printStackTrace();
           // logger.error(this.user + "登录FTP服务失败！" + e.getMessage());
        }
        this.ftpClient.setBufferSize(1024 * 2);
        this.ftpClient.setDataTimeout(30* 1000);
        return isLogin;
    }

    /**
     * @退出关闭服务器链接
     * */
    public void ftpLogOut() {
        if (null != this.ftpClient && this.ftpClient.isConnected()) {
            try {
                boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
                if (reuslt) {
                    System.out.println("成功登出服务器");
                    logger.info("成功登出服务器");
                }
            } catch (IOException e) {
                e.printStackTrace();
               logger.warn("退出FTP服务器异常！" + e.getMessage());


            } finally {
                try {
                    this.ftpClient.disconnect();  //关闭FTP服务器的连接
                } catch (IOException e) {
                    e.printStackTrace();
                   logger.warn("关闭FTP服务器的连接异常！");
                }
            }
        }
    }

    public static boolean checkDirectory(String startDate,String endDate ,FTPClient ftpClient ) {      //安达便利门店信息表.txt  /home/etl/samgao/anda_daily/20180104  和  /20180401
        return  FileCheckUtil.checkFile(startDate , endDate , ftpClient);
    }



    /***
     * 下载文件
     * @param fileName   待下载文件名称 20180124
     * @param localDires 下载到当地那个路径下
     * @param remoteDownLoadPath remoteFileName所在的路径
     * */
    public boolean downloadFile(String fileName, String localDires, String remoteDownLoadPath) {      //安达便利门店信息表.txt  /home/etl/samgao/anda_daily/20180104  和  /20180401

        String strFilePath = localDires + fileName;
        BufferedOutputStream outStream = null;
        boolean success = false;
        long localSize;
        File localFile =new File(strFilePath);     //下载某个文件出现异常，断点续传。
        try {
            this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
            outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
            System.out.println(fileName + "开始下载....");
            //success = this.ftpClient.retrieveFile(new String(fileName.getBytes("ISO-8859-1"), "UTF-8"), outStream);
            success = this.ftpClient.retrieveFile(fileName, outStream);   //乱码解决
            if (success) {
                System.out.println(fileName + "成功下载到" + strFilePath);
                return true;
            }else{
                return  false;
            }
        } catch (Exception e) {
                e.printStackTrace();
            localSize = localFile.length();       //如果发生异常，断点续传。
            ftpClient.setRestartOffset(localSize);
            try {
                this.ftpClient.retrieveFile(fileName ,outStream);
            } catch (IOException ioE) {
             e.printStackTrace();
            }
        } finally {
            if (null != outStream) {     //最后关掉流。
                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!success) {
            System.out.println(fileName + "下载失败!!!");
            //mail.Mail.mailToMe(remoteFileName+"下载失败。当前文件夹下载失败");
            return false;
        }
        return success;
    }

    /***
     * @下载文件夹
     * @param
     * @param FTPDirectoryPath 远程文件夹
     * */
    // 远程文件夹目录“/20180104”  localDirectoryPath本地文件夹路径“C:\\MYJ_pos_data”    C:\MYJ_pos_data\20180104\
    public boolean downLoadDirectory(String localDirectoryPath,String FTPDirectoryPath) throws IOException { // /home/etl/samgao/anda_daily/   和 /20180401
        String dataDirectoryName =FTPDirectoryPath.substring(1,9);  // 20180104
        Boolean haveDirectory=false;
        //System.out.println("Hello World!");
        //判断ftp是否有目标文件夹。如果没有，直接返回false。
        FTPFile[] ftpFiles= this.ftpClient.listFiles("/");
        for (int i = 0; i < ftpFiles.length; i++) {
            if (ftpFiles[i].getName().equals(dataDirectoryName)) {              //当天日期拼成的数据文件夹的字符串。 20180104
                System.out.println(ftpFiles[i].getName() + "，服务器存在当天文件夹");
                haveDirectory= true;
                break;
            }
        }
        if(!haveDirectory){
            System.out.println(dataDirectoryName + "，服务器没有当天文件夹");
            Mail.mailToMe("服务器没有当天的文件夹，下载失败");
            return  false;
        }

        //如果本地有当天文件，删除文件，重新建立目录，下载。
        try {
            String fileName = new File(FTPDirectoryPath).getName();     //  /20180104 --> 20180104
            localDirectoryPath = localDirectoryPath + propertiesUtil.getProperty("linux_filename_seperator")+ fileName + propertiesUtil.getProperty("linux_filename_seperator");    // /home/etl/samgao/anda_daily/20180104   test
            File localDirectoryFile = new File(localDirectoryPath);
            if(localDirectoryFile.exists()) {                         // 检查是否已经存在,有就删除，重新下载。
                System.out.println("本地存在当天文件夹，删除...");
                localDirectoryFile.delete();
                              //  /home/etl/samgao/anda_daily/20180104
            }
            new File(localDirectoryPath).mkdirs();

            FTPFile[] allFile = this.ftpClient.listFiles(FTPDirectoryPath);     //下载全部文件。
            for (int currentPointer = 0; currentPointer < allFile.length; currentPointer++) {
                if (!allFile[currentPointer].isDirectory()) {
                    //错误在这里。或许是ftp连接时间。下载第二个文件就失败。
                    Boolean bl=downloadFile(allFile[currentPointer].getName(),localDirectoryPath, FTPDirectoryPath);  //循环下载文件  /home/etl/samgao/anda_daily/20180104 和 /20180401
                    if(!bl){
                        return  false;   //如果有一个失败，跳出。
                    }
                }
            }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {    // 再次循环，找到文件夹。
                if (allFile[currentFile].isDirectory()) {
                    String strremoteDirectoryPath = FTPDirectoryPath + "/"+ allFile[currentFile].getName();
                    downLoadDirectory(localDirectoryPath,strremoteDirectoryPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Mail.mailToMe("当天文件夹下载成功");
        return true;
    }

    // FtpClient的Set 和 Get 函数
    public FTPClient getFtpClient() {
        return ftpClient;
    }
    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}