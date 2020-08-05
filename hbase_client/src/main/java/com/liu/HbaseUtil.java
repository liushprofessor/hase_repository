
package com.liu;

import com.google.common.base.Splitter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/6/29    Liush     初版
 * ──────────────────────────────────────────
 */
public class HbaseUtil {


    private Admin admin;
    private Configuration configuration;
    private Connection connection;

    public HbaseUtil() {
        configuration= HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","192.168.171.116");
        try {
              connection =ConnectionFactory.createConnection(configuration);
             admin=connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean existTable(String tableName) throws IOException {
       return admin.tableExists(TableName.valueOf(tableName));
    }


    public void createTable(String  name) throws IOException {
        TableName tableName=TableName.valueOf(name);
        HTableDescriptor descriptor=new HTableDescriptor(tableName);
        HColumnDescriptor colum=new HColumnDescriptor("data");
        descriptor.addFamily(colum);
        admin.createTable(descriptor);

    }

    public void addAliexpressProductDetail(String webUrl,String rowkey,String title,String imgUrls,String rangePrice,String overview,String specification,String sku,String sku2,String sku3,String price,String imgDetailUrl) throws IOException {

        Table table=connection.getTable(TableName.valueOf("aliproduct2"));
        addTitle(rowkey, title, table);
        addImgUrls(rowkey,imgUrls,table);
        addRangePrice(rowkey, rangePrice, table);
        addOverview(rowkey, overview, table);
        addSpecification(rowkey, specification, table);
        addSku(rowkey, sku, table);
        addSku2(rowkey, sku2, table);
        addWebUrl(rowkey,webUrl,table);
        addSku3(rowkey,sku3,table);
        if(price==null){
            price=rangePrice;
            addPrice(rowkey, price, table);
        }else {
            addPrice(rowkey, price, table);
        }
        imgDetailUrl(rowkey,imgDetailUrl,table);

    }



    private void imgDetailUrl(String rowkey,String detailUrl,Table hTable) throws IOException {
        if(null==detailUrl){
            return;
        }
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("detailUrl");
        byte[] value=Bytes.toBytes(detailUrl);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);

    }


    private void addWebUrl(String rowkey,String webUrl,Table hTable)throws IOException{
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("webUrl");
        byte[] value=Bytes.toBytes(webUrl);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);


    }

