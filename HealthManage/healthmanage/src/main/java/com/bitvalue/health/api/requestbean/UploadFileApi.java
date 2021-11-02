package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.File;

public class UploadFileApi implements IRequestApi {
    public String path;//本地文件的本地地址
    public int duration;//本地文件的长度

    public String fileLinkUrl;//上传后的文件地址
    public String id;//上传后的文件id

    //接口的上传属性
    public File file;
    @Override
    public String getApi() {
        return Constants.API_CONTENT +  "/fileUpload/uploadOtherFile";
    }
}
