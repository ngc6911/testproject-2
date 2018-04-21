package org.vktest.vktestapp.presentation.ui.adapter;

public interface OnAdapterItemActionListener<T> {
    void onItemClick(T item, int position);
    void onScrollToBottom(T lastItem);
}
