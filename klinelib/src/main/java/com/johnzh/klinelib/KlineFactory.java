package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.CandlesAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.SimpleAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.VolAuxiliaryLines;
import com.johnzh.klinelib.date.SimpleDrawDate;
import com.johnzh.klinelib.date.DrawDate;
import com.johnzh.klinelib.drawarea.DateDrawArea;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.IndicatorDrawArea;
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
    public static final int DATA_HEIGHT = 240;
    public static final int DATE_HEIGHT = 30;
    public static final int INDEX_HEIGHT = 120;

    private Context mContext;

    public KlineFactory(Context context) {
        mContext = context;
    }

    @Override
    public List<DrawArea> createDrawAreas() {
        int dataHeight = (int) dp2Px(DATA_HEIGHT);
        int dateHeight = (int) dp2Px(DATE_HEIGHT);
        int indexHeight = (int) dp2Px(INDEX_HEIGHT);
        List<DrawArea> drawAreaList = new ArrayList<>();
        drawAreaList.add(new IndicatorDrawArea(dataHeight, getIndexListForData()));
        drawAreaList.add(new DateDrawArea(dateHeight, getDefaultDrawDate()));
        drawAreaList.add(new IndicatorDrawArea(indexHeight, getIndexForIndexes()));
        return drawAreaList;
    }

    private DrawDate getDefaultDrawDate() {
        float fontSize = sp2Px( 10);
        float textMargin = dp2Px( 2);
        int color = Color.parseColor("#999999");
        return new SimpleDrawDate(fontSize, textMargin, color);
    }

    private List<Indicator> getIndexListForData() {
        List<Indicator> list = new ArrayList<>();
        list.add(createDefaultIndex(PureKIndicator.class));
        list.add(createDefaultIndex(MAIndicator.class));
        return list;
    }

    private List<Indicator> getIndexForIndexes() {
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

        float dataPaddingHorizontal = toPx(TypedValue.COMPLEX_UNIT_DIP, 0.5f);

        if (clazz.isAssignableFrom(PureKIndicator.class)) {
            float candleLineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 1);
            AuxiliaryLines auxiliaryLines
                    = createDefaultAuxiliaryLines(CandlesAuxiliaryLines.class);
            return new PureKIndicator(auxiliaryLines, posNegColor, dataPaddingHorizontal, candleLineWidth);
        }

        if (clazz.isAssignableFrom(MAIndicator.class)) {
            int[] maColors = {
                    Color.parseColor("#ffb405"),
                    Color.parseColor("#890cff")
            };
            AuxiliaryLines auxiliaryLines
                    = createDefaultAuxiliaryLines(CandlesAuxiliaryLines.class);
            PureKIndicator purKIndex = (PureKIndicator) createDefaultIndex(PureKIndicator.class);
            float lineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 1);
            return new MAIndicator(auxiliaryLines, purKIndex, lineWidth, new int[]{5, 10}, maColors);
        }

        if (clazz.isAssignableFrom(VolIndicator.class)) {
            AuxiliaryLines volAuxiliaryLines = createDefaultAuxiliaryLines(VolAuxiliaryLines.class);
            return new VolIndicator(volAuxiliaryLines, posNegColor, dataPaddingHorizontal);
        }

        return null;
    }

    @Override
    public AuxiliaryLines createDefaultAuxiliaryLines(Class clazz) {
        float fontSize = toPx(TypedValue.COMPLEX_UNIT_SP, 10);
        float lineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 0.5f);
        float textMargin = toPx(TypedValue.COMPLEX_UNIT_DIP, 2);
        int color = Color.parseColor("#999999");

        if (clazz.isAssignableFrom(CandlesAuxiliaryLines.class)) {
            return new CandlesAuxiliaryLines(5, fontSize, lineWidth, textMargin, color);
        }

        if (clazz.isAssignableFrom(SimpleAuxiliaryLines.class)) {
            return new SimpleAuxiliaryLines(2, fontSize, lineWidth, textMargin, color);
        }

        if (clazz.isAssignableFrom(VolAuxiliaryLines.class)) {
            return new VolAuxiliaryLines(2, fontSize, lineWidth, textMargin, color);
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
