package com.slppp.app.modular.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slppp.app.core.rpc.Api;
import com.slppp.app.core.util.UnicodeUtil;
import com.slppp.app.modular.system.dao.*;
import com.slppp.app.modular.system.model.*;
import com.slppp.app.modular.system.service.DecodeService;
import com.slppp.app.modular.system.service.TokenAnalyzingService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class TokenAnalyzingServiceImpl implements TokenAnalyzingService {

    @Resource
    private TokenAssetsMapper tokenAssetsMapper;

    @Resource
    private SlpSendMapper slpSendMapper;

    @Resource
    private UtxoTokenMapper utxoTokenMapper;

    @Resource
    private TokenDestructionMapper tokenDestructionMapper;

    @Resource
    private AddressHashLinkMapper addressHashLinkMapper;

    @Resource
    private DecodeService decodeService;

    @Resource
    private GenesisAddressMapper genesisAddressMapper;

    @Override
    public void Bolck(JSONArray jsonArray) {

        try {

            if (jsonArray.size() > 0) {

                for (Object j : jsonArray) {

                    String txid = (String) j;

                    List<TokenAssets> tokenAssetsList = tokenAssetsMapper.selectByTxid(txid);


                    if (tokenAssetsList != null && tokenAssetsList.size() > 0)
                        continue;

                    try {
                        addressHash(j);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONObject txHex = new JSONObject();

                    try {
                        txHex = Api.GetRawTransaction(txid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray vouts = txHex.getJSONArray("vout");

                    JSONArray vins = txHex.getJSONArray("vin");
                    List<TokenAssets> tokenAssetss = new ArrayList<>();

                    try {
                        tokenAssetss = vins(vins);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Long time = txHex.getLong("time");

                    Map<Integer, String> map = vouts(vouts);

                    boolean flag = false;           // 销毁立flag, 如果最后是false并且当前的vin包含token，则销毁

                    Map<Integer, BigInteger> hashmap = new HashedMap();
                    hashmap.put(1,new BigInteger("0"));

                    List<SlpSend> SlpSendList = new ArrayList<>();
                    List<TokenAssets> TokenAssetsList = new ArrayList<>();
                    List<UtxoToken> utxoTokenList = new ArrayList<>();
                    boolean sendFlag = false;
                    for (Object v : vouts) {

                        JSONObject vout = (JSONObject) v;
                        JSONObject scriptPubKey = vout.getJSONObject("scriptPubKey");
                        String open_hex = scriptPubKey.getString("hex");
                        Integer n = vout.getInteger("n");


                        if (open_hex.contains("76a914")) {
                            String hex = open_hex.replaceFirst("76a914", "");
                            String hexStr = hex.substring(0, 40);
                            String first = hex.replaceFirst(hexStr + "88ac", "");

                            if ("".equals(hexStr))
                                continue;

                            if ("".equals(first))
                                continue;

                            String OP_RETURN = first.replaceFirst("6a06534c502b2b00020201", "");

                            if ("".equals(OP_RETURN))
                                continue;

                            String content = OP_RETURN.substring(0, 2);


                            Integer token_type = UnicodeUtil.decodeHEX(content);
                            OP_RETURN = OP_RETURN.replaceFirst(content, "");

                            if (token_type * 2 > OP_RETURN.length())
                                continue;

                            String token_type_str = OP_RETURN.substring(0, token_type * 2);
                            OP_RETURN = OP_RETURN.replaceFirst(token_type_str, "");

                            if ("47454e45534953".equals(token_type_str)) {

                                String tokenid = "";

                                try {
                                    tokenid = UnicodeUtil.getSHA256(open_hex);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String vouthex = scriptPubKey.getString("hex");
                                String value = vout.getBigDecimal("value").toString();

                                boolean bl = false;

                                try {
                                    bl = decodeService.decodeGenesistoken(OP_RETURN, map, hexStr, tokenid, txid, n, vouthex, value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (bl)
                                    flag = true;
                                else {
                                    flag = false;
                                    break;
                                }

                            } else if ("4d494e54".equals(token_type_str)) {

                                String vouthex = scriptPubKey.getString("hex");
                                String value = vout.getBigDecimal("value").toString();
                                boolean bl = false;

                                try {
                                    bl = decodeService.decodeMinttoken(OP_RETURN, map, hexStr, txid, n, vouthex, value, vins);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (bl)
                                    flag = true;
                                else {
                                    flag = false;
                                    break;
                                }


                            } else if ("53454e44".equals(token_type_str)) {

                                String vouthex = scriptPubKey.getString("hex");
                                String value = vout.getBigDecimal("value").toString();
                                Boolean f = decodeService.decodeSnedToken(OP_RETURN, hexStr, n, vins, txid, hashmap, SlpSendList, TokenAssetsList, vouthex, value, utxoTokenList);

                                if (f || sendFlag)
                                    sendFlag = true;

                            }
                        }

                    }

                    if (sendFlag && hashmap.get(0).compareTo(hashmap.get(1)) >=0) {

                        flag = true;
                        if (SlpSendList != null) {
                            for (SlpSend s : SlpSendList) {
                                slpSendMapper.insertSlpSend(s);
                            }
                        }

                        if (TokenAssetsList != null) {
                            for (TokenAssets t : TokenAssetsList) {
                                tokenAssetsMapper.insertTokenAssets(t);
                            }
                        }

                        if (utxoTokenList != null) {
                            for (UtxoToken ut : utxoTokenList) {
                                utxoTokenMapper.insertUtxoToken(ut);
                            }
                        }

                        if (hashmap.get(0).compareTo(hashmap.get(1)) > 0) {
                            BigInteger amt = hashmap.get(0).subtract(hashmap.get(1));

                            TokenDestruction tokenDestruction = new TokenDestruction();
                            tokenDestruction.setAddress(tokenAssetss.get(0).getAddress());
                            tokenDestruction.setTxid(txid);
                            tokenDestruction.setN(tokenAssetss.get(0).getVout());
                            tokenDestructionMapper.insertTokenDestruction(tokenDestruction);
                            TokenAssets update = new TokenAssets();
                            update.setTokenId(tokenAssetss.get(0).getTokenId());
                            update.setStatus(3);
                            update.setTxid(txid);
                            update.setTime(new Date().getTime());
                            update.setToken(amt);
                            update.setAddress(tokenAssetss.get(0).getAddress());
                            tokenAssetsMapper.insertTokenAssets(update);

                        }
                    }

                    if (!flag && tokenAssetss != null) {            //销毁

                        for (TokenAssets tokenAssets : tokenAssetss) {

                            TokenDestruction tokenDestruction = new TokenDestruction();
                            tokenDestruction.setAddress(tokenAssets.getAddress());
                            tokenDestruction.setTxid(txid);
                            tokenDestruction.setN(tokenAssets.getVout());
                            tokenDestructionMapper.insertTokenDestruction(tokenDestruction);
                            TokenAssets update = new TokenAssets();
                            update.setTokenId(tokenAssets.getTokenId());
                            update.setStatus(3);
                            update.setTxid(txid);
                            update.setTime(new Date().getTime());
                            update.setToken(tokenAssets.getToken());
                            update.setAddress(tokenAssets.getAddress());
                            tokenAssetsMapper.insertTokenAssets(update);

                        }

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void addressHash(Object j) {

        try {

            String tx = (String) j;
            JSONObject transaction = null;
            try {
                transaction = Api.GetRawTransaction(tx);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONArray vouts = transaction.getJSONArray("vout");

            for (Object vout : vouts) {

                JSONObject voutJSON = (JSONObject) vout;
                JSONObject scriptPubKey = voutJSON.getJSONObject("scriptPubKey");
                JSONArray addresss = scriptPubKey.getJSONArray("addresses");

                if (addresss != null) {

                    for (Object address : addresss) {
                        String ad = address.toString();
                        String addressHash = "";
                        try {
                            addressHash = Api.ValidateAddress(ad).getString("scriptPubKey").replaceFirst("76a914", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AddressHashLink addressHashLink = addressHashLinkMapper.findByAddress(ad);
                        if (addressHashLink != null)
                            continue;
                        AddressHashLink insert = new AddressHashLink();
                        insert.setAddress(ad);
                        insert.setAddressHash(addressHash.replaceFirst("88ac", ""));
                        addressHashLinkMapper.insertAddressHashLink(insert);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public List<TokenAssets> vins(JSONArray vins) {

        try {
            List<TokenAssets> tokenAssetsList = new ArrayList<>();

            for (Object v : vins) {

                JSONObject vin = (JSONObject) v;
                String txid = vin.getString("txid");
                Integer vout = vin.getInteger("vout");
                utxoTokenMapper.deleteUtxoToken(txid, vout);
                TokenAssets tokenAssets = tokenAssetsMapper.findByTokenAssetsStatus(txid, vout, 3);
                if (tokenAssets != null)
                    tokenAssetsList.add(tokenAssets); // 状态不为3

            }

            if (tokenAssetsList != null && tokenAssetsList.size() > 0)
                return tokenAssetsList;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public Map<Integer, String> vouts(JSONArray vouts) {

        Map<Integer, String> map = new HashedMap();

        for (Object v : vouts) {

            JSONObject vout = (JSONObject) v;
            JSONObject scriptPubKey = vout.getJSONObject("scriptPubKey");
            Integer n = vout.getInteger("n");
            String addressHash = scriptPubKey.getString("hex");
            map.put(n, addressHash);

        }

        return map;

    }

    @Override
    public boolean mintVins(JSONArray vins, String tokenId) throws Exception {

        for (Object v: vins) {

            JSONObject vin = (JSONObject) v;

            JSONObject json = Api.GetRawTransaction(vin.getString("txid"));
            JSONArray vout = json.getJSONArray("vout");
            JSONObject vv = vout.getJSONObject(vin.getInteger("vout"));
            String addressHash = vv.getJSONObject("scriptPubKey").getString("hex").replaceFirst("76a914","").replaceFirst("88ac","");

            GenesisAddress genesisAddress = genesisAddressMapper.findRaiseAddress(addressHash, tokenId);

            if (genesisAddress != null)
                return true;

        }

        return false;

    }


}
