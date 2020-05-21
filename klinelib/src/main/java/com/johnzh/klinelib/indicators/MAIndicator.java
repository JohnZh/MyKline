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
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.IndicatorData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.ValueRange;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.indicators.data.MA;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class MAIndicator extends AbsIndicator implements ValueRange {

    private PureKIndicator pureKIndicator;
    private float lineWidth;
    private float textSize;
    private float textMargin;
    private int[] ma;
    private int[] colors;

    /**
     * Constructor of MA indicator
     *
     * @param pureKIndicator
     * @param ma {5, 10, 20} means MA5, MA10, MA20
     * @param colors colors for ma[i]
     * @param lineWidth
     * @param textSize
     * @param textMargin
     */
    public MAIndicator(PureKIndicator pureKIndicator,
                       int[] ma, int[] colors, float lineWidth, float textSize, float textMargin) {
        super(pureKIndicator.getAuxiliaryLines());
        this.pureKIndicator = pureKIndicator;
        this.lineWidth = lineWidth;
        this.textSize = textSize;
        this.textMargin = textMargin;
        this.ma = ma;
        this.colors = colors;
        if (ma.length > colors.length) {
            throw new IllegalArgumentException("ma.length is larger than maColors.length");
        }
    }

    @Override
    public void calcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex) {
        pureKIndicator.calcAuxiliaryLines(dataList, startIndex, endIndex);
        super.calcAuxiliaryLines(dataList, startIndex, endIndex);
    }

    @Override
    protected void calcMaxMinPreCalcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex) {
    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
        pureKIndicator.calcIndicator(dataList, startIndex, endIndex);

        resetMaxMin();

        for (int i = startIndex; i < endIndex; i++) {
            IndicatorData indicator = dataList.get(i).getIndicator();

            for (int maKey : this.ma) {
                if (i - maKey < -1) continue; // data is not enough

                MA ma = indicator.get(MA.class);
                Float maValue = ma.get(maKey);
                if (maValue != null) {
                    updateMaxMin(maValue);
                    continue;
                }

                Float newMaValue = calcMaValue(dataList, i, maKey);
                ma.put(maKey, newMaValue);
                updateMaxMin(newMaValue);
            }
        }
    }

    private Float calcMaValue(List<DATA> dataList, int curIndex, int maKey) {
        int start = curIndex - maKey + 1;
        float result = 0;
        for (int i = start; i <= curIndex; i++) {
            float closePrice = dataList.get(i).getClosePrice();
            result += closePrice;
        }
        return result / maKey;
    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        pureKIndicator.drawIndicator(klineView, drawArea, canvas, paint);

        List<DATA> dataList = klineView.getDataList();
        int startIndex = klineView.getStartIndex();
        int endIndex = klineView.getEndIndex();

        for (int i = 0; i < ma.length; i++) {
            int maKey = ma[i];
            int maColor = colors[i];
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(maColor);

            float startX = -1;
            float startY = -1;
            for (int j = startIndex; j < endIndex; j++) {
                Float maValue = dataList.get(j).getIndicator().get(MA.class).get(maKey);
                if (maValue == null) continue;
                float drawX = drawArea.getDrawX(drawArea.getVisibleIndex(j));
                float drawY = drawArea.getDrawY(maValue.floatValue());
                if (startX == -1 && startY == -1) { // first point
                    startX = drawX;
                    startY = drawY;
                } else {
                    canvas.drawLine(startX, startY, drawX, drawY, paint);
                    startX = drawX;
                    startY = drawY;
                }
            }
        }
    }

    @Override
    public void drawIndicatorText(KlineView klineView, DrawArea drawArea, DATA data,
                                  Canvas canvas, Paint paint) {
        MA maData = data.getIndicator().get(MA.class);
        StringBuilder builder = klineView.getSharedObjects().getObject(StringBuilder.class);
        int scale = FloatCalc.get().getFormatScale().getMaxScale();

        float textLeft = drawArea.getLeft();
        for (int i = 0; i < ma.length; i++) {
            int maKey = ma[i];
            int maColor = colors[i];
            Float maValue = maData.get(maKey);
            if (maValue == null) continue;

            String text = builder.append("MA").append(maKey).append(":")
                    .append(FloatCalc.get().format(maValue, scale))
                    .append("  ")
                    .toString();
            paint.setTextSize(textSize);
            paint.setColor(maColor);
            paint.setStyle(Paint.Style.FILL);
            float textWidth = paint.measureText(text);
            float textBottom = drawArea.getTop() + drawArea.getHeight() - textMargin;
            DrawTextTool.drawTextFromLeftBottom(text, textLeft, textBottom, canvas, paint);
            textLeft += textWidth;
            builder.setLength(0);
        }
    }
}
