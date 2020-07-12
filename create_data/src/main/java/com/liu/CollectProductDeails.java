
package com.liu;

import com.alibaba.fastjson.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 功能： TODO(用一句话描述类的功能)
 * <p>
 * ──────────────────────────────────────────
 * version  变更日期    修改人    修改说明
 * ------------------------------------------
 * V1.0.0   2020/7/2    Liush     初版
 * ──────────────────────────────────────────
 */
public class CollectProductDeails {


    public static void main(String[] args) throws IOException {
        WebDriver driver = webDriverInit();
        addProductDetails(driver,null);
    }


    private static void addProductDetails(WebDriver driver,String rowKey) throws IOException {
        List<Product> products = scanProductDetails(rowKey);
        for (Product product:products){
            String productRowKey=product.getRowKey();
            String url=product.getUrl();
            openUrl(driver, url);
            String titleDetail=getTitleDetail(driver);
            String brand=getBrand(driver);
            String describe=getDescribe(driver);
            putProductDetailOnHBase(productRowKey,titleDetail,brand,describe);
            //driver.close();
        }
       if(products.size()>0){
           addProductDetails(driver,products.get(products.size()-1).getRowKey());
       }
    }


    public static void putProductDetailOnHBase(String productRowKey,String titleDetail,String brand,String describe) throws IOException {

        HttpClient client = new DefaultHttpClient();
        Date date = new Date();
        String rowkey = "10000" + date.getTime();
        HttpPost post = new HttpPost("http://127.0.0.1:8081/addProductDetailData");
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("rowKey", rowkey));
        pairs.add(new BasicNameValuePair("productRowKey", productRowKey));
        pairs.add(new BasicNameValuePair("titleDetail", titleDetail));
        pairs.add(new BasicNameValuePair("brand", brand));
        pairs.add(new BasicNameValuePair("describe", describe));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            String message = EntityUtils.toString(response.getEntity(), "utf-8");
            if (!"success".equals(message)) {
                System.out.println(productRowKey+"创建失败");
            }
        } catch (IOException e) {
                e.printStackTrace();
        }




    }






    private static void openUrl(WebDriver driver, String url) {
        driver.get(url);
    }

    private static WebDriver webDriverInit() {
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python37\\Scripts\\chromedriver.exe");
        return driver;
    }

    private static List<Product> scanProductDetails(String rowKeyStart) throws IOException {
        if(null==rowKeyStart){
            rowKeyStart="100000000000000000";
        }
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://127.0.0.1:8081/getProducts?rowkeyStart="+rowKeyStart+"&rowkeyEnd=100009999999999999&size=10");
        HttpResponse response = client.execute(get);
        String productList = EntityUtils.toString(response.getEntity(), "utf-8");
        return JSONArray.parseArray(productList, Product.class);
    }

    private static String getDescribe(WebDriver driver) {
        try {
            return new WebDriverWait(driver,5).until((WebDriver d)->
                    d.findElement(By.id("J_AttrUL")).getAttribute("innerHTML")
            );
        }catch (TimeoutException e){
            return "找不到详情";
        }
    }

    private static String  getBrand(WebDriver driver) {
        try {
            return new WebDriverWait(driver,10).until((WebDriver d)->
                    d.findElement(By.xpath("//*[@class='J_EbrandLogo']")).getText()
            );
        }catch (TimeoutException e){
            return "找不到品牌";
        }
    }

    private static String getTitleDetail(WebDriver driver) {
        try {
             return new WebDriverWait(driver,5).until((WebDriver d)->
                    d.findElement(By.xpath("//div[@id=\"J_DetailMeta\"]/div[1]/div[1]/div/div[1]/h1/a")).getText()
            );

        }catch (TimeoutException e){
            try {
                return new WebDriverWait(driver,5).until((WebDriver d)->
                        d.findElement(By.xpath("//div[@id=\"J_DetailMeta\"]/div[1]/div[1]/div/div[1]/h1")).getText()
                );

            }catch (TimeoutException e2){
                return  "找不到商品详情标题";
            }

        }
    }


}
