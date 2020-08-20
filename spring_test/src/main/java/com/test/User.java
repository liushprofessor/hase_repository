package com.test;

import java.io.Serializable;

/**
 * @author Liush
 * @description
 * @date 2020/8/20 16:19
 **/
public class User implements Serializable {


    private String id;

    private String name;


    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
