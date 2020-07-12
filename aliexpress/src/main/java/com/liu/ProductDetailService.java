
package com.liu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能： TODO(用一句话描述类的功能)
 * <p>
 * ──────────────────────────────────────────
 * version  变更日期    修改人    修改说明
 * ------------------------------------------
 * V1.0.0   2020/7/4    Liush     初版
 * ──────────────────────────────────────────
 */
@Service
public class ProductDetailService {


    @Autowired
    private SaveDataI saveData;

    @Autowired
    private ExcelUtil excelUtil;

    private int threadNum=5;

    public void getDetails() throws Exception {

       /* WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python37\\Scripts\\chromedriver.exe");*/
        List<String> urls = excelUtil.getUrls();
        List<List<String>> group=createGroup(urls);
        if(group==null){
            return;
        }
        ExecutorService service=Executors.newFixedThreadPool(threadNum);
        saveData.init();
        for(List<String> subUrls:group){

            service.submit(new GetDataTask(subUrls,saveData));


        }

    }

    private  List<List<String>> createGroup(List<String> urls){
        if(urls==null || urls.size()==0){
            return null;
        }
        List<List<String>> group=new ArrayList<>();
        int taskNum=urls.size()/threadNum;
        int mod=urls.size()%threadNum;

        if(mod!=0){
            taskNum+=1;
        }
        for(int i=0;i<taskNum;i++){
            if(i==taskNum-1){
                group.add(urls.subList(i*taskNum,urls.size()));
                continue;
            }
            group.add(urls.subList(i*taskNum,i*taskNum+taskNum));

        }
        return group;
    }



