package com.bitvalue.health.base.model;


import io.reactivex.Observable;

/**
 * @author pxk on 2021/10/25
 */
public interface IModel {
    void unSubscribe();

    void addSubscribe(Observable subscription);
}
