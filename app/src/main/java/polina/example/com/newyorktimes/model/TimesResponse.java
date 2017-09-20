package polina.example.com.newyorktimes.model;


import java.util.Collection;

/**
 * Created by polina on 9/19/17.
 */

public class TimesResponse {
    public String status;

    public class Response {
        public Collection<Doc> docs;
    }

    public Response response;
}
