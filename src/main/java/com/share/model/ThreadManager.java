package com.share.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ElNino on 15/7/23.
 */
public class ThreadManager{
    private static ExecutorService instanc = Executors.newFixedThreadPool(10);

    public static synchronized ExecutorService getInstanc(){
        if(instanc == null){
            return Executors.newFixedThreadPool(10);
        }
        return instanc;
    }
}