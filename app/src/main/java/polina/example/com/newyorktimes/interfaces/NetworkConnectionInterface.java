package polina.example.com.newyorktimes.interfaces;

import polina.example.com.newyorktimes.model.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by polina on 9/19/17.
 */

public interface NetworkConnectionInterface {

    @GET("/search/v2/articlesearch.json")
    Call<News> getNews();

}
