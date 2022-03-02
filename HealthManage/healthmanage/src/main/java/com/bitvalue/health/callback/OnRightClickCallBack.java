package com.bitvalue.health.callback;

import com.bitvalue.health.api.responsebean.TaskDeatailBean;

/**
 * @author created by bitvalue
 * @data : 02/21
 */
public interface OnRightClickCallBack {

    void OnItemClick(TaskDeatailBean taskDeatailBean);

    void OnIemVideoVisitClick(TaskDeatailBean taskDeatailBean);

    void OnItemClick(int position);
}
