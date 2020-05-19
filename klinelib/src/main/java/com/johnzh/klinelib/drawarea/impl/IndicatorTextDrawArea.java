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
package com.johnzh.klinelib.drawarea.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/19
 *
 * <p>A draw area which is used to display indicator detail of last data above indicator draw area generally </p>
 */
public class IndicatorTextDrawArea extends ConvertDrawArea {

    private boolean mDrawable;
    private IndicatorDrawArea mIndicatorDrawArea;

    public IndicatorTextDrawArea(int height, IndicatorDrawArea indicatorDrawArea) {
        super(height);
        mIndicatorDrawArea = indicatorDrawArea;
        mDrawable = true;
    }

    public boolean isDrawable() {
        return mDrawable;
    }

    public void setDrawable(boolean drawable) {
        mDrawable = drawable;
    }

    @Override
    public void calculate(List<DATA> list, int startIndex, int endIndex) {
    }

    @Override
    public void draw(KlineView klineView, Canvas canvas, Paint paint) {
        if (mDrawable) {
            Indicator curIndicator = mIndicatorDrawArea.getCurIndicator();
            curIndicator.drawIndicatorText(klineView, this, canvas, paint);
        }
    }
}
