package com.graffitab.ui.fragments.streamables;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class ListStreamablesFragment extends GenericStreamablesFragment {

    @Override
    public void basicInit() {
        super.basicInit();
        setViewType(ViewType.LIST_FULL);
    }
}
