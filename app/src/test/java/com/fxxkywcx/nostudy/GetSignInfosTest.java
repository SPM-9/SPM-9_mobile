package com.fxxkywcx.nostudy;
import android.os.Handler;
import com.fxxkywcx.nostudy.entity.UserSignEntity;
import com.fxxkywcx.nostudy.network.GetSignInfos;
import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;


public class GetSignInfosTest {
    static GetSignInfos signInfos;
    Gson gson;
    @BeforeClass
    public static void initialization(){
        signInfos = GetSignInfos.getInstance();
        Gson gson = new Gson();
    }

    @Test
    public void get() {
        Handler handler= new Handler();
        UserSignEntity userSignEntity = new UserSignEntity();
        signInfos.getSignInfo(handler,userSignEntity);

    }

}
