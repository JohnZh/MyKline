package com.johnzh.klinelib.indexes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.johnzh.klinelib.DrawArea;
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
    private int[] colors;
    private float candleWidth;

    public VolIndex(AuxiliaryLines<KlineData> auxiliaryLines, int[] colors, float candleWidth) {
        super(auxiliaryLines);
        this.colors = colors;
        this.candleWidth = candleWidth;
    }

    @Override
    public void calcIndexAsync(List<KlineData> klineDataList) {
    }

    @Override
    public void calcIndex(List<KlineData> klineDataList, int startIndex, int endIndex) {
    }

    @Override
    public void drawIndex(KlineView klineView, int startIndex, int endIndex, Canvas canvas, Paint paint) {
        DrawArea drawArea = klineView.getDrawArea();
        List<? extends KlineData> klineDataList = klineView.getKlineDataList();
        for (int i = startIndex; i < endIndex; i++) {
            KlineData klineData = klineDataList.get(i);
            float dataX = drawArea.getDataX(drawArea.getVisibleIndex(i, startIndex));
            float dataY = drawArea.getDataY(klineData.getVolume());
            int color = colors[0];
            if (klineData.getClosePrice() < klineData.getOpenPrice()) {
                color = colors[1];
            }
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            RectF rectf = (RectF) klineView.getSharedObjects().getObject(RectF.class);
            rectf.left = dataX - candleWidth / 2;
            rectf.top = dataY;
            rectf.right = dataX + candleWidth / 2;
            rectf.bottom = drawArea.getDataY(0);
            canvas.drawRect(rectf, paint);
        }
    }
}
