package polina.example.com.newyorktimes.networks;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import polina.example.com.newyorktimes.BuildConfig;
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
                System.err.println("URL: " + request.url());
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
