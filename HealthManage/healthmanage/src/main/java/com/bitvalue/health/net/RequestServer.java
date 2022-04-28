package com.bitvalue.health.net;

import com.bitvalue.health.AppConfig;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

public class RequestServer implements IRequestServer {

    @Override
    public String getHost() {
        return AppConfig.getHostUrl();
    }

    @Override
    public String getPath() {
//        return "api/";//TODO 这一截目前不需要
        return "";
    }

    @Override
    public BodyType getType() {
        // 以JSON的形式提交参数
        return BodyType.JSON;
    }
}