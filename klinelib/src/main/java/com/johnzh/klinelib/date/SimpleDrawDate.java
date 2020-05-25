/**
 * MIT License
 *
 * Copyright (c) 2020 JohnZh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.johnzh.klinelib.date;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;

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
    private int textColor;

    public static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public SimpleDrawDate(int textColor, float fontSize, float textMargin) {
        this.fontSize = fontSize;
        this.textMargin = textMargin;
        this.textColor = textColor;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public void setTextMargin(float textMargin) {
        this.textMargin = textMargin;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public void drawDate(KlineView klineView, DateDrawArea drawArea, int startIndex, int endIndex,
                         Canvas canvas, Paint paint) {
        List<DATA> klineDataList = klineView.getDataList();
        DATA data = klineDataList.get(startIndex);

        paint.setColor(textColor);
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
