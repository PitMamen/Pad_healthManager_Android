package com.smile.calendar.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.bitvalue.healthmanage.R;


/**
 * Created by Blaz Solar on 24/05/14.
 */
public class DayView extends AppCompatTextView {

    private static final int[] STATE_CURRENT = { R.attr.state_current };

    private boolean mCurrent;

    public DayView(Context context) {
        super(context);
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCurrent(boolean current) {
        if (mCurrent != current) {
            mCurrent = current;
            refreshDrawableState();
        }
    }

    public boolean isCurrent() {
        return mCurrent;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] state = super.onCreateDrawableState(extraSpace + 1);

        if (mCurrent) {
            mergeDrawableStates(state, STATE_CURRENT);
        }

        return state;
    }
}
