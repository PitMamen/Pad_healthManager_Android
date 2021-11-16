package com.bitvalue.health.ui.fragment.schedule;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.R;
import com.smile.calendar.manager.CalendarManager;
import com.smile.calendar.ui.adapter.ScheduleAdapter;
import com.smile.calendar.util.Event;
import com.smile.calendar.view.CollapseCalendarView;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data :
 */
public class ScheduleMonthFragment extends BaseFragment {

//     @BindView(R.id.recyclerView)
//     RecyclerView recyclerView;
     @BindView(R.id.calendar)
     CollapseCalendarView calendarView;  // 日历View

     private HomeActivity homeActivity;


    private LocalDate selectedDate;//当前选择的日期
    private boolean weekchanged = false;//是否切换了周号
    private CalendarManager mManager;
    private ScheduleAdapter scheduleAdapter;
    private HashMap<Integer, List<Event>> eventMap;


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        selectedDate = LocalDate.now();
//        recyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));
//        scheduleAdapter = new ScheduleAdapter(homeActivity);
//        recyclerView.setAdapter(scheduleAdapter);
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(60));
        calendarView.init(mManager);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_schedulemonth_layout;
    }
}
