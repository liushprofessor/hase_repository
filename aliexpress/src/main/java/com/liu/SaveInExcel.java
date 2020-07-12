
package com.liu;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;


import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/10    Liush     初版
 * ──────────────────────────────────────────
 */
@Service
public class SaveInExcel implements SaveDataI {


    private List<AliEProduct> list;

    private File excelFile;


    private ScheduledExecutorService service= Executors.newScheduledThreadPool(1);


    public SaveInExcel() throws IOException {

    }


    @Override
    public void init() throws Exception {
        service.shutdown();
        this.list =new CopyOnWriteArrayList<>();
        excelFile=new File("D://out/out.xls");
        if(excelFile.exists()){
            excelFile.delete();
        }
        if(!excelFile.getParentFile().exists()){
            excelFile.mkdirs();
        }

        createExcel();
        service=Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(new ExcelWriterTask(list),10,10,TimeUnit.SECONDS);
    }

    private void createExcel() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet();

        HSSFRow row=sheet.createRow((short)(0));
        row.createCell(0).setCellValue("网页地址");
        row.createCell(1).setCellValue("标题");
        row.createCell(2).setCellValue("大图1");
        row.createCell(3).setCellValue("大图2");
        row.createCell(4).setCellValue("大图3");
        row.createCell(5).setCellValue("大图4");
        row.createCell(6).setCellValue("大图5");
        row.createCell(7).setCellValue("大图6");
        row.createCell(8).setCellValue("大图7");
        row.createCell(9).setCellValue("大图8");
        row.createCell(11).setCellValue("大图9");
        row.createCell(12).setCellValue("大图10");
        row.createCell(13).setCellValue("价格区间");

        row.createCell(14).setCellValue("长描述");

        row.createCell(15).setCellValue("短描述");

        row.createCell(16).setCellValue("sku1");
        row.createCell(17).setCellValue("sku2");
        row.createCell(18).setCellValue("sku3");
        row.createCell(19).setCellValue("小图地址");
        row.createCell(20).setCellValue("价格");


        OutputStream os = new FileOutputStream("D://out/out.xls");
        workbook.write(os);
        os.flush();
        os.close();
    }


    @Override
    public void save(String url, String title, List<String> imgUrls, String rangePrice, String overview, String specification, SKU sku1, SKU sku2, SKU sku3) {

        AliEProduct product=createExcelProduct(url, title, imgUrls, overview, specification, sku1, sku2, sku3);
        list.add(product);
        /*Sheet sheet = new Sheet(1, 0, AliEProduct.class);
        sheet.setSheetName("目录");
        excelWriter.write(product,sheet);*/
    }

    private AliEProduct createExcelProduct(String url, String title, List<String> imgUrls, String overview, String specification, SKU sku1, SKU sku2, SKU sku3) {
        AliEProduct product=new AliEProduct();
        product.setWebUrl(url).setTitle(title).setOverview(overview).setSpecification(specification);
        if(sku1!=null && sku1 instanceof ImageSKU){
            ImageSKU imageSKU=(ImageSKU) sku1;
            product.setSmallImgUrl(imageSKU.getUrl());
            product.setPrice(imageSKU.getPrice());
            product.setSku1(imageSKU.getSku());

        }

        if(sku1!=null && sku1 instanceof SpanSKU){
            SpanSKU spanSKU=(SpanSKU) sku1;
            product.setPrice(spanSKU.getPrice());
            product.setSku1(spanSKU.getSku());

        }


        if(sku2!=null && sku2 instanceof ImageSKU){
            ImageSKU imageSKU=(ImageSKU) sku2;
            product.setSmallImgUrl(imageSKU.getUrl());
            product.setPrice(imageSKU.getPrice());
            product.setSku2(imageSKU.getSku());

        }


        if(sku2!=null && sku2 instanceof SpanSKU){
            SpanSKU spanSKU=(SpanSKU) sku2;
            product.setPrice(spanSKU.getPrice());
            product.setSku2(spanSKU.getSku());

        }


        if(sku3!=null && sku3 instanceof ImageSKU){
            ImageSKU imageSKU=(ImageSKU) sku3;
            product.setSmallImgUrl(imageSKU.getUrl());
            product.setPrice(imageSKU.getPrice());
            product.setSku3(imageSKU.getSku());

        }


        if(sku3!=null && sku3 instanceof SpanSKU){
            SpanSKU spanSKU=(SpanSKU) sku3;
            product.setPrice(spanSKU.getPrice());
            product.setSku3(spanSKU.getSku());

        }


        if(imgUrls!=null){
            for(int i=0;i<imgUrls.size();i++){
                try {
                    Field field=product.getClass().getDeclaredField("imgUrl"+(i+1));
                    field.setAccessible(true);
                    field.set(product,imgUrls.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }

        return product;
    }
}
