package com.johnzh.klinelib.drawarea;

import com.johnzh.klinelib.KlineView;

import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
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
