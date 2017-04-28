package com.github.baserecycleradapter.multi;

import jp.satorufujiwara.binder.ViewType;

/**
 * desc:
 * author: ws
 * date: 2017/4/25.
 */

public enum BinderViewType implements ViewType {

    VIEW_TYPE_1,
    VIEW_TYPE_2,
    VIEW_TYPE_3;


    @Override
    public int viewType() {
        return ordinal();
    }
}
