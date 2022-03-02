package com.bitvalue.health.pickdatetime.adapter.datetime;


import androidx.annotation.NonNull;

import com.bitvalue.health.pickdatetime.bean.DateParams;
import com.bitvalue.health.pickdatetime.bean.DatePick;

/**
 * Created by fhf11991 on 2017/8/29.
 */

public class MinuteAdapter extends DatePickAdapter {

    public MinuteAdapter(@NonNull DateParams dateParams, @NonNull DatePick datePick) {
        super(dateParams, datePick);
    }
    @Override
    public String getItem(int position) {
        int number = mData.get(position);
        String value = number < 10 ? ("0" + number) : "" + number;
        return value + "分";
    }

    @Override
    public int getCurrentIndex() {
        return mData.indexOf(mDatePick.minute);
    }

    @Override
    public void refreshValues() {
        setData(getArray(60));
    }
}
