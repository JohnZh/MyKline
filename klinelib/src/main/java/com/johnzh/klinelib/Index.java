package com.johnzh.klinelib;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description: index for kline
 */
public interface Index {

    void calcIndexAsync(List<KlineData> klineDataList);

    void calcIndex(List<KlineData> klineDataList, int startIndex, int endIndex);

    void draw(List<? extends KlineData> klineDataList, Canvas canvas, Paint paint);
}
