package com.john.mykline;

import android.os.Bundle;

import com.john.mykline.bean.KData;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpAgent.getApi().getDailyKline("M2009")
                .enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        List<KData> kDataList = new ArrayList<>();
                        convertData(response.body(), kDataList);
                        updateKlineView(kDataList);
                    }

                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {

                    }
                });
    }

    private void convertData(List<List<String>> body, List<KData> kDataList) {
        for (List<String> stringList : body) {
            kDataList.add(getKData(stringList));
        }
    }

    private KData getKData(List<String> stringList) {
        KData kData = new KData();
        if (stringList.size() >= 6) {
            int i = 0;
            kData.setDate(stringList.get(i++));
            kData.setOpenPrice(stringList.get(i++));
            kData.setHighestPrice(stringList.get(i++));
            kData.setLowestPrice(stringList.get(i++));
            kData.setClosePrice(stringList.get(i++));
            kData.setVolume(stringList.get(i++));
        }
        return kData;
    }

    private void updateKlineView(List<KData> body) {

    }
}
