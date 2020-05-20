package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.CandlesAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.SimpleAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.VolAuxiliaryLines;
import com.johnzh.klinelib.date.DrawDate;
import com.johnzh.klinelib.date.SimpleDrawDate;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorTextDrawArea;
import com.johnzh.klinelib.indicators.Indicator;
import com.johnzh.klinelib.indicators.MAIndicator;
import com.johnzh.klinelib.indicators.PureKIndicator;
import com.johnzh.klinelib.indicators.VolIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public class KlineFactory implements Factory {

    // all units are dp
    public static final int TEXT_HEIGHT = 18;
    public static final int DATA_HEIGHT = 240;
    public static final int DATE_HEIGHT = 24;
    public static final int INDICATOR_HEIGHT = 120;

    private Context mContext;

    public KlineFactory(Context context) {
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
        return list;
    }

    protected List<Indicator> getIndicators2() {
        List<Indicator> list = new ArrayList<>();
        list.add(createDefaultIndex(VolIndicator.class));
        return list;
    }

    @Override
    public Indicator createDefaultIndex(Class clazz) {
        int[] posNegColor = {
                Color.parseColor("#f62048"),
                Color.parseColor("#39ae13")
        };

        float dataPaddingX = dp2Px(0.5f);
        float textSize = sp2Px(10);
        float textMargin = dp2Px(2);

        if (clazz.isAssignableFrom(PureKIndicator.class)) {
            float candleLineWidth = dp2Px(1);
            AuxiliaryLines auxiliaryLines
                    = createDefaultAuxiliaryLines(CandlesAuxiliaryLines.class);
            return new PureKIndicator(auxiliaryLines, posNegColor, dataPaddingX, candleLineWidth);
        }

        if (clazz.isAssignableFrom(MAIndicator.class)) {
            int[] maColors = {
                    Color.parseColor("#ffb405"),
                    Color.parseColor("#890cff")
            };
            PureKIndicator purKIndex = (PureKIndicator) createDefaultIndex(PureKIndicator.class);
            float lineWidth = dp2Px(1);
            return new MAIndicator(purKIndex, lineWidth, textSize,
                    new int[]{5, 10}, maColors);
        }

        if (clazz.isAssignableFrom(VolIndicator.class)) {
            AuxiliaryLines volAuxiliaryLines = createDefaultAuxiliaryLines(VolAuxiliaryLines.class);
            return new VolIndicator(volAuxiliaryLines, posNegColor, dataPaddingX);
        }

        return null;
    }

    @Override
    public AuxiliaryLines createDefaultAuxiliaryLines(Class clazz) {
        float textSize = sp2Px(10);
        float lineWidth = dp2Px(0.5f);
        float textMargin = dp2Px(2);
        int color = Color.parseColor("#999999");

        if (clazz.isAssignableFrom(CandlesAuxiliaryLines.class)) {
            return new CandlesAuxiliaryLines(5, textSize, lineWidth, textMargin, color);
        }

        if (clazz.isAssignableFrom(SimpleAuxiliaryLines.class)) {
            return new SimpleAuxiliaryLines(2, textSize, lineWidth, textMargin, color);
        }

        if (clazz.isAssignableFrom(VolAuxiliaryLines.class)) {
            return new VolAuxiliaryLines(2, textSize, lineWidth, textMargin, color);
        }

        return null;
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
