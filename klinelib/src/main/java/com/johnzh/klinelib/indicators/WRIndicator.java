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
import com.johnzh.klinelib.Interval;
import com.johnzh.klinelib.IntervalAccess;
import com.johnzh.klinelib.IntervalImpl;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.indicators.data.WR;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/21
 *
 * <p>WR indicator</p>
 */
public class WRIndicator extends AbsIndicator implements IntervalAccess {

    private int[] wr;
    private int[] wrColors;
    private float lineWidth;
    private float textSize;
    private final IntervalImpl interval;

    public WRIndicator(AuxiliaryLines auxiliaryLines, int[] wr, int[] wrColors,
                       float lineWidth, float textSize) {
        super(auxiliaryLines);
        this.wr = wr;
        this.wrColors = wrColors;
        this.textSize = textSize;
        this.lineWidth = lineWidth;
        this.interval = new IntervalImpl();

        if (wr.length > wrColors.length) {
            throw new IllegalArgumentException("wr.length is larger than colors.length");
        }
    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
        interval.reset();

        for (int i = startIndex; i < endIndex; i++) {
            IndicatorData indicator = dataList.get(i).getIndicator();
            for (int wr : this.wr) {
                if (i - wr + 1 < 0) continue; // data is not enough

                WR wrData = indicator.get(WR.class);
                Float wrValue = wrData.get(wr);
                if (wrValue != null) {
                    interval.updateMaxMin(wrValue);
                    continue;
                }

                wrValue = calcWrValue(dataList, i, wr);
                wrData.put(wr, wrValue);
                interval.updateMaxMin(wrValue);
            }
        }
    }

    private Float calcWrValue(List<DATA> dataList, int curIndex, int wr) {
        int start = curIndex - wr + 1;
        float highest = Float.MIN_VALUE;
        float lowest = Float.MAX_VALUE;
        for (int i = start; i <= curIndex; i++) {
            highest = Math.max(highest, dataList.get(i).getHighestPrice());
            lowest = Math.min(lowest, dataList.get(i).getLowestPrice());
        }

        if (highest - lowest == 0) return 0f;

        return (highest - dataList.get(curIndex).getClosePrice()) / (highest - lowest) * 100;
    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        List<DATA> dataList = klineView.getDataList();
        int startIndex = klineView.getStartIndex();
        int endIndex = klineView.getEndIndex();

        for (int i = 0; i < wr.length; i++) {
            int wrKey = wr[i];
            int color = wrColors[i];
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(color);

            float startX = -1;
            float startY = -1;
            for (int j = startIndex; j < endIndex; j++) {
                Float wrValue = dataList.get(j).getIndicator().get(WR.class).get(wrKey);
                if (wrValue == null) continue;

                float drawX = drawArea.getDrawX(drawArea.getVisibleIndex(j));
                float drawY = drawArea.getDrawY(wrValue.floatValue());
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
    public void drawIndicatorText(KlineView klineView, DrawArea drawArea, DATA data, Canvas canvas, Paint paint) {
        StringBuilder builder = klineView.getSharedObjects().getObject(StringBuilder.class);
        WR wrData = data.getIndicator().get(WR.class);
        int scale = FloatCalc.get().getFormatScale().getMaxScale();

        float textLeft = drawArea.getLeft();
        for (int i = 0; i < this.wr.length; i++) {
            int wrKey = this.wr[i];
            int color = wrColors[i];
            Float wrValue = wrData.get(wrKey);
            if (wrValue == null) continue;

            String text = builder.append("WR").append(wrKey).append(":")
                    .append(FloatCalc.get().format(wrValue, scale))
                    .append("  ")
                    .toString();
            paint.setTextSize(textSize);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            float textWidth = paint.measureText(text);
            float textCenterX = drawArea.getTop() + drawArea.getHeight() / 2;
            DrawTextTool.drawTextFromLeftCenterX(text, textLeft, textCenterX, canvas, paint);
            textLeft += textWidth;
            builder.setLength(0);
        }
    }

    @Override
    public Interval getInterval() {
        return interval;
    }
}
