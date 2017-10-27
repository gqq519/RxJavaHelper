package com.gqq.rxjavahelper.rxjava2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gqq.rxjavahelper.R;
import com.gqq.rxjavahelper.rxjava1.GankModel;
import com.gqq.rxjavahelper.rxjava1.RetrofitClient;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Map 操作符
//        mapAction();

        // flatMap操作符
        flatMapAction();
    }

    // flatMap 操作符
    private void flatMapAction() {
        RetrofitClient.getGankApi().getGankDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<GankDateModel, ObservableSource<GankModel>>() {
                    @Override
                    public ObservableSource<GankModel> apply(GankDateModel gankDateModel) throws Exception {
                        Toast.makeText(Main2Activity.this, "date:"+gankDateModel.getResults().get(0), Toast.LENGTH_SHORT).show();
                        String date = gankDateModel.getResults().get(0);
                        String[] strings = date.split("-");
                        return RetrofitClient.getGankApi().getGankData1(Integer.valueOf(strings[1]),Integer.valueOf(strings[2]))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<GankModel>() {
                    @Override
                    public void accept(GankModel gankModel) throws Exception {
                        Toast.makeText(Main2Activity.this, "data:"+gankModel.getResults().size(), Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(Main2Activity.this, "throwable:"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Map 操作符
    private void mapAction() {
        RetrofitClient.getGankApi().getGankData1(10,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<GankModel, Integer>() {
                    @Override
                    public Integer apply(GankModel gankModel) throws Exception {
                        return gankModel.getResults().size();
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Toast.makeText(Main2Activity.this, "转换后数据："+integer, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
