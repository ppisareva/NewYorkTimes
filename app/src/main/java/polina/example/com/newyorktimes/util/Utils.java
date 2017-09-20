package polina.example.com.newyorktimes.util;

import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by polina on 9/19/17.
 */

public class Utils {

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public static String getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateResult = df.format(date);
        return dateResult;
    }

}
