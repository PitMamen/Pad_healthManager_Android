package com.bitvalue.healthmanage.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.glide.GlideApp;
import com.bitvalue.healthmanage.http.response.ImageModel;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class NineImageAdapter extends BaseQuickAdapter<ImageModel, BaseViewHolder> {
    public NineImageAdapter(int layoutResId, @Nullable List<ImageModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageModel imageModel) {

        if (imageModel.localUrl.equals(Constants.IMG_ADD)) {
            helper.setImageDrawable(R.id.img_pic, mContext.getResources().getDrawable(R.drawable.icon_tjtp));
        } else {
            ImageView img_pic = helper.getView(R.id.img_pic);
//            GlideApp.with(mContext).load(imageModel.localUrl).into(img_pic);
            GlideApp.with(mContext)
                    .load(imageModel.localUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_pic);
        }


//        helper.setText(R.id.tv_question_item, answerListBean.getContent());
//        if (answerListBean.getIsCorrect().equals("1")) {
//            helper.setTextColor(R.id.tv_question_item, mContext.getResources().getColor(R.color.white));
//            helper.setBackgroundRes(R.id.tv_question_item, R.drawable.circle_question_id_bg_green);
//        } else if (answerListBean.isChoosed() && answerListBean.getIsCorrect().equals("0")) {
//            helper.setTextColor(R.id.tv_question_item, mContext.getResources().getColor(R.color.white));
//            helper.setBackgroundRes(R.id.tv_question_item, R.drawable.circle_question_id_bg_red);
//        } else {
//            helper.setTextColor(R.id.tv_question_item, mContext.getResources().getColor(R.color.text_content));
//            helper.setBackgroundRes(R.id.tv_question_item, R.drawable.circle_question_id_bg_white);
//        }
    }
}
