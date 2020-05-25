/**
 * MIT License
 *
 * Copyright (c) 2020 JohnZh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    public CandlesAuxiliaryLines(int lines, int color, float textSize, float lineWidth, float textMargin) {
        super(lines, color, textSize, lineWidth, textMargin);
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
