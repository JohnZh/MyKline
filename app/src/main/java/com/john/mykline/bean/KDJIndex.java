package com.john.mykline.bean;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.indexes.Index;

import java.util.List;

/**
 * Modified by john on 2020/5/11
 * <p>
 * Description:
 */
public class KDJIndex implements Index<MyKlineData> {

    public static class KDJ {

    }

    @Override
    public void calcIndexAsync(List<MyKlineData> klineDataList) {

    }

    @Override
    public void calcIndex(List<MyKlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void drawIndex(KlineView klineView, int startIndex, int endIndex, Canvas canvas, Paint paint) {

    }
}
