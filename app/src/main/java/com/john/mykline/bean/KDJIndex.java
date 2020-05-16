package com.john.mykline.bean;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.IndexDrawArea;
import com.johnzh.klinelib.indexes.AbsIndex;

import java.util.List;

/**
 * Modified by john on 2020/5/11
 * <p>
 * Description:
 */
public class KDJIndex extends AbsIndex<MyKlineData> {

    public KDJIndex(AuxiliaryLines<MyKlineData> auxiliaryLines) {
        super(auxiliaryLines);
    }

    public static class KDJ {

    }

    @Override
    public void calcIndexAsync(List<MyKlineData> klineDataList) {

    }

    @Override
    public void calcIndex(List<MyKlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void drawIndex(KlineView klineView, IndexDrawArea drawArea, int startIndex, int endIndex, Canvas canvas, Paint paint) {

    }

}
