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
import com.johnzh.klinelib.Interval;
import com.johnzh.klinelib.IntervalAccess;
import com.johnzh.klinelib.IntervalImpl;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.indicators.data.BOLL;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/20
 *
 * <p>BOLL indicator</p>
 */
public class BOLLIndicator extends AbsIndicator implements IntervalAccess {

    private static final String[] TITLES = {"BOLL:", "UPPER:", "LOWER:"};

    private PureKIndicator pureKIndicator;
    private int[] boll;
    private int[] bollColors;
    private float lineWidth;
    private float textSize;
    private IntervalImpl interval;

    /**
     * Constructor of BOLL indicator
     *
     * @param pureKIndicator
     * @param boll {20, 2} means N:20, P:2 of BOLL
     * @param bollColors colors for UPPER, MID, LOWER
     * @param lineWidth
     * @param textSize
     */
    public BOLLIndicator(PureKIndicator pureKIndicator, int[] boll, int[] bollColors,
                         float lineWidth, float textSize) {
        super(pureKIndicator.getAuxiliaryLines());
        this.pureKIndicator = pureKIndicator;
        this.boll = boll;
        this.bollColors = bollColors;
        this.lineWidth = lineWidth;
        this.textSize = textSize;
        this.interval = new IntervalImpl();
        if (this.boll.length < 2) {
            throw new IllegalArgumentException("boll.length must be larger than 2");
        }
    }

    public void setPureKIndicator(PureKIndicator pureKIndicator) {
        this.pureKIndicator = pureKIndicator;
    }

    public void setBoll(int[] boll) {
        this.boll = boll;
    }

    public void setBollColors(int[] bollColors) {
        this.bollColors = bollColors;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
        interval.reset();

        int n = boll[0];
        int p = boll[1];
        for (int i = startIndex; i < endIndex; i++) {
            IndicatorData indicator = dataList.get(i).getIndicator();
            if (i - n < -1) continue; // data is not enough

            BOLL boll = indicator.get(BOLL.class);
            if (boll.getMID() != null) {
                interval.updateMaxMin(boll.getUPPER(), boll.getLOWER());
                continue;
            }

            Float midValue = calcMIDValue(dataList, i, n);
            boll.setMID(midValue);
            float md = calcMDValue(dataList, i, midValue, n);
            float upper = midValue + p * md;
            float lower = midValue - p * md;
            boll.setUPPER(upper);
            boll.setLOWER(lower);
            interval.updateMaxMin(upper, lower);
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

    private Float calcMIDValue(List<DATA> dataList, int curIndex, int n) {
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
            paint.setColor(bollColors[i]);
            float startX = -1;
            float startY = -1;
            int startIndex = klineView.getStartIndex();
            int endIndex = klineView.getEndIndex();
            for (int j = startIndex; j < endIndex; j++) {
                BOLL boll = dataList.get(j).getIndicator().get(BOLL.class);
                Float maValue = boll.getMID();
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
        int scale = FloatCalc.get().getFormatScale().getMaxScale();
        if (boll.getMID() != null) {
            paint.setTextSize(textSize);
            paint.setStyle(Paint.Style.FILL);
            StringBuilder builder = klineView.getSharedObjects().getObject(StringBuilder.class);
            float[] values = {boll.getUPPER(), boll.getMID(), boll.getLOWER()};
            float textLeft = drawArea.getLeft();
            for (int i = 0; i < 3; i++) {
                paint.setColor(bollColors[i]);
                String text = builder.append(TITLES[i])
                        .append(FloatCalc.get().format(values[i], scale))
                        .append("  ")
                        .toString();
                float textWidth = paint.measureText(text);
                float textCenterX = drawArea.getTop() + drawArea.getHeight() / 2;
                DrawTextTool.drawTextFromLeftCenterX(text, textLeft, textCenterX, canvas, paint);
                builder.setLength(0);
                textLeft += textWidth;
            }
        }
    }

    @Override
    public Interval getInterval() {
        return interval;
    }
}
