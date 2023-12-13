package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import com.fxxkywcx.nostudy.entity.ResourceItem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

import java.util.List;

public interface ApiService {
    // 定义获取资源列表的API请求
    @GET("/api/downloadItems")
    Call<List<ResourceItem>> getDownloadItems();

    @GET
    @Streaming
    Call<ResponseBody> downloadImage(@Url String imageUrl);

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
