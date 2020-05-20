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
import com.johnzh.klinelib.indicators.data.BOLL;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/20
 *
 * <p>BOLL indicator</p>
 */
public class BOLLIndicator extends AbsIndicator implements ValueRange {

    private static final String[] TITLES = {"BOLL:", "UPPER:", "LOWER:"};

    private PureKIndicator pureKIndicator;
    private int[] boll;
    private int[] colors;
    private float lineWidth;
    private float textSize;
    private float textMargin;

    /**
     * Constructor of BOLL indicator
     *
     * @param pureKIndicator
     * @param boll {20, 2} means N:20, P:2 of BOLL
     * @param colors colors for UPPER, MID, LOWER
     * @param lineWidth
     * @param textSize
     * @param textMargin
     */
    public BOLLIndicator(PureKIndicator pureKIndicator, int[] boll, int[] colors,
                         float lineWidth, float textSize, float textMargin) {
        super(pureKIndicator.getAuxiliaryLines());
        this.pureKIndicator = pureKIndicator;
        this.boll = boll;
        this.colors = colors;
        this.lineWidth = lineWidth;
        this.textSize = textSize;
        this.textMargin = textMargin;
        if (this.boll.length < 2) {
            throw new IllegalArgumentException("boll.length must be larger than 2");
        }
    }

    @Override
    public void calcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex) {
        pureKIndicator.calcAuxiliaryLines(dataList, startIndex, endIndex);
        super.calcAuxiliaryLines(dataList, startIndex, endIndex);
    }

    @Override
    public void calcIndicatorAsync(List<DATA> dataList) {
    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
        pureKIndicator.calcIndicator(dataList, startIndex, endIndex);

        resetMaxMin();

        int n = boll[0];
        int p = boll[1];
        for (int i = startIndex; i < endIndex; i++) {
            IndicatorData indicator = dataList.get(i).getIndicator();
            if (i - n < -1) continue; // data is not enough

            BOLL boll = indicator.get(BOLL.class);
            if (boll.getMA() != null) {
                updateMaxMin(boll.getUPPER(), boll.getLOWER());
                continue;
            }

            Float maValue = calcMAValue(dataList, i, n);
            boll.setMA(maValue);
            float md = calcMDValue(dataList, i, maValue, n);
            float upper = maValue + p * md;
            float lower = maValue - p * md;
            boll.setUPPER(upper);
            boll.setLOWER(lower);
            updateMaxMin(upper, lower);
        }
    }

    private float calcMDValue(List<DATA> dataList, int curIndex, float maValue, int n) {
        double sum = 0;
        int start = curIndex - n + 1;
        for (int i = start; i < start + n; i++) {
            sum += Math.pow(dataList.get(i).getClosePrice() - maValue, 2);
        }
        return (float) Math.sqrt(sum / n);
    }

    private Float calcMAValue(List<DATA> dataList, int curIndex, int n) {
        int start = curIndex - n + 1;
        float result = 0;
        for (int i = start; i <= curIndex; i++) {
            float closePrice = dataList.get(i).getClosePrice();
            result += closePrice;
        }
        return result / n;
    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        pureKIndicator.drawIndicator(klineView, drawArea, canvas, paint);

        List<DATA> dataList = klineView.getDataList();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        for (int i = 0; i < 3; i++) {
            paint.setColor(colors[i]);
            float startX = -1;
            float startY = -1;
            int startIndex = klineView.getStartIndex();
            int endIndex = klineView.getEndIndex();
            for (int j = startIndex; j < endIndex; j++) {
                BOLL boll = dataList.get(j).getIndicator().get(BOLL.class);
                Float maValue = boll.getMA();
                if (maValue == null) continue;

                float number = maValue.floatValue();
                if (i == 0) {
                    number = boll.getUPPER().floatValue();
                } else if (i == 2) {
                    number = boll.getLOWER().floatValue();
                }

                float drawX = drawArea.getDrawX(drawArea.getVisibleIndex(j));
                float drawY = drawArea.getDrawY(number);
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
        BOLL boll = data.getIndicator().get(BOLL.class);
        int scale = FloatCalc.get().getScale(data.getClosePrice());
        scale = Math.max(2, scale);
        if (boll.getMA() != null) {
            paint.setTextSize(textSize);
            paint.setStyle(Paint.Style.FILL);
            StringBuilder builder = klineView.getSharedObjects().getObject(StringBuilder.class);
            float[] values = {boll.getUPPER(), boll.getMA(), boll.getLOWER()};
            float textLeft = drawArea.getLeft();
            for (int i = 0; i < 3; i++) {
                paint.setColor(colors[i]);
                String text = builder.append(TITLES[i])
                        .append(FloatCalc.get().format(values[i], scale))
                        .append("  ")
                        .toString();
                float textWidth = paint.measureText(text);
                float textBottom = drawArea.getTop() + drawArea.getHeight() - textMargin;
                DrawTextTool.drawTextFromLeftBottom(text, textLeft, textBottom, canvas, paint);
                builder.setLength(0);
                textLeft += textWidth;
            }
        }
    }
}
