package com.bitvalue.health.util.chatUtil;

import static com.bitvalue.health.util.Constants.FRAGEMNT_CONDITONOVERVIEW;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.MessageInfoData;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

/**
 * @author created by bitvalue
 * @data : 05/07
 */
public class CustomDoctorReceptionMessageController {
  public static void onDraw(ICustomMessageViewGroup parent, final CustomDoctorReceptionMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

    // 把自定义消息view添加到TUIKit内部的父容器里
    final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_acceptvisit, null, false);
    parent.addMessageContentView(view);

    // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
    TextView tv_title = view.findViewById(com.bitvalue.sdk.collab.R.id.tv_title);
    TextView tv_docName = view.findViewById(com.bitvalue.sdk.collab.R.id.tv_docname);
    TextView tv_time = view.findViewById(com.bitvalue.sdk.collab.R.id.tv_time);
    TextView tv_docdeptname = view.findViewById(com.bitvalue.sdk.collab.R.id.tv_docdeptname);
    final String text = TUIKitImpl.getAppContext().getString(com.bitvalue.sdk.collab.R.string.no_support_msg);
    if (data == null || EmptyUtil.isEmpty(data.content)) {
      return;
    }
    tv_title.setText(data == null ? text : "医生已接诊");
    tv_docName.setText(data.docName);
    tv_time.setText(data.time);
    tv_docdeptname.setText(data.docdeptName);

    view.setClickable(true);
    view.setOnClickListener(v -> {
    });
  }


}
