package com.graffitab.ui.fragments.comments;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListCommentsFragment extends GenericCommentsFragment {

    @Override
    public void basicInit() {
        super.basicInit();
        setViewType(ViewType.LIST_FULL);
    }
}
