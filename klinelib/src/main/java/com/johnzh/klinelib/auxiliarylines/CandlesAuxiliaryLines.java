package com.johnzh.klinelib.auxiliarylines;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.ValueRange;
import com.johnzh.klinelib.indicators.Index;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description:
 */
public class CandlesAuxiliaryLines extends SimpleAuxiliaryLines {


    public CandlesAuxiliaryLines(int lines, float fontSize, float lineWidth, float textMargin, int color) {
        super(lines, fontSize, lineWidth, textMargin, color);
    }

    @Override
    public void calcHorizontalLines(List<DATA> klineDataList, Index curIndex, int startIndex, int endIndex) {
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        if (curIndex instanceof ValueRange) {
            max = Math.max(((ValueRange) curIndex).getMaximum(), max);
            min = Math.min(((ValueRange) curIndex).getMinimum(), min);
        }

        for (int i = startIndex; i < endIndex; i++) {
            KlineData data = klineDataList.get(i);
            max = Math.max(max, data.getHighestPrice());
            min = Math.min(min, data.getLowestPrice());
        }

        if (max == Float.MIN_VALUE || min == Float.MAX_VALUE) return;

        float interval = FloatCalc.get().subtraction(max, min);
        interval = FloatCalc.get().divide(interval, horizontalLines.length - 1);

        horizontalLines[0] = max;
        horizontalLines[horizontalLines.length - 1] = min;
        for (int i = horizontalLines.length - 2; i > 0; i--) {
            horizontalLines[i] = FloatCalc.get().add(horizontalLines[i + 1], interval);
        }
    }
}
