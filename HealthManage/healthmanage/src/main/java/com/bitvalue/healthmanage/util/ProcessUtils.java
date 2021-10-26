package com.bitvalue.healthmanage.util;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.bitvalue.healthmanage.http.bean.ClientsResultBean;
import com.bitvalue.healthmanage.http.bean.LoginBean;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.widget.manager.SharedPreManager;
import com.hjq.http.EasyHttp;
import com.hjq.http.EasyUtils;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.tencent.imsdk.v2.V2TIMConversation;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ProcessUtils {


    public static int dataProcessing(ArrayList<ClientsResultBean> clientsProcessBeans, ArrayList<ClientsResultBean> sourceData, Context context, List<V2TIMConversation> v2TIMConversationList) {
        for (int i = 0; i < sourceData.size(); i++) {
            ClientsResultBean clientsResultBean = sourceData.get(i);
            List<ClientsResultBean.UserInfoDTO> userInfo = clientsResultBean.userInfo;
            //处理数据，去掉没有集合的套餐
            if (null == userInfo || userInfo.size() == 0) {
                continue;
            }

            LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, context);
            if (loginBean == null) {
                return 0;
            }
            //循环赋groupId，以及赋值未读消息数
            for (int x = 0; x < userInfo.size(); x++) {
                userInfo.get(x).groupID = userInfo.get(x).goodsId + loginBean.getUser().user.userId + userInfo.get(x).userId;
                userInfo.get(x).newMsgNum = DataUtil.getUnreadCount(false, userInfo.get(x).groupID, v2TIMConversationList);
                if (userInfo.get(x).newMsgNum > 0) {
                    userInfo.get(x).hasNew = true;
                }
            }
            //用新的集合接收空套餐的数据
            //组装完数据
            ClientsResultBean newOne = new ClientsResultBean(clientsResultBean.group, userInfo);
            newOne.userInfo = userInfo;
            newOne.num = clientsResultBean.num;
            newOne.group = clientsResultBean.group;
            clientsProcessBeans.add(newOne);

        }

        //获取每个套餐总数，以及所有套餐总数
        int totalNum = 0;
        for (int i = 0; i < clientsProcessBeans.size(); i++) {
            for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                //获取单个套餐的未读消息数
                clientsProcessBeans.get(i).newMsgNum = clientsProcessBeans.get(i).newMsgNum + clientsProcessBeans.get(i).userInfo.get(j).newMsgNum;
            }

            if (clientsProcessBeans.get(i).newMsgNum <= 0) {
                clientsProcessBeans.get(i).newMsgNum = 0;
            }

            //获取所有套餐的总未读消息数
            totalNum = totalNum + clientsProcessBeans.get(i).newMsgNum;
        }
        return totalNum;

    }


    public static int getTotalMessage(ArrayList<ClientsResultBean> clientsProcessBeans, String googsName, String userId) {
        int totalNum = 0;
        for (int i = 0; i < clientsProcessBeans.size(); i++) {
            for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                clientsProcessBeans.get(i).userInfo.get(j).isClicked = false;
                if (clientsProcessBeans.get(i).group.equals(googsName) && clientsProcessBeans.get(i).userInfo.get(j).userId == userId) {
                    clientsProcessBeans.get(i).userInfo.get(j).isClicked = true;

                    //找到点击的子项目，如果有新信息，置为已查阅;并把父级的消息数减去子消息数
                    if (clientsProcessBeans.get(i).userInfo.get(j).hasNew) {
                        clientsProcessBeans.get(i).userInfo.get(j).hasNew = false;
                        clientsProcessBeans.get(i).newMsgNum = clientsProcessBeans.get(i).newMsgNum - clientsProcessBeans.get(i).userInfo.get(j).newMsgNum;
                        if (clientsProcessBeans.get(i).newMsgNum <= 0) {
                            clientsProcessBeans.get(i).newMsgNum = 0;
                        }
                        clientsProcessBeans.get(i).userInfo.get(j).newMsgNum = 0;
                    }
                }
            }
            return totalNum + clientsProcessBeans.get(i).newMsgNum;
        }

        return 0;
    }





}
