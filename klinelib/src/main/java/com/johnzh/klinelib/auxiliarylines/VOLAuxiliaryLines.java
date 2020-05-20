package com.johnzh.klinelib.auxiliarylines;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * The particular AuxiliaryLines for VOL
 */
public class VOLAuxiliaryLines extends SimpleAuxiliaryLines {

    public VOLAuxiliaryLines(int lines, float fontSize, float lineWidth, float textMargin, int color) {
        super(lines, fontSize, lineWidth, textMargin, color);
    }

    @Override
    public void calcHorizontalLines(List<DATA> klineDataList, Indicator curIndicator, int startIndex, int endIndex) {
        max = Math.max(curIndicator.getMaximum(), max);
        min = 0;

        float interval = FloatCalc.get().subtraction(max, min);
        interval = FloatCalc.get().divide(interval, horizontalLines.length - 1);

        horizontalLines[0] = max;
        horizontalLines[horizontalLines.length - 1] = min;
        for (int i = horizontalLines.length - 2; i > 0; i--) {
            horizontalLines[i] = FloatCalc.get().add(horizontalLines[i + 1], interval);
        }
    }
}
