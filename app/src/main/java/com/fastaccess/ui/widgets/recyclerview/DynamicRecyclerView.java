package com.fastaccess.ui.widgets.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.fastaccess.ui.widgets.StateLayout;


/**
 * Created by Kosh on 9/24/2015. copyrights are reserved
 * <p>
 * recyclerview which will showParentOrSelf/showParentOrSelf itself base on adapter
 */
public class DynamicRecyclerView extends RecyclerView {

    private StateLayout emptyView;
    @Nullable private View parentView;

    @NonNull private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override public void onChanged() {
            showEmptyView();
        }

        @Override public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    };

    public DynamicRecyclerView(Context context) {
        this(context, null);
    }

    public DynamicRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;
        addItemDecoration(BottomPaddingDecoration.with(context));
    }

    @Override public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if (isInEditMode()) return;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
            observer.onChanged();
        }
    }

    public void showEmptyView() {
        Adapter<?> adapter = getAdapter();
        if (adapter != null) {
            if (emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    showParentOrSelf(false);
                } else {
                    showParentOrSelf(true);
                }
            }
        } else {
            if (emptyView != null) {
                showParentOrSelf(false);
            }
        }
    }

    private void showParentOrSelf(boolean showRecyclerView) {
        if (parentView == null) {
            setVisibility(showRecyclerView ? VISIBLE : GONE);
        } else {
            parentView.setVisibility(showRecyclerView ? VISIBLE : GONE);
        }
        emptyView.setVisibility(!showRecyclerView ? VISIBLE : GONE);
    }

    public void setEmptyView(@NonNull StateLayout emptyView, @Nullable View parentView) {
        this.emptyView = emptyView;
        this.parentView = parentView;
        showEmptyView();
    }

    public void setEmptyView(@NonNull StateLayout emptyView) {
        setEmptyView(emptyView, null);
    }

    public void hideProgress(@NonNull StateLayout view) {
        view.hideProgress();
    }

    public void showProgress(@NonNull StateLayout view) {
        view.showProgress();
    }
}
