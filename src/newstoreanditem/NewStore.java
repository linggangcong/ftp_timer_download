package newstoreanditem;

import java.util.*;

public class NewStore {

    //输入日期格式为yyyyMMdd
    //功能：选取两个日期，整理出日期段内新店铺的记录。
    public  void startWork(String startDate , String endDate ,String basicInputPath,String storeOutPath) {
        Hashtable<String,String> storeTable = new Hashtable<String, String>();
        String startDatePath= basicInputPath+startDate +"\\安达便利门店信息表.txt";
        String endDatePath= basicInputPath+endDate +"\\安达便利门店信息表.txt";

        StoreCsvFileUtil storeCsvTool =new StoreCsvFileUtil();
        storeTable =storeCsvTool.read(startDatePath);  //固定日期，测试。
        Set<String> set1 =storeTable.keySet();
        storeTable =storeCsvTool.read(endDatePath);
        Set<String> set2 =storeTable.keySet();
        if(set1.size()==0 || set2.size()==0){  //读取文件时，如果其中一天数据不存在，不比较
            System.out.println("有某天的数据文件缺失 ，不执行新店铺生成操作");
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
            output.add(storeTable.get(newStoreCode));
        }
        storeCsvTool.write(output,storeOutPath,startDate,endDate);  //开始日期，结束日期，输出文件的路径。
    }
}
