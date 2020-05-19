package com.john.mykline.bean;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.indicators.AbsIndicator;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/11
 *
 * <p></p>
 */
public class KDJIndicator extends AbsIndicator {

    public KDJIndicator(AuxiliaryLines auxiliaryLines) {
        super(auxiliaryLines);
    }

    @Override
    public void calcIndicatorAsync(List<DATA> dataList) {

    }

    @Override
    public void calcIndicator(List<DATA> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {

    }

    @Override
    public void drawIndicatorText(KlineView klineView, DrawArea drawArea, Canvas canvas, Paint paint) {

    }
}
