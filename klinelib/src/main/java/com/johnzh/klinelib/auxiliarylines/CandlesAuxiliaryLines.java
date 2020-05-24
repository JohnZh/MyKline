package com.johnzh.klinelib.auxiliarylines;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.Interval;
import com.johnzh.klinelib.IntervalAccess;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/24
 *
 * <p>This auxiliary lines is specific for candles </p>
 */
public class CandlesAuxiliaryLines extends SimpleAuxiliaryLines {

    public CandlesAuxiliaryLines(int lines, float textSize, float lineWidth, float textMargin, int color) {
        super(lines, textSize, lineWidth, textMargin, color);
    }

    @Override
    public void calcHorizontalLines(List<DATA> dataList, Indicator indicator, int startIndex, int endIndex) {
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        for (int i = startIndex; i < endIndex; i++) {
            DATA data = dataList.get(i);
            max = Math.max(max, data.getHighestPrice());
            min = Math.min(min, data.getLowestPrice());
        }

        if (indicator instanceof IntervalAccess) {
            Interval interval = ((IntervalAccess) indicator).getInterval();
            max = Math.max(max, interval.getMax());
            min = Math.min(min, interval.getMin());
        }

        float lineSpace = FloatCalc.get().subtraction(max, min);
        lineSpace = FloatCalc.get().divide(lineSpace, horizontalLines.length - 1);

        horizontalLines[0] = max;
        horizontalLines[horizontalLines.length - 1] = min;
        for (int i = horizontalLines.length - 2; i > 0; i--) {
            horizontalLines[i] = FloatCalc.get().add(horizontalLines[i + 1], lineSpace);
        }
    }
}
