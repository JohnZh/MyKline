package com.johnzh.klinelib.indexes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public class VolIndex extends AbsIndex<KlineData> {

    public VolIndex(AuxiliaryLines<KlineData> auxiliaryLines) {
        super(auxiliaryLines);
    }

    @Override
    public void calcIndexAsync(List<KlineData> klineDataList) {

    }

    @Override
    public void calcIndex(List<KlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void drawIndex(KlineView klineView, int startIndex, int endIndex, Canvas canvas, Paint paint) {

    }
}
