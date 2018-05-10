package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by SAM on 2018/4/28.
 */
public class TimeUtil {

    public static List<String> getDateList(String startDateStr, String endDateStr){   //输入20180423  20180423  返回路径列表Array(String)。
        List<String> dateList  = new ArrayList<String>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
             int dateFiled = Calendar.DAY_OF_MONTH;
             Date beginDate = dateFormat.parse(startDateStr);
             Date endDate = dateFormat.parse(endDateStr);
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(beginDate);

            while (beginDate.compareTo(endDate) <= 0) {
                dateList.add(dateFormat.format(beginDate));
                calendar.add(dateFiled, 1);
                beginDate = calendar.getTime();
            }

        } catch (Exception e){
            System.out.println("Exception thrown  :" + e);
        }
        return  dateList;
    }
    public static String getNowDateTimeStamp(){
        Date now = new Date();
        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr = dateFormat.format(now);
        return  nowStr;
    }

}
