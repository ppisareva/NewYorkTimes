package polina.example.com.newyorktimes.model;

/**
 * Created by polina on 9/19/17.
 */

public class FilterParameters {
    String sortBy;
    String date;
    boolean art;
    boolean fashion;
    boolean sport;


    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
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
}
