package com.johnzh.klinelib.indexes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.IndexDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public class VolIndex extends AbsIndex<KlineData> {
    private int[] colors;
    private float dataPaddingHorizontal;

    public VolIndex(AuxiliaryLines<KlineData> auxiliaryLines, int[] colors, float dataPaddingHorizontal) {
        super(auxiliaryLines);
        this.colors = colors;
        this.dataPaddingHorizontal = dataPaddingHorizontal;
    }

    @Override
    public void calcIndexAsync(List<KlineData> klineDataList) {
    }

    @Override
    public void calcIndex(List<KlineData> klineDataList, int startIndex, int endIndex) {
    }

    @Override
    public void drawIndex(KlineView klineView, IndexDrawArea drawArea, int startIndex, int endIndex, Canvas canvas, Paint paint) {
        float candleWidth = drawArea.getOneDataWidth() - 2 * dataPaddingHorizontal;
        List<? extends KlineData> klineDataList = klineView.getKlineDataList();
        for (int i = startIndex; i < endIndex; i++) {
            KlineData klineData = klineDataList.get(i);
            float dataX = drawArea.getDrawX(drawArea.getVisibleIndex(i));
            float dataY = drawArea.getDrawY(klineData.getVolume());
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
            rectf.bottom = drawArea.getDrawY(0);
            canvas.drawRect(rectf, paint);
        }
    }
}
