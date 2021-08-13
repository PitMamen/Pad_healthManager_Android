package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.File;

/**
 *    desc   : 上传图片
 */
public final class UpdateImageApi implements IRequestApi {

    public String fileLinkUrl;
    public String id;

    @Override
    public String getApi() {
        return Constants.API_CONTENT + "/fileUpload/uploadImgFile";
    }

    /** 图片文件 */
    public File file;

    public UpdateImageApi setImage(File file) {
        this.file = file;
        return this;
    }
}