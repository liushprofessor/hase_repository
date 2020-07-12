
package com.liu;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/4    Liush     初版
 * ──────────────────────────────────────────
 */
public class ImageSKU implements SKU {


    private String url;


    private String sku;

    private String price;


    public ImageSKU(String url, String sku, String price) {
        this.url = url;
        this.sku = sku;
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
