package com.fxxkywcx.nostudy.activities.ResourceDownloadActivity;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fxxkywcx.nostudy.R;
import com.fxxkywcx.nostudy.entity.ResourceItem;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ResourceDownloadActivity extends AppCompatActivity {
    private ArrayList<ResourceItem> data=new ArrayList<>();
    private ApiService apiService;
    private ResourceDownloadAdaptor resourceDownloadAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_download);


        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://你的API基本URL/") // 用你的API基本URL替换
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建ApiService
        apiService = retrofit.create(ApiService.class);




            ResourceItem resourceItem = new ResourceItem();
//            resourceItem.getImageView().s
            resourceItem.getTextView().setText("文件i");
            data.add(resourceItem);

        ResourceDownloadAdaptor resourceDownloadAdaptor = new ResourceDownloadAdaptor(data,this);

        // 从API获取资源
        fetchResources();

        RecyclerView recycleView = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);


        recycleView.setAdapter(resourceDownloadAdaptor);
    }
    private void fetchResources() {
        Call<List<ResourceItem>> call = apiService.getDownloadItems();
        call.enqueue(new Callback<List<ResourceItem>>() {
            @Override
            public void onResponse(@NotNull Call<List<ResourceItem>> call, @NotNull Response<List<ResourceItem>> response) {
                if (response.isSuccessful()) {
                    /*// 获取图片的二进制数据的字节数组
                    byte[] imageData = response.body().bytes();

                    // 获取文件名（假设服务器通过响应头 "Content-Disposition" 提供文件名）
                    String contentDisposition = response.headers().get("Content-Disposition");
                    String fileName = extractFileName(contentDisposition);

                    // 将字节数组保存为图片文件
                    saveImageToFile(imageData, fileName);*/

                    // 使用获取到的资源更新你的数据列表

                    data.addAll(response.body());

                    // 通知适配器数据已更改
                    resourceDownloadAdaptor.notifyDataSetChanged();
                } else {
                    // 处理错误
                    Toast.makeText(ResourceDownloadActivity.this, "获取资源失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ResourceItem>> call, @NotNull Throwable t) {
                // 处理网络故障
                Toast.makeText(ResourceDownloadActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String extractFileName(String contentDisposition) {
        // 从 "Content-Disposition" 中提取文件名
        // 这里的解析可能需要根据具体的响应头格式进行调整
        // 示例： "attachment; filename=image.jpg"
        if (contentDisposition != null) {
            int startIndex = contentDisposition.indexOf("filename=");
            if (startIndex != -1) {
                startIndex += "filename=".length();
                int endIndex = contentDisposition.indexOf(";", startIndex);
                if (endIndex == -1) {
                    endIndex = contentDisposition.length();
                }
                return contentDisposition.substring(startIndex, endIndex).trim();
            }
        }
        return "image.jpg"; // 如果无法解析文件名，则使用默认名称
    }

    // 下载图片
    private void downloadImage(String imageUrl) {
        Call<ResponseBody> call = apiService.downloadImage(imageUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 将图片数据保存到本地文件
                    try {
                        saveImageToFile(response.body().bytes(), "image.jpg");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(ResourceDownloadActivity.this, "下载图片失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResourceDownloadActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 下载文件
    private void downloadFile(String fileUrl) {
        Call<ResponseBody> call = apiService.downloadFile(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 将文件数据保存到本地文件
                    try {
                        saveFile(response.body().bytes(), "file.txt");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(ResourceDownloadActivity.this, "下载文件失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResourceDownloadActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 保存图片数据到文件
    private void saveImageToFile(byte[] data, String fileName) {
        // 这里需要根据你的需求选择保存路径和处理方式
        File file = new File(getFilesDir(), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 保存文件数据到文件
    private void saveFile(byte[] data, String fileName) {
        // 这里需要根据你的需求选择保存路径和处理方式
        File file = new File(getFilesDir(), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




