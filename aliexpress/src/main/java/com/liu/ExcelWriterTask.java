
package com.liu;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/11    Liush     初版
 * ──────────────────────────────────────────
 */
public class ExcelWriterTask implements Runnable {

    private List<AliEProduct> products;





    public ExcelWriterTask(List<AliEProduct> products) {
        this.products = products;

    }

    @Override
    public void run() {

            System.out.println("写入ecvel。。。。。。。。。。。。。。。。"+products.size());
            List<AliEProduct> subProducts=null;
            if(products.size()==0){
                return;
            }
            if(products.size()>100){
                subProducts=new ArrayList<>(products.subList(0,100));
                Collections.copy(subProducts,products.subList(0,100));
                products.subList(0,100).clear();
            }else {
                    subProducts=new ArrayList<>(products.subList(0,products.size()));
                    Collections.copy(subProducts,products.subList(0,products.size()));
                    products.subList(0,products.size()).clear();

            }
        FileOutputStream out=null;
        HSSFWorkbook wb=null;
        try {
            FileInputStream fs=new FileInputStream("D://out/out.xls");

            POIFSFileSystem ps=new POIFSFileSystem(fs);

            wb=new HSSFWorkbook(ps);
            HSSFSheet sheet=wb.getSheetAt(0);
            out=new FileOutputStream("D://out/out.xls");

            for(int i=0;i<subProducts.size();i++){
                HSSFRow row=sheet.createRow((short)(sheet.getLastRowNum()+1));
                row.createCell(0).setCellValue(subProducts.get(i).getWebUrl());
                row.createCell(1).setCellValue(subProducts.get(i).getTitle());
                row.createCell(2).setCellValue(subProducts.get(i).getImgUrl1());
                row.createCell(3).setCellValue(subProducts.get(i).getImgUrl2());
                row.createCell(4).setCellValue(subProducts.get(i).getImgUrl3());
                row.createCell(5).setCellValue(subProducts.get(i).getImgUrl4());
                row.createCell(6).setCellValue(subProducts.get(i).getImgUrl5());
                row.createCell(7).setCellValue(subProducts.get(i).getImgUrl6());
                row.createCell(8).setCellValue(subProducts.get(i).getImgUrl7());
                row.createCell(9).setCellValue(subProducts.get(i).getImgUrl8());
                row.createCell(11).setCellValue(subProducts.get(i).getImgUrl9());
                row.createCell(12).setCellValue(subProducts.get(i).getImgUrl10());
                row.createCell(13).setCellValue(subProducts.get(i).getRangePrice());
                if(subProducts.get(i).getOverview().length()< 32767){
                    row.createCell(14).setCellValue(subProducts.get(i).getOverview());
                }else {
                    row.createCell(14).setCellValue("");
                }
                if(subProducts.get(i).getSpecification().length()< 32767){
                    row.createCell(15).setCellValue(subProducts.get(i).getSpecification());

                }else {
                    row.createCell(15).setCellValue("");
                }
                row.createCell(16).setCellValue(subProducts.get(i).getSku1());
                row.createCell(17).setCellValue(subProducts.get(i).getSku2());
                row.createCell(18).setCellValue(subProducts.get(i).getSku3());
                row.createCell(19).setCellValue(subProducts.get(i).getSmallImgUrl());
                row.createCell(20).setCellValue(subProducts.get(i).getPrice());


            }
            out.flush();
            wb.write(out);
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                wb.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}
