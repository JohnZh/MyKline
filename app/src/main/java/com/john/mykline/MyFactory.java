package com.john.mykline;

import android.content.Context;

import com.johnzh.klinelib.DefaultFactory;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorTextDrawArea;
import com.johnzh.klinelib.indicators.BOLLIndicator;
import com.johnzh.klinelib.indicators.Indicator;
import com.johnzh.klinelib.indicators.MAIndicator;
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
    public List<DrawArea> createDrawAreas() {
        int textHeight = (int) dp2Px(TEXT_HEIGHT);
        int dataHeight = (int) dp2Px(DATA_HEIGHT);
        int dateHeight = (int) dp2Px(DATE_HEIGHT);
        int indexHeight = (int) dp2Px(INDICATOR_HEIGHT);

        List<Indicator> list = new ArrayList<>();
        list.add(createDefaultIndex(MAIndicator.class));

        List<Indicator> list1 = new ArrayList<>();
        list1.add(createDefaultIndex(BOLLIndicator.class));

        List<Indicator> list2 = new ArrayList<>();
        list2.add(createDefaultIndex(VOLIndicator.class));

        List<Indicator> list3 = new ArrayList<>();
        list3.add(createDefaultIndex(WRIndicator.class));

        IndicatorDrawArea indicatorArea1 = new IndicatorDrawArea(dataHeight, list);
        IndicatorDrawArea indicatorArea2 = new IndicatorDrawArea(dataHeight, list1);
        IndicatorDrawArea indicatorArea3 = new IndicatorDrawArea(indexHeight, list2);
        IndicatorDrawArea indicatorArea4 = new IndicatorDrawArea(indexHeight, list3);

        List<DrawArea> drawAreaList = new ArrayList<>();
        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea1));
        drawAreaList.add(indicatorArea1);

        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea2));
        drawAreaList.add(indicatorArea2);

        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea3));
        drawAreaList.add(indicatorArea3);

        drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea4));
        drawAreaList.add(indicatorArea4);

        drawAreaList.add(new DateDrawArea(dateHeight, getDefaultDrawDate()));
        return drawAreaList;
    }
}
