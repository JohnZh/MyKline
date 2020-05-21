package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.IndicatorData;
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
public class WRIndicator extends AbsIndicator {

    private final int[] wr;
    private final int[] colors;
    private final float lineWidth;
    private final float textSize;
    private final float textMargin;

    public WRIndicator(AuxiliaryLines auxiliaryLines, int[] wr, int[] colors,
                       float lineWidth, float textSize, float textMargin) {
        super(auxiliaryLines);
        this.wr = wr;
        this.textSize = textSize;
        this.colors = colors;
        this.textMargin = textMargin;
        this.lineWidth = lineWidth;

        if (wr.length > colors.length) {
            throw new IllegalArgumentException("wr.length is larger than colors.length");
        }
    }

    @Override
    protected void calcMaxMinPreCalcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex) {
    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
        resetMaxMin();

        for (int i = startIndex; i < endIndex; i++) {
            IndicatorData indicator = dataList.get(i).getIndicator();
            for (int wr : this.wr) {
                if (i - wr + 1 < 0) continue; // data is not enough

                WR wrData = indicator.get(WR.class);
                Float wrValue = wrData.get(wr);
                if (wrValue != null) {
                    updateMaxMin(wrValue);
                    continue;
                }

                wrValue = calcWrValue(dataList, i, wr);
                wrData.put(wr, wrValue);
                updateMaxMin(wrValue);
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
            int color = colors[i];
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
        int scale = FloatCalc.get().getScale(data.getClosePrice());
        scale = Math.max(2, scale);
        StringBuilder builder = klineView.getSharedObjects().getObject(StringBuilder.class);

        float textLeft = drawArea.getLeft();
        for (int i = 0; i < wr.length; i++) {
            int wrKey = wr[i];
            int color = colors[i];
            Float wrValue = data.getIndicator().get(WR.class).get(wrKey);
            if (wrValue == null) continue;

            String text = builder.append("WR").append(wrKey).append(":")
                    .append(FloatCalc.get().format(wrValue, scale))
                    .append("  ")
                    .toString();
            paint.setTextSize(textSize);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            float textWidth = paint.measureText(text);
            float textBottom = drawArea.getTop() + textMargin;
            DrawTextTool.drawTextFromLeftTop(text, textLeft, textBottom, canvas, paint);
            textLeft += textWidth;
            builder.setLength(0);
        }
    }
}
