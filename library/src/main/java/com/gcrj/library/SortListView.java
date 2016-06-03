package com.gcrj.library;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by zhangxin on 2016-3-7.
 */
public class SortListView<T> extends ListView {

    public final int DRAG_MODE_NORMOL = 0;
    public final int DRAG_MODE_SORT = 1;

    private int mode;
    private View dragView;
    private View nextView;
    private View lastView;
    private Rect rect;
    private int itemHeight;
    private int startY;
    private int dragPosition;
    private int nextPosition;
    private int lastPosition;
    private int delay;
    private List<T> mList;
    private T dragT;
    private T nextT;
    private T lastT;

    public SortListView(Context context) {
        super(context);
        setDividerHeight(0);
    }

    public SortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDividerHeight(0);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof SortAdapter) {
            mList = ((SortAdapter) adapter).getList();
            super.setAdapter(adapter);
        } else {
            throw new IllegalArgumentException("adapter must implements SortAdapter");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (ev.getX() > getWidth() - 200) {
                mode = DRAG_MODE_SORT;
            } else {
                mode = DRAG_MODE_NORMOL;
            }
        }

        if (mode == DRAG_MODE_NORMOL) {
            return super.onTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (itemHeight == 0 && getChildCount() > 1) {
                    itemHeight = (int) (getChildAt(1).getY() - getChildAt(0).getY());
                }
                dragPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                dragView = getChildAt(dragPosition - getFirstVisiblePosition());
                if (dragView == null) {
                    break;
                }

                rect = new Rect(dragView.getLeft(), dragView.getTop(), dragView.getRight(), dragView.getBottom());
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragView == null) {
                    break;
                }

                int delayY = (int) ev.getY() - startY;
                dragView.layout(rect.left, rect.top + delayY, rect.right, rect.bottom + delayY);
                nextView = null;
                lastView = null;

                if (delay == 0) {
                    nextPosition = dragPosition + 1;
                    lastPosition = dragPosition - 1;
                } else if (delay < 0) {
                    nextPosition = dragPosition;
                    lastPosition = dragPosition - 1;
                } else {
                    nextPosition = dragPosition + 1;
                    lastPosition = dragPosition;
                }

                if (nextPosition <= getFirstVisiblePosition() + getChildCount()) {
                    nextView = getChildAt(nextPosition - getFirstVisiblePosition());
                }
                if (lastPosition >= 0) {
                    lastView = getChildAt(lastPosition - getFirstVisiblePosition());
                }

                if (nextView != null && dragView.getBottom() - nextView.getTop() >= itemHeight / 2) {
                    nextView.layout(rect.left, rect.top, rect.right, rect.bottom);
                    rect.top += itemHeight;
                    rect.bottom += itemHeight;
                    startY += itemHeight;

                    dragT = mList.get(dragPosition);
                    nextT = mList.get(dragPosition + 1);
                    mList.remove(dragPosition + 1);
                    mList.add(dragPosition + 1, dragT);
                    mList.remove(dragPosition);
                    mList.add(dragPosition, nextT);

                    dragPosition++;
                    delay++;
                } else if (lastView != null && lastView.getBottom() - dragView.getTop() >= itemHeight / 2) {
                    lastView.layout(rect.left, rect.top, rect.right, rect.bottom);
                    rect.top -= itemHeight;
                    rect.bottom -= itemHeight;
                    startY -= itemHeight;

                    dragT = mList.get(dragPosition);
                    lastT = mList.get(dragPosition - 1);
                    mList.remove(dragPosition);
                    mList.add(dragPosition, lastT);
                    mList.remove(dragPosition - 1);
                    mList.add(dragPosition - 1, dragT);

                    dragPosition--;
                    delay--;
                }
                break;
            case MotionEvent.ACTION_UP:
                dragView.layout(rect.left, rect.top, rect.right, rect.bottom);
                dragView = null;
                nextView = null;
                lastView = null;

                delay = 0;
                ((BaseAdapter) getAdapter()).notifyDataSetChanged();
                break;
        }
        return true;
    }

}
