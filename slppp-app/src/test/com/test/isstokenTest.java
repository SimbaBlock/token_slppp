package com.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.alibaba.fastjson.JSONArray;
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


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GunsApplication.class)
@WebAppConfiguration
public class isstokenTest {

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
        param.put("short_name","XXXC");
        param.put("full_name","SADASD");
        param.put("url","http://www.qq.com");
        param.put("hash","asdkjnzxkcakshsdjkjqhwejhkjshada");
        param.put("precition","8");
        param.put("quantity", "1000000000000000");
        param.put("minter_address","1HEHmhZcyp86kBMG692nS62auZigdPe6QA");
        param.put("issuer_address","17cM8CBjspWDbJcytUjmuqwerbxhhSazr8");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/issueToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());


    }

    //非正常传参测试，地址不合法，minter_address，issuer_address
    @Test
    public void test2() throws Exception {

        JSONObject param = new JSONObject();
        param.put("short_name","123");
        param.put("full_name","324235");
        param.put("url","34534");
        param.put("hash","435345");
        param.put("precition","12312312");
        param.put("quantity", "34534");
        param.put("minter_address","1231");
        param.put("issuer_address","43534");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/issueToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());


    }


    //传非数值类型判断，precition，quantity
    @Test
    public void test3() throws Exception {
        JSONObject param = new JSONObject();
        param.put("short_name","sadasd");
        param.put("full_name","324235");
        param.put("url","34534");
        param.put("hash","435345");
        param.put("precition","435ASDAS345");
        param.put("quantity", "43AASDA5345");
        param.put("minter_address","1NmS6trAxRWXjaZeZLV2PcBRF6C8VhoXzq");
        param.put("issuer_address","1QEyS4o3gLyCte3zm5BGiumNNEFHM6JZyx");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/issueToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }



    //传长度过长参数：short_name， full_name， url， hash， precition， quantity
    @Test
    public void test4() throws Exception {
        JSONObject param = new JSONObject();
        param.put("short_name","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        param.put("full_name","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        param.put("url","sdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        param.put("hash","asajjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjs");
        param.put("precition","100000000000000000000000000000000000000000000000000000000000000000000000000");
        param.put("quantity", "100000000000000000000000000000000000000000000000000000000000000000000000000");
        param.put("minter_address","1NmS6trAxRWXjaZeZLV2PcBRF6C8VhoXzq");
        param.put("issuer_address","1QEyS4o3gLyCte3zm5BGiumNNEFHM6JZyx");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/issueToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }


//  param.put("short_name","asdfaswqerasdfaswqrasdfawedgsadw");
//        param.put("full_name","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
//        param.put("url","sdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
//        param.put("hash","sadjaskhdkjaHkjaakjshdakjshdkjashdaskjdsadjaskhdkjaHkjaakjshdakj");
//        param.put("precition","8");
//        param.put("quantity", "1000000000000000");
//        param.put("minter_address","1NmS6trAxRWXjaZeZLV2PcBRF6C8VhoXzq");
//        param.put("issuer_address","1QEyS4o3gLyCte3zm5BGiumNNEFHM6JZyx");
//1,"GENESIS","1000000000000000000","bbb","wwww.baidu.com", "askhdaksjdhjaskhd", 1000000, new BigInteger("1000000000000000000"), 2
    //创建token，上链
    @Test
    public void test5() throws Exception {
        JSONObject param = new JSONObject();
        param.put("short_name","1000000000000000000");
        param.put("full_name","bbb");
        param.put("url","wwww.baidu.com");
        param.put("hash","askhdaksjdhjaskhd");
        param.put("precition","8");
        param.put("quantity", "1000000000000000");
        param.put("minter_address","1PvDyUGJL6pTuHfNPFyawfisFwPSfKJrce");
        param.put("issuer_address","12VBfNac7xTwDWH7YXjMi2NVJ61uDjZimc");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/issueToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        JSONObject jsonObject = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());

        Integer code = jsonObject.getInteger("code");

        JSONObject data = jsonObject.getJSONObject("data");

       if (code == 200) {

           String hex = data.getString("hex");

           JSONObject sendQaram = new JSONObject();
           sendQaram.put("hex", hex);
           sendQaram.put("type", "issue");

           MvcResult sendTransaction = mockMvc.perform(
                   post("/rest/Api/broadcastTx")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(sendQaram.toString())).andReturn();

           System.out.println(sendTransaction.getResponse().getContentAsString());

       }


    }


    @Test
    public void test6() throws Exception {
        JSONObject param = new JSONObject();
        param.put("token_id","8d9362070ff78f3f5f384ec6f95413718010b782723633d80ee577a3bb424c17");
        param.put("mint_total", "1000000000000000");
        param.put("minter_address", "1QEyS4o3gLyCte3zm5BGiumNNEFHM6JZyx");
        param.put("issuer_address", "1PvDyUGJL6pTuHfNPFyawfisFwPSfKJrce");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/mintToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        JSONObject jsonObject = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());

        Integer code = jsonObject.getInteger("code");

        JSONObject data = jsonObject.getJSONObject("data");

        if (code == 200) {

            String hex = data.getString("hex");

            JSONObject sendQaram = new JSONObject();
            sendQaram.put("hex", hex);
            sendQaram.put("type", "mint");

            MvcResult sendTransaction = mockMvc.perform(
                    post("/rest/Api/broadcastTx")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(sendQaram.toString())).andReturn();

            System.out.println(sendTransaction.getResponse().getContentAsString());

        }


    }

    @Test
    public void test7() throws Exception {
        JSONObject param = new JSONObject();
        param.put("token_id","8d9362070ff78f3f5f384ec6f95413718010b782723633d80ee577a3bb424c17");
        JSONArray jsons = new JSONArray();

        JSONObject json1 = new JSONObject();
        json1.put("account_address", "1QEyS4o3gLyCte3zm5BGiumNNEFHM6JZyx");
        json1.put("amount", "200000000000000");
        jsons.add(json1);

        JSONObject json2 = new JSONObject();
        json2.put("account_address", "1NmS6trAxRWXjaZeZLV2PcBRF6C8VhoXzq");
        json2.put("amount", "600000000000000");
        jsons.add(json2);

        param.put("transfer_to", jsons);
        param.put("transfer_from_address","1PvDyUGJL6pTuHfNPFyawfisFwPSfKJrce");

        MvcResult mvcResult = mockMvc.perform(
                post("/rest/Api/sendToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        JSONObject jsonObject = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());

        Integer code = jsonObject.getInteger("code");

        JSONObject data = jsonObject.getJSONObject("data");

        if (code == 200) {

            String hex = data.getString("hex");

            JSONObject sendQaram = new JSONObject();
            sendQaram.put("hex", hex);
            sendQaram.put("type", "send");

            MvcResult sendTransaction = mockMvc.perform(
                    post("/rest/Api/broadcastTx")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(sendQaram.toString())).andReturn();

            System.out.println(sendTransaction.getResponse().getContentAsString());

        }


    }




}
