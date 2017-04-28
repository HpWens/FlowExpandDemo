package com.github.baserecycleradapter.multi;

import jp.satorufujiwara.binder.Section;

/**
 * desc:
 * author: ws
 * date: 2017/4/25.
 */

enum  MultiType implements Section {

    SECTION_1,
    SECTION_2,
    SECTION_3;

    @Override
    public int position() {
        return ordinal();
    }
}
