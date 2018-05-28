package newstoreanditem;

import util.AndaEtlLogUtil;

import java.util.*;

public class NewProduct {

    //输入日期格式为yyyyMMdd
    //功能：选取两个日期，整理出日期段内新店铺的记录。
    public  void startWork(String startDate , String endDate ,String basicInputPath ,String outpath) {
        Hashtable<String,String> productTable = new Hashtable<String, String>();
        /*String startDatePath= basicInputPath+startDate +"\\商品档案表.txt";
        String endDatePath= basicInputPath+endDate +"\\商品档案表.txt";*/

        String startDatePath= basicInputPath+startDate +"/商品档案表.txt";
        String endDatePath= basicInputPath+endDate +"/商品档案表.txt";

        //String startDatePath="E:\\FTP\\RetailData\\Anda\\"+startDate +"\\商品档案表.txt"; //部署
        //String endDatePath= "E:\\FTP\\RetailData\\Anda\\"+endDate +"\\商品档案表.txt";

        ProductCsvFileUtil csvTool =new ProductCsvFileUtil();
        productTable =csvTool.read(startDatePath);  //固定日期，测试。
        Set<String> set1 =productTable.keySet();
        productTable =csvTool.read(endDatePath);
        Set<String> set2 =productTable.keySet();

        if(set1.size()==0 || set2.size()==0){  //读取文件时，如果其中一天数据不存在，不比较
            System.out.println("有某天数据文件缺失 ，不执行新商品生成");
            AndaEtlLogUtil.produceEtlAndaInfoLog(endDate, startDate+"->"+endDate+"：某天的数据文件缺失，不执行新商品生成");
            return;
        }
        Set<String> result =new HashSet<String>();

        //商品编码的差集。
        result.clear();
        result.addAll(set2);
        result.removeAll(set1);
        System.out.println("差集："+result);

        //构建今天产生的新产品条目输出。
        List<String> output= new ArrayList<String>();    //输出list文件。
        for(String newStoreCode:result){
            output.add(productTable.get(newStoreCode));
        }

        //System.out.println(output);

        //String outFilePath="E:\\DataReal_Data\\DAILY\\NEWITEM\\R1003_ANDA_new_product.csv"; //PRODUCT
        csvTool.write(output,outpath,startDate,endDate);  //开始日期，结束日期，输出文件的路径。
    }
}
