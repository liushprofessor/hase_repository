
package com.liu;

import com.google.common.base.Joiner;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
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

public class SaveInHbase implements SaveDataI {
    @Override
    public void save(String url,String title, List<String> imgUrls, String rangePrice, String overview, String specification, SKU sku1, SKU sku2,SKU sku3) {
        System.out.println(title);
        add(url,title,imgUrls,rangePrice,overview,specification,sku1,sku2,sku3);
    }

    private void add(String weburl,String title, List<String> imgUrls, String rangePrice, String overview, String specification, SKU sku1, SKU sku2,SKU sku3) {

        HttpClient client = new DefaultHttpClient();
        Date date = new Date();
        String rowkey = "20000" + date.getTime();
        HttpPost post = new HttpPost("http://127.0.0.1:8081/addAliexpress");
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("rowkey", rowkey));
        pairs.add(new BasicNameValuePair("title", title));
        pairs.add(new BasicNameValuePair("imgUrls", Joiner.on(",").skipNulls().join(imgUrls)));
        pairs.add(new BasicNameValuePair("rangePrice", rangePrice));
        pairs.add(new BasicNameValuePair("overview", overview));
        pairs.add(new BasicNameValuePair("specification", specification));
        pairs.add(new BasicNameValuePair("webUrl", weburl));
        if(sku1==null){

        }else if(sku2==null && sku3==null){
            if(sku1 instanceof ImageSKU){

                ImageSKU imageSKU1=(ImageSKU) sku1;
                pairs.add(new BasicNameValuePair("sku", imageSKU1.getSku()));
                pairs.add(new BasicNameValuePair("price", imageSKU1.getPrice()));
                pairs.add(new BasicNameValuePair("imgDetailUrl", imageSKU1.getUrl()));

            }
            if(sku1 instanceof SpanSKU){
                SpanSKU spanSKU1=(SpanSKU) sku1;
                pairs.add(new BasicNameValuePair("sku", spanSKU1.getSku()));
                pairs.add(new BasicNameValuePair("price", spanSKU1.getPrice()));

            }

        }else if(sku2!=null && sku3==null){

            if(sku1 instanceof ImageSKU){

                ImageSKU imageSKU1=(ImageSKU) sku1;
                pairs.add(new BasicNameValuePair("sku", imageSKU1.getSku()));
                pairs.add(new BasicNameValuePair("imgDetailUrl", imageSKU1.getUrl()));

            }
            if(sku1 instanceof SpanSKU){
                SpanSKU spanSKU1=(SpanSKU) sku1;
                pairs.add(new BasicNameValuePair("sku", spanSKU1.getSku()));

            }

            if(sku2 instanceof ImageSKU){

                ImageSKU imageSKU2=(ImageSKU) sku2;
                pairs.add(new BasicNameValuePair("sku2", imageSKU2.getSku()));
                pairs.add(new BasicNameValuePair("imgDetailUrl", imageSKU2.getUrl()));
                pairs.add(new BasicNameValuePair("price", imageSKU2.getPrice()));

            }
            if(sku2 instanceof SpanSKU){
                SpanSKU spanSKU2=(SpanSKU) sku2;
                pairs.add(new BasicNameValuePair("sku2", spanSKU2.getSku()));
                pairs.add(new BasicNameValuePair("price", spanSKU2.getPrice()));

            }

        }

        else if(sku2!=null && sku3!=null)  {

            if(sku1 instanceof ImageSKU){

                ImageSKU imageSKU1=(ImageSKU) sku1;
                pairs.add(new BasicNameValuePair("sku", imageSKU1.getSku()));
                pairs.add(new BasicNameValuePair("imgDetailUrl", imageSKU1.getUrl()));

            }
            if(sku1 instanceof SpanSKU){
                SpanSKU spanSKU1=(SpanSKU) sku1;
                pairs.add(new BasicNameValuePair("sku", spanSKU1.getSku()));

            }

            if(sku2 instanceof ImageSKU){

                ImageSKU imageSKU2=(ImageSKU) sku2;
                pairs.add(new BasicNameValuePair("sku2", imageSKU2.getSku()));
                pairs.add(new BasicNameValuePair("imgDetailUrl", imageSKU2.getUrl()));
                //pairs.add(new BasicNameValuePair("price", imageSKU2.getPrice()));

            }
            if(sku2 instanceof SpanSKU){
                SpanSKU spanSKU2=(SpanSKU) sku2;
                pairs.add(new BasicNameValuePair("sku2", spanSKU2.getSku()));

            }
            if(sku3 instanceof SpanSKU){
                SpanSKU spanSKU3=(SpanSKU) sku3;
                pairs.add(new BasicNameValuePair("sku3", spanSKU3.getSku()));
                pairs.add(new BasicNameValuePair("price", spanSKU3.getPrice()));
            }



        }

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            String message = EntityUtils.toString(response.getEntity(), "utf-8");
            if (!"success".equals(message)) {
                System.out.println("失败------------------------------------");
               // throw new RuntimeException("创建失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
