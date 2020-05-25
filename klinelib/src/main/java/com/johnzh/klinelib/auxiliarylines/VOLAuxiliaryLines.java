package com.johnzh.klinelib.auxiliarylines;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * The particular AuxiliaryLines for VOL
 */
public class VOLAuxiliaryLines extends SimpleAuxiliaryLines {

    public VOLAuxiliaryLines(int lines, int color, float fontSize, float lineWidth, float textMargin) {
        super(lines, color, fontSize, lineWidth, textMargin);
    }

    @Override
    public void calcHorizontalLines(List<DATA> dataList, Indicator indicator, int startIndex, int endIndex) {
        float max = Float.MIN_VALUE;
        float min = 0;

        for (int i = startIndex; i < endIndex; i++) {
            KlineData data = dataList.get(i);
            max = Math.max(max, data.getVolume());
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
