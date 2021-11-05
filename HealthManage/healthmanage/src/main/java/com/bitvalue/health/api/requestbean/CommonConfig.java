package com.bitvalue.health.api.requestbean;
import java.util.HashMap;
import java.util.Map;


/**
 * 用于存储数据交互接口信息
 */

public class CommonConfig {

    /******************************App后台环境配置******************************/
    private final static int SERVER_ENV_PRODUCTION = 1; // 生产环境
    private final static int SERVER_ENV_TEST       = 2; // 测试环境
    private final static int SERVER_ENV_DEV        = 3; // 开发环境

    public static int CURRENT_SERVER_EVN = SERVER_ENV_PRODUCTION; // 默认为测试环境
    /******************************App后台环境配置*******************************/

    /*******************************ShareSDK Config******************************/
    // 使用 shardSDK developer@bitvalue.com.cn 账号
    public final static String SHARESDK_APP_KEY = "20d453f7e426a";
    public final static String SHARESDK_APP_SECRET = "b1868ea1a7fa7c29f7805ae9c90d77b4";
    /** 是否在读取通讯录时提醒用户 */
    public final static boolean IS_SHOW_IN_DIALOG = true;

    /*******************************ShareSDK Config******************************/

    /***********************************接口说明***********************************/
    /**
     * 请求格式
     *
     * 公共头统一修改，参数统一为method与data，method为方法名，data公有云创建2个应用：
     * 1、健康湖南 2、科研分析，把ID发给我
     * @党章 为请求参数的JSON字符串
     */
    /** IP:PORT*/
    public static String INTERFACE_BASE = "todo, 暂时用不到了";
    /** 文件上传 */
    public static String INTERFACE_UPLOAD = INTERFACE_BASE + "/upload";
    public static String INTERFACE_USER_HEAD = INTERFACE_BASE + "/head";

   private static String INTERFACE_BASE_LOGIN_PRODUCTION = "http://www.mclouds.org.cn/terminal/"; //正式环境
  //    private static String INTERFACE_BASE_LOGIN_PRODUCTION = "http://192.168.1.122:8083/";//测试环境地址 长沙


    // 南京测试环境-内网-登录接口 8082
    private static String INTERFACE_BASE_LOGIN_TEST = "http://192.168.88.104:8082";
    // 南京开发环境-内网-登录接口 8082
    private static String INTERFACE_BASE_LOGIN_DEV = "http://172.171.16.213:8082";

    public static void updateInterface() { INTERFACE_UPLOAD = INTERFACE_BASE + "upload"; }

    // 移动机房环境-临床辅助诊疗(智能问诊、推理)
    public static final String DISEASE_BASE_URL = "https://www.mclouds.org.cn:30080/reasoning/inference/";
    // 湘雅医院环境-临床辅助诊疗
    //public static final String DISEASE_BASE_URL = "http://222.247.54.203:1066/reasoning/inference/";

    // 移动机房环境-诊疗指南(知识库、疾病知识、典型病例、药品库等)
    public static final String KNOWLEDGE_BASE_URL = "https://www.mclouds.org.cn:30080/KNOWLEDGE/knowledge/";
    // 湘雅医院环境-诊疗指南(知识库、疾病知识、典型病例、药品库等)
    //public static final String KNOWLEDGE_BASE_URL = "http://222.247.54.203:1066/KNOWLEDGE/knowledge/";

    //我的病案
    public static String MY_MEDICAL_BASE_URL = "http://www.mclouds.org.cn/terminal/terminal/";//生产线
   // public static String MY_MEDICAL_BASE_URL = "http://192.168.1.122:8083/terminal/";//长沙测试环境

    //    1:医院；2:企业；3:医生；4:个人;
    public static final int USER_TYPE_HOSPITAL = 1;
    public static final int USER_TYPE_COMPANY = 2;
    public static final int USER_TYPE_DOCTOR = 3;
    public static final int USER_TYPE_PERSONAL = 4;

