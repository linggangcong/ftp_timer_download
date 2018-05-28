package newstoreanditem;

import java.io.*;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by SAM on 2018/2/8.
 */
//从店铺表文件中读出店铺信息。
public class StoreCsvFileUtil {

    public Hashtable<String,String> read(String pathName ) {
        Hashtable<String,String> table=new Hashtable<String, String>();
        String[] lineNum=null;

        //输入的文件是否存在。如果没有，报错，程序退出。 报错：文件没有找到
       /* File inputFile = new File(pathName);
        if(!inputFile.exists()) {
            System.out.println("本地没有新店铺的日期文件夹");
            return null;     //弹出， 不会继续运行。
        }*/


       /* InputStream in = null;
        try {
            String fileName= pathName.substring(pathName.indexOf("/"));
            //ftpClient.changeWorkingDirectory(filePath);
            in = ftpClient.retrieveFileStream(fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // String data = null;
            try {
                String line = "";
                while ((line = br.readLine()) != null) {
                    lineNum= line.split("\t",-2);
                    if( lineNum.length< 5 || lineNum[0].contains("店号")|| lineNum[0].contains("安达")){
                        continue;
                    }
                    String productValue=lineNum[0]+","+lineNum[1]+","+lineNum[3]+","+lineNum[4]+","+""+","+"R10003";
                    String key =lineNum[0];
                    table.put(key,productValue);            //读取csv文件，并保存商品编码字段和一行字段进入hashtable。
                    //System.out.println(key);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*//*else{
            logger.error("in为空，不能读取。");
        }*//*
        return table;*/


        try {
            File csv = new File(pathName);  // CSV文件  捕获没有文件的异常，不退出程序。
            InputStreamReader read = new InputStreamReader(new FileInputStream(csv),"gbk");  //捕捉异常了。
            BufferedReader br = new BufferedReader(read);
            //String[]  lineNum;
            // 读取直到最后一行
            String line = "";
            while ((line = br.readLine()) != null) {     //逐行读取，一次读一行。 如果有空行，就结束了。

               lineNum= line.split("\t",-2);
               if( lineNum.length< 5 || lineNum[0].contains("店号")|| lineNum[0].contains("安达")){
                    continue;
               }
               String productValue=lineNum[0]+","+lineNum[1]+","+lineNum[3]+","+lineNum[4]+","+""+","+"R10003";
               String key =lineNum[0];
               table.put(key,productValue);            //读取csv文件，并保存商品编码字段和一行字段进入hashtable。
                //System.out.println(key);
            }
            br.close();
        } catch (Exception e) {
            // 捕获File对象生成时的异常
            e.printStackTrace();
        }
        return table;
    }

    //把新店铺的数据集合（List<String> ）写出为文件。
    public  void write(List<String> newStore , String outputPath, String startDateStr , String endDateStr ) {
        try {
            File newStoreFile = new File(outputPath);   // CSV文件
            if(!newStoreFile.exists()){   //判断
                newStoreFile.createNewFile();
            }
            // 追加模式  true设置
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(newStoreFile,true),"gbk");
            BufferedWriter bw = new BufferedWriter(write);
            // 新增一行数据
            //Iterator<String> iterator= newProduct.iterator();
            bw.write(startDateStr+"-->"+endDateStr+"，新店铺的记录如下："+"\r");
            for(String line:newStore){
                bw.write(line);
            bw.newLine();    // IOException: Stream closed  bw是空的。
             //System.out.println(pho);
            }
            bw.write("\r\n");
            //bw.write("\r\n");
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            // 捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            // 捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }

}
