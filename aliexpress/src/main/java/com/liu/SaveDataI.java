
package com.liu;

import java.io.FileNotFoundException;
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
public interface SaveDataI {


    void save(String url,String title,List<String> imgUrls,String rangePrice,String overview,String specification,SKU sku1,SKU sku2,SKU sku3) ;

    default void init() throws Exception {

    }


}
