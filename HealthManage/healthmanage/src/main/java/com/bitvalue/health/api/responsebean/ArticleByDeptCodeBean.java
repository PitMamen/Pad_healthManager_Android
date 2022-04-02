package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 04/01
 */
public class ArticleByDeptCodeBean implements Serializable {


    private List<ArticleBean> list;
    private int total;

    public List<ArticleBean> getList() {
        return list;
    }

    public void setList(List<ArticleBean> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
