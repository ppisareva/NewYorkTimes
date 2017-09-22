package polina.example.com.newyorktimes.networks;

import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import polina.example.com.newyorktimes.BuildConfig;
import polina.example.com.newyorktimes.adapter.NewsAdapter;
import polina.example.com.newyorktimes.model.FilterParameters;
import polina.example.com.newyorktimes.model.New;
import polina.example.com.newyorktimes.model.TimesResponse;
import polina.example.com.newyorktimes.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by polina on 9/19/17.
 */

public class Networks {
    private static final String BASE_URL = "https://api.nytimes.com/svc/";
    public static TimesNewsService getService() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TimesNewsService.class);
    }



}
