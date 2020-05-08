package com.john.mykline;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Modified by john on 2020/5/7
 * <p>
 * Description:
 *
 * http:///
 */
public class HttpAgent {

    interface ApiList {

        // ?symbol=M2009
        @GET("futures/api/json.php/IndexService.getInnerFuturesDailyKLine")
        Call<List<List<String>>> getDailyKline(@Query("symbol") String symbol);
    }

    public static final String STOCK_BASE_URL = "http://stock2.finance.sina.com.cn/";
    private static HttpAgent sAgent;

    private ApiList mApiList;

    public static HttpAgent getInstance() {
        if (sAgent == null) {
            sAgent = new HttpAgent();
        }
        return sAgent;
    }

    public static ApiList getApi() {
        return getInstance().mApiList;
    }

    private HttpAgent() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STOCK_BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiList = retrofit.create(ApiList.class);
    }


}
