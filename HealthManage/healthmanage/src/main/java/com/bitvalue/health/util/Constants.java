package com.bitvalue.health.util;

public class Constants {

    //公司的  腾讯IM SDKAppID和秘钥
    public static final String ROOM_ID = "room_id";
    public static final String USER_ID = "user_id";
    public static final String IDCARD_NUMBER = "idcard_number";
    public static final String NEED_TOAST = "need_toast";
    public static final String HOST_URL = "http://manager.mclouds.org.cn:8008";    //http://192.168.1.122/
//    public static final String HOST_URL = "http://hmg.mclouds.org.cn";
    public static int screenWidth, screenHeight;
    public static final String KEY_REMEMBER_PSD = "key_remember_psd";
    public static final String KEY_TOKEN = "Authorization";
    public static final String KYE_USER_BEAN = "kye_user_bean";
    public static  String ROLE_TYPE = "casemanager"; //默认角色是个案管理师
    public static  String DEPT_CODE = "dept_code"; //科室代码
    public static final String KEY_PSD = "key_psd";
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

    public static final String API_ACCOUNT = "/account-api";
    public static final String API_HEALTH = "/health-api";
    public static final String API_CONTENT = "/content-api";
    public static final String API_TDUCK = "/questionnaire-api";
    public static final String API_ORDER = "/order-api";
    public static final String DATA_MSG = "data_msg";
    public static final String MSG_SINGLE = "msg_single";
    public static final String MSG_MULTI = "msg_multi";
    public static final String MSG_TYPE = "msg_type";
    public static final String MSG_IDS = "msg_ids";//要发送消息到患者的userId
    public static final String IMG_ADD = "img_add";
    public static final int MAX_IMG = 9;
    public static final String MSG_CUSTOM_ID = "msg_custom_id";
    public static final String PLAN_LIST_BEAN = "plan_list_bean";
    public static final String PLAN_LIST = "planList";
    public static final String GET_MISSION_OBJ = "get_mission_obj";
    public static String VIDEO_FOR_MSG = "video_for_msg";
    public static final String PLAN_MSG = "plan_msg";
    public static final String PLAN_PREVIEW = "plan_preview";

    public static final String ADD_VIDEO_DATA = "add_video_data";
    public static final String QUESTION_DETAIL = "question_detail";
    public static final String ARTICLE_DETAIL = "article_detail";
    public static final String PLAN_ID = "plan_id";
    public static final String RIGTH_TYPE = "ICUConsultNum";

    public static final int GET_CONVERSATION_COUNT = 100;

    // 存储
    public static final String USERINFO = "userInfo";
    public static final String ACCOUNT = "account";
    public static final String PWD = "password";
    public static final String ROOM = "room";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String LOGOUT = "logout";
    public static final String ICON_URL = "icon_url";

    public static final String ACTION_ARTICLE = "goto_articledisplay";
    public static final String ACTION_ARTICLE_DETAIL = "goto_articleDetail";
    public static final String ARTICLE_LIST = "article_list";
    public static final String ARTICLEDETAIL = "articleDetail";
    public static final String ARTICLEBEAN = "articleBean";
    public static final String ARTICLETITLE = "articleTitle";
    public static final String SHOWBOTTOM_LAYOUT = "isShowBottomLayout";

    public static final String ACTION_QUESTION = "goto_questiondisplay";
    public static final String ACTION_QUESTION_DETAIL = "goto_questionDetail";
    public static final String QUESTIONDETAIL = "questionDetail";
    public static final String QUESTIONLIST = "question_list";
    public static final String QUESTIONBEAN = "bean";
    public static final String BED_RESERVATION = "bedreservation";
    public static final String BED_RESERVATION_VALUE = "isbedreservation_activity";

    public static final String CHAT_TYPE = "chat_type";
    public static final String SINGLECHAT = "singlechat";
    public static final String MASSCHAT = "msg_multi";
    public static final String USER_IDS = "userIDs";
    public static final String GOOD_IDS = "goodIDS";
    public static final String CHAT_INFO = "chatInfo";
    public static final String CALL_MODEL = "callModel";
    public static final String IS_OFFLINE_PUSH_JUMP = "is_offline_push";

    public static final String IM_PRIVACY_PROTECTION = "https://web.sdk.qcloud.com/document/Tencent-IM-Privacy-Protection-Guidelines.html";
    public static final String IM_USER_AGREEMENT = "https://web.sdk.qcloud.com/document/Tencent-IM-User-Agreement.html";
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


    public static final String HOSPITAL_CODE = "444885559";//线上环境

    /**
     * Eventbus 消息类型  TYPE = 2  云门诊   TYPE=1  健康管理
     */
    public static final int EVENT_MES_TYPE_CLOUDCLINC = 2;
    public static final int EVENTBUS_MES_HEALTHMANAGER = 1;
    public static final String DOC_ID = "docId";
    public static final String INDEX_NAME = "indexName";

    public static final int TARGET_WIDTH = 2160;  //预览图 最大宽度
    public static final int TARGET_HEIGHT = 1440; //预览图 最大高度

    public static final int PREVIEWTARGET_WIDTH = 1920;  //缩略图 最大宽度
    public static final int PREVIEWTARGET_HEIGHT = 1440;  //缩略图 最大高度


//    //自己的
//    public static final int IM_APPId =1400548652;
//    public static final String IM_PRIVATE_KEY ="e3f3f2db3fa1ebb73d1fa009f774c668e93577ce8f15492409582cd041b6e712";
}
