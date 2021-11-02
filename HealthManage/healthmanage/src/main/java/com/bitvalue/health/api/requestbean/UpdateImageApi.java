package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.File;

/**
 *    desc   : 上传图片
 */
public final class UpdateImageApi implements IRequestApi {

    public String fileLinkUrl;
    public String id;
    /** 图片文件 */
    public File file;

    public UpdateImageApi setImage(File file) {
        this.file = file;
        return this;
    }

    @Override
    public String getApi() {
        return Constants.API_CONTENT + "/fileUpload/uploadImgFile";
    }


}