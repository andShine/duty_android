package com.jujinziben.duty.common;

/**
 *
 * Created by liu on 2017/9/8.
 */

public class HomeEvent {
    private boolean isNeedRefresh;

    public boolean isNeedRefresh() {
        return isNeedRefresh;
    }

    public HomeEvent setNeedRefresh(boolean needRefresh) {
        isNeedRefresh = needRefresh;
        return this;
    }
}
