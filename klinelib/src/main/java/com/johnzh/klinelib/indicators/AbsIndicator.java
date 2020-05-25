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
package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public abstract class AbsIndicator implements Indicator {

    private AuxiliaryLines auxiliaryLines;


    public AbsIndicator(AuxiliaryLines auxiliaryLines) {
        this.auxiliaryLines = auxiliaryLines;
    }

    @Override
    public void calcIndicatorAsync(List<DATA> dataList) {
    }

    @Override
    public void calcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex) {
        this.auxiliaryLines.calcHorizontalLines(dataList, this, startIndex, endIndex);
        this.auxiliaryLines.calcVerticalLines(dataList, this, startIndex, endIndex);
    }

    @Override
    public void drawAuxiliaryLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        this.auxiliaryLines.drawHorizontalLines(klineView, drawArea, canvas, paint);
        this.auxiliaryLines.drawVerticalLines(klineView, drawArea, canvas, paint);
    }

    @Override
    public AuxiliaryLines getAuxiliaryLines() {
        return auxiliaryLines;
    }
}
