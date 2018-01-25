package com.linky.liuhe.presenter;

import android.content.Context;

import com.linky.liuhe.api.rxsubscriber.RxSubscriber;
import com.linky.liuhe.api.rxsubscriber.Transformer;
import com.linky.liuhe.bean.LiuheBean;
import com.linky.liuhe.utils.DataUtils;
import com.linky.liuhe.utils.ToastUtils;
import com.linky.liuhe.view.callback.HomeView;

import java.util.List;
import java.util.Map;

/**
 * @author linky
 * @date 1/24/18
 */

public class HomePresenter extends BasePresenter<HomeView> {

    public void loadLiuheData(String year) {

        DataUtils.getStatisByYear(mActivity, year)
                .flatMap(beans -> {
                    mBaseView.onStatisLoaded(beans);
                    return DataUtils.getSxStatisByYear(mActivity, year);
                })
                .flatMap(beans -> {
                    mBaseView.onSxStatisLoaded(beans);
                    return DataUtils.getColorBallByYear(mActivity, year);
                })
                .flatMap(map -> {
                    mBaseView.onColorBallLoaded(map);
                    return DataUtils.loadAllDataFromDB(mActivity, year);
                })
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxSubscriber<List<LiuheBean>>() {

                    @Override
                    public boolean showDialog() {
                        return true;
                    }

                    @Override
                    public String showMessage() {
                        return "正在查询...";
                    }

                    @Override
                    public void onErrorOccur(String msg) {
                        ToastUtils.showLong(msg);
                    }

                    @Override
                    public Context activity() {
                        return mActivity;
                    }

                    @Override
                    public void onNextResult(List<LiuheBean> beans) {
                        mBaseView.onLiuheDataLoaded(beans);
                    }
                });
    }
}
