package com.johnzh.klinelib.gesture;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description:
 */
public final class DragInfo {
    private float actionDownX;

    private float oneDataWidth;
    private int draggedDataAmount;
    private int preDraggedDataAmount;
    private int maxDraggedDataAmount;

    public void setActionDownX(float actionDownX) {
        this.actionDownX = actionDownX;
    }

    public float getActionDownX() {
        return actionDownX;
    }

//    public void updateDragDistanceX(float scale) {
//        dragDistanceX = dragDistanceX * scale;
//        preDragDistanceX = preDragDistanceX * scale;
//        if (dragDistanceX > maxDragDistanceX) {
//            dragDistanceX = maxDragDistanceX;
//        }
//        if (preDragDistanceX > maxDragDistanceX) {
//            preDragDistanceX = maxDragDistanceX;
//        }
//    }

    public void setOneDataWidth(float oneDataWidth) {
        this.oneDataWidth = oneDataWidth;
    }

    public int getDraggedDataAmount() {
        return draggedDataAmount;
    }

    public int calcDraggedDataAmount(float moveX) {
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
}
