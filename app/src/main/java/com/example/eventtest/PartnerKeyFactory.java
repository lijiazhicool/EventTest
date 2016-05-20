package com.example.eventtest;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by bjhl on 16/5/18.
 */
public class PartnerKeyFactory {
    private static final String[] hexDigits = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
        "b", "c", "d", "e", "f" };

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("room_id", "12345");
        map.put("name", "zhangsan");
        map.put("partnerId", "1");
        System.err.println(CreatePartnerKey(map, "6y/4QI1DSl9cRdoSw2BJYRzw/thwLrJ0DkgcXsuqs6vCvh00chN2oFc+dzHJscVYDRfjlMugaYm6F6MhQ5EwWQ=="));
    }

    public static String CreatePartnerKey(HashMap<String, String> map, String partner_key) {
        // 1.根据参数进行排序
        Map<String, String> resultMap = sortMapByKey(map);
        // 2.拼接
        StringBuilder signBuilder = new StringBuilder();
        Set<Map.Entry<String, String>> entrySet = resultMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            signBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        signBuilder.append("&partner_key=").append(partner_key);
        // 3.md5
        String sign = toMD5(signBuilder.toString());
        return sign;
    }

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    public static String toMD5(String origin) {
        try {
            MessageDigest ex = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(ex.digest(origin.getBytes()));
        } catch (Exception var2) {
            return null;
        }
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();

        for (int i = 0; i < b.length; ++i) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = 256 + b;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

}

class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
