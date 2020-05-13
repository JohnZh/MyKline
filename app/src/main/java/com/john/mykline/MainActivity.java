package com.john.mykline;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.john.mykline.bean.MyKlineData;
import com.john.mykline.databinding.ActivityMainBinding;
import com.johnzh.klinelib.KlineConfig;
import com.johnzh.klinelib.indexes.MAIndex;
import com.johnzh.klinelib.indexes.PureKIndex;
import com.johnzh.klinelib.indexes.VolIndex;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.klineView.setConfig(
                new KlineConfig.Builder()
                        .index(binding.klineView.getFactory().createDefaultIndex(PureKIndex.class))
                        .index(binding.klineView.getFactory().createDefaultIndex(MAIndex.class))
                        .index(binding.klineView.getFactory().createDefaultIndex(VolIndex.class))
                        .build());
        binding.pureK.setOnClickListener(v -> {
            binding.klineView.selectIndex(0);
        });
        binding.ma.setOnClickListener(v -> {
            binding.klineView.selectIndex(1);
        });
        binding.vol.setOnClickListener(v -> {
            binding.klineView.selectIndex(2);
        });
        binding.combination.setOnClickListener(v -> {

        });

        HttpAgent.getApi().getDailyKline("M2009")
                .enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        List<MyKlineData> myKlineDataList = new ArrayList<>();
                        convertData(response.body(), myKlineDataList);
                        updateKlineView(myKlineDataList);
                    }

                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {

                    }
                });
    }

    private void convertData(List<List<String>> body, List<MyKlineData> kDataList) {
        for (List<String> stringList : body) {
            kDataList.add(getKData(stringList));
        }
    }

    private MyKlineData getKData(List<String> stringList) {
        MyKlineData kData = new MyKlineData();
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

    private void updateKlineView(List<MyKlineData> body) {
        binding.klineView.setKlineDataList(body);
    }
}
