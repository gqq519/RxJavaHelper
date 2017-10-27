package com.gqq.rxjavahelper.rxjava1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gqq.rxjavahelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Main1Activity extends AppCompatActivity {

    @BindView(R.id.tv_show)
    TextView tvShow;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);

        // 1. 线程切换
//        rxjavaThread();

        // 2. 与Retrofit的结合使用
        getRetrofitData();

    }

    // Retrofit 结合使用
    private void getRetrofitData() {
        // Disposable 的容器，退出时调用clear() 方法用于取消数据的接收和数据处理
        compositeDisposable.add(
                RetrofitClient.getGankApi().getGankData1(10, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GankModel>() {
                            @Override
                            public void accept(GankModel gankModel) throws Exception {
                                Toast.makeText(Main1Activity.this, "数据：" + gankModel.getResults().size(), Toast.LENGTH_SHORT).show();

                                // 更新UI，比如请求数据的展示等
                                tvShow.setText("请求的数据：" + gankModel.getResults().size());
                                /**
                                 * 如果请求缓慢，正在请求时退出页面，更新UI会报错，需要在页面退出的时候取消请求，Retrofit可以通过Call来取消，使用RxJava之后，可以通过取消事件的接收来完成。
                                 * 取消事件的接收可以通过Disposable的dispose()方法，如果页面中有多个请求（多个Disposable）
                                 * RxJava提供一个容器：CompositeDisposable，可以在进行请求时CompositeDisposable.add()添加到容器中，退出时CompositeDisposable.clear()
                                 */

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(Main1Activity.this, "error:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消所有的事件接收
        compositeDisposable.clear();
    }


    // 了解线程的切换
    private void rxjavaThread() {
        // 发送事件
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.i("TAG", "subscribe-thread:" + Thread.currentThread());
                e.onNext(1);
            }
        });

        // 接收事件
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

                // 运行的线程与observeOn设定的线程有关

                Log.i("TAG", "subscribe-thread:" + Thread.currentThread());
                Toast.makeText(Main1Activity.this, "integer:" + integer, Toast.LENGTH_SHORT).show();
            }
        };

        /**
         * 线程：
         * Schedulers.newThread()
         * Schedulers.io()
         * AndroidSchedulers.mainThread()

         Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
         Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
         Schedulers.newThread() 代表一个常规的新线程
         AndroidSchedulers.mainThread() 代表Android的主线程

         *
         * 线程可以多次指定
         * 1. 事件发送的线程subscribeOn多次指定，仅第一次指定的有效
         * 2. 事件接收线程observeOn多次指定，会一直切换直到最后一次指定的线程
         *
         */
        // 建立连接
        observable
                // 切换线程:最常用的如下：事件处理和发送在后台线程，接收事件及数据处理在主线程
                .subscribeOn(Schedulers.newThread())// 订阅事件线程
                .subscribeOn(Schedulers.io())// 订阅事件线程
                .observeOn(Schedulers.io())// 接收事件线程
                .observeOn(AndroidSchedulers.mainThread())// 接收事件线程
                // 未切换线程
                .subscribe(consumer)
        ;
    }
}