    // 获取登录接口baseUrl
    public static String GetLoginBaseUrl() {
        String loginBaseUrl = null;
        switch (CURRENT_SERVER_EVN) {
            case SERVER_ENV_PRODUCTION:
                loginBaseUrl = INTERFACE_BASE_LOGIN_PRODUCTION;
                break;
            case SERVER_ENV_TEST:
                loginBaseUrl = INTERFACE_BASE_LOGIN_TEST;
                break;
            case SERVER_ENV_DEV:
                loginBaseUrl = INTERFACE_BASE_LOGIN_DEV;
                break;
        }

        return loginBaseUrl;
    }

    /**
     * 获取自定义的版本号后缀
     * 根据后台服务器环境，在版本后后面追加不同的字符，方便测试查看
     */
    public static String GetVersionSuffix() {
        String suffix = "";
        switch (CURRENT_SERVER_EVN) {
            case SERVER_ENV_PRODUCTION:
                suffix = "P";
                break;
            case SERVER_ENV_TEST:
                suffix = "T";
                break;
            case SERVER_ENV_DEV:
                suffix = "D";
                break;
        }

        return suffix;
    }

    // 分页，默认每页条数
    public  static final int DEFAULT_ITEM_NUMS_A_PAGE = 10;

    //分页，第一页
    public  static final int DEFAULT_ITEM_PAGE_FIRST = 1;
    //全科相关功能成功返回码
    public  static final int KNOWLEDGE_SUCCESS_CODE = 1;

    //公有云相关成功返回码
    public static final int PUBLIC_CLOUD_SUCESS_RETUREN_CODE = 0;

    //发送验证码的类型---手机
    public static final int VERIFICATION_CODE_TYPE_MOBILE = 1;

    //发送验证码的类型---邮箱
    public static final int VERIFICATION_CODE_TYPE_EMAIL = 2;


    //触发发送验证码的动作---通用
    public static final int VERIFICATION_CODE_ACTION_TYPE_COMMON = 1;

    //触发发送验证码的动作---注册
    public static final int VERIFICATION_CODE_ACTION_TYPE_REGISTER = 2;



    //全科功能跳转详情页 需要查询的关键词（疾病、病历等名称）
    public  static final String KNOWLEDGE_DETAIL_KEYWORD = "KNOWLEDGE_DETAIL_KEYWORD";
    /*错误码中文字串映射*/
    public final static Map<Integer, String> ERROR_MSG_MAP = new HashMap<Integer, String>(){{
        put(0,"执行成功");
        put(1,"内部错误");
        put(2,"数据库操作错误");
        put(3,"未知错误");
        put(4,"参数错误");
        put(5,"缓存操作错误");
        put(6,"用户未登录");
        put(100001,"用户名、密码不匹配");
        put(100002,"将Token写入Redis时错误");
        put(100004,"系统配置表加载失败");
        put(100005,"系统配置表中找不到CA管理中心服务地址的配置");
        put(100006,"系统配置表中找不到相似病历搜索服务地址的配置");
        put(100007,"系统配置表中找不到云终端心跳服务地址的配置");
        put(100008,"所属机构已经被删除");
        put(100009,"所属科室已经被删除");
        put(100010,"没有可用的VPN服务器");
        put(100012,"云终端已经被删除");
        put(100013,"云终端已经停用");
        put(100014,"终端IMEI号与服务器中的不一致");
        put(100015,"云终端已过有效期");
        put(110001,"Token不存在");
        put(110002,"将Token从Redis删除时发生错误");
        put(110003,"参数传递的Token为空字符串或null");
        put(120001,"终端IMEI号为空或null");
        put(120002,"终端在数据库中不存在");
        put(120003,"终端IMEI号与token不匹配");
        put(120004,"Token为空或null");
        put(210001,"用户没有查询权限");
        put(210002,"无可用服务");
    }};
    /***********************************日志说明***********************************/
    /**
     * 1、配置日志公共前缀
     * 2、配置日志级别
     * 3、打印日志开关（用于调试与发布切换）
     */
    /** 日志前缀 */
    public final static String LOG_CUSTOM_TAG_PREFIX = "";
    /** 是否保存日志到SD卡*/
    public final static boolean LOG_IS_SAVE_TO_SD = IsOpenLogInfo();
    /** LEVEL ERROR */
    public final static boolean LEVEL_ERROR = IsOpenLogInfo();
    /** LEVEL WARN */
    public final static boolean LEVEL_WARN = IsOpenLogInfo();
    /** LEVEL DEBUG */
    public final static boolean LEVEL_DEBUG = IsOpenLogInfo();
    /** LEVEL INFO */
    public final static boolean LEVEL_INFO = IsOpenLogInfo();
    /** LEVEL VERBOSE */
    public final static boolean LEVEL_VERBOSE = IsOpenLogInfo();
    //TODO 未知，知道的修正下吧！多谢！
    public final static boolean LEVEL_WTF = IsOpenLogInfo();

