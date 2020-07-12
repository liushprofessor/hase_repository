
package com.liu;

/**
 * 功能： TODO(用一句话描述类的功能)
 * <p>
 * ──────────────────────────────────────────
 * version  变更日期    修改人    修改说明
 * ------------------------------------------
 * V1.0.0   2020/7/10    Liush     初版
 * ──────────────────────────────────────────
 */
public class Test {
    public static void main(String[] args) {

        String sellNum="1万笔";
        if (sellNum.contains("万笔")) {
            String num = sellNum.substring(0, sellNum.indexOf("万笔"));
            int numInt = (int) (Float.valueOf(num) * 10000);
            System.out.println(numInt);
            return;
        }
        if (sellNum.contains("笔")) {
            String num = sellNum.substring(0, sellNum.indexOf("笔"));
            int numInt = Integer.valueOf(num);
            System.out.println(numInt);
        }

    }
}
