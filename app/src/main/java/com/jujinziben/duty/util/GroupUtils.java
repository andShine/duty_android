package com.jujinziben.duty.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupUtils {

    // 将elem置换到当前位置的offset后
    public static void displace(List<String> list, String elem, int offset) {
        int index = list.indexOf(elem);
        list.remove(elem);
        list.add(index + offset, elem);
    }

    public static String getStrFromList(List<String> data) {
        StringBuilder listStr = new StringBuilder();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) listStr = new StringBuilder(data.get(i));
                else listStr.append(",").append(data.get(i));
            }
        }
        return listStr.toString();
    }

    public static List<String> getListFromStr(String data) {
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(data) && data.length() > 0) {
            String[] datas = data.split(",");
            list.addAll(Arrays.asList(datas));
        }
        return list;
    }

    // 得到list前几个的分组
    public static List<String> getGroup(List<String> list, int num) {
        List<String> group = new ArrayList<>();
        if (null != list && list.size() > 0) {
            group = list.subList(0, num);
        }
        return group;
    }

    // 重置分组
    public static String resetGroup(String oldGroup, String lastGroup) {
        String newGroup;
        newGroup = oldGroup.substring(lastGroup.length() + 1, oldGroup.length());
        newGroup = newGroup + "," + lastGroup;
        return newGroup;
    }

    // 删除重复元素，并保持顺序
    public static void removeDuplicate(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Object element : list) {
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

}
