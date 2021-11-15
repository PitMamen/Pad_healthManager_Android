package com.bitvalue.health.ui.fragment.workbench;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.api.responsebean.VisitBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.DiagnosisTreatmentAdapter;
import com.bitvalue.health.ui.adapter.VisitAdapter;
import com.bitvalue.health.ui.adapter.WaitVisitListAdapter;
import com.bitvalue.health.ui.fragment.docfriend.DocFriendsFragment;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * @author created by bitvalue
 * @data : 11/10
 */
public class WorkBenchFragment extends BaseFragment implements OnItemClickCallback {

    @BindView(R.id.list_tobeseen)
    RecyclerView recyclerView_tobeseen;  //待就诊 list

    @BindView(R.id.list_zzjz)
    RecyclerView recyclerView_zzjzlist;  // 正在就诊 list

    @BindView(R.id.rl_wait_list)
    RecyclerView recyclerView_dsflblist;  //待随访就诊列表 list


    @BindView(R.id.lvc_main)
    LineChartView lineChartView;  //折现图控件


    @BindView(R.id.pcv_main)
    PieChartView mPieChartView;  //圆图控件

    @BindView(R.id.clccv_main)
    ComboLineColumnChartView mComboView;        //线性与柱状控件


    private ArrayList<VisitBean> beanArrayList = new ArrayList<>();


    private VisitAdapter visitAdapter; //待诊列表 adapter
    private DiagnosisTreatmentAdapter diagnosisTreatmentAdapter; //诊治中 adapter
    private WaitVisitListAdapter waitvisitAdapter; //待随访列表 adapter


    private HomeActivity homeActivity;


