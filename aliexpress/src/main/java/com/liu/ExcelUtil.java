
package com.liu;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/5    Liush     初版
 * ──────────────────────────────────────────
 */
@Service
public class ExcelUtil {




    public    List<String> getUrls() throws FileNotFoundException {

        Sheet sheet = new Sheet(1, 0, UrlExcelModel.class);

        List<Object> readList = EasyExcelFactory.read(new FileInputStream("D://2/2.xlsx"), sheet);
        List<String> urls=new ArrayList<>();
        // 存 ExcelMode 实体的 集合
        for(Object o:readList){
            UrlExcelModel urlExcelModel=(UrlExcelModel)o;
            urls.add(urlExcelModel.getUrl());

        }
        return urls;
    }


}
