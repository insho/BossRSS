package com.inshodesign.bossrss.Adapters;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Sends objects from adapters to fragments when user clicks/long-presses adapter items
 *
 * @see RSSItemsAdapter
 * @see RSSListAdapter
 *
 * @see com.inshodesign.bossrss.Fragments.RSSItemsFragment
 * @see com.inshodesign.bossrss.Fragments.RSSListFragment
 */
public class RxBus {
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());
    private final Subject<Object, Object> _busLongClick = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }
    public void sendLongClick(Object o) {
        _busLongClick.onNext(o);
    }

    public Observable<Object> toClickObserverable() {
        return _bus;
    }
    public Observable<Object> toLongClickObserverable() {
        return _busLongClick;
    }
}