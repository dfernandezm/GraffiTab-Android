package com.graffitab.ui.fragments.streamables;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GridStreamablesFragment extends GenericStreamablesFragment {

    @Override
    public void basicInit() {
        super.basicInit();
        setViewType(ViewType.GRID);
    }
}
