package util;

import java.util.Comparator;

/**
 * Created by SAM on 2018/5/11.
 */
public class SortUtil implements Comparator {

    public int compare(Object arg0,Object arg1){
        String date0 = (String)arg0;
        String date1= (String)arg1;
        int flag = date0.compareTo(date1);
        return flag;
    }
}
