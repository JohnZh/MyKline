package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.IndicatorData;
import com.johnzh.klinelib.KlineData;
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

    private PureKIndicator pureKIndex;
    private float lineWidth;
    private float textSize;
    private int[] ma;
    private int[] colors;

    private float maxPrice;
    private float minPrice;

    public MAIndicator(PureKIndicator pureKIndex,
                       float lineWidth, float textSize,
                       int[] ma, int[] colors) {
        super(pureKIndex.getAuxiliaryLines());
        this.pureKIndex = pureKIndex;
        this.lineWidth = lineWidth;
        this.textSize = textSize;
        this.ma = ma;
        this.colors = colors;
        if (ma.length != colors.length) {
            throw new IllegalArgumentException("ma.length is not equal to maColors.length");
        }
    }

    @Override
    public float getMaximum() {
        return maxPrice;
    }

    @Override
    public float getMinimum() {
        return minPrice;
    }

    @Override
    public void calcIndicatorAsync(List<DATA> dataList) {
    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
        resetMaxMinPrice();

        for (int i = startIndex; i < endIndex; i++) {
            IndicatorData indicatorData = dataList.get(i).getIndicator();
            if (indicatorData == null) {
                indicatorData = dataList.get(i).newIndicator();
            }

            for (int maKey : this.ma) {
                if (i - maKey < -1) continue; // data is not enough

                Float maValue = indicatorData.get(MA.class).get(maKey);
                if (maValue != null) {
                    updateMaxMinPrice(maValue);
                    continue;
                }

                Float newMaValue = calcMaValue(dataList, i, maKey);
                updateMaxMinPrice(newMaValue);
                indicatorData.get(MA.class).put(maKey, newMaValue);
            }
            dataList.get(i).setIndicator(indicatorData);
        }
    }

    private void resetMaxMinPrice() {
        maxPrice = Float.MIN_VALUE;
        minPrice = Float.MAX_VALUE;
    }

    private void updateMaxMinPrice(Float price) {
        maxPrice = Math.max(maxPrice, price);
        minPrice = Math.min(minPrice, price);
    }

    private Float calcMaValue(List<? extends KlineData> klineDataList, int curIndex, int maKey) {
        int start = curIndex - maKey + 1;
        float result = 0;
        for (int i = start; i <= curIndex; i++) {
            float closePrice = klineDataList.get(i).getClosePrice();
            result = FloatCalc.get().add(result, closePrice);
        }
        return FloatCalc.get().divide(result, maKey);
    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        pureKIndex.drawIndicator(klineView, drawArea, canvas, paint);

        List<DATA> dataList = klineView.getDataList();

        for (int i = 0; i < ma.length; i++) {
            int maKey = ma[i];
            int maColor = colors[i];
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(maColor);

            float startX = -1;
            float startY = -1;
            int startIndex = klineView.getStartIndex();
            int endIndex = klineView.getEndIndex();
            for (int j = startIndex; j < endIndex; j++) {
                Float maValue = dataList.get(j).getIndicator().get(MA.class).get(maKey);
                if (maValue == null) continue;
                float dataX = drawArea.getDrawX(drawArea.getVisibleIndex(j));
                float dataY = drawArea.getDrawY(maValue.floatValue());
                if (startX == -1 && startY == -1) { // first point
                    startX = dataX;
                    startY = dataY;
                } else {
                    canvas.drawLine(startX, startY, dataX, dataY, paint);
                    startX = dataX;
                    startY = dataY;
                }
            }
        }
    }

    @Override
    public void drawIndicatorText(KlineView klineView, DrawArea drawArea, DATA data,
                                  Canvas canvas, Paint paint) {
        float textLeft = drawArea.getLeft();
        for (int i = 0; i < ma.length; i++) {
            int maKey = ma[i];
            int maColor = colors[i];
            Float maValue = data.getIndicator().get(MA.class).get(maKey);
            if (maValue == null) continue;
            int scale = FloatCalc.get().getScale(data.getClosePrice());
            StringBuilder builder = klineView.getSharedObjects().getObject(StringBuilder.class);
            String text = builder.append("MA").append(maKey).append(":")
                    .append(FloatCalc.get().format(maValue, scale))
                    .append("  ")
                    .toString();
            paint.setTextSize(textSize);
            paint.setColor(maColor);
            paint.setStyle(Paint.Style.FILL);
            float textWidth = paint.measureText(text);
            float textBottom = drawArea.getTop() + drawArea.getHeight();
            DrawTextTool.drawTextFromLeftBottom(text, textLeft, textBottom, canvas, paint);
            textLeft += textWidth;
        }
    }
}
