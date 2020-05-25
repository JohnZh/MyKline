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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.Interval;
import com.johnzh.klinelib.IntervalAccess;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.SharedObjects;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public class SimpleAuxiliaryLines implements AuxiliaryLines {

    protected float[] horizontalLines;

    private int color;
    private float textSize;
    private float lineWidth;
    private float textMargin;

    private void setHorizontalLinePaint(Paint paint) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
    }

    private void setTextPaint(Paint paint) {
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
    }

    public SimpleAuxiliaryLines(int lines, int color, float textSize, float lineWidth, float textMargin) {
        this.horizontalLines = new float[lines < 2 ? 2 : lines];
        this.color = color;
        this.textSize = textSize;
        this.lineWidth = lineWidth;
        this.textMargin = textMargin;
    }

    public int getLines() {
        return horizontalLines.length;
    }

    public int getColor() {
        return color;
    }

    public float getTextSize() {
        return textSize;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public float getTextMargin() {
        return textMargin;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setTextMargin(float textMargin) {
        this.textMargin = textMargin;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setLines(int lines) {
        this.horizontalLines = new float[lines < 2 ? 2 : lines];
    }

    @Override
    public void calcHorizontalLines(List<DATA> dataList, Indicator indicator, int startIndex, int endIndex) {
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        if (indicator instanceof IntervalAccess) {
            Interval interval = ((IntervalAccess) indicator).getInterval();
            max = Math.max(max, interval.getMax());
            min = Math.min(min, interval.getMin());
        }

        float interval = FloatCalc.get().subtraction(max, min);
        interval = FloatCalc.get().divide(interval, horizontalLines.length - 1);

        horizontalLines[0] = max;
        horizontalLines[horizontalLines.length - 1] = min;
        for (int i = horizontalLines.length - 2; i > 0; i--) {
            horizontalLines[i] = FloatCalc.get().add(horizontalLines[i + 1], interval);
        }
    }

    @Override
    public void calcVerticalLines(List<DATA> dataList, Indicator indicator, int startIndex, int endIndex) {

    }

    @Override
    public void drawHorizontalLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        SharedObjects sharedObjects = klineView.getSharedObjects();
        float width = drawArea.getWidth();
        float left = drawArea.getLeft();
        int scale = FloatCalc.get().getScale(horizontalLines[0]);
        scale = scale != FloatCalc.get().getFormatScale().getMinScale()
                ? FloatCalc.get().getFormatScale().getMaxScale()
                : FloatCalc.get().getFormatScale().getMinScale();
        for (int i = 0; i < horizontalLines.length; i++) {
            float number = horizontalLines[i];
            float top = drawArea.getDrawY(number);
            float right = left + width;
            Path path = sharedObjects.getObject(Path.class);
            path.moveTo(left, top);
            path.lineTo(right, top);
            setHorizontalLinePaint(paint);
            canvas.drawPath(path, paint);

            setTextPaint(paint);
            String priceText = FloatCalc.get().format(number, scale);
            if (i == 0) {
                float textRight = right - textMargin;
                float textTop = top + textMargin;
                DrawTextTool.drawTextFromRightTop(priceText, textRight, textTop, canvas, paint);
            } else {
                float textRight = right - textMargin;
                float textBottom = top - textMargin;
                DrawTextTool.drawTextFromRightBottom(priceText, textRight, textBottom, canvas, paint);
            }
        }
    }

    @Override
    public void drawVerticalLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {

    }

    @Override
    public float getMax() {
        return horizontalLines[0];
    }

    @Override
    public float getMin() {
        return horizontalLines[horizontalLines.length - 1];
    }
}
