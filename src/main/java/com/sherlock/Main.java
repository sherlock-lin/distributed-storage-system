package com.sherlock;

import com.sherlock.service.NetService;
import com.sherlock.service.NetServiceImpl;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
public class Main {
    public static void main(String[] args) {
        NetService netService = new NetServiceImpl();
        netService.start();
    }
}