    private void addSku3(String rowkey,String sku3,Table hTable)throws IOException{
        if(sku3==null){
            return;
        }
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("sku3");
        byte[] value=Bytes.toBytes(sku3);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);


    }


    private void addSku2(String rowkey,String sku2,Table hTable) throws IOException {
        if(sku2==null){
            return;
        }
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("sku2");
        byte[] value=Bytes.toBytes(sku2);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);


    }

    private void addSku(String rowkey,String sku,Table hTable) throws IOException {
        if(sku==null){
            return;
        }
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("sku");
        byte[] value=Bytes.toBytes(sku);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);


    }


    private void addSpecification(String rowkey,String specification,Table hTable) throws IOException {
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("specification");
        byte[] value=Bytes.toBytes(specification);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);

    }


    private void addOverview(String rowkey,String overview,Table hTable) throws IOException {

        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("overview");
        byte[] value=Bytes.toBytes(overview);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);

    }


    private void addRangePrice(String rowkey,String rangePrice,Table hTable) throws IOException {
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("rangePrice");
        byte[] value=Bytes.toBytes(rangePrice);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);

    }


    public void addImgUrls(String rowkey,String imgUrls,Table hTable) throws IOException {
        Splitter split = Splitter.on(',').trimResults().omitEmptyStrings();
        int i=0;
        for(String imgUrl:split.split(imgUrls)){
            byte[] row= Bytes.toBytes(rowkey);
            byte[] columnFamily=Bytes.toBytes("data");
            byte[] qualifier=Bytes.toBytes("imgurl"+i);
            byte[] value=Bytes.toBytes(imgUrl);
            Put put=new Put(row);
            put.addColumn(columnFamily,qualifier,value);
            hTable.put(put);
            i++;




        }

    }


    public void addProduct(String rowkey,String title,String price,String sellNum,String shopName,String url) throws IOException {

        Table table=connection.getTable(TableName.valueOf("taobaoList"));
        addTitle(rowkey, title, table);
        addPrice(rowkey, price, table);
        addSellNum(rowkey, sellNum, table);
        addShopName(rowkey, shopName, table);
        addUrl(rowkey, url, table);
    }



    public void addArea(String rowkey,String code,String sheng,String di,String xian)throws IOException {
        Table table=connection.getTable(TableName.valueOf("AREA"));
        addSheng(rowkey,sheng,table);
        addDi(rowkey,di,table);
        addXian(rowkey,xian,table);
        addCode(rowkey,code,table);
    }

    private void addCode(String rowkey,String code,Table table) throws IOException {

        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("CODE"),Bytes.toBytes(code));
        table.put(put);
    }

    private void addSheng(String rowkey,String sheng,Table table) throws IOException {
        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("SHENG"),Bytes.toBytes(sheng));
        table.put(put);
    }

    private void addDi(String rowkey,String di,Table table) throws IOException {
        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("DI"),Bytes.toBytes(di));
        table.put(put);
    }


    private void addXian(String rowkey,String xian,Table table) throws IOException {
        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("XIAN"),Bytes.toBytes(xian));
        table.put(put);
    }




    public void addProductDetailData(String rowKey,String productRowKey,String titleDetail,String describe,String brand) throws IOException {

        Table table=connection.getTable(TableName.valueOf("taobaoDetailList"));
        addroductRowKey(rowKey,productRowKey,table);
        addTitleDetail(rowKey,titleDetail,table);
        addDescribe( rowKey, describe,table);
        addBrand( rowKey, brand, table);
    }

    private void addroductRowKey(String rowkey,String productRowKey,Table table) throws IOException {
        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("productRowKey"),Bytes.toBytes(productRowKey));
        table.put(put);
    }
    private void addTitleDetail(String rowkey,String titleDetail,Table table) throws IOException {
        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("titleDetail"),Bytes.toBytes(titleDetail));
        table.put(put);
    }

    private void addDescribe(String rowkey,String describe,Table table) throws IOException {
        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("describe"),Bytes.toBytes(describe));
        table.put(put);
    }

    private void addBrand(String rowkey,String brand,Table table) throws IOException {
        Put put=new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("data"),Bytes.toBytes("brand"),Bytes.toBytes(brand));
        table.put(put);
    }




    public Product getProduct(String rowkey) throws IOException, NoSuchFieldException, IllegalAccessException {
        Table table=connection.getTable(TableName.valueOf("taobaoList"));
        Get get=new Get(Bytes.toBytes(rowkey));
        Result result=table.get(get);
        List<Cell> cells=result.listCells();
        if(cells==null){
            return null;
        }
        Product product=new Product();
        String rowkey2=Bytes.toString(result.getRow());
        Field field2=product.getClass().getDeclaredField("rowKey");
        field2.setAccessible(true);
        field2.set(product,rowkey2);
        for(Cell cell:cells){
            System.out.println(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
            Field name=product.getClass().getDeclaredField(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
            name.setAccessible(true);
            name.set(product,Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));

        }
        return product;
    }


    public List<Product> scanProduct(String rowkeyStart,String rowkeyEnd,int size) throws IOException, NoSuchFieldException, IllegalAccessException {
        Scan scan=new Scan();
        scan.setReversed(false);
        FilterList filterList=new FilterList();
        Filter greater=new RowFilter(CompareFilter.CompareOp.GREATER,new BinaryComparator(Bytes.toBytes(rowkeyStart)));
        Filter less=new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL,new BinaryComparator(Bytes.toBytes(rowkeyEnd)));
        Filter pageFilter=new PageFilter(size);
        filterList.addFilter(greater);
        filterList.addFilter(less);
        filterList.addFilter(pageFilter);
        scan.setFilter(filterList);
        Table table=connection.getTable(TableName.valueOf("taobaoList"));
        ResultScanner results=table.getScanner(scan);
        if(results==null){
            return null;
        }
        List<Product> products=new ArrayList<Product>();
        for(Result result :results){
            String rowkey2=Bytes.toString(result.getRow());
            List<Cell> cells=result.listCells();
            Product product=new Product();
            Field field2=product.getClass().getDeclaredField("rowKey");
            field2.setAccessible(true);
            field2.set(product,rowkey2);
            for(Cell cell:cells){
                String key=Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                String value=Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength());
                Field field=product.getClass().getDeclaredField(key);
                field.setAccessible(true);
                field.set(product,value);
            }
            products.add(product);

        }

        return products;
    }



    private void addUrl(String rowkey, String url, Table hTable) throws IOException {
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("url");
        byte[] value=Bytes.toBytes(url);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);
    }


    private void addTitle(String rowkey, String title, Table hTable) throws IOException {
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("title");
        byte[] value=Bytes.toBytes(title);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);
    }

    private void addPrice(String rowkey, String price, Table hTable) throws IOException {
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("price");
        byte[] value=Bytes.toBytes(price);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);
    }

    private void addSellNum(String rowkey, String sellNum, Table hTable) throws IOException{
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("sellNum");
        byte[] value=Bytes.toBytes(sellNum);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);
    }

    private void addShopName(String rowkey, String shopName, Table hTable) throws IOException {
        byte[] row= Bytes.toBytes(rowkey);
        byte[] columnFamily=Bytes.toBytes("data");
        byte[] qualifier=Bytes.toBytes("shopName");
        byte[] value=Bytes.toBytes(shopName);
        Put put=new Put(row);
        put.addColumn(columnFamily,qualifier,value);
        hTable.put(put);
    }


}
