
package com.liu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/5    Liush     初版
 * ──────────────────────────────────────────
 */
@RestController
public class Controller {

    @Autowired
    private ProductDetailService service;

    @RequestMapping("test")
    public void test() throws Exception {

        service.getDetails();

    }







}
