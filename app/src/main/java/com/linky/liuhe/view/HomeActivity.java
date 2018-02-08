package com.linky.liuhe.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.linky.liuhe.R;
import com.linky.liuhe.bean.LiuheBean;
import com.linky.liuhe.bean.SxYearStatisBean;
import com.linky.liuhe.bean.YearTeStatisBean;
import com.linky.liuhe.presenter.HomePresenter;
import com.linky.liuhe.utils.DisplayUtils;
import com.linky.liuhe.utils.PreferenceUtils;
import com.linky.liuhe.view.callback.HomeView;
import com.linky.liuhe.view.widget.CustomerValueFormatter;
import com.linky.liuhe.view.widget.SxValueFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * @author Linky
 */
public class HomeActivity extends AppCompatActivity
        implements HomeView, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    public static final String SHARE_PREF_KEY = "YEAR";

    private LiuheAdapter mAdapter;
    private List<LiuheBean> mDatas = new ArrayList<>();
    private HomePresenter mPresenter;
    private Toolbar mToolbar;

    private int mYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mYear = getPreSaveYear();

        mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(String.valueOf(mYear) + "年号码统计情况");
        mToolbar.setTitleTextColor(Color.WHITE);
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView rvLiuHe = findViewById(R.id.rv_liuhe);
        rvLiuHe.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LiuheAdapter();
        rvLiuHe.setAdapter(mAdapter);

        mPresenter = new HomePresenter();
        mPresenter.attachView(this, this);

        mPresenter.loadLiuheData(String.valueOf(mYear));
    }

    private int getPreSaveYear() {
        return PreferenceUtils.getInt(PreferenceUtils.getSharePref(this), SHARE_PREF_KEY, 2017);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.liuhe_menu, menu);
        return true;
    }

    @Override
    public void onLiuheDataLoaded(List<LiuheBean> beans) {
        mAdapter.addAll(beans);

        // 每次加载显示后，持久化到本地
        PreferenceUtils.putInt(PreferenceUtils.getSharePref(this), SHARE_PREF_KEY, mYear);
    }

    @Override
    public void onStatisLoaded(List<YearTeStatisBean> beans) {
        mAdapter.setNumberDatas(beans);
    }

    @Override
    public void onSxStatisLoaded(List<SxYearStatisBean> beans) {
        mAdapter.setBarChartDatas(beans);
    }

    @Override
    public void onColorBallLoaded(Map<String, Integer> maps) {
        mAdapter.setPieChartDatas(maps);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        Bundle bundle = new Bundle();
        bundle.putInt(PickYearFragment.SELECTED, mYear);
        PickYearFragment fragment = PickYearFragment.newInstance(bundle);
        fragment.show(getSupportFragmentManager(), "year picker");
        fragment.setItemClickListener(year -> {

            try {
                mYear = Integer.parseInt(year);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            mToolbar.setTitle(year + "年号码统计情况");
            mPresenter.loadLiuheData(year);
            fragment.dismiss();

        });

        return false;
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    class LiuheAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        static final int SX = 0;
        static final int COLOR = 1;
        static final int NUMBER = 2;

        List<SxYearStatisBean> mBarChartDatas;
        Map<String, Integer> mPieChartDatas;
        List<YearTeStatisBean> mNumberDatas;

        void setNumberDatas(List<YearTeStatisBean> numberDatas) {
            this.mNumberDatas = numberDatas;
        }

        void setBarChartDatas(List<SxYearStatisBean> barChartDatas) {
            this.mBarChartDatas = barChartDatas;
        }

        void setPieChartDatas(Map<String, Integer> pieChartDatas) {
            this.mPieChartDatas = pieChartDatas;
        }

        void addAll(List<LiuheBean> beans) {
            mDatas.clear();
            mDatas.addAll(beans);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == SX) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bar_chart, parent, false);
                return new MyBarChartHolder(view);
            } else if (viewType == COLOR) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pie_chart, parent, false);
                return new MyPieChartHolder(view);
            } else if (viewType == NUMBER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_number_start, parent, false);
                return new MyNumberHolder(view);
            }

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liuhe, parent, false);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            int dip10 = DisplayUtils.dip2px(parent.getContext(), 10);
            params.setMargins(dip10, dip10, dip10, dip10);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            if (viewHolder instanceof MyBarChartHolder) {

                MyBarChartHolder holder = (MyBarChartHolder) viewHolder;
                if (mBarChartDatas != null) {
                    holder.barChart.setData(generateBarData(mBarChartDatas));
                }

            } else if (viewHolder instanceof MyPieChartHolder) {

                MyPieChartHolder holder = (MyPieChartHolder) viewHolder;
                if (mPieChartDatas != null) {
                    holder.pieChart.setData(generatePieData(mPieChartDatas));
                    holder.pieChart.highlightValues(null);
                    holder.pieChart.invalidate();
                }

            } else if (viewHolder instanceof MyNumberHolder) {

                MyNumberHolder holder = (MyNumberHolder) viewHolder;
                if (mNumberDatas != null) {
                    Collections.sort(mNumberDatas, (o1, o2) -> o1.seasons - o2.seasons);
                    holder.tv.setText(String.format("%s 年号码开奖次数统计：", mYear));
                    fillNumberStatisView(holder.ll1, holder.ll2, mNumberDatas);
                    mNumberDatas = null;
                }

            } else {

                position -= 3;
                MyViewHolder holder = (MyViewHolder) viewHolder;
                holder.mTvSeason.setText(String.format("期数：%s", mDatas.get(position).season));
                holder.mTvDate.setText(String.format("时间：%s", mDatas.get(position).getOpenTime()));

                holder.mTvP1.setText(String.valueOf(mDatas.get(position).p1));
                holder.mTvZodiacP1.setText(String.valueOf(mDatas.get(position).zodiac1));

                int color = LiuheBean.getBallColor(mDatas.get(position).p1);
                holder.mTvP1.setBackgroundResource(color);
                if (color == R.drawable.blue_cicle) {
                    holder.mTvP1.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                }

                holder.mTvP2.setText(String.valueOf(mDatas.get(position).p2));
                holder.mTvZodiacP2.setText(String.valueOf(mDatas.get(position).zodiac2));
                color = LiuheBean.getBallColor(mDatas.get(position).p2);
                holder.mTvP2.setBackgroundResource(color);
                if (color == R.drawable.blue_cicle) {
                    holder.mTvP2.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                }

                holder.mTvP3.setText(String.valueOf(mDatas.get(position).p3));
                holder.mTvZodiacP3.setText(String.valueOf(mDatas.get(position).zodiac3));
                color = LiuheBean.getBallColor(mDatas.get(position).p3);
                holder.mTvP3.setBackgroundResource(color);
                if (color == R.drawable.blue_cicle) {
                    holder.mTvP3.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                }

                holder.mTvP4.setText(String.valueOf(mDatas.get(position).p4));
                holder.mTvZodiacP4.setText(String.valueOf(mDatas.get(position).zodiac4));
                color = LiuheBean.getBallColor(mDatas.get(position).p4);
                holder.mTvP4.setBackgroundResource(color);
                if (color == R.drawable.blue_cicle) {
                    holder.mTvP4.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                }

                holder.mTvP5.setText(String.valueOf(mDatas.get(position).p5));
                holder.mTvZodiacP5.setText(String.valueOf(mDatas.get(position).zodiac5));
                color = LiuheBean.getBallColor(mDatas.get(position).p5);
                holder.mTvP5.setBackgroundResource(color);
                if (color == R.drawable.blue_cicle) {
                    holder.mTvP5.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                }

                holder.mTvP6.setText(String.valueOf(mDatas.get(position).p6));
                holder.mTvZodiacP6.setText(String.valueOf(mDatas.get(position).zodiac6));
                color = LiuheBean.getBallColor(mDatas.get(position).p6);
                holder.mTvP6.setBackgroundResource(color);
                if (color == R.drawable.blue_cicle) {
                    holder.mTvP6.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                }

                holder.mTvTe.setText(String.valueOf(mDatas.get(position).te));
                holder.mTvZodiacTe.setText(String.valueOf(mDatas.get(position).zodiacTe));
                color = LiuheBean.getBallColor(mDatas.get(position).te);
                holder.mTvTe.setBackgroundResource(color);
                if (color == R.drawable.blue_cicle) {
                    holder.mTvTe.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                }
            }
        }

        private void fillNumberStatisView(LinearLayout ll1, LinearLayout ll2, List<YearTeStatisBean> beans) {

            while (ll1.getChildCount() > 3) {
                ll1.removeView(ll1.getChildAt(ll1.getChildCount() - 1));
            }

            while (ll2.getChildCount() > 3) {
                ll2.removeView(ll2.getChildAt(ll2.getChildCount() - 1));
            }

            int size = beans.size();
            for (int i = 0; i < size; i++) {

                View itemView1 = LayoutInflater.from(ll1.getContext()).inflate(R.layout.item_table, ll1, false);
                TextView tvNumber1 = itemView1.findViewById(R.id.tv_number);
                TextView tvSeasons1 = itemView1.findViewById(R.id.tv_seasons);

                String te = String.valueOf(beans.get(i).te);
                String seasons = String.valueOf(beans.get(i).seasons);

                tvNumber1.setText(te);
                tvSeasons1.setText(seasons);

                ll1.addView(itemView1);

                if (i == size - 1) {
                    break;
                }

                View itemView2 = LayoutInflater.from(ll2.getContext()).inflate(R.layout.item_table, ll2, false);
                TextView tvNumber2 = itemView2.findViewById(R.id.tv_number);
                TextView tvSeasons2 = itemView2.findViewById(R.id.tv_seasons);

                ++i;
                String te2 = String.valueOf(beans.get(i).te);
                String seasons2 = String.valueOf(beans.get(i).seasons);

                tvNumber2.setText(te2);
                tvSeasons2.setText(seasons2);

                ll2.addView(itemView2);
            }
        }

        private PieData generatePieData(Map<String, Integer> map) {

            int red = map.get("red");
            int blue = map.get("blue");
            int green = map.get("green");

            ArrayList<PieEntry> entries = new ArrayList<>();

            if (red != 0) {
                entries.add(new PieEntry(red, "红波 " + red + "期"));
            }

            if (blue != 0) {
                entries.add(new PieEntry(blue, "蓝波 " + blue + "期"));
            }

            if (green != 0) {
                entries.add(new PieEntry(green, "绿波 " + green + "期"));
            }

            PieDataSet dataSet = new PieDataSet(entries, mYear +"年号码波色占比");

            dataSet.setDrawIcons(false);

            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(ColorTemplate.MATERIAL_COLORS[2]);
            colors.add(ColorTemplate.MATERIAL_COLORS[3]);
            colors.add(ColorTemplate.MATERIAL_COLORS[0]);

            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            return data;
        }

        private BarData generateBarData(List<SxYearStatisBean> beans) {

            float barWidth = 9f;
            float spaceForBar = 10f;
            ArrayList<BarEntry> yVals1 = new ArrayList<>();

            int size = beans.size();
            for (int i = 0; i < size; i++) {
                yVals1.add(new BarEntry(SxValueFormatter.getIndexBySx(beans.get(i).tesx) * spaceForBar, beans.get(i).seasons));
            }

            BarDataSet set1;
            set1 = new BarDataSet(yVals1, mYear + "年生肖开奖次数");

            set1.setDrawIcons(false);
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueFormatter(new CustomerValueFormatter());
            data.setBarWidth(barWidth);

            return data;
        }

        @Override
        public int getItemCount() {
            return mDatas.size() + 3;
        }

        class MyNumberHolder extends RecyclerView.ViewHolder {

            LinearLayout ll1;
            LinearLayout ll2;
            TextView tv;

            MyNumberHolder(View itemView) {
                super(itemView);
                ll1 = itemView.findViewById(R.id.ll_number_times_1);
                ll2 = itemView.findViewById(R.id.ll_number_times_2);
                tv = itemView.findViewById(R.id.tv_number_statis);
            }
        }

        class MyPieChartHolder extends RecyclerView.ViewHolder {

            PieChart pieChart;

            MyPieChartHolder(View itemView) {
                super(itemView);
                pieChart = itemView.findViewById(R.id.chart);
                pieChart.setUsePercentValues(true);
                pieChart.setCenterText("波色占比");
                pieChart.getDescription().setEnabled(false);
            }
        }

        class MyBarChartHolder extends RecyclerView.ViewHolder {

            BarChart barChart;

            MyBarChartHolder(View itemView) {
                super(itemView);
                barChart = itemView.findViewById(R.id.chart);
                barChart.setFitBars(true);
                barChart.getDescription().setEnabled(false);

                IAxisValueFormatter xAxisFormatter = new SxValueFormatter();
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(xAxisFormatter);
                xAxis.setLabelCount(12);
                xAxis.setDrawGridLines(false);

                YAxis rightAxis = barChart.getAxisRight();
                rightAxis.setAxisMinimum(0f);

                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setAxisMinimum(0f);
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_date)
            TextView mTvDate;

            @BindView(R.id.tv_season)
            TextView mTvSeason;

            @BindView(R.id.tv_p1)
            TextView mTvP1;

            @BindView(R.id.tv_p1_zodiac)
            TextView mTvZodiacP1;

            @BindView(R.id.tv_p2)
            TextView mTvP2;

            @BindView(R.id.tv_p2_zodiac)
            TextView mTvZodiacP2;

            @BindView(R.id.tv_p3)
            TextView mTvP3;

            @BindView(R.id.tv_p3_zodiac)
            TextView mTvZodiacP3;

            @BindView(R.id.tv_p4)
            TextView mTvP4;

            @BindView(R.id.tv_p4_zodiac)
            TextView mTvZodiacP4;

            @BindView(R.id.tv_p5)
            TextView mTvP5;

            @BindView(R.id.tv_p5_zodiac)
            TextView mTvZodiacP5;

            @BindView(R.id.tv_p6)
            TextView mTvP6;

            @BindView(R.id.tv_p6_zodiac)
            TextView mTvZodiacP6;

            @BindView(R.id.tv_te)
            TextView mTvTe;

            @BindView(R.id.tv_te_zodiac)
            TextView mTvZodiacTe;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class PickYearFragment extends DialogFragment {

        public static final String SELECTED = "selected";

        public static PickYearFragment newInstance(Bundle bundle) {
            PickYearFragment fragment = new PickYearFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        YearAdapter mYearAdapter;
        private Action1<String> mItemClickListener;

        public void setItemClickListener(Action1<String> listener) {
            this.mItemClickListener = listener;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            int selected = 1976;
            if (bundle != null) {
                selected = bundle.getInt(SELECTED);
            }

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.dialog_year_picker, container, false);
            rv.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            mYearAdapter = new YearAdapter(selected);
            mYearAdapter.setItemClickListener(mItemClickListener);
            rv.setAdapter(mYearAdapter);
            return rv;
        }

        class YearAdapter extends RecyclerView.Adapter<YearAdapter.YearHolder> {

            private int selected;
            private View mPreSelectedV;

            private Action1<String> mItemClickListener;

            public void setItemClickListener(Action1<String> listener) {
                this.mItemClickListener = listener;
            }

            YearAdapter(int selected) {
                this.selected = selected;
            }

            @Override
            public YearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_year, parent, false);
                return new YearHolder(view);
            }

            @Override
            public void onBindViewHolder(YearHolder holder, int position) {

                int curYear = 1976 + position;
                String year = String.valueOf(curYear);
                TextView tv = (TextView) holder.itemView;
                tv.setText(year);
                tv.setSelected(curYear == selected);
                tv.setTag(year);

                tv.setOnClickListener(v -> {

                    if (mPreSelectedV != v) {
                        mPreSelectedV.setSelected(false);
                        v.setSelected(true);
                        mPreSelectedV = v;

                        mItemClickListener.call((String) v.getTag());
                    }
                });

                if (curYear == selected) {
                    mPreSelectedV = tv;
                }
            }

            @Override
            public int getItemCount() {
                return 43;
            }

            class YearHolder extends RecyclerView.ViewHolder {

                YearHolder(View itemView) {
                    super(itemView);

                }
            }
        }
    }
}