    //初始化当前Fragment的实例
    public static WorkBenchFragment getInstance(boolean is_need_toast) {
        WorkBenchFragment workbenchFragment = new WorkBenchFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        workbenchFragment.setArguments(bundle);
        return workbenchFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                beanArrayList.add(new VisitBean("株洲市人民医院", "2021/08/14", "14:20~15:30", "云门诊"));
            } else if (i % 3 == 0) {
                beanArrayList.add(new VisitBean("长沙市人民医院", "2021/08/15", "16:20~18:30", "院内门诊"));
            } else if (i % 4 == 0) {
                beanArrayList.add(new VisitBean("常德市人民医院", "2021/02/05", "12:20~11:30", "联合门诊"));
            } else if (i % 5 == 0) {
                beanArrayList.add(new VisitBean("湘潭市人民医院", "2021/04/13", "12:04~16:30", "远程门诊"));
            }
        }
        recyclerView_tobeseen.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView_tobeseen.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));


        recyclerView_zzjzlist.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView_zzjzlist.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));

        recyclerView_dsflblist.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView_dsflblist.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));


        visitAdapter = new VisitAdapter(R.layout.item_visit_patient, this, beanArrayList);
        waitvisitAdapter = new WaitVisitListAdapter(R.layout.item_visit_patient, beanArrayList);
        diagnosisTreatmentAdapter = new DiagnosisTreatmentAdapter(R.layout.item_visit_patient, this, beanArrayList);


        recyclerView_tobeseen.setAdapter(visitAdapter);
        recyclerView_zzjzlist.setAdapter(diagnosisTreatmentAdapter);
        recyclerView_dsflblist.setAdapter(waitvisitAdapter);


        /***
         * 以下为控件 初始化
         */
        /**
         * 禁用视图重新计算 主要用于图表在变化时动态更改，不是重新计算
         * 类似于ListView中数据变化时，只需notifyDataSetChanged()，而不用重新setAdapter()
         */
        lineChartView.setViewportCalculationEnabled(false);
    }


    @Override
    public void initData() {
        super.initData();
        setPointsValues();          //设置每条线的节点值
        setLinesDatas();            //设置每条线的一些属性
        resetViewport();            //计算并绘图


        setPieDatas(); //饼图设置视图

        setComboDatas();  //设置柱状图视图
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_workbench;
    }

    @Override
    public void onItemClick(VideoClientsResultBean item, int messageCount) {

    }


    /*=========== 其他相关 ==========*/
    private ValueShape pointsShape = ValueShape.CIRCLE; //点的形状(圆/方/菱形)
    private int numberOfLines = 1;                      //图上折线/曲线的显示条数
    private int maxNumberOfLines = 4;                   //图上折线/曲线的最多条数
    private int numberOfPoints = 12;                    //图上的节点数
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints]; //将线上的点放在一个数组中
    private boolean isPointsHasSelected = false;        //设置节点点击后效果(消失/显示标签)


    private boolean isHasAxes = true;                   //是否显示坐标轴
    private boolean isHasAxesNames = true;              //是否显示坐标轴名称
    private LineChartData mLineData;                    //图表数据
    String[] Axisdate = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};//X轴的标注
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();  //X轴坐标集合

    private List<AxisValue> getAxisValues() {
        for (int i = 0; i < Axisdate.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(Axisdate[i]));
        }
        return mAxisXValues;

    }


    /**
     * 折线图 利用随机数设置每条线对应节点的值
     */
    private void setPointsValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 500f;
            }
        }
    }

    /**
     * 折线图  设置线的相关数据
     */
    private void setLinesDatas() {
        List<Line> lines = new ArrayList<>();
        //循环将每条线都设置成对应的属性
        for (int i = 0; i < numberOfLines; ++i) {
            //节点的值
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            /*========== 设置线的一些属性 ==========*/
            Line line = new Line(values);               //根据值来创建一条线
            line.setColor(ChartUtils.COLORS[i]);        //设置线的颜色
            line.setShape(pointsShape);                 //设置点的形状
            line.setHasLines(true);               //设置是否显示线
            line.setHasPoints(true);             //设置是否显示节点
            line.setCubic(false);                     //设置线是否立体或其他效果
            line.setFilled(true);                   //设置是否填充线下方区域
            line.setHasLabels(false);       //设置是否显示节点标签
            //设置节点点击的效果
            line.setHasLabelsOnlyForSelected(isPointsHasSelected);
            //如果节点与线有不同颜色 则设置不同颜色
//            if (isPointsHaveDifferentColor) {
//                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
//            }
            lines.add(line);
        }

        mLineData = new LineChartData(lines);                      //将所有的线加入线数据类中
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);           //设置基准数(大概是数据范围)
        //如果显示坐标轴
        Axis axisX = new Axis();                    //X轴
        Axis axisY = new Axis().setHasLines(true);  //Y轴
        axisX.setName("月份");                //设置名称
        axisX.setTextSize(5);
        axisX.setValues(getAxisValues());   //设置X轴坐标
        axisX.setTextColor(Color.GRAY);             //X轴灰色
        axisY.setTextColor(Color.GRAY);             //Y轴灰色
        axisY.setName("看诊人数");
        axisY.setTextSize(5);
        mLineData.setAxisXBottom(axisX);            //设置X轴位置 下方
        mLineData.setAxisYLeft(axisY);              //设置Y轴位置 左边
        lineChartView.setZoomEnabled(true);  //设置可缩放
        lineChartView.setLineChartData(mLineData);    //设置图表控件
    }


    /**
     * 折现图  重点方法，计算绘制图表
     */
    private void resetViewport() {
        //创建一个图标视图 大小为控件的最大大小
        final Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.left = 0;                             //坐标原点在左下
        v.bottom = 0;
        v.top = 500;                            //最高点为1000
        v.right = numberOfPoints - 1;           //右边为点 坐标从0开始 点号从1 需要 -1
        lineChartView.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChartView.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }


    /*========= 数据相关 =========*/
    private PieChartData mPieChartData;                 //饼状图数据


    /***
     * 饼图
     */
    /**
     * 设置相关数据
     */
    private void setPieDatas() {
        int numValues = 6;                //把一张饼切成6块

        /*===== 随机设置每块的颜色和数据 =====*/
        List<SliceValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
            values.add(sliceValue);
        }

        /*===== 设置相关属性 类似Line Chart =====*/
        mPieChartData = new PieChartData(values);
        mPieChartView.setValueSelectionEnabled(false);
        mPieChartData.setHasLabels(true);
        mPieChartData.setHasLabelsOutside(true);
        mPieChartData.setHasCenterCircle(true);
        mPieChartView.setValueSelectionEnabled(false);

        mPieChartView.setPieChartData(mPieChartData);         //设置控件
    }


    /***
     * 柱形图相关设置
     */

    /**
     * 设置结合起来的数据
     */
    private ComboLineColumnChartData mComboData;        //联合图表数据

    private void setComboDatas() {
        //需要同时设置柱状数据和线性数据
        mComboData = new ComboLineColumnChartData(setColumnData(), setLineData());
        //坐标轴的相关设置 类似 Line Chart
        if (isHasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisX.setName("年龄");
            axisY.setName("数量");
            mComboData.setAxisXBottom(axisX);
            mComboData.setAxisYLeft(axisY);
        } else {
            mComboData.setAxisXBottom(null);
            mComboData.setAxisYLeft(null);
        }
        //设置数据
        mComboView.setComboLineColumnChartData(mComboData);
    }

    /**
     * 设置柱状图数据 类似 Column Chart 省略注释
     *
     * @return 柱状图数据
     */
    private ColumnChartData setColumnData() {
        int numSubcolumns = 1;
        int numColumns = 10;

        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;

        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 10 + 5, ChartUtils.COLOR_GREEN));
            }
            columns.add(new Column(values));
        }

        return new ColumnChartData(columns);
    }

    /**
     * 设置线性图数据 类似Line Chart 省略注释
     *
     * @return 线性图数据
     */
    private LineChartData setLineData() {
        List<Line> lines = new ArrayList<>();

        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setCubic(true);   //是否是曲线
            line.setHasLabels(false);   //是否有标签
            line.setHasLines(true);   //是否有线
            line.setHasPoints(false);  //是否有节点
            lines.add(line);
        }
        return new LineChartData(lines);
    }


}
