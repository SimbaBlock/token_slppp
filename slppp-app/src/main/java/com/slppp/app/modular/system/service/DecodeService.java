package com.slppp.app.modular.system.service;

import com.alibaba.fastjson.JSONArray;
import com.slppp.app.modular.system.model.SlpSend;
import com.slppp.app.modular.system.model.TokenAssets;
import com.slppp.app.modular.system.model.UtxoToken;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface DecodeService {

    public Boolean decodeGenesistoken(String content, Map<Integer, String> map, String hexStr, String tokenId, String txid, Integer n, String vouthex, String value);

    public Boolean decodeMinttoken(String content, Map<Integer, String> map, String hexStr, String tx, Integer n, String vouthex, String value, JSONArray vins);

    public Boolean decodeSnedToken(String content, String toAddressHash, Integer n, JSONArray vins, String tx,
                                   Map<Integer, BigInteger> hashmap, List<SlpSend> SlpSendList, List<TokenAssets> TokenAssetsList,
                                   String vouthex, String value, List<UtxoToken> UtxoTokenList);
}
