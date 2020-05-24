/**
 * MIT License
 * <p>
 * Copyright (c) 2020 JohnZh
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.johnzh.klinelib.auxiliarylines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.Interval;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Generally, horizontal lines are about price, indicator. vertical lines are about date
 */
public interface AuxiliaryLines extends Interval {

    void calcHorizontalLines(List<DATA> dataList, Indicator indicator, int startIndex, int endIndex);

    void calcVerticalLines(List<DATA> dataList, Indicator indicator, int startIndex, int endIndex);

    void drawHorizontalLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint);

    void drawVerticalLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint);
}