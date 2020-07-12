
package com.liu;

import com.google.common.base.Function;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;


/**
 * 功能： TODO(用一句话描述类的功能)
 * <p>
 * ──────────────────────────────────────────
 * version  变更日期    修改人    修改说明
 * ------------------------------------------
 * V1.0.0   2020/6/28    Liush     初版
 * ──────────────────────────────────────────
 */
public class CollectProductList {

    private static boolean isLogin = false;

    private static int selectBrandNum = 0;

    //账号
    private static String account="account";
    //密码
    private static String password="123456";


    public static void main(String[] args) throws Exception {
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python37\\Scripts\\chromedriver.exe");
        driver.get("https://list.tmall.com/search_product.htm?q=%CA%D6%BB%FA&type=p&vmarket=&spm=875.7931836%2FB.a2227oh.d100&from=mallfp..pc_1_searchbutton");
        if (!isLogin(driver)) {
            login(driver);
        }
        String total = getTotal(driver, 10);
        String currunt = getCurrentNum(driver, 10);
        while (true) {
            if (isForbidden(driver)) {
                driver.navigate().back();
                clickNext(driver, 10);
            }
            Random random = new Random();
            int nextPageWaitTime = random.nextInt(20000) + 3000;
            Thread.sleep(nextPageWaitTime);
            int totalNum = Integer.valueOf(total);
            int curruntNum = Integer.valueOf(currunt);
            if (curruntNum < totalNum) {
                List<WebElement> elements = driver.findElements(By.xpath("/html/body/div[1]/div/div[5]/div/div[7]/div"));
                Thread.sleep(5000);
                getProductList(driver, elements);
            }
            clickNext(driver, 10);
        }


    }


    private static boolean isForbidden(WebDriver driver) {
        try {
            String tip = new WebDriverWait(driver, 5).until(
                    (WebDriver d) -> driver.findElement(By.xpath("//div[@class=\"tips\"]/p[1]")).getText()
            );
            if ("亲，小二正忙，滑动一下马上回来".equals(tip.trim())) {
                return true;
            }
            return false;
        } catch (Exception e) {

            return false;

        }

    }


    private static void clickNext(WebDriver driver, int tryTime) throws InterruptedException {
        try {
            if (tryTime > 0) {
                new WebDriverWait(driver, 5).until(
                        (WebDriver d) -> driver.findElement(By.className("ui-page-next"))
                );
                driver.findElement(By.className("ui-page-next")).click();
                return;
            }
            throw new RuntimeException("无法点击下一页");
        } catch (TimeoutException e) {
            driver.navigate().refresh();
            Thread.sleep(5000);
            clickNext(driver, --tryTime);
        }


    }

//
    private static String getCurrentNum(WebDriver driver, int tryTime) throws InterruptedException {
        try {
            if (tryTime > 0)
                return new WebDriverWait(driver, 5).until(
                        (WebDriver d) -> driver.findElement(By.xpath("//input[@name='jumpto']")).getAttribute("value")
                );
        } catch (TimeoutException e) {
            driver.navigate().refresh();
            Thread.sleep(5000);
            return getTotal(driver, --tryTime);
        }
        throw new RuntimeException("无法获取总页数");

    }

    private static String getTotal(WebDriver driver, int tryTime) throws InterruptedException {
        try {
            if (tryTime > 0)
                return new WebDriverWait(driver, 5).until(
                        (WebDriver d) -> d.findElement(By.xpath("//input[@name=\"totalPage\"]")).getAttribute("value")
                );
        } catch (TimeoutException e) {
            driver.navigate().refresh();
            Thread.sleep(5000);
            return getCurrentNum(driver, --tryTime);
        }
        throw new RuntimeException("无法获取当前页数");

    }


