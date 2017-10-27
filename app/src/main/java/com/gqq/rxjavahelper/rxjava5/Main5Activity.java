package com.gqq.rxjavahelper.rxjava5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gqq.rxjavahelper.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// http://www.jianshu.com/p/9b1304435564
public class Main5Activity extends AppCompatActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_flowable)
    public void onViewClicked() {

        // 基本使用
//        flowable1();
        // 异常及处理能力
//        flowable2();
        // 处理能力的设定
//        flowable3();
        // 异步处理能力
        flowable4();

    }

    @OnClick(R.id.btn_request)
    public void toRequest() {
        subscription.request(96);// 96
        subscription.request(128); // 96
        subscription.request(95); // 下游只接受，上游不继续发送
    }

    // 基本使用
    private void flowable1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> e) throws Exception {

                                Log.i("TAG", "emit 1");
                                e.onNext(1);
                                Log.i("TAG", "emit 2");
                                e.onNext(2);
                                Log.i("TAG", "emit 3");
                                e.onNext(3);
                                Log.i("TAG", "emit onComplete");
                                e.onComplete();
                            }
                        },
                /**
                 * 参数：背压参数
                 */
                BackpressureStrategy.ERROR)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.i("TAG", "onSubscribe");
                        /**
                         * 注意注意：下游的处理能力
                         */
//                        s.request(Long.MAX_VALUE);
                        s.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("TAG", "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w("TAG", "onError", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete");
                    }
                });
    }

    /**
     * Flowable的源码中有buffersize的大小定义:128
     */
    private void flowable2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> e) throws Exception {

                                /**
                                 * 大小128，超过会报MissingBackpressureException 异常
                                 */
                                for (int i = 0; i < 129; i++) {
                                    e.onNext(i);
                                    Log.i("TAG", "emit " + i);
                                }
                            }
                        },
                /**
                 * 参数：背压参数
                 */
                BackpressureStrategy.ERROR)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.i("TAG", "onSubscribe");
                        /**
                         * 注意注意：下游的处理能力
                         */
                        s.request(Long.MAX_VALUE);
//                        s.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("TAG", "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w("TAG", "onError", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete");
                    }
                });
    }

    /**
     * 同步处理：
     * 上游如何得知下游的处理能力呢？
     * 利用上游的FlowableEmitter的requested方法
     */
    private void flowable3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {

                Log.i("TAG", "emit 1");
                e.onNext(1);
                Log.i("TAG", "after emit 1, requested = " + e.requested());

                Log.i("TAG", "emit 2");
                e.onNext(2);
                Log.i("TAG", "after emit 2, requested = " + e.requested());

                Log.i("TAG", "emit 3");
                e.onNext(3);
                Log.i("TAG", "after emit 3, requested = " + e.requested());

                Log.i("TAG", "emit onComplete");
                e.onComplete();
                Log.i("TAG", "after emit onComplete, requested = " + e.requested());

            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.i("TAG", "onSubscribe");
                        /**
                         * 1. 不调用request时，不告知处理能力的时候，同步处理，为 0
                         * 2. 调了的时候呢：要求多少就有多少，请求多次就累加
                         * 3. 什么时候做减法呢？发送事件接受事件之后。。
                         */
                        s.request(10);// 我要十个
//                        s.request(100);// 我要一百个
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("TAG", "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w("TAG", "onError", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete");
                    }
                });
    }

    /**
     * 异步
     * 默认的水缸大小128,发送128个事件之后，外部调用request(96),处理96个事件，然后上游在发送96个事件
     * 为什么是96，换成其他128啥的也都是96
     */
    private void flowable4() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {

                Log.d("TAG", "First requested = " + e.requested());
                boolean flag;
                for (int i = 0; ; i++) {
                    flag = false;
                    while (e.requested() == 0) {
                        if (!flag) {
                            Log.d("TAG", "Oh no! I can't emit value!");
                            flag = true;
                        }
                    }
                    e.onNext(i);
                    Log.d("TAG", "emit " + i + " , requested = " + e.requested());
                }
//                Log.i("TAG", "emit requested = " + e.requested());

            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.i("TAG", "onSubscribe");
                        /**
                         * 1. 不调用request时，不告知处理能力的时候，128
                         * 2. 调了的时候呢：异步处理，不受影响
                         * 3. 外部请求
                         */
//                        s.request(10);// 我要十个
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("TAG", "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w("TAG", "onError", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete");
                    }
                });
    }
}
