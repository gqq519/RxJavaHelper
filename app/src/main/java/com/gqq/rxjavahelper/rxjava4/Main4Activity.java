package com.gqq.rxjavahelper.rxjava4;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gqq.rxjavahelper.R;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

// http://www.jianshu.com/p/0f2d6c2387c9
public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_backpressure)
    public void onViewClicked() {
//        backpressure1();// 循环发送消息
//        backPressure2();// 数量上控制发送
//        backPressure3();// 间隔取样控制发送
        backPressure4();// 控制发送速率

    }

    /**
     * 循环发送事件，内存迅速增加，会出现内存溢出
     * 场景：频繁快速的请求数据，而没有及时处理
     */
    private void backpressure1() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        });
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + "," + s;
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(Main4Activity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 减缓内存的消耗（并没有什么用，实际也不会这么处理）
     * 1. 数量上抑制
     *    1. 筛选 （只是减缓了内存溢出）
     *    2. 间隔取样
     *    在一定程度上可以抑制，但是会造成数据丢失
     * 2. 速率上抑制
     * 使用Flowable ，通过背压解决，Flowable怎么用————> Main5Activity
     */

    // 1. 筛选
    public void backPressure2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).observeOn(Schedulers.io())
                /** 1. 第一种方式
                 * filter筛选，符合条件才会发送到接收端
                 */
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 100 == 0;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("TAG","integer："+integer);
                    }
                });
    }

    /**
     * 2. 间隔取样
     */
    public void backPressure3() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).observeOn(Schedulers.io())
                /** 2. 第二种方式
                 * sample取样，每隔两秒取出一个事件发送
                 */
                .sample(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("TAG","integer："+integer);
                    }
                });
    }

    /**
     * 3. 控制发送的速率
     */
    public void backPressure4() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                    // 发送间隔1秒
                    SystemClock.sleep(1000);
                }
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("TAG","integer："+integer);
                    }
                });
    }
}
