package com.linky.liuhe.view.callback;

import com.linky.liuhe.bean.LiuheBean;
import com.linky.liuhe.bean.SxYearStatisBean;
import com.linky.liuhe.bean.YearTeStatisBean;
import com.linky.liuhe.presenter.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author linky
 * @date 1/16/18
 */

public interface LiuheView extends BaseView {

    void onDataInitComplete();
    void onTotalSeasonsLoaded(Integer i);
    void onLatestNumberLoaded(LiuheBean bean);
    void onTotalTeStatisLoaded(List<YearTeStatisBean> beans);
    void onSxYearStatisLoaded(List<SxYearStatisBean> beans);
    void onColorBallStatisLoaded(Map<String, Integer> map);

}
