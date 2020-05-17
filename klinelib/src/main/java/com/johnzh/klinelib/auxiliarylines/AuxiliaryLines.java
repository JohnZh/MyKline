package com.johnzh.klinelib.auxiliarylines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.ValueRange;
import com.johnzh.klinelib.drawarea.IndexDrawArea;
import com.johnzh.klinelib.indicators.Index;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: generally, horizontal lines are about price, index. vertical lines are about date
 */
public interface AuxiliaryLines<T extends KlineData> extends ValueRange {

    void calcHorizontalLines(List<T> klineDataList, Index<T> curIndex, int startIndex, int endIndex);

    void calcVerticalLines(List<T> klineDataList, int startIndex, int endIndex);

    void drawHorizontalLines(KlineView klineView, IndexDrawArea drawArea, Canvas canvas, Paint paint);

    void drawVerticalLines(KlineView klineView, IndexDrawArea drawArea, Canvas canvas, Paint paint);
}