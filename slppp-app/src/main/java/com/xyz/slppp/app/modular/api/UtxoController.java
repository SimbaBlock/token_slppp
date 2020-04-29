package com.xyz.slppp.app.modular.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyz.slppp.app.core.util.HttpUtil;
import com.xyz.slppp.app.core.util.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api/utxo")
public class UtxoController {


    @ResponseBody
    @RequestMapping(value="/getUtxo", method = RequestMethod.POST)
    public JsonResult getUtxo(String address) throws Exception {

        JSONObject params = new JSONObject();
        params.put("jsonrpc","1.0");
        params.put("id","curltest");
        params.put("method","getutxo");

        JSONArray jsons = new JSONArray();
        jsons.add(address);
        params.put("params", jsons);

        String utxos = HttpUtil.doPost("http://47.110.137.123:8666/", params.toJSONString());

        JSONObject data = (JSONObject) JSONObject.parse(utxos);
        JSONArray datas = data.getJSONArray("result");

        return new JsonResult().addData("list", datas);

    }


}
