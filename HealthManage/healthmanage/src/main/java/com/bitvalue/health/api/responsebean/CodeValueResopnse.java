package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 05/11
 */
public class CodeValueResopnse implements Serializable {
    public String code;
    public String codeGroup;
    public String id;
    public String indexNum;
    public String parentCode;
    public String remark;
    public String value;

    @Override
    public String toString() {
        return "CodeValueResopnse{" +
                "code='" + code + '\'' +
                ", codeGroup='" + codeGroup + '\'' +
                ", id='" + id + '\'' +
                ", indexNum='" + indexNum + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", remark='" + remark + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
