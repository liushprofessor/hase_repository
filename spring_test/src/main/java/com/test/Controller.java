package com.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liush
 * @description
 * @date 2020/8/19 21:07
 **/
@RestController
public class Controller {





    @PostMapping("/addUser")
    public User addUser(User user){


         return user;
    }


    @PostMapping("/updateUser")
    public User updateUser(@RequestBody User user){

    return user;
    }





}
