package com.johnzh.klinelib.auxiliarylines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.PriceRange;
import com.johnzh.klinelib.indexes.Index;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: generally, horizontal lines are about price, index. vertical lines are about date
 */
public interface AuxiliaryLines<T extends KlineData> extends PriceRange {

    void calcHorizontalLines(List<T> klineDataList, Index<T> curIndex, int startIndex, int endIndex);

    void calcVerticalLines(List<T> klineDataList, int startIndex, int endIndex);

    void onDrawHorizontalLines(KlineView klineView, Canvas canvas, Paint paint);

    void onDrawVerticalLines(KlineView klineView, Canvas canvas, Paint paint);
}