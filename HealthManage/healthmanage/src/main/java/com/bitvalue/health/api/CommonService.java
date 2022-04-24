package com.bitvalue.health.api;


import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.requestbean.DocListBean;
import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.requestbean.LoginReqBean;
import com.bitvalue.health.api.requestbean.PersonalDataBean;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.requestbean.ReportStatusBean;
import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.requestbean.RequestPlanPatientListBean;
import com.bitvalue.health.api.requestbean.ResetPasswordRequestBean;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.requestbean.SendUserRemind;
import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.requestbean.VideoPatientStatusBean;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.ArticleByDeptCodeBean;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.DepartmentResponeBean;
import com.bitvalue.health.api.responsebean.GoodListBean;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.InpatientBean;
import com.bitvalue.health.api.responsebean.LoginResBean;
import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PatientBaseInfoBean;
import com.bitvalue.health.api.responsebean.PatientResultBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.api.responsebean.AnswerResultBean;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.api.responsebean.SearchArticleResult;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/***
 *接口类
 */
public interface CommonService {


    /**
     * 登录接口
     *
     * @param loginReqBean
     * @return
     */
    @POST("/account-api/login")
    Observable<ApiResult<LoginResBean>> login(@Body LoginReqBean loginReqBean);


    /**
     * 获取病历
     */
    @POST("/health-api/medical/doctor/qryUserMedicalCase")
    Observable<ApiResult<ArrayList<SaveCaseApi>>> qryUserMedicalCase(@Body GetHistoryApi qryCaseHistory);


    /**
     * 保存病历
     */
    @POST("/health-api/medical/doctor/saveUserMedicalCase")
    Observable<ApiResult<SaveCaseApi>> commitCaseHistory(@Body SaveCaseApi CaseHistory);


    /**
     * 上传就诊状态  就诊状态（1：待就诊 2：就诊中 3：已完成）
     */
    @POST("health-api/medical/doctor/updateAttendanceStatus")
    Observable<ApiResult<String>> updateStatus(@Body ReportStatusBean reportStatusBean);


    /**
     * 根据预约id获取患者的一行预约信息
     *
     * @param
     * @return id  患者ID
     */
    @GET("health-api/health/patient/getPatientAppointmentById")
    Observable<ApiResult<VideoPatientStatusBean>> getVideoPatientStatus(@Query("id") String id);


    /**
     * 获取医生个人信息
     *
     * @return
     */
    @GET("/account-api/individualInfo/getDoctorInfo")
    Observable<ApiResult<PersonalDataBean>> getDocPersonalDetail();


    /***
     * 我的健康管理客户
     */
    @POST("health-api/health/doctor/qryMyPatients")
    Observable<ApiResult<ArrayList<ClientsResultBean>>> getMyPatients();


    /****
     * 查看任务详情
     */
    @GET("health-api/health/patient/queryHealthPlanContent")
    Observable<ApiResult<TaskPlanDetailBean>> queryHealthPlanContent(@Query("contentId") String contentid, @Query("planType") String planType, @Query("userId") String userid);


    /**
     * 健康计划详情查看（任务列表查看）
     *
     * @param planID
     * @return
     */
    @GET("health-api/health/patient/queryHealthPlan")
    Observable<ApiResult<PlanDetailResult>> queryhealtPlan(@Query("planId") String planID);

    /**
     * 健康计划详情查看（任务列表查看）
     *
     * @param planID
     * @return
     */
    @GET("health-api/health/patient/queryHealthPlan")
    Observable<ApiResult<PlanTaskDetail>> queryTaskDetail(@Query("planId") int planID, @Query("taskId") int taskId, @Query("userId") String userId);

    /**
     * 健康计划  任务按时间排序
     *
     * @param planID
     * @return
     */
    @GET("health-api/patient/queryHealthPlanTaskList")
    Observable<ApiResult<List<HealthPlanTaskListBean>>> queryHealthPlanTaskList(@Query("planId") String planID);


    /***
     * 按科室查询套餐接口
     */
    @GET("health-api/health/patient/queryGoodsList")
    Observable<ApiResult<List<GoodListBean>>> queryGoodList(@Query("departmentId") int departmentId, @Query("goodsType") String goodType);


    /***
     * 提交健康评估 接口
     * @param bodyRequest
     * @return
     */
    @POST("health-api/health/doctor/sendUserEevaluate")
    Observable<ApiResult<SaveAnalyseApi>> commitHealthAnaly(@Body SaveAnalyseApi bodyRequest);

    /***
     * 发送健康提醒消息
     * @param bodyRequest
     * @return
     */
    @POST("health-api/health/doctor/sendUserRemind")
    Observable<ApiResult> sendUserRemind(@Body SendUserRemind bodyRequest);

    /****
     * 查看任务详情
     */
    @GET("health-api/health/patient/queryHealthPlanContent")
    Observable<ApiResult<TaskDetailBean>> queryHealthPlanContentByUpload(@Query("contentId") String contentid, @Query("planType") String planType, @Query("userId") String userid);


    /**
     * 获取健康评估详情
     */
    @GET("health-api/health/doctor/getUserEevaluate")
    Observable<ApiResult<SaveAnalyseApi>> getUserEevaluate(@Query("id") int id);


    /***
     * 获取我的健康管理计划接口(套餐)
     */
    @POST("health-api/health/doctor/qryMyPlanTemplates")
    Observable<ApiResult<ArrayList<PlanListBean>>> getMyPlanTemplate();


