package com.zhengshang.meeting.ui.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 拖动排序 listview
 * Created by sun on 2016/2/22.
 */
public class SortListView extends ListView implements AdapterView.OnItemLongClickListener {

    private static final String LOG_TAG = "SortListView";
    private int startY;
    private static final int DRAG_IMG_SHOW = 1;
    private static final int DRAG_IMG_NOT_SHOW = 0;
    private ImageView dragImageView;
    private WindowManager.LayoutParams dragImageViewParams;
    private WindowManager windowManager;

    private int preDraggedOverPositon = AdapterView.INVALID_POSITION;
    private boolean isViewOnDrag = false;
    private DragListViewAdapter dragAdater;

    public SortListView(Context context) {
        super(context);
        init();
    }

    public SortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SortListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnItemLongClickListener(this);
        //初始化显示被拖动item的image view
        dragImageView = new ImageView(getContext());
        dragImageView.setTag(DRAG_IMG_NOT_SHOW);
        //初始化用于设置dragImageView的参数对象
        dragImageViewParams = new WindowManager.LayoutParams();
        //获取窗口管理对象，用于后面向窗口中添加dragImageView
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //被按下时记录按下的坐标
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //获取触摸点相对于屏幕的坐标
            startY = (int) ev.getRawY();
        }
        //dragImageView处于被拖动时，更新dragImageView位置
        else if (ev.getAction() == MotionEvent.ACTION_MOVE && isViewOnDrag) {
            Log.i(LOG_TAG, "" + ev.getRawX() + " " + ev.getRawY());
            //设置触摸点为dragImageView中心
            dragImageViewParams.x = dragImageView.getWidth() / 2;
            dragImageViewParams.y = (int) (ev.getRawY() - dragImageView.getHeight() / 2);
            //更新窗口显示
            windowManager.updateViewLayout(dragImageView, dragImageViewParams);
            //获取当前触摸点的item position
            int currDraggedPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            //如果当前停留位置item不等于上次停留位置的item 并且不是第一个位置，交换本次和上次停留的item
            if (currDraggedPosition != AdapterView.INVALID_POSITION && currDraggedPosition != preDraggedOverPositon) {
                dragAdater.swapView(preDraggedOverPositon, currDraggedPosition);
                preDraggedOverPositon = currDraggedPosition;
            }
        }
        //释放dragImageView
        else if (ev.getAction() == MotionEvent.ACTION_UP && isViewOnDrag) {
            dragAdater.showHideView();
            if ((int) dragImageView.getTag() == DRAG_IMG_SHOW) {
                windowManager.removeView(dragImageView);
                dragImageView.setTag(DRAG_IMG_NOT_SHOW);
            }
            isViewOnDrag = false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - getHeaderViewsCount();
        //记录长按item位置
        preDraggedOverPositon = position;

        //获取被长按item的drawing cache
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        //通过被长按item，获取拖动item的bitmap
        Bitmap dragBitmap = Bitmap.createBitmap(view.getDrawingCache());

        //设置拖动item的参数
        dragImageViewParams.gravity = Gravity.TOP | Gravity.LEFT;
        //设置拖动item的宽和高
        dragImageViewParams.width = dragBitmap.getWidth();
        dragImageViewParams.height = dragBitmap.getHeight();
        //设置触摸点为绘制拖动item的中心
        dragImageViewParams.x = dragImageViewParams.width / 2;
        dragImageViewParams.y = (startY - dragImageViewParams.height / 2);
        dragImageViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        dragImageViewParams.format = PixelFormat.TRANSLUCENT;
        dragImageViewParams.windowAnimations = 0;

        //dragImageView为被拖动item的容器，清空上一次的显示
        if ((int) dragImageView.getTag() == DRAG_IMG_SHOW) {
            windowManager.removeView(dragImageView);
            dragImageView.setTag(DRAG_IMG_NOT_SHOW);
        }

        //设置本次被长按的item
        dragImageView.setImageBitmap(dragBitmap);

        //添加拖动item到屏幕
        windowManager.addView(dragImageView, dragImageViewParams);
        dragImageView.setTag(DRAG_IMG_SHOW);
        isViewOnDrag = true;
        //设置被长按item不显示
        dragAdater.hideView(position);
        return true;
    }

    public interface DragListViewAdapter {
        void hideView(int pos);

        void showHideView();

        void swapView(int draggedPos, int destPos);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            if (adapter instanceof DragListViewAdapter) {
                dragAdater = (DragListViewAdapter) adapter;
            } else {
                throw new IllegalStateException("adapter must implements DragListViewAdapter ");
            }
        }
    }
}
