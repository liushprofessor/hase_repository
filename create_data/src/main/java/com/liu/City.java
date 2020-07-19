package com.liu;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class City {

    private static Integer code=0;

    private static boolean havaDi=false;

    private static boolean haveXian=false;


    public static void main(String[] args) throws IOException, InterruptedException {


        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python37\\Scripts\\chromedriver.exe");

        driver.get("http://xzqh.mca.gov.cn/map");

        WebElement shengSelectElement = driver.findElement(By.xpath("//select[@name='shengji']"));

        Select shengSelect = new Select(shengSelectElement);
        List<WebElement> shengOptions = shengSelect.getOptions();


        for (int i = 0; i < shengOptions.size(); i++) {
            String sheng=null;
            havaDi=false;
            haveXian=false;
            if (!"-1".equals(shengOptions.get(i).getAttribute("value"))) {

                 sheng=shengOptions.get(i).getAttribute("value");
                shengSelect.selectByIndex(i);

                getDiji(driver,sheng);
            }
            if(!havaDi && !haveXian && sheng !=null){
                String rowkey=Long.toString(new Date().getTime());
                code++;
                putAreaOnHBase(rowkey,code.toString(),sheng,sheng,sheng);


            }

        }


    }


    private static void getDiji(WebDriver driver, String sheng) throws IOException, InterruptedException {


        Thread.sleep(2000);
        WebElement dijiSelectElement = driver.findElement(By.xpath("//select[@name='diji']"));
        Select dijiSelect = new Select(dijiSelectElement);
        List<WebElement> dijiOptions = dijiSelect.getOptions();

        for (int i = 0; i < dijiOptions.size(); i++) {
            if (!"-1".equals(dijiOptions.get(i).getAttribute("value"))) {
                havaDi=true;
                String diji = dijiOptions.get(i).getAttribute("value");
                dijiSelect.selectByIndex(i);

                Thread.sleep(2000);
                WebElement xianElement = driver.findElement(By.xpath("//select[@name='xianji']"));
                Select xianjiSelect = new Select(xianElement);
                List<WebElement> xianjiOptions = xianjiSelect.getOptions();
                for (int k = 0; k < xianjiOptions.size(); k++) {

                    if (!"-1".equals(xianjiOptions.get(k).getAttribute("value"))) {

                        haveXian=true;
                        String xianji = xianjiOptions.get(k).getAttribute("value");

                        String rowkey=Long.toString(new Date().getTime());
                        System.out.println(sheng + "-------" + diji + "-----------" + xianji);
                        code++;
                        putAreaOnHBase(rowkey,code.toString(),sheng,diji,xianji);

                    }


                }

                if(!haveXian){
                    String rowkey=Long.toString(new Date().getTime());
                    code++;
                    putAreaOnHBase(rowkey,code.toString(),sheng,diji,diji);

                }
            }


        }


    }



    public static void putAreaOnHBase(String rowKey,String code,String sheng,String di,String xian) throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://127.0.0.1:8081/addArea");
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("rowKey", rowKey));
        pairs.add(new BasicNameValuePair("sheng", sheng));
        pairs.add(new BasicNameValuePair("di", di));
        pairs.add(new BasicNameValuePair("xian", xian));
        pairs.add(new BasicNameValuePair("code", code));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            String message = EntityUtils.toString(response.getEntity(), "utf-8");
            if (!"success".equals(message)) {
                System.out.println(rowKey+"创建失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}
