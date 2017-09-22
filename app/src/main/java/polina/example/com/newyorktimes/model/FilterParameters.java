package polina.example.com.newyorktimes.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by polina on 9/19/17.
 */

public class FilterParameters {
    String date;
    boolean art;
    boolean fashion;
    boolean sport;
    int page;
    String sort;

    public String getKayWord() {
        return kayWord;
    }

    public void setKayWord(String kayWord) {
        this.kayWord = kayWord;
    }

    String kayWord;



    public FilterParameters() {
        page = 0;

    }

    public String getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
       return df.format(date);
    }


    public String getDesk() {
        String result = "news_desk:(";
        result = result+ (art?"\"Arts\"":"" )+(fashion?"\"Fashion & Style\"":"")+(sport?"\"Sports\"":"") +")";
        System.err.println(result);
        return result;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isArt() {
        return art;
    }

    public void setArt(boolean art) {
        this.art = art;
    }

    public boolean isFashion() {
        return fashion;
    }

    public void setFashion(boolean fashion) {
        this.fashion = fashion;
    }

    public boolean isSport() {
        return sport;
    }

    public void setSport(boolean sport) {
        this.sport = sport;
    }

    public boolean isChecked() {
        return art||sport||fashion;
    }

    public int nextPage() {
        return page+1;
    }
}
