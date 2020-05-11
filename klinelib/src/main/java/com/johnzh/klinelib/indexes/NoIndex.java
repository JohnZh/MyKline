package com.johnzh.klinelib.indexes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.johnzh.klinelib.DrawArea;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description: no index implement, just k line candles
 */
public class NoIndex implements Index {

    private int positiveColor;
    private int negativeColor;
    private float candleWidth;
    private float candleLineWidth;

    public NoIndex(int positiveColor, int negativeColor, float candleWidth, float candleLineWidth) {
        this.positiveColor = positiveColor;
        this.negativeColor = negativeColor;
        this.candleWidth = candleWidth;
        this.candleLineWidth = candleLineWidth;
    }

    @Override
    public void calcIndexAsync(List<? extends KlineData> klineDataList) {

    }

    @Override
    public void calcIndex(List<? extends KlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void onDraw(KlineView klineView, int startIndex, int endIndex, Canvas canvas, Paint paint) {
        DrawArea drawArea = klineView.getDrawArea();
        List<? extends KlineData> klineDataList = klineView.getKlineDataList();
        for (int i = startIndex; i < endIndex; i++) {
            KlineData klineData = klineDataList.get(i);
            int visibleIndex = drawArea.getVisibleIndex(i, startIndex);

            float dataX = drawArea.getDataX(visibleIndex);
            float firstY = drawArea.getDataY(klineData.getHighestPrice());
            float secondY = drawArea.getDataY(klineData.getClosePrice());
            float thirdY = drawArea.getDataY(klineData.getOpenPrice());
            float fourthY = drawArea.getDataY(klineData.getLowestPrice());

            int color = positiveColor;
            if (klineData.getClosePrice() < klineData.getOpenPrice()) {
                color = negativeColor;
                float tmp = thirdY;
                thirdY = secondY;
                secondY = tmp;
            }

            drawLongLowerShadow(dataX, firstY, secondY, thirdY, fourthY, color, canvas, paint);
            RectF rectF = (RectF) klineView.getSharedObjects().getObject(RectF.class);
            drawCandleBody(dataX, secondY, thirdY, color, rectF,canvas, paint);
        }
    }

    protected void drawLongLowerShadow(float dataX,
                                     float firstY, float secondY, float thirdY, float fourthY,
                                     int color, Canvas canvas, Paint paint) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(candleLineWidth);
        canvas.drawLine(dataX, firstY, dataX, secondY, paint);
        canvas.drawLine(dataX, thirdY, dataX, fourthY, paint);
    }

    protected void drawCandleBody(float dataX, float secondY, float thirdY, int color, RectF rectF, Canvas canvas, Paint paint) {
        if (secondY == thirdY) {
            canvas.drawLine(dataX - candleWidth / 2, secondY,
                    dataX + candleWidth / 2, thirdY,
                    paint);
        } else {
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            rectF.left = dataX - candleWidth / 2;
            rectF.top = secondY;
            rectF.right = dataX + candleWidth / 2;
            rectF.bottom = thirdY;
            canvas.drawRect(rectF, paint);
        }
    }
}
