package com.owangwang.demopull;

import rx.Observable;
import rx.Subscriber;

public class myClass {
    public static void main(String[] args) {
        Subscriber subscriber=new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext"+s);
            }

            @Override
            public void onStart() {
                System.out.println("onStart");
            }
        };

        Observable observable=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("数据1");
                subscriber.onNext("数据2");
                subscriber.onNext("数据3");
                subscriber.onCompleted();
            }

        });
        observable.subscribe(subscriber);
    }
}
