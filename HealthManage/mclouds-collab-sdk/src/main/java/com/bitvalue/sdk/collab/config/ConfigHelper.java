package com.bitvalue.sdk.collab.config;

import android.os.Environment;

import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.TUIKitImpl;

import java.io.File;

public class ConfigHelper {

    public ConfigHelper() {

    }

    public TUIKitConfigs getConfigs() {
        GeneralConfig config = new GeneralConfig();
        // 显示对方是否已读的view将会展示
        config.setShowRead(true);
//        config.setAppCacheDir(DemoApplication.instance().getFilesDir().getPath());
        config.setAppCacheDir(TUIKitImpl.getAppContext().getFilesDir().getPath());//TODO 这里改了一个context
        if (new File(Environment.getExternalStorageDirectory() + "/111222333").exists()) {//这个文件没有生成，则不是测试环境
            config.setTestEnv(true);
        }
        TUIKit.getConfigs().setGeneralConfig(config);
        TUIKit.getConfigs().setCustomFaceConfig(initCustomFaceConfig());
        return TUIKit.getConfigs();
    }

    private CustomFaceConfig initCustomFaceConfig() {
        //创建一个表情组对象
//        CustomFaceGroup faceConfigs = new CustomFaceGroup();
//        //设置表情组每页可显示的表情列数
//        faceConfigs.setPageColumnCount(5);
//        //设置表情组每页可显示的表情行数
//        faceConfigs.setPageRowCount(2);
//        //设置表情组号
//        faceConfigs.setFaceGroupId(1);
//        //设置表情组的主ICON
//        faceConfigs.setFaceIconPath("4349/xx07@2x.png");
//        //设置表情组的名称
//        faceConfigs.setFaceIconName("4350");
//        for (int i = 1; i <= 15; i++) {
//            //创建一个表情对象
//            CustomFaceConfig faceConfig = new CustomFaceConfig();
//            String index = "" + i;
//            if (i < 10)
//                index = "0" + i;
//            //设置表情所在Asset目录下的路径
//            faceConfig.setAssetPath("4349/xx" + index + "@2x.png");
//            //设置表情所名称
//            faceConfig.setFaceName("xx" + index + "@2x");
//            //设置表情宽度
//            faceConfig.setFaceWidth(240);
//            //设置表情高度
//            faceConfig.setFaceHeight(240);
//            faceConfigs.addCustomFace(faceConfig);
//        }
//        groupFaces.add(faceConfigs);

        return null;
    }

}
