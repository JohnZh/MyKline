package com.johnzh.klinelib.gesture;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description:
 */
public final class DragInfo {
    private float maxDragDistanceX;
    private float actionDownX;
    private float dragDistanceX;
    private float preDragDistanceX;

    public void setActionDownX(float actionDownX) {
        this.actionDownX = actionDownX;
    }

    public float getActionDownX() {
        return actionDownX;
    }

    public void setPreDragDistanceX(float preDragDistanceX) {
        this.preDragDistanceX = preDragDistanceX;
    }

    public void setMaxDragDistanceX(float maxDragDistanceX) {
        this.maxDragDistanceX = maxDragDistanceX;
    }

    public void setDragDistanceX(float dragDistanceX) {
        this.dragDistanceX = dragDistanceX;
    }

    public float getMaxDragDistanceX() {
        return maxDragDistanceX;
    }

    public float getDragDistanceX() {
        return dragDistanceX;
    }

    public void calcDragDistanceX(float actionMoveX) {
        dragDistanceX = actionMoveX - actionDownX + preDragDistanceX;
    }
}
