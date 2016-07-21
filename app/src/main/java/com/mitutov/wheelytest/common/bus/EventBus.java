package com.mitutov.wheelytest.common.bus;

import javax.inject.Inject;

import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class EventBus extends SerializedSubject<Object, Object> {

    @Inject
    public EventBus() {
        super(PublishSubject.create());
    }

    public void send(Object o) {
        this.onNext(o);
    }
}
