package polina.example.com.newyorktimes.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.DatePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import polina.example.com.newyorktimes.model.Doc;
import polina.example.com.newyorktimes.model.New;
import polina.example.com.newyorktimes.model.TimesResponse;
import retrofit2.Response;

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

    private static Date getDate(String date){

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
           return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static int getYear(String date){
        DateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(getDate(date)));

    }public static int getMonth(String date){
        DateFormat df = new SimpleDateFormat("MM");
        return Integer.parseInt(df.format(getDate(date)));

    }public static int getDay(String date){
        DateFormat df = new SimpleDateFormat("dd");
        return Integer.parseInt(df.format(getDate(date)));

    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public static List<New> parseResponse(Response<TimesResponse> response) {
        List<New> result = new ArrayList<>();
            if (!response.isSuccessful()) {
                System.err.println("Ooops.. " + response.message());
                return new ArrayList<>();
            }
            List<Doc> doc = (List<Doc>) response.body().response.docs;
            for (int i = 0; i < doc.size(); i++) {
                Doc d = doc.get(i);
                List<Doc.Multimedia> m = (List<Doc.Multimedia>) d.multimedia;
                String url = "";
                if (m.size() > 0) {
                    url = "http://www.nytimes.com/" + m.get(0).url;
                }
                New newItem = new New(d.headline.main, d.snippet, url, d.web_url, d.new_desk);
                if (!m.isEmpty()) {
                    newItem.setSize(m.get(0).width, m.get(0).height);
                }
                result.add(newItem);
            }

        return result;
    }
}
