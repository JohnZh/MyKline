package com.john.mykline;

import android.content.Context;
import android.graphics.Color;

import com.johnzh.klinelib.DefaultFactory;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.CandlesAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.SimpleAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.VOLAuxiliaryLines;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorTextDrawArea;
import com.johnzh.klinelib.element.SimpleDrawDate;
import com.johnzh.klinelib.indicators.Indicator;
import com.johnzh.klinelib.indicators.MAIndicator;
import com.johnzh.klinelib.indicators.PureKIndicator;
import com.johnzh.klinelib.indicators.VOLIndicator;
import com.johnzh.klinelib.indicators.WRIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JohnZh on 2020/5/24
 *
 * <p>Factory to custom style</p>
 */
public class MyFactory2 extends DefaultFactory {
    public MyFactory2(Context context) {
        super(context);
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

        List<Indicator> indicators1 = new ArrayList<>();
        indicators1.add(maIndicator);

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

    @Override
    public <T extends AuxiliaryLines> T getAuxiliaryLines(Class<T> clazz, int lines) {
        float textSize = sp2Px(10);
        float lineWidth = dp2Px(0.5f);
        float textMargin = dp2Px(2);
        int color = Color.parseColor("#307D9D");

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
}
