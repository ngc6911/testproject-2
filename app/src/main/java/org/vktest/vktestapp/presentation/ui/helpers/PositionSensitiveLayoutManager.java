package org.vktest.vktestapp.presentation.ui.helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

public class PositionSensitiveLayoutManager extends StaggeredGridLayoutManager {

    public PositionSensitiveLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                          int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PositionSensitiveLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    private OnLayoutManagerUpdated onLayoutManagerUpdated;

    public void setOnLayoutManagerUpdated(OnLayoutManagerUpdated onLayoutManagerUpdated) {
        this.onLayoutManagerUpdated = onLayoutManagerUpdated;
    }

    private int lastState = 0;

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if(getVisibleCount() > 0) {
            if (onLayoutManagerUpdated != null) {
                onLayoutManagerUpdated.requestNewItems();
            }

            if(lastState != getVisibleCount()) {
                lastState = getVisibleCount();
                Log.i("Layout Manager", String.format("Visible count %d from last state %d",
                        getVisibleCount(), lastState));
            }
        }
    }

    public int getVisibleCount(){
        int[] firstVisiblePositions = findFirstVisibleItemPositions(null);
        int[] lastVisiblePositions = findLastVisibleItemPositions(null);

        for(int i = lastVisiblePositions.length - 1; i >= 0; i-- ){
            if(lastVisiblePositions[i] >= 0) {
                return lastVisiblePositions[i] - firstVisiblePositions[0] + 1;
            }
        }

        return 0;
    }

    public interface OnLayoutManagerUpdated {
        void requestNewItems();
    }
}
