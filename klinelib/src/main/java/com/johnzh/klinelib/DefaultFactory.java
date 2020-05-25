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
import com.johnzh.klinelib.auxiliarylines.CandlesAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.SimpleAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.VOLAuxiliaryLines;
import com.johnzh.klinelib.element.DrawDate;
import com.johnzh.klinelib.element.SimpleDrawDate;
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
    public static final int DATA_HEIGHT = 200;
    public static final int DATE_HEIGHT = 24;
    public static final int INDICATOR_HEIGHT = 120;

    public static final int[] posNegColor = {
            Color.parseColor("#f62048"),
            Color.parseColor("#39ae13")
    };

    public static final int[] maColors = {
            Color.parseColor("#1A7AD5"),
            Color.parseColor("#FFB405"),
            Color.parseColor("#7C3BB9")
    };

    public static final int[] bollColors = {
            Color.parseColor("#1A7AD5"),
            Color.parseColor("#FFB405"),
            Color.parseColor("#7C3BB9")
    };

    public static final int[] wrColors = {
            Color.parseColor("#1A7AD5"),
            Color.parseColor("#FFB405")
    };

    private Context mContext;

    public DefaultFactory(Context context) {
        mContext = context;
    }

    @Override
    public List<List<Indicator>> getIndicatorsList() {
        List<List<Indicator>> list = new ArrayList<>();

        float dataPaddingX = dp2Px(0.5f);
        float textSize = sp2Px(10);
        int textColor = Color.parseColor("#999999");
        float lineWidth = dp2Px(1);

        // indicators 1
        PureKIndicator pureKIndicator =
                new PureKIndicator(
                        getAuxiliaryLines(CandlesAuxiliaryLines.class, 5),
                        posNegColor, dataPaddingX, lineWidth);
        MAIndicator maIndicator = new MAIndicator(pureKIndicator, new int[]{5, 10}, maColors, lineWidth, textSize);
        BOLLIndicator bollIndicator = new BOLLIndicator(pureKIndicator, new int[]{20, 2}, bollColors, lineWidth, textSize);

        List<Indicator> indicators1 = new ArrayList<>();
        indicators1.add(pureKIndicator);
        indicators1.add(maIndicator);
        indicators1.add(bollIndicator);

        // indicators 2
        VOLIndicator volIndicator = new VOLIndicator(
                getAuxiliaryLines(VOLAuxiliaryLines.class, 2),
                posNegColor, dataPaddingX, textSize, textColor);
        WRIndicator wrIndicator = new WRIndicator(
                getAuxiliaryLines(SimpleAuxiliaryLines.class, 2),
                new int[]{6, 10}, wrColors, lineWidth, textSize);

        List<Indicator> indicators2 = new ArrayList<>();
        indicators2.add(volIndicator);
        indicators2.add(wrIndicator);

        list.add(indicators1);
        list.add(indicators2);
        return list;
    }


    @Override
    public <T extends AuxiliaryLines> T getAuxiliaryLines(Class<T> clazz, int lines) {
        float textSize = sp2Px(10);
        float lineWidth = dp2Px(0.5f);
        float textMargin = dp2Px(2);
        int color = Color.parseColor("#999999");

        if (clazz.isAssignableFrom(SimpleAuxiliaryLines.class)) {
            return (T) new SimpleAuxiliaryLines(lines, color, textSize, lineWidth, textMargin);
        }
        if (clazz.isAssignableFrom(VOLAuxiliaryLines.class)) {
            return (T) new VOLAuxiliaryLines(lines, color, textSize, lineWidth, textMargin);
        }
        if (clazz.isAssignableFrom(CandlesAuxiliaryLines.class)) {
            return (T) new CandlesAuxiliaryLines(lines, color, textSize, lineWidth, textMargin);
        }
        return null;
    }

    @Override
    public <T extends DrawDate> T getDrawDate(Class<T> clazz) {
        if (clazz.isAssignableFrom(SimpleDrawDate.class)) {
            float fontSize = sp2Px(10);
            float textMargin = dp2Px(2);
            int color = Color.parseColor("#999999");
            return (T) new SimpleDrawDate(color, fontSize, textMargin);
        }
        return null;
    }

    @Override
    public List<DrawArea> createDrawAreaList() {
        int textHeight = (int) dp2Px(TEXT_HEIGHT);
        int dataHeight = (int) dp2Px(DATA_HEIGHT);
        int dateHeight = (int) dp2Px(DATE_HEIGHT);
        int indexHeight = (int) dp2Px(INDICATOR_HEIGHT);

        List<List<Indicator>> indicatorsList = getIndicatorsList();
        IndicatorDrawArea indicatorArea1 = new IndicatorDrawArea(dataHeight, indicatorsList.get(0));
        IndicatorDrawArea indicatorArea2 = new IndicatorDrawArea(indexHeight, indicatorsList.get(1));

        List<DrawArea> drawAreaList = new ArrayList<>();
        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea1));
        drawAreaList.add(indicatorArea1);
        drawAreaList.add(new DateDrawArea(dateHeight, getDrawDate(SimpleDrawDate.class)));
        drawAreaList.add(indicatorArea2);
        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea2));
        return drawAreaList;
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
