package polina.example.com.newyorktimes.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Created by polina on 9/19/17.
 */

public class Doc {
    @SerializedName("_id")
    public String id;

    public String web_url;
    public String pub_date;
    public String snippet;
    public Headline headline;
    public String new_desk;

   public class Headline {
        public String main;

    }


    public class Multimedia {
        public String url;
        public int width;
        public int height;

        @Override
        public String toString() {
            return url;
        }
    }

    public Collection<Multimedia> multimedia;


    @Override
    public String toString() {
        return "Doc{" +
                "id='" + id + '\'' +
                ", uri='" + web_url + '\'' +
                ", pub_date='" + pub_date + '\'' +
                ", snippet='" + snippet + '\'' +
                ", headline=" + headline +
                ", multimedia=" + multimedia +
                ", new_desk=" + new_desk +
                '}';
    }
}
