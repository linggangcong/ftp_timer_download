package newstoreanditem;

import java.io.*;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by SAM on 2018/2/8.
 */
public class ProductCsvFileUtil {      //要求文件为txt格式,GB2312，字段内容没有“，”。字段数量和字段内容固定格式。

    public  Hashtable<String,String> read(String  pathName) {
        Hashtable<String,String> table=new Hashtable<String, String>();
        String[] lineNum=null;
        try {
            File csv = new File(pathName);  // CSV文件
            InputStreamReader read = new InputStreamReader(new FileInputStream(csv),"gbk");
            BufferedReader br = new BufferedReader(read);
            //List<String> lineList=null;
            // 读取直到最后一行


            String line = "";
            //String[] lineNum=null;
            while ((line = br.readLine()) != null) {//逐行读取，一次读一行。
                //String lineUtf= transformTextToUTF8(line, "UTF-8");
                lineNum= line.split("\t",-2);
                if( lineNum.length< 49 || lineNum[0].contains("ABC类别")){
                    continue;
                }
                String productValue=lineNum[20]+","+lineNum[1]+","+lineNum[3]+","+lineNum[2]+","+lineNum[48]+","+lineNum[4]+","+lineNum[22]+","+lineNum[15]+","+lineNum[26]+","+"R10003";
                String key =lineNum[1];
                table.put(key,productValue);      //读取csv文件，并保存商品编码字段和一行字段进入hashtable。
                //System.out.println(key);
            }
            br.close();
            //System.out.println(pathName+lineList.size());

        } catch (Exception e) {
            // 捕获File对象生成时的异常
            e.printStackTrace();
        }
        return table;
    }
    public static String transformTextToUTF8(String text, String encoding) {
        String value = null;
        try {
            value = new String(text.getBytes(), 0, text.length(), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    public  void write(List<String> newProduct ,String pathName, String startDateStr ,String endDateStr) {
        try {
            File newProductFile = new File(pathName); // 新产品的CSV文件

            if(!newProductFile.exists()){
                newProductFile.createNewFile();
            }
            // 追加模式
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(newProductFile,true),"gbk");
            BufferedWriter bw = new BufferedWriter(write);
            bw.write(startDateStr+"-->"+endDateStr+"，新商品的记录如下："+"\r");
            for(String line:newProduct){
                bw.write(line);
                bw.newLine();    // IOException: Stream closed  bw是空的。
                //System.out.println(pho);
            }
            bw.write("\r\n");
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