    /***
     * 根据关键字查询调查问卷
     */
    @GET("health-api/health/doctor/qryQuestByKeyWord")
    Observable<ApiResult<QuestionResultBean>> qryQuestByKeyWord(@Query("pageSize") int pageSize, @Query("start") int start, @Query("keyWord") String keyWord);


    /***
     * 根据关键字查询就诊记录
     */
    @GET("health-api/medical/doctor/qryMyMedicalRecords")
    Observable<ApiResult<ArrayList<PatientResultBean>>> qryMyMedicalRecords(@Query("type") String type, @Query("keyWord") String keyword);


    /***
     * 添加文章中获取文章数据
     */
    @GET("health-api/health/patient/allArticlesPage")
    Observable<ApiResult<ArticleByDeptCodeBean>> getUsefullArticle(@Query("pageSize") int pageSize, @Query("start") int start, @Query("deptCode") String deptCode);

    /**
     * 根据关键字搜索文章
     */
    @GET("health-api/health/patient/articleByTitle")
    Observable<ApiResult<SearchArticleResult>> qryarticleByTitle(@Query("pageSize") int pageSize, @Query("start") int start, @Query("title") String title);


    /**
     * 退出登录接口
     */
    @GET("/account-api/logout")
    Observable<ApiResult<String>> logout();


    /**
     * 修改密码
     */
    @POST("/account-api/accountInfo/resetAccountInfo")
    Observable<ApiResult<String>> resetPassWord(@Body ResetPasswordRequestBean requestBean);


    /**
     * 所有所有患者（骨肿瘤接口   已弃用）
     *
     * @param leaveBean
     * @return
     */

    @POST("bone-api/inner/qryPatientList")
    Observable<ApiResult<NewLeaveBean>> qryPatientList(@Body RequestNewLeaveBean leaveBean);


    /**
     * 所有患者、未注册患者、未分配患者
     *
     * @param leaveBean
     * @return
     */

    @POST("health-api/patient/qryPatientListByReg")
    Observable<ApiResult<NewLeaveBean>> qryallAllocatedPatientList(@Body AllocatedPatientRequest leaveBean);


    /***
     * 查询所有已分配计划的患者列表
     * @param leaveBean
     * @return
     */
    @POST("health-api/patient/qryPlanPatientList")
    Observable<ApiResult<NewLeaveBean>> qryPlanPatientList(@Body RequestPlanPatientListBean leaveBean);




    /**
     * 查看患者上传资料
     *
     * @param bean
     * @return
     */
    @POST("health-api/health/doctor/qryUserLocalVisit")
    Observable<ApiResult<List<TaskDetailBean>>> qryUserLocalVisit(@Body UserLocalVisitBean bean);


    /**
     * 病区接口
     */
    @GET("/account-api/businessManagement/getInpatientAreaList")
    Observable<ApiResult<List<InpatientBean>>> getInpatientAreaList(@Query("departmentId") String departmentId);

    /**
     * 科室接口
     */
    @GET("/account-api/businessManagement/getDepartmentList")
    Observable<ApiResult<List<DepartmentResponeBean>>> getDepartmentList();


    /**
     * 所有新出院患者
     *
     * @param leaveBean
     * @return
     */

    @POST("bone-api/inner/qryPatientNewOutList")
    Observable<ApiResult<NewLeaveBean>> getAllNewLeaveHospitolPatients(@Body RequestNewLeaveBean leaveBean);


    /***
     * 权益使用记录
     */
    @GET("health-api/patient/queryRightsUserRecord")
    Observable<ApiResult<QueryRightsRecordBean>> queryRightsUserRecord(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("userId") String userId, @Query("rightsId") int rightsId);


    /**
     * 我的权益
     */
    @GET("health-api/patient/queryMyRights")
    Observable<ApiResult<List<MyRightBean>>> queryMyRights(@Query("userId") String userId, @Query("id") String id);

    /**
     * 个案管理师 完成申请处理
     */
    @POST("health-api/patient/finishMidRequest")
    Observable<ApiResult<FinshMidRequestBean>> finishMidRequest(@Body FinshMidRequestBean finshMidRequestBean);


    /***
     * 查询待办
     */
    @GET("health-api/health/patient/getUserTask")
    Observable<ApiResult<List<TaskDeatailBean>>> getUserTask(@Query("execFlag") int execFlag, @Query("taskType") int taskType, @Query("userId") String docUserId);


    /**
     * 医师结束问诊 扣除权益接口
     */
    @POST("health-api/patient/saveRightsUseRecord")
    Observable<ApiResult<SaveRightsUseBean>> saveRightsRecord(@Body SaveRightsUseBean saveRightsUseBean);


    /**
     * 根据科室ID查询 该科室下的所有医师
     */
    @GET("account-api/businessManagement/getDoctorList")
    Observable<ApiResult<List<DocListBean>>> getDoctorList(@Query("departmentId") String departmentId);

    /**
     * 根据患者 ID  查询更多和基本信息
     */
    @GET("account-api/userInfo/getBaseInfo")
    Observable<ApiResult<PatientBaseInfoBean>> getPatientBaseInfo(@Query("userId") int userId);

    /**
     * 查询审核记录
     */
    @GET("health-api/patient/qryRightsUserLog")
    Observable<ApiResult<List<DataReViewRecordResponse>>> getDataReviewRecord(@Query("tradeId") String tardeid,@Query("userId") String userId,@Query("dealType") String dealType);  //dealType String

    /**
     * 保存审核记录
     */
    @POST("health-api/patient/saveRightsUserLog")
    Observable<ApiResult<DataReViewRecordResponse>> saveDataReviewRecord(@Body DataReViewRecordResponse saveReViewRecordRequest);


}
