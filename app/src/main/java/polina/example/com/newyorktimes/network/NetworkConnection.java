package polina.example.com.newyorktimes.network;

import polina.example.com.newyorktimes.interfaces.NetworkConnectionInterface;
import polina.example.com.newyorktimes.model.News;
import retrofit2.Call;
import retrofit2.http.Path;

/**
 * Created by polina on 9/19/17.
 */

public class NetworkConnection  implements NetworkConnectionInterface{


    public static final String BASE_URL = "https://api.nytimes.com/svc";



    @Override
    public Call<News> getNews() {
        return null;
    }
}
