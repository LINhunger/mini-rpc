package com.hunger.service.impl;

import com.hunger.service.CalculateService;


/**
 * Created by 小排骨 on 2018/1/14.
 */
public class CalculateServiceImpl implements CalculateService {

    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int multi(int a, int b) {
        return a*b;
    }
}
