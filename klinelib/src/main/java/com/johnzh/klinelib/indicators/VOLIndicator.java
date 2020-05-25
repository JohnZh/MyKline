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
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public class VOLIndicator extends AbsIndicator {
    private int[] posNegColors;
    private float dataPaddingX;
    private float textSize;
    private int textColor;
    private boolean solidPosCandles;
    private boolean solidNegCandles;

    /**
     *
     * @param auxiliaryLines
     * @param posNegColors colors[0] is positiveColor, colors[1] is negativeColor
     * @param dataPaddingX as marginLeft and marginRight of candle
     * @param textSize
     * @param textColor
     */
    public VOLIndicator(AuxiliaryLines auxiliaryLines, int[] posNegColors, float dataPaddingX,
                        float textSize, int textColor) {
        super(auxiliaryLines);
        this.posNegColors = posNegColors;
        this.dataPaddingX = dataPaddingX;
        this.textSize = textSize;
        this.textColor = textColor;
        this.solidPosCandles = true;
        this.solidNegCandles = true;
    }

    public VOLIndicator(AuxiliaryLines auxiliaryLines, int[] posNegColors, float dataPaddingX,
                        float textSize, int textColor, boolean solidPosCandles, boolean solidNegCandles) {
        super(auxiliaryLines);
        this.posNegColors = posNegColors;
        this.dataPaddingX = dataPaddingX;
        this.textSize = textSize;
        this.textColor = textColor;
        this.solidPosCandles = solidPosCandles;
        this.solidNegCandles = solidNegCandles;
    }

    public int[] getPosNegColors() {
        return posNegColors;
    }

    public float getDataPaddingX() {
        return dataPaddingX;
    }

    public float getTextSize() {
        return textSize;
    }

    public int getTextColor() {
        return textColor;
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

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setSolidPosCandles(boolean solidPosCandles) {
        this.solidPosCandles = solidPosCandles;
    }

    public void setSolidNegCandles(boolean solidNegCandles) {
        this.solidNegCandles = solidNegCandles;
    }

    @Override
    public void calcIndicator(List<DATA> klineDataList, int startIndex, int endIndex) {
    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        float candleWidth = drawArea.getOneDataWidth() - 2 * dataPaddingX;
        List<DATA> dataList = klineView.getDataList();
        int startIndex = klineView.getStartIndex();
        int endIndex = klineView.getEndIndex();
        for (int i = startIndex; i < endIndex; i++) {
            KlineData klineData = dataList.get(i);
            float dataX = drawArea.getDrawX(drawArea.getVisibleIndex(i));
            float dataY = drawArea.getDrawY(klineData.getVolume());
            int color = posNegColors[0];
            boolean positive = true;
            if (klineData.getClosePrice() < klineData.getOpenPrice()) {
                color = posNegColors[1];
                positive = false;
            }

            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            if ((!solidPosCandles && positive) || (!solidNegCandles && !positive)) {
                paint.setStyle(Paint.Style.STROKE);
            }
            RectF rectf = klineView.getSharedObjects().getObject(RectF.class);
            rectf.left = dataX - candleWidth / 2;
            rectf.top = dataY;
            rectf.right = dataX + candleWidth / 2;
            rectf.bottom = drawArea.getDrawY(0);
            canvas.drawRect(rectf, paint);
        }
    }

    @Override
    public void drawIndicatorText(KlineView klineView, DrawArea drawArea, DATA data,
                                  Canvas canvas, Paint paint) {
        StringBuilder builder = klineView.getSharedObjects().getObject(StringBuilder.class);
        int scale = FloatCalc.get().getScale(getAuxiliaryLines().getMax());
        String text = builder.append("VOL:")
                .append(FloatCalc.get().format(data.getVolume(), scale)).toString();
        float textLeft = drawArea.getLeft();
        float textCenterX = drawArea.getTop() + drawArea.getHeight() / 2;

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);
        paint.setColor(textColor);

        DrawTextTool.drawTextFromLeftCenterX(text, textLeft, textCenterX, canvas, paint);
    }
}
