package com.mobo.daymatter.manager;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 抖音式短视频滑动工具类
 */
public class PagerSnapLayoutManager extends LinearLayoutManager {
    private static final String TAG = "PagerSnapLayoutManager";
    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;
    private int mDrift;//位移，用来判断移动方向
    private int mLastSelectedPosition;

    public  PagerSnapLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
        init();
    }

    public PagerSnapLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    private void init() {
        mLastSelectedPosition = -1;
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        view.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                int position = getPosition(view);
                Log.d(TAG, "onChildViewAttachedToWindow " + position);
                if (mOnViewPagerListener != null && getChildCount() == 1) {
                    mOnViewPagerListener.onPageSelected(view, position, mDrift >= 0);
                    mLastSelectedPosition = position;
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                int position = getPosition(view);
                Log.d(TAG, "onChildViewDetachedFromWindow " + position);
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageRelease(view, position, mDrift >= 0);
                }
            }
        });
    }

    /**
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        if (RecyclerView.SCROLL_STATE_IDLE != state) {
            return;
        }
        View view = mPagerSnapHelper.findSnapView(this);
        if (view == null) {
            return;
        }
        int position = getPosition(view);
        if (mOnViewPagerListener != null && mLastSelectedPosition != position) {
            mOnViewPagerListener.onPageSelected(view, position, mDrift >= 0);
            mLastSelectedPosition = position;
        }
    }

    /**
     * 监听竖直方向的相对偏移量
     *
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }


    /**
     * 监听水平方向的相对偏移量
     *
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnViewPagerListener(OnViewPagerListener listener) {
        this.mOnViewPagerListener = listener;
    }

    public interface OnViewPagerListener {

        /**
         * PagerItem释放的监听
         *
         * @param itemPager 当前页面
         * @param position  当前页面在列表中的位置
         * @param isNext    滑动方向
         */
        void onPageRelease(View itemPager, int position, boolean isNext);

        /**
         * PagerItem选中的监听
         *
         * @param itemPager 当前页面
         * @param position  当前页面在列表中的位置
         * @param isNext    滑动方向
         */
        void onPageSelected(View itemPager, int position, boolean isNext);
    }
}
