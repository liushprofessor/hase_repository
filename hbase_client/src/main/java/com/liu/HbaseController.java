
package com.liu;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/6/29    Liush     初版
 * ──────────────────────────────────────────
 */
@RestController
public class HbaseController {

    HbaseUtil hbaseUtil=new HbaseUtil();


    @RequestMapping("createTable")
    public String createTable(String name){
        try {
            hbaseUtil.createTable(name);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }

    }

    @RequestMapping("addArea")
    public String addArea(String rowKey ,String code,String sheng,String di,String xian){

        try {
            hbaseUtil.addArea(rowKey,code,sheng,di,xian);
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }



    @RequestMapping("addProductDetailData")
    public String addProductDetailData(String rowKey,String productRowKey,String titleDetail,String describe,String brand){
        try {
            hbaseUtil.addProductDetailData(rowKey,productRowKey,titleDetail,describe,brand);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }

    }



    @RequestMapping("addProductData")
    public String addProductData(String rowkey,String title,String price,String sellNum,String shopName,String url){

        try {
            hbaseUtil.addProduct(rowkey,title,price,sellNum,shopName,url);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }

    }

    @RequestMapping("getProduct")
    public Product getProduct(String rowkey){
        try {
            return hbaseUtil.getProduct(rowkey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("addAliexpress")
    public String addAliexpressProductDetail(String webUrl,String rowkey,String title,String imgUrls,String rangePrice,String overview,String specification,String sku,String sku2,String sku3,String price,String imgDetailUrl) throws IOException {
    try {

        hbaseUtil.addAliexpressProductDetail(webUrl,rowkey,title,imgUrls,rangePrice,overview,specification,sku,sku2,sku3,price,imgDetailUrl);
        return "success";
    }catch (Exception e){
        return "fail";
    }



    }


    @RequestMapping("getProducts")
    public List<Product> getProducts(String rowkeyStart,String rowkeyEnd,int size) throws IllegalAccessException, NoSuchFieldException, IOException {

        return hbaseUtil.scanProduct(rowkeyStart,rowkeyEnd,size);

    }







}
