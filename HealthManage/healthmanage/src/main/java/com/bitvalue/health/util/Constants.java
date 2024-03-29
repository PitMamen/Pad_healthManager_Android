package com.bitvalue.health.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    //公司的  腾讯IM SDKAppID和秘钥
    public static final String USER_ID = "user_id";
    public static final String NEED_TOAST = "need_toast";
//        public static final String HOST_URL = "https://develop.mclouds.org.cn";    //http://192.168.1.122/  测试环境
//    public static final String HOST_URL = "http://develop.mclouds.org.cn:8009";    //http://192.168.1.122/  重构验证环境
//        public static final String HOST_URL = "http://hmg.mclouds.org.cn";       //正式环境
        public static final String HOST_URL = "https://ys.mclouds.org.cn";       //演示环境  https://ys.mclouds.org.cn/

    //电子病历 接口(由于加密处理,病历的两个接口 是单独接口)
    public static String HOST_URL_LIST = HOST_URL + "/ehr-api/ehr/v1/list";   //获取病历列表  测试
    public static String HOST_URL_LIST_RECORD = HOST_URL + "/ehr-api/ehr/v1/getRecord";   //获取病历详情 测试


    public static int screenWidth, screenHeight;
    public static final String KEY_REMEMBER_PSD = "key_remember_psd";
    public static final String KEY_TOKEN = "Authorization";
    public static final String KYE_USER_BEAN = "kye_user_bean";
    public static final String IM_IPPID = "im_ippid";
    public static final String USER_SIG = "userSig";
    public static final String IM_SECRETKEY = "secretkey";
    public static String DEPT_CODE = "dept_code"; //科室代码
    public static final String KEY_PSD = "key_psd";
    public static final String MESSAGEINFO = "messageinfo";
    public static final String KEY_ACCOUNT = "key_account";
    public static final String FRAGMENT_CHAT = "fragment_chat";
    public static final String FRAGMENT_RECORD_CHAT = "chat_record_fragment";
    public static final String KEY_IM_AUTO_LOGIN = "key_im_auto_login";
    public static final String FRAGMENT_HEALTH_PLAN = "fragment_health_plan";
    public static final String FRAGMENT_HEALTH_NEW = "fragment_health_new";
    public static final String FRAGMENT_HEALTH_MODIFY = "fragment_health_modify";
    public static final String FRAGMENT_ADD_PAPER = "fragment_add_paper";
    public static final String FRAGMENT_ADD_QUESTION = "fragment_add_question";
    public static final String FRAGMENT_SEND_MSG = "fragment_send_msg";
    public static final String FRAGMENT_SEND_MSG_DISPLAY = "fragment_send_msg_display";
    public static final String FRAGMENT_HEALTH_ANALYSE = "fragment_health_analyse";
    public static final String FRAGMENT_HEALTH_ANALYSE_DISPLAY = "fragment_health_analyse_display";
    public static final String FRAGMENT_HEALTH_PLAN_DETAIL = "fragment_health_plan_detail";
    public static final String FRAGMENT_ADD_VIDEO = "fragment_add_video";
    public static final String FRAGMENT_QUESTION_DETAIL = "fragment_question_detail";
    public static final String FRAGMENT_ARTICLE_DETAIL = "fragment_article_detail";
    public static final String FRAGMENT_USER_DATA = "fragment_user_data";
    public static final String FRAGMENT_PLAN_MSG = "fragment_plan_msg";
    public static final String FRAGMENT_HEALTH_PLAN_PREVIEW = "fragment_health_plan_preview";
    public static final String FRAGMENT_MORE_DATA = "fragment_more_data";
    public static final String FRAGMENT_WRITE_HEALTH = "fragment_write_health";
    public static final String FRAGMENT_HEALTH_HISTORY_PREVIEW = "fragment_health_history_preview";
    public static final String FRAGMENT_PERSONAL_DATA = "fragment_personal_data";
    public static final String FRAGMENT_CHAT_LOG = "fragment_chat_log";
    public static final String FRAGMENT_SEND_REMIND = "fragment_send_remind";
    public static final String FRAGMENT_QUICKREPLY = "fragment_quickreply";
    public static final String FRAGMENT_NEW_LYDISCHARGED_PATIENT = "fragment_newly_changepatient";
    public static final String FRAGMENT_PLAN_LIST = "fragment_plan_list";
    public static final String FRAGMENT_DETAIL = "patient_detail";
    public static final String FRAGMENT_SEND_MESSAGE = "send_message";
    public static final String FRAGMENT_INTERESTSUSER_APPLY = "interestsuser_apply";
    public static final String FRAGMENT_INTERESTSUSER_APPLY_BYDOC = "interestsuser_apply_by_doc";
    public static final String TASKDETAIL = "task_detail";
    public static final String DATA_REVIEW = "data_review";
    public static final String MORE_DATA = "more_data";
    public static final String FRAGEMNT_PHONE_CONSULTATION = "fragemnt_phone_consultation";
    public static final String FRAGEMNT_CONDITONOVERVIEW = "fragemnt_conditonoverview";
    public static final String ACOUNT_SERVICE = "servicer";
    public static final String ACOUNT_CASEMANAGER = "casemanager";
    public static final String ACOUNT_DOCTOR = "doctor";

    public static final String API_ACCOUNT = "/account-api";
    public static final String INFO_API = "/info-api";
    public static final String API_HEALTH = "/health-api";
    public static final String API_CONTENT = "/content-api";
    public static final String API_TDUCK = "/questionnaire-api";
    public static final String API_ORDER = "/order-api";
    public static final String DATA_MSG = "data_msg";
    public static final String MSG_SINGLE = "msg_single";
    public static final String MSG_TYPE = "msg_type";
    public static final String MSG_IDS = "msg_ids";//要发送消息到患者的userId
    public static final String IMG_ADD = "img_add";
    public static final int MAX_IMG = 9;
    public static final String MSG_CUSTOM_ID = "msg_custom_id";
    public static final String PLAN_LIST_BEAN = "plan_list_bean";
    public static String VIDEO_FOR_MSG = "video_for_msg";
    public static final String PLAN_MSG = "plan_msg";
    public static final String PLAN_PREVIEW = "plan_preview";

    public static final String ADD_VIDEO_DATA = "add_video_data";
    public static final String QUESTION_DETAIL = "question_detail";
    public static final String ARTICLE_DETAIL = "article_detail";
    public static final String PLAN_ID = "plan_id";
    public static final String RIGTH_TYPE = "ICUConsultNum";
    public static final String PATIENT_EXPECTTIME = "patient_expecttime";
    public static final String PATIENT_TRADEID = "patient_tradeIde";

    public static final int GET_CONVERSATION_COUNT = 100;

    // 存储
    public static final String USERINFO = "userInfo";

    public static final String ACTION_ARTICLE_DETAIL = "goto_articleDetail";
    public static final String ARTICLEDETAIL = "articleDetail";
    public static final String CHAT_INFO = "chatInfo";
    public static final String LOCAL_PUBLIC_KEY = "publicKey";
    public static final String LOCAL_PRIVATER_KEY = "privateKey";
    public static final String APK_LOCAL_PATH = "apk_local_path";
    public static final String APK_URL = "apk_url";
    public static final String ISCLICKUPDATE = "clicl_update";

    /**
     * 索引
     */
    public static final String INDEX = "index";
    /**
     * 数量
     */
    public static final String AMOUNT = "amount";

    /**
     * 文件
     */
    public static final String FILE = "file";

    /**
     * 图片
     */
    public static final String IMAGE = "picture";

    /**
     * 视频
     */
    public static final String VIDEO = "video";


    /**
     * Eventbus 消息类型  TYPE = 2  云门诊   TYPE=1  健康管理
     */
    public static final String DOC_ID = "docId";
    public static final String INDEX_NAME = "indexName";
    public static final String SERIALNUMBER = "serialNumber";  //流水记录
    public static final String HOSPITALCODE = "hospitalcode";  //流水记录

    public static final int TARGET_WIDTH = 2160;  //预览图 最大宽度
    public static final int TARGET_HEIGHT = 1440; //预览图 最大高度

    public static final int PREVIEWTARGET_WIDTH = 1920;  //缩略图 最大宽度
    public static final int PREVIEWTARGET_HEIGHT = 1440;  //缩略图 最大高度


    public static final int PREVIEWTARGET_WIDTH1 = 3456;  //缩略图 最大宽度
    public static final int PREVIEWTARGET_HEIGH1 = 4608;  //缩略图 最大高度


    public static final String QUERY_DEALTPE_OF_TEXTNUM = "USED_TEXTNUM";  //图文咨询的查询 条数
    public static final String QUERY_DEALTPE_OF_VIDEONUM = "USED_VIDEONUM";  //视频咨询的查询时长
    public static final String VIDEO_SURPLUS_TIME = "surplus_time";  //视频通话剩余时长


    /**
     * 日志 类型
     */
    public static final int LOG_ERROR = 1;
    public static final int LOG_LOG = 2;
    public static final int LOG_RECORD = 3;
    public static final int LOG_FAIL = 4;
    public static final int LOG_OPENRECOD = 5;
    public static final int LOG_FATAL = 6;
    public static final int LOG_ALLEVENT = 7;

}
