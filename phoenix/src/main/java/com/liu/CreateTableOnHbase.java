
package com.liu;

import java.sql.*;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/6    Liush     初版
 * ──────────────────────────────────────────
 */
public class CreateTableOnHbase {


    public static void main(String[] args) throws SQLException {

        addDataIntoProducts();


    	}


    private static void addDataIntoProducts() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:phoenix:hztnode1:2181","root","root");
        PreparedStatement statement=connection.prepareStatement("upsert into taobaoproducts(id,shopname,title,url,sellnum,brand,describe) select T1.\"id\", T1.\"shopName\" , T1.\"title\"  ,T1.\"url\",T1.\"sellNum\",T2.\"brand\",T2.\"describe\"  " +
                "                from \"taobaoList\" as T1 " +
                "                 inner join" +
                "                 \"taobaoDetailList\" as T2" +
                "                 ON T1.\"id\"=T2.\"productRowKey\"");

        System.out.println(statement.executeUpdate());
        connection.commit();

    }


    private static void innerJoin() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:phoenix:hztnode1:2181","root","root");
        PreparedStatement statement=connection.prepareStatement("select T1.\"title\" AS TITLE,T1.\"sellNum\" AS SELLNUM,T1.\"shopName\" AS SHOP,T1.\"price\" AS PRICE" +
                " from \"taobaoList\" as T1" +
                " inner join" +
                " \"taobaoDetailList\" as T2" +
                " ON T1.\"id\"=T2.\"productRowKey\" WHERE T1.\"id\"='100001594042362527'");
        //插入和查询字符串只能是单引号，字段默认都是大写，需要加转义"不能是'
        ResultSet set =statement.executeQuery();
        while (set.next()){
            System.out.print(set.getString("TITLE")+"-------"+set.getString("SELLNUM")
            +set.getString("SHOP")+"--------"+set.getString("PRICE"));
            System.out.println();


        }
    }


    private static void createListInnerJoinProduct() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:phoenix:hztnode1:2181","root","root");
        PreparedStatement statement=connection.prepareStatement("create table taobaoproducts(id varchar primary key,shopname varchar ,title varchar,url varchar,sellnum varchar,brand varchar,describe varchar)");
        statement.execute();



    }


    private static void createTaobaoListView() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:phoenix:hztnode1:2181","root","root");
        PreparedStatement statement=connection.prepareStatement("create VIEW \"taobaoList\" (\"id\" VARCHAR PRIMARY KEY ,\"data\".\"sellNum\" VARCHAR, " +
                "\"data\".\"title\" VARCHAR ," +
                "\"data\".\"url\" VARCHAR,"+
                "\"data\".\"shopName\" VARCHAR,"+
                "\"data\".\"price\" VARCHAR"+
                ") column_encoded_bytes=0");
        statement.execute();
    }

    private static void createTaobaoDetailList() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:phoenix:hztnode1:2181","root","root");

        PreparedStatement statement=connection.prepareStatement("create VIEW \"taobaoDetailList\" (\"id\" VARCHAR PRIMARY KEY ,\"data\".\"titleDetail\" VARCHAR, " +
                "\"data\".\"describe\" VARCHAR ," +
                "\"data\".\"productRowKey\" VARCHAR,"+
                "\"data\".\"brand\" VARCHAR"+
                ") column_encoded_bytes=0");

        System.out.println(statement.execute());
    }


}
