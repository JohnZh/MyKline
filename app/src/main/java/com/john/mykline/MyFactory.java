package com.john.mykline;

import android.content.Context;
import android.graphics.Color;

import com.johnzh.klinelib.DefaultFactory;
import com.johnzh.klinelib.auxiliarylines.CandlesAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.SimpleAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.VOLAuxiliaryLines;
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
 * Created by JohnZh on 2020/5/23
 *
 * <p>My Custom Factory</p>
 */
public class MyFactory extends DefaultFactory {
    public MyFactory(Context context) {
        super(context);
    }

    @Override
    public List<List<Indicator>> getIndicatorsList() {
        List<List<Indicator>> list = new ArrayList<>();

        float dataPaddingX = dp2Px(0.5f);
        float textSize = sp2Px(10);
        int textColor = Color.parseColor("#999999");
        float lineWidth = dp2Px(1);

        PureKIndicator pureKIndicator =
                new PureKIndicator(
                        getAuxiliaryLines(CandlesAuxiliaryLines.class, 5),
                        posNegColor, dataPaddingX, lineWidth);
        MAIndicator maIndicator = new MAIndicator(pureKIndicator, new int[]{5, 10}, maColors, lineWidth, textSize);
        BOLLIndicator bollIndicator = new BOLLIndicator(pureKIndicator, new int[]{20, 2}, bollColors, lineWidth, textSize);

        List<Indicator> indicators1 = new ArrayList<>();
        indicators1.add(maIndicator);

        List<Indicator> indicators2 = new ArrayList<>();
        indicators2.add(bollIndicator);

        VOLIndicator volIndicator = new VOLIndicator(
                getAuxiliaryLines(VOLAuxiliaryLines.class, 2),
                posNegColor, dataPaddingX, textSize, textColor);
        WRIndicator wrIndicator = new WRIndicator(
                getAuxiliaryLines(SimpleAuxiliaryLines.class, 2),
                new int[]{6, 10}, wrColors, lineWidth, textSize);

        List<Indicator> indicators3 = new ArrayList<>();
        indicators3.add(volIndicator);

        List<Indicator> indicators4 = new ArrayList<>();
        indicators4.add(wrIndicator);

        list.add(indicators1);
        list.add(indicators2);
        list.add(indicators3);
        list.add(indicators4);
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
        IndicatorDrawArea indicatorArea2 = new IndicatorDrawArea(dataHeight, indicatorsList.get(1));
        IndicatorDrawArea indicatorArea3 = new IndicatorDrawArea(indexHeight, indicatorsList.get(2));
        IndicatorDrawArea indicatorArea4 = new IndicatorDrawArea(indexHeight, indicatorsList.get(3));

        List<DrawArea> drawAreaList = new ArrayList<>();
        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea1));
        drawAreaList.add(indicatorArea1);

        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea2));
        drawAreaList.add(indicatorArea2);

        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea3));
        drawAreaList.add(indicatorArea3);

        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea4));
        drawAreaList.add(indicatorArea4);

        drawAreaList.add(new DateDrawArea(dateHeight, getDrawDate(SimpleDrawDate.class)));
        return drawAreaList;
    }
}