    /*public void getDetails() throws InterruptedException, FileNotFoundException {
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python37\\Scripts\\chromedriver.exe");

        List<String> urls = excelUtil.getUrls();

        createUrlGroup(urls);
        for (String url : urls) {

            try {
                driver.get(url);
                if (isDialogOpen(driver)) {
                    closeDialog(driver);

                }

                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");
                Thread.sleep(3000);
                String title = getTitle(driver, 3);
                String priceRange = getPriceRange(driver, 3);
                List<String> imagesUrl = getImagesUrl(driver);
                String overView = getOverView(driver);
                String specification = getSpecification(driver);
                getSKUData(driver, title, priceRange, imagesUrl, overView, specification);
            } catch (Exception e) {
                e.printStackTrace();
                continue;

            }


        }


    }




    private String getSpecification(WebDriver driver) {
        try {
            WebElement specificationElement = driver.findElement(By.xpath("//*[@id=\"product-detail\"]/div[1]/div/div[1]/ul/li[3]/div"));
            specificationElement.click();
            Thread.sleep(3000);
            return driver.findElement(By.className("product-specs")).getAttribute("innerHTML");
        } catch (NoSuchElementException | InterruptedException e) {
            return "-1";
        }

    }


    private void closeDialog(WebDriver driver) {
        driver.findElement(By.className("next-dialog-close")).click();

    }

    private boolean isDialogOpen(WebDriver driver) {
        try {
            new WebDriverWait(driver, 5).until((WebDriver d) -> driver.findElement(By.className("next-dialog-body")));
            return true;
        } catch (TimeoutException e) {

            return false;
        }

    }


    private void getSKUData(WebDriver driver, String title, String priceRange, List<String> imagesUrl, String overView, String specification) throws InterruptedException {
        if (!hasSKU(driver)) {

            return;
        }

        WebElement sku1Element = getSku1Element(driver);
        if (isImageSKU(sku1Element)) {
            List<WebElement> imageElements = sku1Element.findElements(By.xpath("./ul[@class='sku-property-list']/li[not(contains(@class,'disabled'))]"));
            for (WebElement imageElement : imageElements) {
                ImageSKU image1SKU = getImageSKUAndPrice(imageElement);
                if (hasSKU2(driver)) {
                    WebElement sku2Element = getSku2Element(driver);
                    if (isSpanSKU(sku2Element)) {
                        saveDetailsBySpan(title, priceRange, imagesUrl, image1SKU, sku2Element, overView, specification);
                        continue;
                    }
                }
                saveData.save(title, imagesUrl, priceRange, overView, specification, image1SKU, null);


            }
            return;
        }

        if (isSpanSKU(sku1Element)) {
            List<WebElement> span1Elements = sku1Element.findElements(By.xpath("./ul[@class='sku-property-list']/li[not(contains(@class,'disabled'))]"));
            for (WebElement span1Element : span1Elements) {
                SpanSKU spanSKU = getSpanSkuAndPrice(span1Element);
                if (hasSKU2(driver)) {
                    WebElement sku2Element = getSku2Element(driver);
                    if (isSpanSKU(sku2Element)) {
                        saveDetailsBySpan(title, priceRange, imagesUrl, spanSKU, sku2Element, overView, specification);
                    }
                    if (isImageSKU(sku2Element)) {
                        saveDetailsByImg(driver, title, priceRange, imagesUrl, spanSKU, overView, specification);
                    }


                } else {

                    saveData.save(title, imagesUrl, priceRange, overView, specification, spanSKU, null);


                }
            }

        }
    }

    private void saveDetailsByImg(WebDriver driver, String title, String priceRange, List<String> imagesUrl, SpanSKU spanSKU, String overView, String specification) throws InterruptedException {
        WebElement sku2Elements = getSku2Element(driver);
        List<WebElement> imageElements = sku2Elements.findElements(By.xpath("./ul[@class='sku-property-list']/li[not(contains(@class,'disabled'))]"));
        for (WebElement image2Element : imageElements) {
            ImageSKU image2SKU = getImageSKUAndPrice(image2Element);
            image2Element.findElement(By.xpath("./div/img")).click();
            saveData.save(title, imagesUrl, priceRange, overView, specification, spanSKU, image2SKU);
        }
    }

    private void saveDetailsBySpan(String title, String priceRange, List<String> imagesUrl, SKU spanSKU, WebElement sku2Element, String overView, String specification) throws InterruptedException {
        List<WebElement> span2Elements = sku2Element.findElements(By.xpath("./ul[@class='sku-property-list']/li[not(contains(@class,'disabled'))]"));
        for (WebElement span2Element : span2Elements) {
            SpanSKU span2SKU = getSpanSkuAndPrice(span2Element);
            span2Element.findElement(By.xpath("./div/span")).click();
            saveData.save(title, imagesUrl, priceRange, overView, specification, spanSKU, span2SKU);
        }
    }

    private ImageSKU getImageSKUAndPrice(WebElement imageElement) throws InterruptedException {
        ImageSKU imageSKU = getImageSKU(imageElement);
        imageElement.findElement(By.xpath("./div/img")).click();
        Thread.sleep(3000);
        String price = imageElement.findElement(By.xpath("//span[@class='product-price-value'][1]")).getText();
        imageSKU.setPrice(price);
        return imageSKU;
    }

    private SpanSKU getSpanSkuAndPrice(WebElement span1Element) throws InterruptedException {
        SpanSKU spanSKU = getSpanSKU(span1Element);

        span1Element.findElement(By.xpath("./div/span")).click();
        Thread.sleep(3000);
        String price = span1Element.findElement(By.xpath("//span[@class='product-price-value'][1]")).getText();
        spanSKU.setPrice(price);
        return spanSKU;
    }


    private String getOverView(WebDriver driver) {
        try {
            return driver.findElement(By.id("product-description")).getAttribute("innerHTML");
        } catch (NoSuchElementException e) {

            return "-1";
        }

    }


    private WebElement getSku1Element(WebDriver driver) {

        return driver.findElement(By.xpath("//div[@class='product-sku']//div[@class='sku-property'][1]"));
    }


    private WebElement getSku2Element(WebDriver driver) {

        return driver.findElement(By.xpath("//div[@class='sku-property'][2]"));
    }


    private int getSkuNum(WebDriver driver) {
        try {
            List<WebElement> skus = driver.findElements(By.xpath("//div[@class='product-sku']//div[@class='sku-property']"));
            if (skus != null) {
                return skus.size();
            }
            return 0;
        } catch (NoSuchElementException e) {

            return 0;
        }

    }


    private boolean hasSKU(WebDriver driver) {
        try {
            new WebDriverWait(driver, 3).until((WebDriver d) -> driver.findElement(By.className("product-sku")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }

    }

    private boolean hasSKU2(WebDriver driver) {
        try {
            new WebDriverWait(driver, 3).until((WebDriver d) -> driver.findElement(By.xpath("//div[@class='product-sku']/div[1]/div[@class='sku-property'][2]")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }

    }


    private boolean isSpanSKU(WebElement imageUl) {

        try {
            imageUl.findElement(By.xpath(".//span"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }


    }


    private boolean isImageSKU(WebElement imageUl) {

        try {
            imageUl.findElement(By.xpath(".//img"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }


    }


    private ImageSKU getImageSKU(WebElement imageSkuElement) {
        try {
            String url = imageSkuElement.findElement(By.xpath("./div/img")).getAttribute("src");
            String sku = imageSkuElement.findElement(By.xpath("./div/img")).getAttribute("title");
            return new ImageSKU(url, sku, "-1");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ImageSKU("-1", "-1", "-1");
        }

    }


    private SpanSKU getSpanSKU(WebElement spanSkuElement) {
        try {
            String sku = spanSkuElement.findElement(By.xpath("./div/span")).getText();
            return new SpanSKU(sku, "-1");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new SpanSKU("-1", "-1");
        }
    }


    private List<String> getImagesUrl(WebDriver driver) {
        List<WebElement> imageList = null;
        List<String> imagesUrl = new ArrayList<>();
        try {
            imageList = new WebDriverWait(driver, 10).until((WebDriver d) -> d.findElements(By.xpath("//ul[@class='images-view-list']/li")));
        } catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println("找不到小图列表");
            return imagesUrl;
        }

        for (WebElement li : imageList) {
            try {
                imagesUrl.add(li.findElement(By.xpath(".//img")).getAttribute("src"));
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                imagesUrl.add("-1");

            }
        }
        return imagesUrl;

    }


    private String getPriceRange(WebDriver driver, int tryTimes) {

        while (tryTimes > 0) {
            try {
                return new WebDriverWait(driver, 10).until((WebDriver d) ->
                        d.findElement(By.className("product-price-value")).getText()
                );

            } catch (TimeoutException e) {
                tryTimes -= 1;

            }


        }

        return "-1";

    }


    private String getTitle(WebDriver driver, int tryTimes) {
        try {
            while (tryTimes > 0) {
                return new WebDriverWait(driver, 10).until((WebDriver d) ->
                        d.findElement(By.className("product-title-text")).getText()
                );

            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            tryTimes -= 1;

        }

        return "-1";
    }
*/

}
