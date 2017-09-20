package polina.example.com.newyorktimes.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Created by polina on 9/19/17.
 */

class Doc {
    @SerializedName("_id")
    public String id;

    public String uri;
    public String pub_date;
    public String snippet;
    public Headline headline;
    public String new_desk;

    class Headline {
        String main;

    }


    public class Multimedia {
        String url;
    }

    public Collection<Multimedia> multimedia;


    @Override
    public String toString() {
        return "Doc{" +
                "id='" + id + '\'' +
                ", uri='" + uri + '\'' +
                ", pub_date='" + pub_date + '\'' +
                ", snippet='" + snippet + '\'' +
                ", headline=" + headline +
                ", multimedia=" + multimedia +
                ", new_desk=" + new_desk +
                '}';
    }
}
