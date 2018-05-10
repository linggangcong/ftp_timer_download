package util;

import java.net.InetAddress;

/**
 * Created by SAM on 2018/5/10.
 */
public class NetUtil {
    public static String  getHostAddress(){
        String ip = "未知";
        try {
            InetAddress  address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch(Exception e) {
            System.out.println(e);
        }
        return  ip;
    }
    public static String  getHostName(){
        String hostname = "未知";
        try {
            InetAddress  address = InetAddress.getLocalHost();
            hostname = address.getHostName();
        } catch(Exception e) {
            System.out.println(e);
        }
        return  hostname;
    }

}
