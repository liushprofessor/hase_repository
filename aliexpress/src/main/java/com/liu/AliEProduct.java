
package com.liu;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * 功能： TODO(用一句话描述类的功能)
 * <p>
 * ──────────────────────────────────────────
 * version  变更日期    修改人    修改说明
 * ------------------------------------------
 * V1.0.0   2020/7/11    Liush     初版
 * ──────────────────────────────────────────
 */
public class AliEProduct extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String webUrl;
    @ExcelProperty(index = 1)
    private String title;
    @ExcelProperty(index = 2)
    private String imgUrl1;
    @ExcelProperty(index = 3)
    private String imgUrl2;
    @ExcelProperty(index = 4)
    private String imgUrl3;
    @ExcelProperty(index = 5)
    private String imgUrl4;
    @ExcelProperty(index = 6)
    private String imgUrl5;
    @ExcelProperty(index = 7)
    private String imgUrl6;
    @ExcelProperty(index = 8)
    private String imgUrl7;
    @ExcelProperty(index = 9)
    private String imgUrl8;
    @ExcelProperty(index = 10)
    private String imgUrl9;
    @ExcelProperty(index = 11)
    private String imgUrl10;
    @ExcelProperty(index = 12)
    private String rangePrice;
    @ExcelProperty(index = 13)
    private String overview;
    @ExcelProperty(index = 14)
    private String specification;
    @ExcelProperty(index = 15)
    private String sku1;
    @ExcelProperty(index = 16)
    private String sku2;
    @ExcelProperty(index =17)
    private String sku3;
    @ExcelProperty(index = 18)
    private String smallImgUrl;
    @ExcelProperty(index = 19)
    private String  price;



    public AliEProduct() {
    }


    public String getWebUrl() {
        return webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public String getImgUrl4() {
        return imgUrl4;
    }

    public String getImgUrl5() {
        return imgUrl5;
    }

    public String getImgUrl6() {
        return imgUrl6;
    }

    public String getImgUrl7() {
        return imgUrl7;
    }

    public String getImgUrl8() {
        return imgUrl8;
    }

    public String getImgUrl9() {
        return imgUrl9;
    }

    public String getImgUrl10() {
        return imgUrl10;
    }

    public String getRangePrice() {
        return rangePrice;
    }

    public String getOverview() {
        return overview;
    }

    public String getSpecification() {
        return specification;
    }

    public String getSku1() {
        return sku1;
    }

    public String getSku2() {
        return sku2;
    }

    public String getSku3() {
        return sku3;
    }

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public AliEProduct setWebUrl(String webUrl) {
        this.webUrl = webUrl;
        return this;
    }

    public AliEProduct setTitle(String title) {
        this.title = title;
        return this;
    }

    public AliEProduct setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
        return this;
    }

    public AliEProduct setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
        return this;
    }

    public AliEProduct setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
        return this;
    }

    public AliEProduct setImgUrl4(String imgUrl4) {
        this.imgUrl4 = imgUrl4;
        return this;
    }

    public AliEProduct setImgUrl5(String imgUrl5) {
        this.imgUrl5 = imgUrl5;
        return this;
    }

    public AliEProduct setImgUrl6(String imgUrl6) {
        this.imgUrl6 = imgUrl6;
        return this;
    }

    public AliEProduct setImgUrl7(String imgUrl7) {
        this.imgUrl7 = imgUrl7;
        return this;
    }

    public AliEProduct setImgUrl8(String imgUrl8) {
        this.imgUrl8 = imgUrl8;
        return this;
    }

    public AliEProduct setImgUrl9(String imgUrl9) {
        this.imgUrl9 = imgUrl9;
        return this;
    }

    public AliEProduct setImgUrl10(String imgUrl10) {
        this.imgUrl10 = imgUrl10;
        return this;
    }

    public AliEProduct setRangePrice(String rangePrice) {
        this.rangePrice = rangePrice;
        return this;
    }

    public AliEProduct setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public AliEProduct setSpecification(String specification) {
        this.specification = specification;
        return this;
    }

    public AliEProduct setSku1(String sku1) {
        this.sku1 = sku1;
        return this;
    }

    public AliEProduct setSku2(String sku2) {
        this.sku2 = sku2;
        return this;
    }

    public AliEProduct setSku3(String sku3) {
        this.sku3 = sku3;
        return this;
    }

    public AliEProduct setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public AliEProduct setPrice(String price) {
        this.price = price;
        return this;
    }
}
