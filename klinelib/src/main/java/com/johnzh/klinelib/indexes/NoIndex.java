package com.johnzh.klinelib.indexes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class NoIndex implements Index {

    @Override
    public void calcIndexAsync(List<? extends KlineData> klineDataList) {

    }

    @Override
    public void calcIndex(List<? extends KlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void onDraw(KlineView klineView, int startIndex, int endIndex, Canvas canvas, Paint paint) {

    }

}
