package com.linky.liuhe.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.linky.liuhe.R;
import com.linky.liuhe.bean.LiuheBean;
import com.linky.liuhe.bean.SxYearStatisBean;
import com.linky.liuhe.bean.YearTeStatisBean;
import com.linky.liuhe.presenter.LiuheDataPresenter;
import com.linky.liuhe.view.callback.LiuheView;
import com.linky.liuhe.view.widget.CustomerValueFormatter;
import com.linky.liuhe.view.widget.SxValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Linky
 */
public class MainActivity extends AppCompatActivity implements
        LiuheView, OnChartValueSelectedListener, View.OnClickListener {

    private BarChart mBarChart;
    private PieChart mPieChart;
    private LiuheDataPresenter mPresenter;

    @BindView(R.id.ll_number_times_1)
    LinearLayout mLlNumberTimes1;

    @BindView(R.id.ll_number_times_2)
    LinearLayout mLlNumberTimes2;

    @BindView(R.id.tv_total) TextView mTvTotal;

    @BindView(R.id.tv_date) TextView mTvDate;
    @BindView(R.id.tv_season) TextView mTvSeason;
    @BindView(R.id.tv_p1) TextView mTvP1;
    @BindView(R.id.tv_p2) TextView mTvP2;
    @BindView(R.id.tv_p3) TextView mTvP3;
    @BindView(R.id.tv_p4) TextView mTvP4;
    @BindView(R.id.tv_p5) TextView mTvP5;
    @BindView(R.id.tv_p6) TextView mTvP6;
    @BindView(R.id.tv_te) TextView mTvTe;

    @BindView(R.id.tv_p1_zodiac) TextView mTvZodiacP1;
    @BindView(R.id.tv_p2_zodiac) TextView mTvZodiacP2;
    @BindView(R.id.tv_p3_zodiac) TextView mTvZodiacP3;
    @BindView(R.id.tv_p4_zodiac) TextView mTvZodiacP4;
    @BindView(R.id.tv_p5_zodiac) TextView mTvZodiacP5;
    @BindView(R.id.tv_p6_zodiac) TextView mTvZodiacP6;
    @BindView(R.id.tv_te_zodiac) TextView mTvZodiacTe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        findViewById(R.id.include).setOnClickListener(this);

        mBarChart = findViewById(R.id.bar_chart);
        mBarChart.setFitBars(true);
        mBarChart.getDescription().setEnabled(false);

        IAxisValueFormatter xAxisFormatter = new SxValueFormatter();
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setLabelCount(12);
        xAxis.setDrawGridLines(false);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        mPieChart = findViewById(R.id.pie_chart);
        mPieChart.setUsePercentValues(true);
        mPieChart.setCenterText("波色占比");
        mPieChart.getDescription().setEnabled(false);

        mPresenter = new LiuheDataPresenter();
        mPresenter.attachView(this, this);
        mPresenter.initDb();
    }

    @Override
    public void onDataInitComplete() {
        mPresenter.getTotalStatis();
    }

    @Override
    public void onLatestNumberLoaded(LiuheBean bean) {
        showLatestNumber(bean);
    }

    @Override
    public void onTotalSeasonsLoaded(Integer integer) {
        mTvTotal.setText(String.format(Locale.getDefault(), "1976年至今，总共开了：%d 期", integer));
    }

    @Override
    public void onSxYearStatisLoaded(List<SxYearStatisBean> beans) {

        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < beans.size(); i++) {
            yVals1.add(new BarEntry(SxValueFormatter.getIndexBySx(beans.get(i).tesx) * spaceForBar, beans.get(i).seasons));
        }

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "2017");

        set1.setDrawIcons(false);
        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueFormatter(new CustomerValueFormatter());
        data.setBarWidth(barWidth);

        mBarChart.setData(data);
        mBarChart.invalidate();
    }

    @Override
    public void onColorBallStatisLoaded(Map<String, Integer> map) {

        int red = map.get("red");
        int blue = map.get("blue");
        int green = map.get("green");

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(red, "红波" + red + "期"));
        entries.add(new PieEntry(blue, "蓝波" + blue + "期"));
        entries.add(new PieEntry(green, "绿波" + green + "期"));

        PieDataSet dataSet = new PieDataSet(entries, "波色占比");

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
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);
        mPieChart.invalidate();
    }

    @Override
    public void onTotalTeStatisLoaded(List<YearTeStatisBean> beans) {

        Collections.sort(beans, (o1, o2) -> o2.seasons - o1.seasons);

        int size = beans.size();
        for (int i = 0; i < size; i++) {

            View itemView1 = LayoutInflater.from(this).inflate(R.layout.item_table, mLlNumberTimes1, false);
            TextView tvNumber1 = itemView1.findViewById(R.id.tv_number);
            TextView tvSeasons1 = itemView1.findViewById(R.id.tv_seasons);

            String te = String.valueOf(beans.get(i).te);
            String seasons = String.valueOf(beans.get(i).seasons);

            tvNumber1.setText(te);
            tvSeasons1.setText(seasons);

            mLlNumberTimes1.addView(itemView1);

            if (i == size - 1) {
                break;
            }

            View itemView2 = LayoutInflater.from(this).inflate(R.layout.item_table, mLlNumberTimes2, false);
            TextView tvNumber2 = itemView2.findViewById(R.id.tv_number);
            TextView tvSeasons2 = itemView2.findViewById(R.id.tv_seasons);

            ++i;
            String te2 = String.valueOf(beans.get(i).te);
            String seasons2 = String.valueOf(beans.get(i).seasons);

            tvNumber2.setText(te2);
            tvSeasons2.setText(seasons2);

            mLlNumberTimes2.addView(itemView2);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void showLatestNumber(LiuheBean bean) {

        mTvDate.setText(String.format("时间：%s", bean.open_time));
        mTvSeason.setText(String.format("期数：%s", bean.season));

        mTvP1.setText(String.valueOf(bean.p1));
        mTvZodiacP1.setText(String.valueOf(bean.zodiac1));

        int color = LiuheBean.getBallColor(bean.p1);
        mTvP1.setBackgroundResource(color);
        if (color == R.drawable.blue_cicle) {
            mTvP1.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }

        mTvP2.setText(String.valueOf(bean.p2));
        mTvZodiacP2.setText(String.valueOf(bean.zodiac2));
        color = LiuheBean.getBallColor(bean.p2);
        mTvP2.setBackgroundResource(color);
        if (color == R.drawable.blue_cicle) {
            mTvP2.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }

        mTvP3.setText(String.valueOf(bean.p3));
        mTvZodiacP3.setText(String.valueOf(bean.zodiac3));
        color = LiuheBean.getBallColor(bean.p3);
        mTvP3.setBackgroundResource(color);
        if (color == R.drawable.blue_cicle) {
            mTvP3.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }

        mTvP4.setText(String.valueOf(bean.p4));
        mTvZodiacP4.setText(String.valueOf(bean.zodiac4));
        color = LiuheBean.getBallColor(bean.p4);
        mTvP4.setBackgroundResource(color);
        if (color == R.drawable.blue_cicle) {
            mTvP4.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }

        mTvP5.setText(String.valueOf(bean.p5));
        mTvZodiacP5.setText(String.valueOf(bean.zodiac5));
        color = LiuheBean.getBallColor(bean.p5);
        mTvP5.setBackgroundResource(color);
        if (color == R.drawable.blue_cicle) {
            mTvP5.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }

        mTvP6.setText(String.valueOf(bean.p6));
        mTvZodiacP6.setText(String.valueOf(bean.zodiac6));
        color = LiuheBean.getBallColor(bean.p6);
        mTvP6.setBackgroundResource(color);
        if (color == R.drawable.blue_cicle) {
            mTvP6.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }

        mTvTe.setText(String.valueOf(bean.te));
        mTvZodiacTe.setText(String.valueOf(bean.zodiacTe));
        color = LiuheBean.getBallColor(bean.te);
        mTvTe.setBackgroundResource(color);
        if (color == R.drawable.blue_cicle) {
            mTvTe.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }
    }
}
