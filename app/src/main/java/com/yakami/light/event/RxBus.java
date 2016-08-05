package com.yakami.light.event;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Yakami on 2016/3/30.
 * 使用RxJava实现EventBus，消息总线
 *
 */
@SuppressWarnings("all")
public class RxBus {

    private static RxBus rxBus;
    /**
     * Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，要避免该问题，需要将 Subject转换为一个
     * SerializedSubject ，上述RxBus类中把线程非安全的PublishSubject包装成线程安全的Subject。
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者。
     */
    private final Subject<Event<?>, Event<?>> _bus = new SerializedSubject<>(PublishSubject.create());

    private RxBus() {
    }

    //单例模式
    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    //发送事件
    public void send(Event<?> o) {
        _bus.onNext(o);
    }

    //发送事件，第二参数为指定返回的事件类型，message参数由于是泛型，取出事件类型后，直接将返回值覆盖message即可。
    public void send(Event.EventType sendType, Event.EventType receiveType) {
        Event<Object> event = new Event<>();
        event.type = sendType;
        event.message = receiveType;
        send(event);
    }

    //返回Observable对象，用于支持SubscriberBuilder订阅事件，面向内部类的
    public Observable<Event<?>> toObservable() {
        return _bus;
    }

    //返回订阅者构建类，用于面向外部订阅
    //对于直接传过来的Activity，由于使用了RxLifecycle库的RxActivity，所以默认实现了FragmentLifecycleProvider和ActivityLifecycleProvider
    public static SubscriberBuilder with(FragmentLifecycleProvider provider) {
        return new SubscriberBuilder(provider);
    }

    public static SubscriberBuilder with(ActivityLifecycleProvider provider) {
        return new SubscriberBuilder(provider);
    }


    public static class SubscriberBuilder {

        private FragmentLifecycleProvider mFragLifecycleProvider;
        private ActivityLifecycleProvider mActLifecycleProvider;
        private FragmentEvent mFragmentEndEvent;
        private ActivityEvent mActivityEndEvent;
        private Event.EventType event;
        private Action1<? super Event<?>> onNext;
        private Action1<Throwable> onError;

        //传进的FragmentLifecycleProvider等都是用于监听activity或fragment的生命周期的，用于适时中断序列，以免内存泄漏
        public SubscriberBuilder(FragmentLifecycleProvider provider) {
            this.mFragLifecycleProvider = provider;
        }

        public SubscriberBuilder(ActivityLifecycleProvider provider) {
            this.mActLifecycleProvider = provider;
        }

        //设置要订阅的事件类型
        public SubscriberBuilder setEvent(Event.EventType event) {
            this.event = event;
            return this;
        }

        //设置终止事件类型，与RxLifecycle库相关
        public SubscriberBuilder setEndEvent(FragmentEvent event) {
            this.mFragmentEndEvent = event;
            return this;
        }

        public SubscriberBuilder setEndEvent(ActivityEvent event) {
            this.mActivityEndEvent = event;
            return this;
        }

        //设置回调
        public SubscriberBuilder onNext(Action1<? super Event<?>> action) {
            this.onNext = action;
            return this;
        }

        public SubscriberBuilder onError(Action1<Throwable> action) {
            this.onError = action;
            return this;
        }

        public Observable observable() {
            if (mFragLifecycleProvider != null) {
                return RxBus.getInstance().toObservable()
                        .compose(mFragLifecycleProvider.bindUntilEvent(mFragmentEndEvent))
                        .filter(events -> events.type == event);
            }
            if (mActLifecycleProvider != null) {
                return RxBus.getInstance().toObservable()
                        .compose(mActLifecycleProvider.bindUntilEvent(mActivityEndEvent))
                        .filter(events -> events.type == event);
            }
            return null;
        }

        //创建订阅
        public void create() {
            _create();
        }

        //创建订阅
        public Subscription _create() {
            if (mFragLifecycleProvider != null) {
                return RxBus.getInstance().toObservable()
                        //这里利用RxLifecycle提供的生命周期，适时终止队列
                        .compose(mFragLifecycleProvider.bindUntilEvent(mFragmentEndEvent))
                        //订阅的响应规则，事件必须相等
                        .filter(events -> events.type == event)
                        //订阅
                        .subscribe(onNext, onError == null ? Throwable::printStackTrace : onError);
            }
            if (mActLifecycleProvider != null) {
                return RxBus.getInstance().toObservable()
                        .compose(mActLifecycleProvider.bindUntilEvent(mActivityEndEvent))
                        .filter(events -> events.type == event)
                        .subscribe(onNext, onError == null ? Throwable::printStackTrace : onError);
            }
            return null;
        }
    }

}








