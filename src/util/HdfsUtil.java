package util;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.List;

/**
 * Created by SAM on 2018/5/7.
 */
public class HdfsUtil {
    static  Configuration conf = new Configuration(true);
    static PropertiesUtil propertiesUtil=new PropertiesUtil();
    static{
        //指定hadoop fs的地址
        conf.set("fs.default.name", propertiesUtil.getProperty("fs.default.name"));
    }
    // 上传文件到hdfs,多个日期。
    public static void moveToHdfs(String startDay ,String endDay){
        try {
            FileSystem hdfs= FileSystem.get(conf);   //错误。 IO。
            List<String> dayList = TimeUtil.getDateList(startDay,endDay);
            for(String  day:dayList){
                Path src =new Path(propertiesUtil.getProperty("local_daily_dir")+day);
                //HDFS为止
                Path dst =new Path(propertiesUtil.getProperty("hdfs_des_dir")+"/"+day);   //错误  /usr/samgao/input/anda/20180507
                if (hdfs.exists(dst)) {
                    AndaEtlLogUtil.produceEtlAndaInfoLog(day, "上传到hdfs,发现已经存在该日期文件夹，删除");
                    hdfs.delete(dst, true);     //true的意思是，就算output有东西，也一带删除
                }
                hdfs.copyFromLocalFile(src, dst);
                System.out.println("Upload to"+conf.get("fs.default.name"));
                AndaEtlLogUtil.produceEtlAndaSuccessLog(day, "当天的数据文件上传到"+conf.get("fs.default.name"));
                //本地文件
               /* FileStatus files[]=hdfs.listStatus(dst);
                for(FileStatus file:files){
                    System.out.println(file.getPath());
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
