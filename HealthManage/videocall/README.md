本文档主要介绍如何快速集成视频会议SDK

## 目录结构

```
videocall
├─ Beauty       // 美颜面板，包含美颜，滤镜，动效等基本效果
├─ UVCCamera    // UVC（USB Video-Class）视频采集盒/摄像头相关底层驱动和处理逻辑
└─ Source       // 视频会议业务逻辑
```

## 依赖设置

1. 在项目根目录settings.gradle中增加：
```
include ':videocall:Source'
include ':videocall:Beauty'
include ':videocall:UVCCamera'

include(':videocall:UVCCamera:libuvccommon')
project(':videocall:UVCCamera:libuvccommon').projectDir = new File(rootProject.projectDir, "videocall/UVCCamera/libs/libuvccommon")

include(':videocall:UVCCamera:libuvccamera')
project(':videocall:UVCCamera:libuvccamera').projectDir = new File(rootProject.projectDir, "videocall/UVCCamera/libs/libuvccamera")
```

在调用方模块build.gradle中dependencies下添加：
```
implementation project(':videocall:Source')
```

*【注意】* 以上依赖设置已经配好。

## 调用方法

在需要启用视频的地方，调用如下方法进入视频会议Activity：
```
MeetingMainActivity.enterRoom(Context context,          // 当前Activity对象，或者getApplicationContext()
                              String roomId,            // 房间号，由于小程序采用纯数字形式，所以此处只能为纯数字
                              String userId,            // 用户Id，长度不超过64字节
                              String userName,          // 用户名称，长度不超过64字节
                              String userAvatar,        // 头像URL
                              int sdkAppId,             // TRTC SDK的AppID
                              userSig,                  // 用户鉴权信息签名，线上版本必须由Server生成
                              boolean openCamera,       // 是否默认打开摄像头
                              boolean openAudio,        // 是否默认打开麦克风
                              boolean enableUVCCamera); // 是否支持USB摄像头/视频采集盒
```

调用示例请见`CustomVideoCallMessageController.getDetail()`。

*【注意】* SDK所需要的SecretKey请不要写死在客户端代码中， 一旦上线很容易造成密码泄露，导致被第三方盗用TRTC视频会议账号， 因此`userSig`需要由Server端生成。

## 回调

视频会议结束后，会通过`EventBus`发送一个`MeetingEndEvent`对象作为回调，目前在`ChatFragment`下已通过如下方法监听并处理，如有需要请自行修改：
```
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MeetingEndEvent meetingEndEvent) {//不用区分类型，全部直接转换成json发送消息出去
        reportStatus("3");
    }
```
