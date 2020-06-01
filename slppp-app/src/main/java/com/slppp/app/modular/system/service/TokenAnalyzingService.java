package com.slppp.app.modular.system.service;

import com.alibaba.fastjson.JSONArray;
import com.slppp.app.modular.system.model.TokenAssets;

import java.util.List;
import java.util.Map;

public interface TokenAnalyzingService {

    public void Bolck(JSONArray jsonArray);

    public void addressHash(Object j);

    public List<TokenAssets> vins(JSONArray vins);

    public Map<Integer, String> vouts(JSONArray vouts);

    public boolean mintVins(JSONArray vins, String tokenId) throws Exception;

}

