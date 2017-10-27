package com.gqq.rxjavahelper.rxjava3;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gqq.rxjavahelper.R;

import java.io.InterruptedIOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class Main3Activity extends AppCompatActivity {

    /**
     * Zip 操作符
     * 1. 将两次发送的事件组合到一起：A、B、C／1、2、3-－－> A1、B2、C3
     * 2. 事件数量不一致时，最短流决定
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_zip)
    public void onViewClicked() {

//        zipAction();// 使用
        zipAction1();// 模拟耗时

    }

    private void zipAction() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.i("TAG", "onNext:1");
                e.onNext(2);
                Log.i("TAG", "onNext:2");
                e.onNext(3);
                Log.i("TAG", "onNext:3");
                e.onNext(4);
                Log.i("TAG", "onNext:4");
                e.onComplete();
                Log.i("TAG", "onComplete1");
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                Log.i("TAG", "onNext:A");
                e.onNext("B");
                Log.i("TAG", "onNext:B");
                e.onNext("C");
                Log.i("TAG", "onNext:C");
//                e.onNext("D");
//                Log.i("TAG","onNext:D");
                e.onComplete();
                Log.i("TAG", "onComplete2");
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + "," + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("TAG", "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.i("TAG", "onNext:" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i("TAG", "onError");
            }

            @Override
            public void onComplete() {
                Log.i("TAG", "onComplete");
            }
        });
    }

    // 模拟耗时操作
    private void zipAction1() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                /**
                 * 模拟耗时的网络请求
                 */
                e.onNext(1);
                SystemClock.sleep(1000);
                Log.i("TAG", "onNext:1");
                e.onNext(2);
                SystemClock.sleep(1000);
                Log.i("TAG", "onNext:2");
                e.onNext(3);
                SystemClock.sleep(1000);
                Log.i("TAG", "onNext:3");
                e.onNext(4);
                SystemClock.sleep(1000);
                Log.i("TAG", "onNext:4");
                e.onComplete();
                Log.i("TAG", "onComplete1");
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                /**
                 * 模拟耗时的网络请求
                 */
                e.onNext("A");
                SystemClock.sleep(1000);
                Log.i("TAG", "onNext:A");
                e.onNext("B");
                SystemClock.sleep(1000);
                Log.i("TAG", "onNext:B");
                e.onNext("C");
                SystemClock.sleep(1000);
                Log.i("TAG", "onNext:C");
//                e.onNext("D");
//                Thread.sleep(1000);
//                Log.i("TAG","onNext:D");
                e.onComplete();
                Log.i("TAG", "onComplete2");

                /**
                 * 使用Thread.sleep()会导致崩溃，原因是：长数据流所在的线程正在结束但还没有结束,而在sleep的过程中线程被结束了所以报错
                 * 改为SystemClock.sleep();
                 * 静态代码块解决，加在哪？
                 */
//                static {
//                    RxJavaPlugins.setErrorHandler(new Consumer() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            if (throwable instanceof InterruptedIOException) {
//                                Log.d(TAG, "Io interrupted");
//                            }
//                        }
//                    });
//                }

            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + "," + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("TAG", "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.i("TAG", "onNext:" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i("TAG", "onError");
            }

            @Override
            public void onComplete() {
                Log.i("TAG", "onComplete");
            }
        });
    }
}
