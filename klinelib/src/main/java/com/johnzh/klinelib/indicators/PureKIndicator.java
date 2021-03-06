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
import android.graphics.RectF;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description: no index implement, just k line candles
 */
public class PureKIndicator extends AbsIndicator {

    private int[] posNegColors;
    private float dataPaddingX;
    private float candleLineWidth;
    private boolean solidPosCandles;
    private boolean solidNegCandles;

    /**
     * @param auxiliaryLines
     * @param posNegColors colors[0] is positiveColor, colors[1] is negativeColor
     * @param dataPaddingX as marginLeft and marginRight of candle
     * @param candleLineWidth
     */
    public PureKIndicator(AuxiliaryLines auxiliaryLines,
                          int[] posNegColors, float dataPaddingX, float candleLineWidth) {
        super(auxiliaryLines);
        this.posNegColors = posNegColors;
        this.dataPaddingX = dataPaddingX;
        this.candleLineWidth = candleLineWidth;
        this.solidPosCandles = true;
        this.solidNegCandles = true;
    }

    /**
     * @param auxiliaryLines
     * @param posNegColors colors[0] is positiveColor, colors[1] is negativeColor
     * @param dataPaddingX as marginLeft and marginRight of candle
     * @param candleLineWidth
     * @param solidPosCandles true is solid candle
     */
    public PureKIndicator(AuxiliaryLines auxiliaryLines, int[] posNegColors, float dataPaddingX,
                          float candleLineWidth, boolean solidPosCandles, boolean solidNegCandles) {
        super(auxiliaryLines);
        this.posNegColors = posNegColors;
        this.dataPaddingX = dataPaddingX;
        this.candleLineWidth = candleLineWidth;
        this.solidPosCandles = solidPosCandles;
        this.solidNegCandles = solidNegCandles;
    }

    public int[] getPosNegColors() {
        return posNegColors;
    }

    public float getDataPaddingX() {
        return dataPaddingX;
    }

    public float getCandleLineWidth() {
        return candleLineWidth;
    }

    public boolean isSolidPosCandles() {
        return solidPosCandles;
    }

    public boolean isSolidNegCandles() {
        return solidNegCandles;
    }

    public void setPosNegColors(int[] posNegColors) {
        this.posNegColors = posNegColors;
    }

    public void setDataPaddingX(float dataPaddingX) {
        this.dataPaddingX = dataPaddingX;
    }

    public void setCandleLineWidth(float candleLineWidth) {
        this.candleLineWidth = candleLineWidth;
    }

    public void setSolidPosCandles(boolean solidPosCandles) {
        this.solidPosCandles = solidPosCandles;
    }

    public void setSolidNegCandles(boolean solidNegCandles) {
        this.solidNegCandles = solidNegCandles;
    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        List<DATA> dataList = klineView.getDataList();
        int startIndex = klineView.getStartIndex();
        int endIndex = klineView.getEndIndex();
        for (int i = startIndex; i < endIndex; i++) {
            KlineData klineData = dataList.get(i);
            int visibleIndex = drawArea.getVisibleIndex(i);

            float drawX = drawArea.getDrawX(visibleIndex);
            float firstY = drawArea.getDrawY(klineData.getHighestPrice());
            float secondY = drawArea.getDrawY(klineData.getClosePrice());
            float thirdY = drawArea.getDrawY(klineData.getOpenPrice());
            float fourthY = drawArea.getDrawY(klineData.getLowestPrice());
            float candleWidth = drawArea.getOneDataWidth() - 2 * dataPaddingX;

            int color = posNegColors[0];
            boolean positive = true;
            if (klineData.getClosePrice() < klineData.getOpenPrice()) {
                positive = false;
                color = posNegColors[1];
                float tmp = thirdY;
                thirdY = secondY;
                secondY = tmp;
            }

            drawLongLowerShadow(drawX, firstY, secondY, thirdY, fourthY, color, canvas, paint);
            RectF rectF = klineView.getSharedObjects().getObject(RectF.class);
            drawCandleBody(drawX, secondY, thirdY, color, candleWidth, positive, rectF, canvas, paint);
        }
    }

    @Override
    public void drawIndicatorText(KlineView klineView, DrawArea drawArea, DATA data, Canvas canvas, Paint paint) {

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

    protected void drawCandleBody(float drawX, float secondY, float thirdY, int color, float candleWidth,
                                  boolean positive, RectF rectF, Canvas canvas, Paint paint) {
        if (secondY == thirdY) {
            canvas.drawLine(drawX - candleWidth / 2, secondY,
                    drawX + candleWidth / 2, thirdY,
                    paint);
        } else {
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            if ((!solidPosCandles && positive) || (!solidNegCandles && !positive)) {
                paint.setStyle(Paint.Style.STROKE);
            }
            rectF.left = drawX - candleWidth / 2;
            rectF.top = secondY;
            rectF.right = drawX + candleWidth / 2;
            rectF.bottom = thirdY;
            canvas.drawRect(rectF, paint);
        }
    }
}
