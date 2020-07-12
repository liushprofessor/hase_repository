
package com.liu;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/10    Liush     初版
 * ──────────────────────────────────────────
 */
public class Test {


   /* public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        for(int i=0;i<10000;i++){
            list.add("222");
        }

        List<List<String>> list2=test(list);
        System.out.println(list2);
    }*/

   /* private static List<List<String>> test(List<String> urls){
        List<List<String>> group=new ArrayList<>();
        int taskNum=urls.size()/1000;
        int mod=urls.size()%1000;

        if(mod!=0){
            taskNum+=1;
        }
        for(int i=0;i<taskNum;i++){
            if(i==taskNum-1){
                group.add(urls.subList(i*1000,urls.size()));
                continue;
            }
            group.add(urls.subList(i*1000,i*1000+1000));

        }
        return group;
    }*/

}
