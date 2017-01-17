package com.graffitab.ui.fragments.streamables;

/**
 * Created by georgichristov on 16/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class SwimlaneStreamablesFragment extends GenericStreamablesFragment {

    @Override
    public void basicInit() {
        setViewType(ViewType.SWIMLANE);
    }
}
