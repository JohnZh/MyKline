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
package com.johnzh.klinelib.gesture;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description:
 */
public final class DragInfo {

    public interface Listener {
        /**
         * Call when drag occur, totalData = remainingData + draggedData + visibleData
         *
         * @param remainingData the remaining data in the left of visible data
         * @param draggedData   the dragged data in the right of visible data
         * @param visibleData   the quality of visible data
         */
        void onDrag(int remainingData, int draggedData, int visibleData);
    }

    private float actionDownX;

    private int draggedDataAmount;
    private int preDraggedDataAmount;
    private int maxDraggedDataAmount;

    public void setActionDownX(float actionDownX) {
        this.actionDownX = actionDownX;
    }

    public float getActionDownX() {
        return actionDownX;
    }

    public int getDraggedDataAmount() {
        return draggedDataAmount;
    }

    public int calcDraggedDataAmount(float moveX, float oneDataWidth) {
        int newDraggedDataAmount
                = (int) ((moveX - actionDownX) / oneDataWidth) + preDraggedDataAmount;
        if (newDraggedDataAmount > maxDraggedDataAmount) {
            newDraggedDataAmount = maxDraggedDataAmount;
        }
        if (newDraggedDataAmount < 0) {
            newDraggedDataAmount = 0;
        }
        return newDraggedDataAmount;
    }

    public void setDraggedDataAmount(int draggedDataAmount) {
        this.draggedDataAmount = draggedDataAmount;
    }

    public int getPreDraggedDataAmount() {
        return preDraggedDataAmount;
    }

    public void setPreDraggedDataAmount(int preDraggedDataAmount) {
        this.preDraggedDataAmount = preDraggedDataAmount;
    }

    public int getMaxDraggedDataAmount() {
        return maxDraggedDataAmount;
    }

    public void setMaxDraggedDataAmount(int maxDraggedDataAmount) {
        this.maxDraggedDataAmount = maxDraggedDataAmount;

        if (maxDraggedDataAmount < draggedDataAmount) {
            draggedDataAmount = maxDraggedDataAmount;
        }
        if (maxDraggedDataAmount < preDraggedDataAmount) {
            preDraggedDataAmount = maxDraggedDataAmount;
        }
    }

    public boolean isLeftMost() {
        return maxDraggedDataAmount != 0
                && draggedDataAmount == maxDraggedDataAmount;
    }

    public boolean isRightMost() {
        return maxDraggedDataAmount != 0
                && draggedDataAmount == 0;
    }

    public void clear() {
        draggedDataAmount = 0;
        preDraggedDataAmount = 0;
        maxDraggedDataAmount = 0;
    }
}
