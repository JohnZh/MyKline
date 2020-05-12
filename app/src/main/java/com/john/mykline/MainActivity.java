package com.john.mykline;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;

import com.john.mykline.bean.MyKlineData;
import com.johnzh.klinelib.KlineConfig;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.indexes.Index;
import com.johnzh.klinelib.indexes.MAIndex;
import com.johnzh.klinelib.indexes.PureKIndex;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private KlineView mKlineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKlineView = findViewById(R.id.klineView);

        mKlineView.setConfig(
                new KlineConfig.Builder()
                        .index(createMAIndex())
                        .build());

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

    private Index createMAIndex() {
        int[] posNegColor = {
                Color.parseColor("#f62048"),
                Color.parseColor("#39ae13")
        };

        float candleWidth = mKlineView.toPx(TypedValue.COMPLEX_UNIT_DIP, 6);
        float lineWidth = mKlineView.toPx(TypedValue.COMPLEX_UNIT_DIP, 1);
        PureKIndex pureKIndex = new PureKIndex(posNegColor, candleWidth, lineWidth);

        int[] maColors = {
                Color.parseColor("#ffb405"),
                Color.parseColor("#890cff")
        };
        return new MAIndex(pureKIndex, lineWidth, new int[]{ 5, 10}, maColors);
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
        mKlineView.setKlineDataList(body);
    }
}
