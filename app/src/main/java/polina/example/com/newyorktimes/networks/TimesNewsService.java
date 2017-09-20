package polina.example.com.newyorktimes.networks;

import java.util.List;
import java.util.Map;

import polina.example.com.newyorktimes.model.TimesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by polina on 9/19/17.
 */

public interface TimesNewsService {
    public static final String BASE_URL = "https://api.nytimes.com/svc/";

    @GET("search/v2/articlesearch.json")
    Call<TimesResponse> getNews(@Query("page") int page, @Query("q") String search, @Query("sort") String sort, @Query("begin_date") String data, @Query("fq") String news_desk);

}
