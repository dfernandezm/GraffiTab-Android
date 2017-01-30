package com.graffitab.model;

import com.graffitabsdk.model.GTComment;

/**
 * Created by georgichristov on 30/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTCommentExtension extends GTComment {

    public enum State {SENT, SENDING, FAILED};

    private State state = State.SENT;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
