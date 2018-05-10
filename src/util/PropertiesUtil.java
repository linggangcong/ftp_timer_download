package util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    public   Properties properties = new Properties();  // 如果给它初始化为null， 但是并没有赋予空间。

    public  String getProperty(String k) {
         //Properties properties = null;
        InputStream inputStream =Thread.currentThread().getContextClassLoader().getResourceAsStream("my.properties"); //这种方法可以。
        try {
            properties.load(inputStream);        //加载属性列表 反序列化。
            inputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return  properties.getProperty(k);

    }
}
