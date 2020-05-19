package com.johnzh.klinelib.auxiliarylines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.ValueRange;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: generally, horizontal lines are about price, index. vertical lines are about date
 */
public interface AuxiliaryLines extends ValueRange {

    void calcHorizontalLines(List<DATA> dataList, Indicator curIndicator, int startIndex, int endIndex);

    void calcVerticalLines(List<DATA> dataList, int startIndex, int endIndex);

    void drawHorizontalLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint);

    void drawVerticalLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint);
}