    // 根据配置的服务器环境来决定打印的日志级别
    public static boolean IsOpenLogInfo() {
        boolean isOpenLogInfo = true;
        switch (CURRENT_SERVER_EVN) {
            case SERVER_ENV_PRODUCTION:
//                isOpenLogInfo = false;//true
                break;
            case SERVER_ENV_TEST:
                isOpenLogInfo = true;
                break;
            case SERVER_ENV_DEV:
                isOpenLogInfo = true;
                break;
        }

        return isOpenLogInfo;
    }
    /***********************************日志说明***********************************/

    /****************************SharedPreference Key****************************/
    public final static int SP_DEFAULT_VALUE_INT = -100;
    public final static boolean SP_DEFAULT_VALUE_BOOLEAN = false;
    public final static String SP_KEY_BASE_DATA_VERSION = "SP_KEY_BASE_DATA_VERSION";
    public final static String SP_KEY_BASE_DATA = "SP_KEY_BASE_DATA";
    public final static String SP_KEY_CONTACT_VERSION = "SP_KEY_CONTACT_VERSION";
    public final static String SP_KEY_CONTACT = "SP_KEY_CONTACT";
    public final static String SP_KEY_MESSAGE_SOUND_REMINDER = "SP_KEY_MESSAGE_SOUND_REMINDER";
    public final static String SP_KEY_MESSAGE_VIBRATE_REMINDER = "SP_KEY_MESSAGE_VIBRATE_REMINDER";
    /****************************SharedPreference Key****************************/

