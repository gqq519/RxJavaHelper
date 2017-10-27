package com.gqq.rxjavahelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gqq.rxjavahelper.rxjava1.Main1Activity;
import com.gqq.rxjavahelper.rxjava2.Main2Activity;
import com.gqq.rxjavahelper.rxjava3.Main3Activity;
import com.gqq.rxjavahelper.rxjava4.Main4Activity;
import com.gqq.rxjavahelper.rxjava5.Main5Activity;
import com.gqq.rxjavahelper.rxjava6.Main6Activity;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Demo 依据 http://www.jianshu.com/u/c50b715ccaeb 系列教程
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.main_list)
    ListView listView;

    private Demo[] demos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        demos = createDemos();
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, demos));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(demos[position].intent);
    }

    private Demo[] createDemos() {
        return new Demo[]{
                new Demo("RxJava线程及结合retrofit", new Intent(this, Main1Activity.class)),
                new Demo("RxJava的Map操作", new Intent(this, Main2Activity.class)),
                new Demo("RxJava的Zip操作", new Intent(this, Main3Activity.class)),
                new Demo("RxJava的backPressure", new Intent(this, Main4Activity.class)),
                new Demo("RxJava的Flowable", new Intent(this, Main5Activity.class)),
                new Demo("RxJava的文件读取案例", new Intent(this, Main6Activity.class))
        };
    }

    static class Demo {
        String name;
        Intent intent;

        private Demo(String name, Intent intent) {
            this.name = name;
            this.intent = intent;
        }

        public String toString() {
            return name;
        }
    }

    // 基本的使用：事件发送与接收
    public void rxjava() {
        // Observable 被订阅者 发送事件、变化者
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                // 发送事件：可以一直发送
                e.onNext(1);
                e.onNext(2);
//                e.onNext(3);
//                e.onNext(4);
//                e.onComplete();
                e.onError(new Throwable("error"));
//                e.onError(new Throwable("error2"));// Error之后再发送error，崩溃
                e.onComplete();
                e.onNext(5);
                e.onNext(6);


            }
        });

        // 订阅者：接收处理事件
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
//                Log.i("TAG","onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {

//                Log.i("TAG","onNext:"+integer);
            }

            @Override
            public void onError(Throwable e) {
//                Log.i("TAG","onError:"+e.getMessage());
            }

            @Override
            public void onComplete() {
//                Log.i("TAG","onComplete");
            }
        };
        // 建立连接
        observable.subscribe(observer);
    }
}
