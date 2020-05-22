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
package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.SimpleAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.VOLAuxiliaryLines;
import com.johnzh.klinelib.date.DrawDate;
import com.johnzh.klinelib.date.SimpleDrawDate;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorTextDrawArea;
import com.johnzh.klinelib.indicators.BOLLIndicator;
import com.johnzh.klinelib.indicators.Indicator;
import com.johnzh.klinelib.indicators.MAIndicator;
import com.johnzh.klinelib.indicators.PureKIndicator;
import com.johnzh.klinelib.indicators.VOLIndicator;
import com.johnzh.klinelib.indicators.WRIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public class DefaultFactory implements Factory {

    // all units are dp
    public static final int TEXT_HEIGHT = 18;
    public static final int DATA_HEIGHT = 240;
    public static final int DATE_HEIGHT = 24;
    public static final int INDICATOR_HEIGHT = 120;

    private Context mContext;

    public DefaultFactory(Context context) {
        mContext = context;
    }

    @Override
    public List<DrawArea> createDrawAreas() {
        int textHeight = (int) dp2Px(TEXT_HEIGHT);
        int dataHeight = (int) dp2Px(DATA_HEIGHT);
        int dateHeight = (int) dp2Px(DATE_HEIGHT);
        int indexHeight = (int) dp2Px(INDICATOR_HEIGHT);

        IndicatorDrawArea indicatorArea1 = new IndicatorDrawArea(dataHeight, getIndicators1());
        IndicatorDrawArea indicatorArea2 = new IndicatorDrawArea(indexHeight, getIndicators2());

        List<DrawArea> drawAreaList = new ArrayList<>();
        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea1));
        drawAreaList.add(indicatorArea1);
        drawAreaList.add(new DateDrawArea(dateHeight, getDefaultDrawDate()));
        drawAreaList.add(indicatorArea2);
        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea2));
        return drawAreaList;
    }

    @Override
    public <T extends Indicator> T createDefaultIndex(Class<T> clazz) {
        int[] posNegColor = {
                Color.parseColor("#f62048"),
                Color.parseColor("#39ae13")
        };

        int[] maColors = {
                Color.parseColor("#1A7AD5"),
                Color.parseColor("#FFB405"),
                Color.parseColor("#7C3BB9")
        };

        float dataPaddingX = dp2Px(0.5f);
        float textSize = sp2Px(10);
        float textMargin = dp2Px(2);
        int textColor = Color.parseColor("#999999");
        float lineWidth = dp2Px(1);

        if (clazz.isAssignableFrom(PureKIndicator.class)) {
            float candleLineWidth = dp2Px(1);
            SimpleAuxiliaryLines auxiliaryLines
                    = createDefaultAuxiliaryLines(SimpleAuxiliaryLines.class, 5);
            return (T) new PureKIndicator(auxiliaryLines, posNegColor, dataPaddingX, candleLineWidth);
        }

        if (clazz.isAssignableFrom(MAIndicator.class)) {
            PureKIndicator purKIndex = createDefaultIndex(PureKIndicator.class);
            return (T) new MAIndicator(purKIndex, new int[]{5, 10}, maColors,
                    lineWidth, textSize, textMargin);
        }

        if (clazz.isAssignableFrom(BOLLIndicator.class)) {
            PureKIndicator purKIndex = createDefaultIndex(PureKIndicator.class);
            return (T) new BOLLIndicator(purKIndex, new int[]{20, 2}, maColors,
                    lineWidth, textSize, textMargin);
        }

        if (clazz.isAssignableFrom(VOLIndicator.class)) {
            AuxiliaryLines volAuxiliaryLines = createDefaultAuxiliaryLines(VOLAuxiliaryLines.class);
            return (T) new VOLIndicator(volAuxiliaryLines, posNegColor, dataPaddingX,
                    textSize, textColor, textMargin);
        }

        if (clazz.isAssignableFrom(WRIndicator.class)) {
            SimpleAuxiliaryLines simpleAuxiliaryLines
                    = createDefaultAuxiliaryLines(SimpleAuxiliaryLines.class);
            return (T) new WRIndicator(simpleAuxiliaryLines, new int[]{6, 10}, maColors,
                    lineWidth, textSize, textMargin);
        }

        return null;
    }

    public <T extends AuxiliaryLines> T createDefaultAuxiliaryLines(Class<T> clazz) {
        return createDefaultAuxiliaryLines(clazz, 2);
    }

    @Override
    public <T extends AuxiliaryLines> T createDefaultAuxiliaryLines(Class<T> clazz, int lines) {
        float textSize = sp2Px(10);
        float lineWidth = dp2Px(0.5f);
        float textMargin = dp2Px(2);
        int color = Color.parseColor("#999999");

        if (clazz.isAssignableFrom(SimpleAuxiliaryLines.class)) {
            return (T) new SimpleAuxiliaryLines(lines, textSize, lineWidth, textMargin, color);
        }

        if (clazz.isAssignableFrom(VOLAuxiliaryLines.class)) {
            return (T) new VOLAuxiliaryLines(lines, textSize, lineWidth, textMargin, color);
        }

        return null;
    }

    protected DrawDate getDefaultDrawDate() {
        float fontSize = sp2Px(10);
        float textMargin = dp2Px(2);
        int color = Color.parseColor("#999999");
        return new SimpleDrawDate(fontSize, textMargin, color);
    }

    protected List<Indicator> getIndicators1() {
        List<Indicator> list = new ArrayList<>();
        list.add(createDefaultIndex(PureKIndicator.class));
        list.add(createDefaultIndex(MAIndicator.class));
        list.add(createDefaultIndex(BOLLIndicator.class));
        return list;
    }

    protected List<Indicator> getIndicators2() {
        List<Indicator> list = new ArrayList<>();
        list.add(createDefaultIndex(VOLIndicator.class));
        list.add(createDefaultIndex(WRIndicator.class));
        return list;
    }


    public float sp2Px(float value) {
        return toPx(TypedValue.COMPLEX_UNIT_SP, value);
    }

    public float dp2Px(float value) {
        return toPx(TypedValue.COMPLEX_UNIT_DIP, value);
    }

    private float toPx(int unit, float value) {
        return TypedValue.applyDimension(unit, value, mContext.getResources().getDisplayMetrics());
    }
}
