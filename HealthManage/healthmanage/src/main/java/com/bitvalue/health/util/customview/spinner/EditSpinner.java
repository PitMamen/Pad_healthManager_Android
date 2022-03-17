package com.bitvalue.health.util.customview.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bitvalue.health.callback.ItemClickLisenterCallback;
import com.bitvalue.healthmanage.R;

import java.util.List;

/**
 * Created by WrBug on 2017/2/26 0026.
 */
public class EditSpinner extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private EditText editText;
    private ImageView mRightIv;
    private View mRightImageTopView;
    private Context mContext;
    private ListPopupWindow popupWindow;
    BaseEditSpinnerAdapter adapter;
    private long popupWindowHideTime;
    //    private Animation mAnimation;
//    private Animation mResetAnimation;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private ItemClickLisenterCallback callback;
    private int maxLine = 1;

    public EditSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(attrs);
        initAnimation();
    }


    public void setItemData(List<String> data) {
        adapter = new SimpleAdapter(mContext, data);
        setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemClickListener(ItemClickLisenterCallback callback) {
        this.callback = callback;
    }


    public void setText(String text) {
        editText.setText(text);
    }

    public void setTextColor(int color) {
        editText.setTextColor(color);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public void setRightImageDrawable(Drawable drawable) {
        mRightIv.setImageDrawable(drawable);
    }

    public void setRightImageResource(int res) {
        mRightIv.setImageResource(res);
    }

    public void setAdapter(BaseEditSpinnerAdapter adapter) {
        this.adapter = adapter;
        setBaseAdapter(this.adapter);
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
        editText.setMaxLines(this.maxLine);
    }

    private void initAnimation() {
//        mAnimation = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mAnimation.setDuration(300);
//        mAnimation.setFillAfter(true);
//        mResetAnimation = new RotateAnimation(-90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mResetAnimation.setDuration(300);
//        mResetAnimation.setFillAfter(true);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.edit_spinner, this);
        editText = (EditText) findViewById(R.id.edit_sipnner_edit);
        mRightIv = (ImageView) findViewById(R.id.edit_spinner_expand);
        mRightImageTopView = findViewById(R.id.edit_spinner_expand_above);
        mRightImageTopView.setOnClickListener(this);
        mRightImageTopView.setClickable(false);
        mRightIv.setOnClickListener(this);
//        mRightIv.setRotation(90);
        editText.addTextChangedListener(this);
        TypedArray tArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.EditSpinner);
        editText.setHint(tArray.getString(R.styleable.EditSpinner_hint));
        int imageId = tArray.getResourceId(R.styleable.EditSpinner_rightImage, 0);
        if (imageId != 0) {
            mRightIv.setImageResource(imageId);
        }
        int bg = tArray.getResourceId(R.styleable.EditSpinner_Background, 0);
        if (bg != 0) {
            editText.setBackgroundResource(bg);
        }
        maxLine = tArray.getInt(R.styleable.EditSpinner_maxLine, 1);
        editText.setMaxLines(maxLine);
        tArray.recycle();
    }

    private final void setBaseAdapter(BaseAdapter adapter) {
        if (popupWindow == null) {
            initPopupWindow();
        }
        popupWindow.setAdapter(adapter);
    }

    private void initPopupWindow() {
        popupWindow = new ListPopupWindow(mContext) {

            @Override
            public void show() {
                super.show();
                mRightImageTopView.setClickable(true);
//                mRightIv.startAnimation(mAnimation);
            }

            @Override
            public void dismiss() {
                super.dismiss();
            }

        };
        popupWindow.setOnItemClickListener(this);
        popupWindow.setDropDownGravity(Gravity.END);  //
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnchorView(editText);
        popupWindow.setOnDismissListener(() -> {
            popupWindowHideTime = System.currentTimeMillis();
//            mRightIv.startAnimation(mResetAnimation);
        });
    }


    @Override
    public final void onClick(View v) {
        togglePopupWindow();
    }

    private void togglePopupWindow() {
        if (System.currentTimeMillis() - popupWindowHideTime > 200) {
            if (adapter == null || popupWindow == null) {
                return;
            }
            showFilterData("");
        }
    }

    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e("TAG", "onItemClick11: " + ((BaseEditSpinnerAdapter) parent.getAdapter()).getItemString(position));
        String selectString = ((BaseEditSpinnerAdapter) parent.getAdapter()).getItemString(position);
        String departName = selectString;
        editText.setText(selectString.length() >= 9 ? selectString.substring(0, 8) : selectString);
        editText.setTextColor(getResources().getColor(R.color.text_desc_dark));
        mRightImageTopView.setClickable(false);
        popupWindow.dismiss();
//        if (mOnItemClickListener != null) {
//            mOnItemClickListener.onItemClick(parent, view, position, id);
//        }

        if (callback != null) {
            callback.ItemClick(departName);
        }
    }

    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public final void afterTextChanged(Editable s) {
        String key = s.toString();
        editText.setSelection(key.length());
        if (!TextUtils.isEmpty(key)) {
            if (key.contains("(") || key.contains(")") || key.contains("（") || key.contains("）")) {
                Log.e("TAG", "含括号！！！！！");
            } else {
                showFilterData(key);
            }
        } else {
            if (popupWindow != null)
                popupWindow.dismiss();
        }
    }

    private void showFilterData(String key) {
        if (popupWindow == null || adapter == null || adapter.getEditSpinnerFilter() == null) {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            return;
        }
        if (adapter.getEditSpinnerFilter().onFilter(key)) {
            popupWindow.dismiss();
        } else {
            popupWindow.show();
        }

    }


}
