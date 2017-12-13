package com.owangwang.demopull;

import org.junit.Test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
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
        //输出结果为：
        //range：0
        //range：1...
//        Observable.range(0,20).repeat(3).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                System.out.println("range:"+integer.intValue());
//            }
//        });
        //--------------------------------------------------------------------------
//        Observable.just(1,2,3).flatMapIterable(new Func1<Integer, Iterable<Integer>>() {
//            @Override
//            public Iterable<Integer> call(Integer integer) {
//                List<Integer> mlist=new ArrayList<Integer>();
//                mlist.add(integer+1);
//                mlist.add(integer+2);
//                mlist.add(integer+3);
//`
//                return mlist;
//            }
//        }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                System.out.println("flagMapIterable:"+integer);
//            }
//        });
        //---------------------------------------------------------------------------
//        Swordsman s1=new Swordsman("第一个","2");
//        Swordsman s2=new Swordsman("第一个","3");
//        Swordsman s3=new Swordsman("第一个","2");
//        Swordsman s4=new Swordsman("第一个","1");
//        Swordsman s5=new Swordsman("第一个","3");
//        Swordsman s6=new Swordsman("第一个","1");
//        Observable<GroupedObservable<String,Swordsman>> observable1111=
//                Observable.just(s1,s2,s3,s4,s5,s6).groupBy(new Func1<Swordsman, String>() {
//            @Override
//            public String call(Swordsman swordsman) {
//
//                return swordsman.getLevel();
//            }
//        });
//        Observable.concat(observable1111).subscribe(new Action1<Swordsman>() {
//            @Override
//            public void call(Swordsman swordsman) {
//                System.out.println("groupBy:"+swordsman.getName()+"--------"+swordsman.getLevel());
//            }
//        });
        //-----------------------------------------------------------------------------
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                for (int i=0;i<100;i++){
//                    subscriber.onNext(i);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                subscriber.onCompleted();
//            }
//        })
//                .throttleFirst(200, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
////                System.out.println("-------"+integer);
//                Log.d("Tag","-------"+integer);
//
//            }
//        });
        //========================================
        Observable<Integer> observable1=Observable.just(1,2,3,4,5,6,7,8);
        Observable<Integer> observable2=Observable.just(10,20,30,40,50,60,70,80);
        Observable.merge(observable1,observable2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("Tag","===="+integer);
            }
        });

    }
}