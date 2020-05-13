package com.johnzh.klinelib.auxiliarylines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.johnzh.klinelib.DrawArea;
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.PriceRange;
import com.johnzh.klinelib.SharedObjects;
import com.johnzh.klinelib.indexes.Index;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description:
 */
public class DefaultAuxiliaryLines implements AuxiliaryLines<KlineData> {

    private float[] horizontalLines;
    private float fontSize;
    private float lineWidth;
    private float textMargin;
    private int color;

    public DefaultAuxiliaryLines(int lines, float fontSize, float lineWidth, float textMargin, int color) {
        this.horizontalLines = new float[lines];
        this.fontSize = fontSize;
        this.lineWidth = lineWidth;
        this.textMargin = textMargin;
        this.color = color;
    }

    public void setHorizontalLinePaint(Paint paint) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
    }

    public void setTextPaint(Paint paint) {
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void calcHorizontalLines(List<KlineData> klineDataList, Index<KlineData> curIndex, int startIndex, int endIndex) {
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        for (int i = startIndex; i < endIndex; i++) {
            KlineData data = klineDataList.get(i);
            max = Math.max(max, data.getHighestPrice());
            min = Math.min(min, data.getLowestPrice());
        }

        if (curIndex instanceof PriceRange) {
            max = Math.max(((PriceRange) curIndex).getMaxPrice(), max);
            min = Math.min(((PriceRange) curIndex).getMinPrice(), min);
        }

        if (max == Float.MIN_VALUE || min == Float.MAX_VALUE) return;

        float interval = FloatCalc.get().subtraction(max, min);
        interval = FloatCalc.get().divide(interval, horizontalLines.length - 1);

        horizontalLines[0] = max;
        horizontalLines[horizontalLines.length - 1] = min;
        for (int i = horizontalLines.length - 2; i > 0; i--) {
            horizontalLines[i] = FloatCalc.get().add(horizontalLines[i + 1], interval);
        }
    }

    @Override
    public void calcVerticalLines(List<KlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void onDrawHorizontalLines(KlineView klineView, Canvas canvas, Paint paint) {
        DrawArea drawArea = klineView.getDrawArea();
        SharedObjects sharedObjects = klineView.getSharedObjects();
        float width = drawArea.getWidth();
        float left = drawArea.getLeft();
        for (int i = 0; i < horizontalLines.length; i++) {
            float price = horizontalLines[i];
            float top = drawArea.getDataY(price);
            float right = left + width;
            Path path = (Path) sharedObjects.getObject(Path.class);
            path.moveTo(left, top);
            path.lineTo(right, top);
            setHorizontalLinePaint(paint);
            canvas.drawPath(path, paint);

            setTextPaint(paint);
            StringBuilder builder = (StringBuilder) sharedObjects.getObject(StringBuilder.class);
            String priceText = builder.append(price).toString();
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
    public void onDrawVerticalLines(KlineView klineView, Canvas canvas, Paint paint) {

    }

    @Override
    public float getMaxPrice() {
        return horizontalLines[0];
    }

    @Override
    public float getMinPrice() {
        return horizontalLines[horizontalLines.length - 1];
    }
}