package com.linky.liuhe.presenter;

import android.content.Context;

import com.linky.liuhe.api.rxsubscriber.RxSubscriber;
import com.linky.liuhe.api.rxsubscriber.Transformer;
import com.linky.liuhe.utils.DataUtils;
import com.linky.liuhe.utils.ToastUtils;
import com.linky.liuhe.view.callback.LiuheView;

import java.util.Map;

/**
 * @author linky
 * @date 1/16/18
 */

public class LiuheDataPresenter extends BasePresenter<LiuheView> {

    public void initDb() {
        DataUtils.initDb(mActivity)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxSubscriber<Boolean>() {

            @Override
            public boolean showDialog() {
                return true;
            }

            @Override
            public String showMessage() {
                return "正在获取数据...";
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
            public void onNextResult(Boolean success) {
                mBaseView.onDataInitComplete();
            }
        });
    }

    /**
     * 获取全部统计数据
     */
    public void getTotalStatis() {
        DataUtils.getlatestNumber(mActivity)
                .compose(Transformer.switchSchedulers())
                .flatMap(bean -> {
                    mBaseView.onLatestNumberLoaded(bean);
                    return DataUtils.getTotalSeasons(mActivity);
                })
                .compose(Transformer.switchSchedulers())
                .flatMap(i -> {
                    mBaseView.onTotalSeasonsLoaded(i);
                    return DataUtils.getStatisByYear(mActivity, "");
                })
                .compose(Transformer.switchSchedulers())
                .flatMap(o -> {
                    mBaseView.onTotalTeStatisLoaded(o);
                    return DataUtils.getSxStatisByYear(mActivity, "");
                })
                .compose(Transformer.switchSchedulers())
                .flatMap(o -> {
                    mBaseView.onSxYearStatisLoaded(o);
                    return DataUtils.getColorBallByYear(mActivity, "");
                })
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxSubscriber<Map<String, Integer>>() {
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
                    public void onNextResult(Map<String, Integer> map) {
                        mBaseView.onColorBallStatisLoaded(map);
                    }
                });
        }

}
