package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.IndexDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description: no index implement, just k line candles
 */
public class PureKIndex extends AbsIndex {

    private int[] colors;
    private float dataPaddingHorizontal;
    private float candleLineWidth;

    /**
     * @param auxiliaryLines
     * @param colors                colors[0] is positiveColor, colors[1] is negativeColor
     * @param dataPaddingHorizontal
     * @param candleLineWidth
     */
    public PureKIndex(AuxiliaryLines auxiliaryLines,
                      int[] colors, float dataPaddingHorizontal, float candleLineWidth) {
        super(auxiliaryLines);
        this.colors = colors;
        this.dataPaddingHorizontal = dataPaddingHorizontal;
        this.candleLineWidth = candleLineWidth;
    }

    @Override
    public void calcIndexAsync(List<DATA> dataList) {

    }

    @Override
    public void calcIndex(List<DATA> dataList, int startIndex, int endIndex) {

    }

    @Override
    public void drawIndex(KlineView klineView, IndexDrawArea drawArea, int startIndex, int endIndex, Canvas canvas, Paint paint) {
        List<? extends KlineData> klineDataList = klineView.getDataList();
        for (int i = startIndex; i < endIndex; i++) {
            KlineData klineData = klineDataList.get(i);
            int visibleIndex = drawArea.getVisibleIndex(i);

            float drawX = drawArea.getDrawX(visibleIndex);
            float firstY = drawArea.getDrawY(klineData.getHighestPrice());
            float secondY = drawArea.getDrawY(klineData.getClosePrice());
            float thirdY = drawArea.getDrawY(klineData.getOpenPrice());
            float fourthY = drawArea.getDrawY(klineData.getLowestPrice());
            float candleWidth = drawArea.getOneDataWidth() - 2 * dataPaddingHorizontal;

            int color = colors[0];
            if (klineData.getClosePrice() < klineData.getOpenPrice()) {
                color = colors[1];
                float tmp = thirdY;
                thirdY = secondY;
                secondY = tmp;
            }

            drawLongLowerShadow(drawX, firstY, secondY, thirdY, fourthY, color, canvas, paint);
            RectF rectF = (RectF) klineView.getSharedObjects().getObject(RectF.class);
            drawCandleBody(drawX, secondY, thirdY, color, candleWidth, rectF, canvas, paint);
        }
    }

    protected void drawLongLowerShadow(float drawX,
                                       float firstY, float secondY, float thirdY, float fourthY,
                                       int color, Canvas canvas, Paint paint) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(candleLineWidth);
        canvas.drawLine(drawX, firstY, drawX, secondY, paint);
        canvas.drawLine(drawX, thirdY, drawX, fourthY, paint);
    }

    protected void drawCandleBody(float drawX, float secondY, float thirdY, int color, float candleWidth, RectF rectF, Canvas canvas, Paint paint) {
        if (secondY == thirdY) {
            canvas.drawLine(drawX - candleWidth / 2, secondY,
                    drawX + candleWidth / 2, thirdY,
                    paint);
        } else {
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            rectF.left = drawX - candleWidth / 2;
            rectF.top = secondY;
            rectF.right = drawX + candleWidth / 2;
            rectF.bottom = thirdY;
            canvas.drawRect(rectF, paint);
        }
    }
}
