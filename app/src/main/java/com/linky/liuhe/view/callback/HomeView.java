package com.linky.liuhe.view.callback;

import com.linky.liuhe.bean.LiuheBean;
import com.linky.liuhe.bean.SxYearStatisBean;
import com.linky.liuhe.bean.YearTeStatisBean;
import com.linky.liuhe.presenter.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Linky
 *
 */

public interface HomeView extends BaseView {
    void onLiuheDataLoaded(List<LiuheBean> beans);
    void onStatisLoaded(List<YearTeStatisBean> beans);
    void onSxStatisLoaded(List<SxYearStatisBean> beans);
    void onColorBallLoaded(Map<String, Integer> maps);
}
