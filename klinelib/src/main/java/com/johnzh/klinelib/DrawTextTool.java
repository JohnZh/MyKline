package com.johnzh.klinelib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Modified by john on 2020/5/10
 * <p>
 * Description:
 */
public class DrawTextTool {

    /**
     * drawText from the left-bottom point of the text
     *
     * @param text
     * @param left text left on the x-axis
     * @param bottom text bottom on the y-axis
     * @param canvas
     * @param paint
     */
    public static void drawTextFromLeftBottom(String text, float left, float bottom,
                                              Canvas canvas, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float offset = fontHeight / 2 - fontMetrics.bottom;
        float y = bottom - fontHeight / 2 + offset;
        canvas.drawText(text, left, y, paint);
    }

    /**
     * drawText from the left-top point of the text
     *
     * @param text
     * @param left text left on the x-axis
     * @param top text top on the y-axis
     * @param canvas
     * @param paint
     */
    public static void drawTextFromLeftTop(String text, float left, float top,
                                           Canvas canvas, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float offset = fontHeight / 2 - fontMetrics.bottom;
        float y = top + fontHeight / 2 + offset;
        canvas.drawText(text, left, y, paint);
    }

    public static void getTextBgFromLeftTop(String text, float left, float top,
                                            RectF rectF, Paint paint) {
        getTextBgFromLeftTop(text, left, top, 0, 0, rectF, paint);
    }

    public static void getTextBgFromLeftTop(String text, float left, float top,
                                            int paddingX, int paddingY,
                                            RectF rectF, Paint paint) {
        getTextBgFromLeftTop(text, left, top, paddingX, paddingY, paddingX, paddingY, rectF, paint);
    }

    public static void getTextBgFromLeftTop(String text, float left, float top,
                                            int paddingLeft, int paddingTop,
                                            int paddingRight, int paddingBottom,
                                            RectF rectF, Paint paint) {
        float width = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        rectF.left = left - paddingLeft;
        rectF.right = left + width + paddingRight;
        rectF.top = top - paddingTop;
        rectF.bottom = top + height + paddingBottom;
    }

    /**
     * drawText from the right-top point of the text
     *
     * @param text
     * @param right text left on the x-axis
     * @param top text top on the y-axis
     * @param canvas
     * @param paint
     */
    public static void drawTextFromRightTop(String text, float right, float top,
                                            Canvas canvas, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float offset = fontHeight / 2 - fontMetrics.bottom;
        float textWidth = paint.measureText(text);
        float left = right - textWidth;
        float y = top + fontHeight / 2 + offset;
        canvas.drawText(text, left, y, paint);
    }

    /**
     * drawText from the right-bottom point of the text
     *
     * @param text
     * @param right text right on the x-axis
     * @param bottom text bottom on the y-axis
     * @param canvas
     * @param paint
     */
    public static void drawTextFromRightBottom(String text, float right, float bottom,
                                            Canvas canvas, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float offset = fontHeight / 2 - fontMetrics.bottom;
        float textWidth = paint.measureText(text);
        float left = right - textWidth;
        float y = bottom - fontHeight / 2 + offset;
        canvas.drawText(text, left, y, paint);
    }
}