    public final static String BUNDLE_KEY_IS_FIRST = "BUNDLE_KEY_IS_FIRST";
    /****************************Bundle Key****************************/
    public final static String BUNDLE_KEY_SIGN_IN_STATISTICS_DETAIL_REQUEST = "BUNDLE_KEY_SIGN_IN_STATISTICS_DETAIL_REQUEST";
    public final static String BUNDLE_KEY_PROBLEM_STATISTICS_DETAILED_LIST = "BUNDLE_KEY_PROBLEM_STATISTICS_DETAILED_LIST";
    public final static String BUNDLE_KEY_PROBLEM_STATISTICS_DETAILED_LIST_TITLE = "BUNDLE_KEY_PROBLEM_STATISTICS_DETAILED_LIST_TITLE";
    public final static String BUNDLE_KEY_WEB_VIEW_DETAIL_TITLE = "BUNDLE_KEY_WEB_VIEW_DETAIL_TITLE";
    public final static String BUNDLE_KEY_WEB_VIEW_DETAIL_URL = "BUNDLE_KEY_WEB_VIEW_DETAIL_URL";
    public final static String BUNDLE_KEY_LOCATION_NAME_KEY = "BUNDLE_KEY_LOCATION_NAME_KEY";
    public final static String BUNDLE_KEY_LOCATION_LATITUDE = "BUNDLE_KEY_LOCATION_LATITUDE";
    public final static String BUNDLE_KEY_LOCATION_LONGITUDE = "BUNDLE_KEY_LOCATION_LONGITUDE";
    public final static String BUNDLE_KEY_WORK_LOG_ID = "BUNDLE_KEY_WORK_LOG_ID";
    public final static String BUNDLE_KEY_WORK_LOG_MODEL = "BUNDLE_KEY_WORK_LOG_MODEL";
    public final static String BUNDLE_KEY_WORK_LOG_IS_MODIFY = "BUNDLE_KEY_WORK_LOG_IS_MODIFY";
    public final static String BUNDLE_KEY_WORK_LOG_OPERATE_TYPE = "BUNDLE_KEY_WORK_LOG_OPERATE_TYPE";
    public final static String BUNDLE_KEY_WORK_LOG_PIE_DETAIL = "BUNDLE_KEY_WORK_LOG_PIE_DETAIL";
    public final static String BUNDLE_KEY_WORK_LOG_All_RANKING = "BUNDLE_KEY_WORK_LOG_All_RANKING";
    public final static String BUNDLE_KEY_MODIFY_PASSWORD_ACCOUNT = "BUNDLE_KEY_MODIFY_PASSWORD_ACCOUNT";
    public final static String BUNDLE_KEY_MODIFY_PASSWORD_FROME = "BUNDLE_KEY_MODIFY_PASSWORD_FROME";
    public final static String BUNDLE_KEY_MODIFY_USER_INFO_TYPE = "BUNDLE_KEY_MODIFY_USER_INFO_TYPE";
    public final static String BUNDLE_KEY_DOWNLOAD_APK_NAME = "BUNDLE_KEY_DOWNLOAD_APK_NAME";
    public final static String BUNDLE_KEY_DOWNLOAD_APK_URL = "BUNDLE_KEY_DOWNLOAD_APK_URL";
    /****************************Bundle Key****************************/

    public final static String APPLICATION_NAME = "cityman";

    /**
     * 工作日志：新增、详情与修改标识
     */
    public final static int WORK_LOG_DEFAULT = -1;
    public final static int WORK_LOG_NEW = 0;
    public final static int WORK_LOG_DETAIL = 1;
    public final static int WORK_LOG_MODIFY = 2;

    /*
     * 问题步骤(问题状态)
     */
    public final static String PROBLEM_STATUS_CM_DISTRICT_PROCESS = "CM_DISTRICT_PROCESS";//区城管局和区环保局公用这一个状态，所有在显示的时候要分开处理
    public final static String PROBLEM_STATUS_CM_DISTRICT_PROCESS_TEXT = "区城管局处理";
    public final static String PROBLEM_STATUS_CM_DISTRICT_PROCESS_TEXT_EPA = "区环保局处理";
    public final static String PROBLEM_STATUS_CM_STREET_PROCESS = "CM_STREET_PROCESS";
    public final static String PROBLEM_STATUS_CM_STREET_PROCESS_TEXT = "街道处理";
    public final static String PROBLEM_STATUS_CM_COMMUNITY_PROCESS = "CM_COMMUNITY_PROCESS";
    public final static String PROBLEM_STATUS_CM_COMMUNITY_PROCESS_TEXT = "社区处理";
    public final static String PROBLEM_STATUS_CM_COMMUNITY_USER_PROCESS = "CM_COMMUNITY_USER_PROCESS";
    public final static String PROBLEM_STATUS_CM_COMMUNITY_USER_PROCESS_TEXT = "社区协管员处理";
    public final static String PROBLEM_STATUS_CM_ASSIST_DEPT_PROCESS = "CM_ASSIST_DEPT_PROCESS";
    public final static String PROBLEM_STATUS_CM_ASSIST_DEPT_PROCESS_TEXT = "协办单位处理";
    public final static String PROBLEM_STATUS_CM_ASSIST_DEPT_USER_PROCESS = "CM_ASSIST_DEPT_USER_PROCESS";
    public final static String PROBLEM_STATUS_CM_ASSIST_DEPT_USER_PROCESS_TEXT = "网格化人员处理";
    public final static String PROBLEM_STATUS_CM_DISTRICT_CONFIRM = "CM_DISTRICT_CONFIRM";//区城管局和区环保局公用这一个状态，所有在显示的时候要分开处理
    public final static String PROBLEM_STATUS_CM_DISTRICT_CONFIRM_TEXT = "区城管局核实中";
    public final static String PROBLEM_STATUS_CM_DISTRICT_CONFIRM_TEXT_EPA = "区环保局核实中";
    public final static String PROBLEM_STATUS_FINISHED = "FINISHED";
    public final static String PROBLEM_STATUS_FINISHED_TEXT = "已办结";
    public final static String PROBLEM_STATUS_NONE = "NONE";
    public final static String PROBLEM_STATUS_NONE_TEXT = "已撤卷";
    public final static String PROBLEM_STATUS_DISTRICT_DELAYING_APPLY = "project/districtQueryDelayingApply";
    public final static String PROBLEM_STATUS_DISTRICT_DELAYING_APPLY_TEXT = "待延期批复";