    private static void addDataToHbase(String title, String price, String sellNum, String shopName, String url) throws UnsupportedEncodingException {
        HttpClient client = new DefaultHttpClient();
        Date date = new Date();
        String rowkey = "10000" + date.getTime();
        HttpPost post = new HttpPost("http://127.0.0.1:8081/addProductData");

        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("rowkey", rowkey));
        pairs.add(new BasicNameValuePair("title", title));
        pairs.add(new BasicNameValuePair("price", price));
        pairs.add(new BasicNameValuePair("sellNum", sellNum));
        pairs.add(new BasicNameValuePair("shopName", shopName));
        pairs.add(new BasicNameValuePair("url", url));
        //pairs.add(new BasicNameValuePair("brand", brand));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            String message = EntityUtils.toString(response.getEntity(), "utf-8");
            if (!"success".equals(message)) {
                throw new RuntimeException("创建失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void getProductList(WebDriver driver, List<WebElement> elements) throws InterruptedException, UnsupportedEncodingException {
        for (WebElement webElement : elements) {
            String title = getTitle(webElement, 3);
            String price = getPrice(webElement, 3);
            String sellNum = getSellNum(webElement, 3);
            String shopName = getShopName(webElement, 3);
            String url = getDetailUrl(webElement, 3);
            System.out.println(title);
            System.out.println(price);
            System.out.println(sellNum);
            System.out.println(shopName);
            /*showProductDetail(webElement);
            Set<String> windowHandles = switchProductDetailsWindow(driver);
            String brand=getBrand(driver);*/
            addDataToHbase(title, price, sellNum, shopName, url);
            //switchToProductListWindow(driver, windowHandles);
            System.out.println("------------------------------");
        }
    }


    private static String getDetailUrl(WebElement webElement, int tryTime) throws InterruptedException {
        if (tryTime > 0) {
            try {
                return webElement.findElement(By.xpath("./div[@class='product-iWrap']/div[2]/a")).getAttribute("href");
            } catch (NoSuchElementException e) {
                Thread.sleep(2000);
                return getDetailUrl(webElement, --tryTime);
            }
        }
        return "找不到链接";


    }

    private static String getShopName(WebElement webElement, int tryTime) throws InterruptedException {
        if (tryTime > 0) {
            try {
                return webElement.findElement(By.xpath("./div[@class='product-iWrap']/div[@class='productShop']/a")).getText();
            } catch (NoSuchElementException e) {
                Thread.sleep(2000);
                return getShopName(webElement, --tryTime);
            }
        }
        return "找不到店名";

    }

    private static void switchToProductListWindow(WebDriver driver, Set<String> windowHandles) {
        driver.switchTo().window(windowHandles.toArray()[0].toString());
    }

    private static Set<String> switchProductDetailsWindow(WebDriver driver) {
        Set<String> windowHandles = driver.getWindowHandles();
        driver.switchTo().window(windowHandles.toArray()[2].toString());
        return windowHandles;
    }

    private static void showProductDetail(WebElement webElement) {
        webElement.findElement(By.xpath("./div/div[2]/a")).click();
    }

    private static String getSellNum(WebElement webElement, int tryTime) throws InterruptedException {
        if (tryTime > 0) {
            try {
                return webElement.findElement(By.xpath("./div[@class='product-iWrap']/p[@class='productStatus']/span/em")).getText();
            } catch (NoSuchElementException e) {
                Thread.sleep(2000);
                return getSellNum(webElement, --tryTime);
            }
        }
        return "找不到销售额";

    }

    private static String getPrice(WebElement webElement, int tryTime) throws InterruptedException {
        if (tryTime > 0) {
            try {
                return webElement.findElement(By.xpath("./div[@class='product-iWrap']/p[@class='productPrice']//em")).getText();
            } catch (NoSuchElementException e) {
                Thread.sleep(2000);
                return getPrice(webElement, --tryTime);
            }
        }
        return "找不到价格";


    }

    private static String getTitle(WebElement webElement, int tryTime) throws InterruptedException {
        if (tryTime > 0) {
            try {
                return webElement.findElement(By.xpath("./div[@class='product-iWrap']/div[2]/a")).getAttribute("title");
            } catch (NoSuchElementException e) {
                Thread.sleep(2000);
                return getTitle(webElement, --tryTime);
            }
        }
        return "找不到标题";

    }


    private static String getBrand(WebDriver driver) throws InterruptedException {
        Thread.sleep(3000);
        driver.switchTo().defaultContent();
        try {
            String brand = selectBrand(driver);
            System.out.println(brand);
            selectBrandNum = 0;
            return brand;
        } catch (NoSuchElementException e) {
            selectBrandNum++;
            if (selectBrandNum == 4) {
                return "找不到品牌";
            }
            String brand = getBrand(driver);


        }
        return null;
    }

    private static String selectBrand(WebDriver driver) {
        return new WebDriverWait(driver, 30).until(new com.google.common.base.Function<WebDriver, String>() {
            @Override
            public String apply(@Nullable WebDriver webDriver) {
                return driver.findElement(By.xpath("//*[@id=\"J_BrandAttr\"]/div/b")).getText();
            }
        });
    }


    private static boolean isLogin(WebDriver driver) {
        try {
            return !"请登录".equals(driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/p[2]/a[1]")).getText());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return true;
        }

    }

    private static void login(WebDriver driver) throws InterruptedException {

        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/p[2]/a[1]")).click();
        Thread.sleep(10000);
        WebElement loginFrame = driver.findElement(By.xpath("//*[@id='J_loginIframe']"));
        driver.switchTo().frame(loginFrame);
        driver.findElement(By.xpath("//*[@id=\"fm-login-id\"]")).sendKeys(account);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"fm-login-password\"]")).sendKeys(password);
        WebElement moveElement = driver.findElement(By.xpath("//*[@id='nc_1_n1z']"));
        Actions actions = new Actions(driver);
        actions = actions.clickAndHold(moveElement).moveByOffset(108, 0);
        Thread.sleep(500);
        actions.moveByOffset(150, 0);
        actions.perform();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id='login-form']/div[4]/button")).click();
        Thread.sleep(10000);
        isLogin = true;
        /*WebElement moveElement=driver.findElement(By.xpath("//*[@id="nc_1_n1z"]"));
        Actions actions=new Actions(driver);
        actions=actions.clickAndHold(moveElement).moveByOffset(108, 0);
        Thread.sleep(500);
        actions.moveByOffset(150, 0);
        actions.perform();
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/form/div[4]/button")).click();
        isLogin=true;*/

    }


}
