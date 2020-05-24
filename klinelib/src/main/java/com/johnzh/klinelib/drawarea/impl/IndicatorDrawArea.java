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
package com.johnzh.klinelib.drawarea.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.YAxisConverter;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by john on 2020/5/9
 * <p>
 * A draw area which is used to calculate and draw indicator
 */
public class IndicatorDrawArea extends ConvertDrawArea implements YAxisConverter {

    private List<Indicator> mIndicatorList;
    private int mCurIndex;

    @Override
    public void calculate(List<DATA> list, int startIndex, int endIndex) {
        getCurIndicator().calcIndicator(list, startIndex, endIndex);
        getCurIndicator().calcAuxiliaryLines(list, startIndex, endIndex);
    }

    @Override
    public void draw(KlineView klineView, Canvas canvas, Paint paint) {
        getCurIndicator().drawAuxiliaryLines(klineView, this, canvas, paint);
        getCurIndicator().drawIndicator(klineView, this, canvas, paint);
    }

    public IndicatorDrawArea(int height, @NonNull List<Indicator> indicatorList) {
        super(height);
        mIndicatorList = indicatorList;
        mCurIndex = 0;

        if (mIndicatorList.isEmpty()) {
            throw new IllegalArgumentException("indexList cannot be empty");
        }
    }

    public void setIndicatorList(List<Indicator> indicatorList) {
        mIndicatorList = indicatorList;
    }

    public Indicator getCurIndicator() {
        return mIndicatorList.get(mCurIndex);
    }

    public void selectIndicator(int indexesIndex) {
        if (indexesIndex >= 0 && indexesIndex < mIndicatorList.size()){
            if (indexesIndex != mCurIndex) {
                mCurIndex = indexesIndex;
            }
        }
    }

    public List<Indicator> getIndicatorList() {
        return mIndicatorList;
    }

    @Override
    public float getNumber(float drawY) {
        AuxiliaryLines auxiliaryLines = getCurIndicator().getAuxiliaryLines();
        float max = auxiliaryLines.getMax();
        float min = auxiliaryLines.getMin();
        return max - (drawY - top) / height * (max - min);
    }

    @Override
    public float getDrawY(float number) {
        AuxiliaryLines auxiliaryLines = getCurIndicator().getAuxiliaryLines();
        float max = auxiliaryLines.getMax();
        float min = auxiliaryLines.getMin();
        if (number < min || number > max) {
            return -1;
        }
        float offset = (max - number) / (max - min) * height;
        return top + offset;
    }
}
