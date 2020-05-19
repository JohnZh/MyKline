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
package com.johnzh.klinelib.drawarea.impl;

import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.drawarea.DrawArea;

import java.util.List;

/**
 * Created by john on 2020/5/16
 * <p>
 * A rect draw area which does not contain padding
 */
public abstract class RectDrawArea implements DrawArea {

    protected int width;
    protected int height;
    protected int left;
    protected int top;
    protected boolean hide;

    public RectDrawArea() {
        this(-1, -1); // -1 match parent
    }

    public RectDrawArea(int height) {
        this(-1, height); // -1 match parent
    }

    public RectDrawArea(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void prepareOnDraw(KlineView view, List<DrawArea> allDrawAreas) {
        this.left = view.getPaddingLeft();
        if (width < 0) { // match parent
            this.width = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        }

        int i = 0;
        for (; i < allDrawAreas.size(); i++) {
            if (allDrawAreas.get(i) == this) break;
        }
        if (i == 0) {
            this.top = view.getPaddingTop();
            if (this.height < 0) {
                this.height = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
            }
        } else {
            DrawArea preDrawArea = allDrawAreas.get(i - 1);
            this.top = preDrawArea.getTop() + preDrawArea.getHeight();
            if (this.height < 0) {
                this.height = view.getHeight()
                        - (preDrawArea.getTop() + preDrawArea.getHeight())
                        - view.getPaddingBottom();
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean contains(float x, float y) {
        return width != 0 && height != 0  // check for empty first
                && x >= left && x <= left + width && y >= top && y <= top + height;
    }

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public int getTop() {
        return top;
    }

    @Override
    public void hide(boolean hide) {
        this.hide = hide;
    }

    @Override
    public boolean isVisible() {
        return !hide;
    }
}