    public final static Map<String, String> PROBLEM_STATUS_ONE_LINE_4_CM = new HashMap<String, String>(){{
        put(PROBLEM_STATUS_CM_DISTRICT_PROCESS,PROBLEM_STATUS_CM_DISTRICT_PROCESS_TEXT);
        put(PROBLEM_STATUS_CM_STREET_PROCESS,PROBLEM_STATUS_CM_STREET_PROCESS_TEXT);
        put(PROBLEM_STATUS_CM_COMMUNITY_PROCESS,PROBLEM_STATUS_CM_COMMUNITY_PROCESS_TEXT);
        put(PROBLEM_STATUS_CM_COMMUNITY_USER_PROCESS,PROBLEM_STATUS_CM_COMMUNITY_USER_PROCESS_TEXT);
        put(PROBLEM_STATUS_CM_ASSIST_DEPT_PROCESS,PROBLEM_STATUS_CM_ASSIST_DEPT_PROCESS_TEXT);
        put(PROBLEM_STATUS_CM_ASSIST_DEPT_USER_PROCESS,PROBLEM_STATUS_CM_ASSIST_DEPT_USER_PROCESS_TEXT);
        put(PROBLEM_STATUS_CM_DISTRICT_CONFIRM,PROBLEM_STATUS_CM_DISTRICT_CONFIRM_TEXT);
        put(PROBLEM_STATUS_FINISHED,PROBLEM_STATUS_FINISHED_TEXT);
        put(PROBLEM_STATUS_NONE,PROBLEM_STATUS_NONE_TEXT);
    }};

    public final static Map<String, String> PROBLEM_STATUS_ONE_LINE_4_EPA  = new HashMap<String, String>(){{
        putAll(PROBLEM_STATUS_ONE_LINE_4_CM);
        put(PROBLEM_STATUS_CM_DISTRICT_PROCESS,PROBLEM_STATUS_CM_DISTRICT_PROCESS_TEXT_EPA);
        put(PROBLEM_STATUS_CM_DISTRICT_CONFIRM,PROBLEM_STATUS_CM_DISTRICT_CONFIRM_TEXT_EPA);
    }};

    // 病历类型
    public final static String MR_INDEX_ALL_ID = "all";
    public final static String MR_INDEX_ALL_TEXT = "所有";
    public final static String MR_INDEX_MENZHEN_ID = "menzhen";
    public final static String MR_INDEX_MENZHEN_TEXT = "门诊";
    public final static String MR_INDEX_ZHUYUAN_ID = "zhuyuan";
    public final static String MR_INDEX_ZHUYUAN_TEXT = "住院";

    public final static Map<String, String> MR_INDEX_MAP = new HashMap<String, String>(){{
        put(MR_INDEX_ALL_ID, MR_INDEX_ALL_TEXT);
        put(MR_INDEX_MENZHEN_ID, MR_INDEX_MENZHEN_TEXT);
        put(MR_INDEX_ZHUYUAN_ID, MR_INDEX_ZHUYUAN_TEXT);
    }};
}
