package com.luoyurui.common.util;

import com.luoyurui.common.model.StandardSubmit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户cmpp发送短信，临时存储的位置
 */
public class CMPPSubmitRepoMapUtil {

    private static ConcurrentHashMap<String, StandardSubmit> map = new ConcurrentHashMap<>();

    public static void put(int sequence, StandardSubmit submit) {
        map.put(sequence + "", submit);
    }

    public static StandardSubmit get(int sequence) {
        return map.get(sequence + "");
    }

    public static StandardSubmit remove(int sequence) {
        return map.remove(sequence + "");
    }
}
