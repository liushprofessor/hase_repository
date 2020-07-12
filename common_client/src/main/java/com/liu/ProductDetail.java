
package com.liu;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/2    Liush     初版
 * ──────────────────────────────────────────
 */
public class ProductDetail {

    private String rowKey;

    private String productRowKey;

    private String titleDetail;

    private String describe;

    private String brand;

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getTitleDetail() {
        return titleDetail;
    }

    public void setTitleDetail(String titleDetail) {
        this.titleDetail = titleDetail;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductRowKey() {
        return productRowKey;
    }

    public void setProductRowKey(String productRowKey) {
        this.productRowKey = productRowKey;
    }
}
