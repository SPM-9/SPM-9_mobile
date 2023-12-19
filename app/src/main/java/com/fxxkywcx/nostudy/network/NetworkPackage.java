package com.fxxkywcx.nostudy.network;

import com.fxxkywcx.nostudy.Final;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NetworkPackage {
    // arg2
    public static final int SUCCEED = 0;
    public static final int NETWORK_FAILURE = 1;
    protected static final Gson gson = new GsonBuilder().setDateFormat(Final.format.toPattern()).create();
    protected static final String servletUrl = "http://" + Final.tomcatSocket + "/YiWangNoChangXue";
    protected static final OkHttpClient okpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request req = chain.request().newBuilder()
                            .addHeader("AndroidApp", "true")
                            .build();
                    Response resp = chain.proceed(req);
                    return resp;
                }
            })
            .build();
}
