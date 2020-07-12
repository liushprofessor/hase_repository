
package com.liu;

import java.sql.*;

/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/2    Liush     初版
 * ──────────────────────────────────────────
 */
public class PhoenixTest {



    public static void main(String[] args) throws SQLException {

        //jdbc:phoenix [ :<zookeeper quorum> [ :<port number> [ :<root node> [ :<principal> [ :<keytab file> ] ] ] ] ]
        Connection connection=DriverManager.getConnection("jdbc:phoenix:hztnode1:2181","root","root");
        PreparedStatement statement =connection.prepareStatement(" select * from 'taobaoList'");
        ResultSet resultSet=statement.executeQuery();

        while (resultSet.next()){

           System.out.println( resultSet.getString("STATE"));
           System.out.println(resultSet.getString("CITY"));
           System.out.println(resultSet.getString("POPULATION"));


        }


    	}






}
