package util;

import java.sql.*;
import java.util.List;

/**
 * Created by SAM on 2018/5/10.
 */
public class SqlServerUtil {
    public static PropertiesUtil properties = new PropertiesUtil();
    private static final String SQL_SERVER_CONN_URL = properties.getProperty("sqlserver.connect.url");
    static Connection conn;
   public static int   insert(String sql, List<String> params) {   //插入语句。
        int res = 0;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            conn = DriverManager.getConnection(SQL_SERVER_CONN_URL);
            // Configure to be Read Only
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int i= 0 ; i< params.size() - 1; i++ ) {
                int j = i + 1;
                statement.setString(j, params.get(i));  //把字符串列表内的字符串全部导入sql内的参数。
            }
            res = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  res;
    }

    public static List<String>  query(String sql, String colName) {   //JDBC查询数据库语句。 执行sql语句，获取ResultSet， resultList是ResultSet中某个列的值的list.
        List<String> resultList = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            Connection conn = DriverManager.getConnection(SQL_SERVER_CONN_URL);
            // Configure to be Read Only
            Statement statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            // Execute Query
            ResultSet rs = statement.executeQuery(sql);
            // Iterate Over ResultSet
            while (rs.next()) {
                resultList.add(rs.getString(colName));
                System.out.println(rs.getString(colName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  resultList;
    }

/*
    def getLatestSuccessDay(sql: String): String ={    //  banner=R10003 , data_day最近，而且 log_level全部非error。
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance()
        val conn = DriverManager.getConnection(SQL_SERVER_CONN_URL)
        var latestSuccessDay: String =""
        // println("hello")
        try {
            // Configure to be Read Only
            val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

            // Execute Query
            val rs: ResultSet = statement.executeQuery(sql)

            // Iterate Over ResultSet
            while (rs.next) {
                resultList = resultList ++ List(rs.getString(colName))
                println(rs.getString(colName))
            }

        }
        catch {
            case _: Exception => println("===>")
        }
    finally {
            conn.close
        }
        latestSuccessDay
    }
    */
}
