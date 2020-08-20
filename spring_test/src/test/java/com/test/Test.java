package com.test;


import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;


/**
 * @author Liush
 * @description
 * @date 2020/8/19 21:08
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {



    @Autowired
    private WebApplicationContext webApplicationContext;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");


    private MockMvc mockMvc;


    @Before
    public void setUp(){

        mockMvc=MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(documentationConfiguration(restDocumentation)).build();

    }

    /**
     * requestFields 可以理解为添加的示例数据入{"id":"222","name":"Liush"}
     * responseFields 同理
     * requestParameters 为入参数,使用此方法添加后会生成入场表格文件
     */

    @org.junit.Test
    public void addUserTest()throws Exception{
        mockMvc.perform(post("/addUser").accept(MediaType.APPLICATION_JSON).param("id","1").
            param("name","liush").header("token","token")
        ).andExpect(jsonPath("$.id",is("1"))).andExpect(jsonPath("$.name",is("liush"))).
        andExpect(status().isOk()).andDo(document("addUser",
                requestHeaders(headerWithName("token").description("校验token")),
                requestParameters(parameterWithName("id").description("用户id"),
                        parameterWithName("name").description("用户名")
                        ),
                responseFields(fieldWithPath("id").description("用户id"),
                        fieldWithPath("name").description("用户名")

                        )
                ));

    }


    @org.junit.Test
    public void updateUser() throws Exception {
        User user=new User("222","Liush");
        mockMvc.perform(post("/updateUser").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(user)))
        .andExpect(jsonPath("$.id",is("222"))).andExpect(status().isOk()).
                andDo(document("updateUser",
                        requestFields(fieldWithPath("id").description("用户id"),
                                fieldWithPath("name").description("用户名")

                ), responseFields(fieldWithPath("id").description("用户id"),fieldWithPath("name").description("用户名"))

                ))

        ;


    }








}
