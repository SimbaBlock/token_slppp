package com.test;

import com.alibaba.fastjson.JSONObject;
import com.xyz.slppp.app.GunsApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GunsApplication.class)
@WebAppConfiguration
public class MintTokenTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    //正常传参测试
    @Test
    public void test1() throws Exception {

        JSONObject param = new JSONObject();
        param.put("token_id","becec790a9bb1f68f1ac7bf63c6498c19eab04974551e518e6a0310ed55d4676");
        param.put("mint_total","1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        param.put("minter_address","19VvkL2jZctXr7NQEWwuSyXMAWGzMCzFba");
        param.put("issuer_address","1LQ5kvQUoE1iat3aVkjRkmj6nwvntkgz1m");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/mintToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }



}
