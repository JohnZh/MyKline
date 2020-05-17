package com.johnzh.klinelib.date;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.drawarea.DateDrawArea;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public class SimpleDrawDate implements DrawDate {

    private float fontSize;
    private float textMargin;
    private int color;

    public static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public SimpleDrawDate(float fontSize, float textMargin, int color) {
        this.fontSize = fontSize;
        this.textMargin = textMargin;
        this.color = color;
    }

    @Override
    public void drawDate(KlineView klineView, DateDrawArea drawArea, int startIndex, int endIndex,
                         Canvas canvas, Paint paint) {
        List<? extends KlineData> klineDataList = klineView.getKlineDataList();
        KlineData data = klineDataList.get(startIndex);

        paint.setColor(color);
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.FILL);

        String dateText = data.getDate() != null ?
                DATE_FORMAT.format(data.getDate()) : "";
        float textLeft = drawArea.getLeft();
        float textTop = drawArea.getTop();
        textLeft += textMargin;
        textTop += textMargin;
        DrawTextTool.drawTextFromLeftTop(dateText, textLeft, textTop, canvas, paint);

        int visibleIndex = drawArea.getVisibleIndex(endIndex - 1);
        if (visibleIndex == klineView.getCandles() - 1) {
            data = klineDataList.get(endIndex - 1);
            dateText = data.getDate() != null ?
                    DATE_FORMAT.format(data.getDate()) : "";
            float textRight = drawArea.getLeft() + drawArea.getWidth();
            textRight -= textMargin;
            DrawTextTool.drawTextFromRightTop(dateText, textRight, textTop, canvas, paint);
        }
    }
}
