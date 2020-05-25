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
     * Get a background rect from text left and top with padding
     *
     * @param text
     * @param left
     * @param top
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromLeftTop(String text, float left, float top,
                                            RectF rectF, Paint paint) {
        getTextBgFromLeftTop(text, left, top, 0, 0, rectF, paint);
    }

    /**
     * Get a background rect from text left and top with padding
     *
     * @param text
     * @param left
     * @param top
     * @param paddingX
     * @param paddingY
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromLeftTop(String text, float left, float top,
                                            int paddingX, int paddingY,
                                            RectF rectF, Paint paint) {
        getTextBgFromLeftTop(text, left, top, paddingX, paddingY, paddingX, paddingY, rectF, paint);
    }

    /**
     * Get a background rect from text left and top with padding
     *
     * @param text
     * @param left
     * @param top
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     * @param rectF
     * @param paint
     */
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
     * Get a background rect from text left and bottom with padding
     *
     * @param text
     * @param left
     * @param bottom
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromLeftBottom(String text, float left, float bottom,
                                               RectF rectF, Paint paint) {
        getTextBgFromLeftBottom(text, left, bottom, 0, 0, rectF, paint);
    }

    /**
     * Get a background rect from text left and bottom with padding
     *
     * @param text
     * @param left
     * @param bottom
     * @param paddingX
     * @param paddingY
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromLeftBottom(String text, float left, float bottom,
                                               int paddingX, int paddingY,
                                               RectF rectF, Paint paint) {
        getTextBgFromLeftBottom(text, left, bottom, paddingX, paddingY, paddingX, paddingY, rectF, paint);
    }

    /**
     * Get a background rect from text left and bottom with padding
     *
     * @param text
     * @param left
     * @param bottom
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromLeftBottom(String text, float left, float bottom,
                                               int paddingLeft, int paddingTop,
                                               int paddingRight, int paddingBottom,
                                               RectF rectF, Paint paint) {
        float width = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        rectF.left = left - paddingLeft;
        rectF.right = left + width + paddingRight;
        rectF.top = bottom - height - paddingTop;
        rectF.bottom = bottom + paddingBottom;
    }

    /**
     * Get a background rect from text right and top with padding
     *
     * @param text
     * @param right
     * @param top
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromRightTop(String text, float right, float top,
                                             RectF rectF, Paint paint) {
        getTextBgFromRightTop(text, right, top, 0,0, rectF, paint);
    }

    /**
     * Get a background rect from text right and top with padding
     *
     * @param text
     * @param right
     * @param top
     * @param paddingX
     * @param paddingY
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromRightTop(String text, float right, float top,
                                             int paddingX, int paddingY,
                                             RectF rectF, Paint paint) {
        getTextBgFromRightTop(text, right, top, paddingX, paddingY, paddingX, paddingY, rectF, paint);
    }

    /**
     * Get a background rect from text right and top with padding
     *
     * @param text
     * @param right
     * @param top
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromRightTop(String text, float right, float top,
                                             int paddingLeft, int paddingTop,
                                             int paddingRight, int paddingBottom,
                                             RectF rectF, Paint paint) {
        float width = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        rectF.left = right - width - paddingLeft;
        rectF.right = right + paddingRight;
        rectF.top = top - paddingTop;
        rectF.bottom = top + height + paddingBottom;
    }


    /**
     * Get a background rect from text right and bottom with padding
     *
     * @param text
     * @param right
     * @param bottom
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromRightBottom(String text, float right, float bottom,
                                                RectF rectF, Paint paint) {
        getTextBgFromRightBottom(text, right, bottom, 0, 0,  rectF, paint);
    }

    /**
     * Get a background rect from text right and bottom with padding
     *
     * @param text
     * @param right
     * @param bottom
     * @param paddingX
     * @param paddingY
     * @param rectF
     * @param paint
     */
    public static void getTextBgFromRightBottom(String text, float right, float bottom,
                                                int paddingX, int paddingY,
                                                RectF rectF, Paint paint) {
        getTextBgFromRightBottom(text, right, bottom, paddingX, paddingY, paddingX, paddingY, rectF, paint);
    }

        /**
         * Get a background rect from text right and bottom with padding
         *
         * @param text
         * @param right
         * @param bottom
         * @param paddingLeft
         * @param paddingTop
         * @param paddingRight
         * @param paddingBottom
         * @param rectF
         * @param paint
         */
    public static void getTextBgFromRightBottom(String text, float right, float bottom,
                                                int paddingLeft, int paddingTop,
                                                int paddingRight, int paddingBottom,
                                                RectF rectF, Paint paint) {
        float width = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        rectF.left = right - width - paddingLeft;
        rectF.right = right + paddingRight;
        rectF.top = bottom - height - paddingTop;
        rectF.bottom = bottom + paddingBottom;
    }

    /**
     * drawText from the left-top point of the text
     *
     * @param text
     * @param left   text left on the x-axis
     * @param top    text top on the y-axis
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

    /**
     * drawText from the left-CenterX point of the text
     *
     * @param text
     * @param left
     * @param centerX
     * @param canvas
     * @param paint
     */
    public static void drawTextFromLeftCenterX(String text, float left, float centerX,
                                               Canvas canvas, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float offset = fontHeight / 2 - fontMetrics.bottom;
        float y = centerX + offset;
        canvas.drawText(text, left, y, paint);
    }

    /**
     * drawText from the left-bottom point of the text
     *
     * @param text
     * @param left   text left on the x-axis
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
     * drawText from the right-top point of the text
     *
     * @param text
     * @param right  text left on the x-axis
     * @param top    text top on the y-axis
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
     * drawText from the right-CenterX point of the text
     *
     * @param text
     * @param right
     * @param centerX
     * @param canvas
     * @param paint
     */
    public static void drawTextFromRightCenterX(String text, float right, float centerX,
                                                Canvas canvas, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float offset = fontHeight / 2 - fontMetrics.bottom;
        float textWidth = paint.measureText(text);
        float left = right - textWidth;
        float y = centerX + offset;
        canvas.drawText(text, left, y, paint);
    }

    /**
     * drawText from the right-bottom point of the text
     *
     * @param text
     * @param right  text right on the x-axis
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
