package com.unuldur.uminc.connection;

/**
 * Created by julie on 11/12/2017.
 */

public interface IConnection {
    String getPage(String address);
    String getPage(String address, String username, String password);
    String getPage(String address,String type, String username, String password,int depth);
}
