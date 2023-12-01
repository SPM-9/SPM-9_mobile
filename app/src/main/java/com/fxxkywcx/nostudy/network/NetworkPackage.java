package com.fxxkywcx.nostudy.network;

import com.fxxkywcx.nostudy.Final;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;

public class NetworkPackage {
    // arg2
    public static final int SUCCEED = 0;
    public static final int NETWORK_FAILURE = 1;
    protected final Gson gson = new GsonBuilder().setDateFormat(Final.format.toPattern()).create();
    protected final String servletUrl = "http://" + Final.tomcatSocket + "/YiWangNoChangXue";
    protected final OkHttpClient okpClient = new OkHttpClient();
}
