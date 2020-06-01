package com.slppp.app.modular.system.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slppp.app.core.rpc.Api;
import com.slppp.app.modular.system.model.*;
import com.slppp.app.modular.system.service.*;
import com.slppp.app.core.common.annotion.TimeStat;
import com.slppp.app.core.util.UnicodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
@ConditionalOnProperty(prefix = "guns.scheduler-switch", name = "signature", havingValue = "true")
@Slf4j
public class SignatureScheduler {

    @Autowired
    private SignatureScheduler self;

    @Autowired
    private BlockCountService blockCountService;

    @Autowired
    private AddressHashLinkService addressHashLinkService;

    @Autowired
    private SlpService slpService;

    @Autowired
    private GenesisAddressService genesisAddressService;

    @Autowired
    private SlpMintService slpMintService;

    @Autowired
    private SlpSendService slpSendService;

    @Autowired
    private TokenAssetsService tokenAssetsService;

    @Autowired
    private TokenDestructionService tokenDestructionService;

    @Autowired
    private UtxoTokenService utxoTokenService;

    @Autowired
    private ScriptSlpService scriptSlpService;

    @Autowired
    private ScriptTokenLinkService scriptTokenLinkService;

    @Autowired
    private ScriptUtxoTokenLinkService scriptUtxoTokenLinkService;

    @Autowired
    private AddressScriptLinkService addressScriptLinkService;

    @Autowired
    private ScriptSlpMintService scriptSlpMintService;

    @Autowired
    private ScriptSlpSendService scriptSlpSendService;

    @Autowired
    private ScriptTokenDestructionService scriptTokenDestructionService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void work() {
        self.start();
    }

    Boolean sendFlag = false;

    List<TokenAssets> tokenAssetss = null;
    List<ScriptTokenLink> scriptTokenLink = null;
    List<AddressScriptLink> addressScriptLink = null;

    @TimeStat
    @Transactional(rollbackFor=Exception.class)
    public void start() {

        try {

            Integer count = blockCountService.findBlockCount();

            Integer json = 0;

            try {

                json = Api.GetBlockCount();

            } catch (Exception e) {

                e.printStackTrace();

            }


            if (count < json + 1) {
                JSONObject block = new JSONObject();

                try {
                    String blockHash = Api.GetBlockHash(count);
                    block = Api.GetBlock(blockHash);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONArray jsonArray = block.getJSONArray("tx");

                try {
                    block(jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                count ++;
                blockCountService.updateBlock(count);

            } else {
                List<String> txList = new ArrayList<>();

                try {
                    txList = Api.GetRawMemPool();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONArray jsonArray = new JSONArray();
                for (String tx: txList) {
                    jsonArray.add(tx);
                }
                try {
                    block(jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public String list67(String content, List<String> addressList, StringBuffer scrpit) {

        String if67 = content.substring(0, 2);

        content = content.replaceFirst(if67, "");

        if ("67".equals(if67)) {

            String oph1 = content.substring(0, 6);

            scrpit.append(oph1);

            if (!"76a914".equals(oph1)) {
                return null;
            }

            content = content.replaceFirst(oph1, "");
            String address2 = content.substring(0, 40);
            addressList.add(address2);
            scrpit.append(address2);

            content = content.replaceFirst(address2, "");

            String opa = content.substring(0, 4);

            if (!"88ac".equals(opa)) {
                return null;
            }

            scrpit.append(opa);

            content = content.replaceFirst(opa, "");

            return content;

        }

        return null;

    }

    public Integer decode(Integer type, JSONObject scriptPubKey, JSONObject vout, Integer n, String txid, Map map, JSONArray vins, Map hashmap, List<Object> SlpSendList,
                         List<Object> TokenAssetsList, List<Object> utxoTokenList, Boolean flag, String first, String hexStr, List<String> addressList, List<AddressScriptLink> addressScriptLink) {

        if ("".equals(first))
            return 1;  // continue

        first = first.replaceFirst("6a", "");

        String leng_hex = first.substring(0,2);
        first = first.replaceFirst(leng_hex, "");

        String OP_RETURN = "";

        if ("4c".equals(leng_hex)) {

            String length_hex = first.substring(0, 2);
            Integer length = UnicodeUtil.decodeHEX(length_hex);
            first = first.replaceFirst(length_hex, "");
            OP_RETURN = first.substring(0, length*2);
            first = first.replaceFirst(OP_RETURN,"");
            if (!"".equals(first))
                return 1;  // continue

        } else if ("4d".equals(leng_hex)) {

            String length_hex = first.substring(0, 4);
            Integer length = UnicodeUtil.decodeHEX(length_hex);
            first = first.replaceFirst(length_hex, "");
            OP_RETURN = first.substring(0, length*2);
            first = first.replaceFirst(OP_RETURN,"");
            if (!"".equals(first))
                return 1;  // continue

        } else if ("4e".equals(leng_hex)) {

            String length_hex = first.substring(0, 6);
            Integer length = UnicodeUtil.decodeHEX(length_hex);
            first = first.replaceFirst(length_hex, "");
            OP_RETURN = first.substring(0, length*2);
            first = first.replaceFirst(OP_RETURN,"");
            if (!"".equals(first))
                return 1;  // continue

        } else {

            Integer length = UnicodeUtil.decodeHEX(leng_hex);
            first = first.replaceFirst(leng_hex, "");
            OP_RETURN = first.substring(0, length*2);
            first = first.replaceFirst(OP_RETURN,"");
            if (!"".equals(first))
                return 1;  // continue

        }

        String slpp = OP_RETURN.substring(0, 20);

        if (!"06534c502b2b00020201".equals(slpp)) {
            return 1;  // continue
        }

        OP_RETURN = OP_RETURN.replaceFirst("06534c502b2b00020201", "");

        if ("".equals(OP_RETURN))
            return 1;  // continue

        String content = OP_RETURN.substring(0, 2);


        Integer token_type = UnicodeUtil.decodeHEX(content);
        OP_RETURN = OP_RETURN.replaceFirst(content, "");

        if (token_type * 2 > OP_RETURN.length())
            return 1;  // continue

        String token_type_str = OP_RETURN.substring(0, token_type * 2);
        OP_RETURN = OP_RETURN.replaceFirst(token_type_str, "");

        if ("47454e45534953".equals(token_type_str)) {

            String tokenid = "";

            try {
                String nn = UnicodeUtil.intToHex(n);
                tokenid = UnicodeUtil.getSHA256(txid + nn);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String vouthex = scriptPubKey.getString("hex");
            String value = vout.getBigDecimal("value").toString();

            boolean bl = false;

            try {
                bl = decodeGenesistoken(type, OP_RETURN, map, hexStr, tokenid, txid, n, vouthex, value, addressList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bl)
                flag = true;
            else {
                flag = false;
                return 2;  // break
            }

        } else if ("4d494e54".equals(token_type_str)) {

            String vouthex = scriptPubKey.getString("hex");
            String value = vout.getBigDecimal("value").toString();
            boolean bl = false;

            try {
                bl = decodeMinttoken(type, vins, OP_RETURN, map, hexStr, txid, n, vouthex, value, addressList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bl)
                flag = true;
            else {
                flag = false;
                return 2;  // break
            }

        } else if ("53454e44".equals(token_type_str)) {

            String vouthex = scriptPubKey.getString("hex");
            String value = vout.getBigDecimal("value").toString();
            Boolean f = decodeSnedToken(type, OP_RETURN, hexStr, n, vins, txid, hashmap, SlpSendList, TokenAssetsList, vouthex, value, utxoTokenList, addressList, addressScriptLink);

            if (f || sendFlag)
                sendFlag = true;

            if (sendFlag)
                flag = false;

        }

        if (flag)
            return 3;  //false
        else
            return 4; // true
    }

    @Transactional(rollbackFor=Exception.class)
    public void block(JSONArray jsonArray) {

        try {

            if (jsonArray.size() > 0) {

                for (Object j : jsonArray) {

                    String txid = (String) j;

                    List<TokenAssets> tokenAssetsList = tokenAssetsService.selectByTxid(txid);

                    if (tokenAssetsList != null && tokenAssetsList.size() > 0)
                        continue;

                    List<ScriptTokenLink> scriptTokenLinkList = scriptTokenLinkService.selectByTxid(txid);

                    if (scriptTokenLinkList != null && scriptTokenLinkList.size() > 0)
                        continue;

                    JSONObject txHex = new JSONObject();

                    try {
                        txHex = Api.GetRawTransaction(txid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        addressHash(txHex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    JSONArray vouts = txHex.getJSONArray("vout");

                    JSONArray vins = txHex.getJSONArray("vin");
                    tokenAssetss = new ArrayList<>();
                    scriptTokenLink = new ArrayList<>();
                    addressScriptLink = new ArrayList<>();

                    try {
                        tokenAssetss = tokenvins(vins);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        scriptTokenLink = scripttokenvins(vins);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Map<Integer, String> map = vouts(vouts);
                    sendFlag = false;
                    boolean flag = false;           // 销毁立flag, 如果最后是false并且当前的vin包含token，则销毁

                    Map<Integer, BigInteger> hashmap = new HashedMap();
                    hashmap.put(1, new BigInteger("0"));

                    List<Object> SlpSendList = new ArrayList<>();
                    List<Object> TokenAssetsList = new ArrayList<>();
                    List<Object> utxoTokenList = new ArrayList<>();

                    Integer type = 0;
                    for (Object v : vouts) {

                        JSONObject vout = (JSONObject) v;
                        JSONObject scriptPubKey = vout.getJSONObject("scriptPubKey");
                        String open_hex = scriptPubKey.getString("hex");
                        Integer n = vout.getInteger("n");

                        String h = open_hex.substring(0,2);

                        if ("63".equals(h)) {

                            List<String> addressList = new ArrayList<>();

                            StringBuffer scrpit = new StringBuffer();
                            open_hex = open_hex.replaceFirst(h, "");
                            String oph = open_hex.substring(0, 6);
                            scrpit.append(h);
                            scrpit.append(oph);
                            if (!"76a914".equals(oph)) {
                                continue;
                            }
                            open_hex = open_hex.replaceFirst(oph, "");

                            String address1 = open_hex.substring(0, 40);
                            addressList.add(address1);
                            scrpit.append(address1);

                            open_hex = open_hex.replaceFirst(address1, "");

                            String opa = open_hex.substring(0, 4);
                            scrpit.append(opa);

                            if (!"88ac".equals(opa)) {
                                continue;
                            }

                            open_hex = open_hex.replaceFirst(opa, "");

                            String if67 = open_hex.substring(0, 2);
                            scrpit.append(if67);
                            Boolean falg = false;

                            if ("67".equals(if67)) {

                                String fs = list67(open_hex, addressList, scrpit);

                                if (fs == null)
                                    continue;
                                else {

                                    open_hex = fs;

                                    while (true) {
                                        fs = list67(open_hex, addressList, scrpit);
                                        if (fs == null)
                                            break;
                                        open_hex = fs;
                                        fs = open_hex.substring(0, 2);

                                        if (!"67".equals(fs))
                                            break;
                                    }

                                    if (open_hex == null)
                                        continue;
                                }
                                falg = true;
                            }

                            String a68 = open_hex.substring(0, 2);

                            scrpit.append(a68);

                            if (!"68".equals(a68) && !falg){
                                continue;
                            }

                            open_hex = open_hex.replaceFirst(a68, "");

                            type = 2;
                            Integer f = decode(type, scriptPubKey, vout, n, txid, map, vins, hashmap, SlpSendList,
                                    TokenAssetsList, utxoTokenList, flag, open_hex, scrpit.toString(), addressList, addressScriptLink);


                            if (f == 1) {
                                continue;
                            } else if (f == 2) {
                                break;
                            } else if (f == 3) {
                                flag = false;
                            } else if (f == 4) {
                                flag = true;
                            }


                        } else if (h.contains("76")) {

                            String hex = open_hex.replaceFirst("76a914", "");
                            String hexStr = hex.substring(0, 40);
                            open_hex = hex.replaceFirst(hexStr + "88ac", "");

                            type = 1;
                            Integer f = decode(1, scriptPubKey, vout, n, txid, map, vins, hashmap, SlpSendList,
                                    TokenAssetsList, utxoTokenList, flag, open_hex, hexStr,  null, null);

                            if (f == 1) {
                                continue;
                            } else if (f == 2) {
                                break;
                            } else if (f == 3) {
                                flag = false;
                            } else if (f == 4) {
                                flag = true;
                            }

                        }

                    }

                    if (sendFlag && hashmap.get(0).compareTo(hashmap.get(1)) >= 0) {

                        if (SlpSendList != null) {
                            for (Object s : SlpSendList) {
                                if (s instanceof SlpSend) {
                                    SlpSend ss = (SlpSend)s;
                                    slpSendService.insertSlpSend(ss);
                                } else if (s instanceof ScriptSlpSend) {
                                    ScriptSlpSend sss = (ScriptSlpSend) s;
                                    scriptSlpSendService.insertSlpSend(sss);
                                }

                            }
                        }

                        if (TokenAssetsList != null) {
                            for (Object t : TokenAssetsList) {
                                if (t instanceof TokenAssets) {
                                    TokenAssets ta = (TokenAssets)t;
                                    tokenAssetsService.insertTokenAssets(ta);
                                } else if (t instanceof ScriptTokenLink) {
                                    ScriptTokenLink sctl = (ScriptTokenLink) t;
                                    scriptTokenLinkService.insert(sctl);
                                }
                            }
                        }

                        if (utxoTokenList != null) {
                            for (Object ut : utxoTokenList) {
                                if (ut instanceof UtxoToken) {
                                    UtxoToken utl = (UtxoToken) ut;
                                    utxoTokenService.insertUtxoToken(utl);
                                } else if (ut instanceof ScriptUtxoTokenLink) {
                                    ScriptUtxoTokenLink sutl = (ScriptUtxoTokenLink) ut;
                                    scriptUtxoTokenLinkService.insert(sutl);
                                }
                            }
                        }

                        if (addressScriptLink != null) {
                            for (AddressScriptLink asl: addressScriptLink) {
                                addressScriptLinkService.insert(asl);
                            }
                        }


                        if (hashmap.get(0).compareTo(hashmap.get(1)) > 0) {
                            BigInteger amt = hashmap.get(0).subtract(hashmap.get(1));

                            TokenDestruction tokenDestruction = new TokenDestruction();
                            tokenDestruction.setAddress(tokenAssetss.get(0).getAddress());
                            tokenDestruction.setTxid(txid);
                            tokenDestruction.setN(tokenAssetss.get(0).getVout());
                            tokenDestructionService.insertTokenDestruction(tokenDestruction);
                            TokenAssets update = new TokenAssets();
                            update.setTokenId(tokenAssetss.get(0).getTokenId());
                            update.setStatus(3);
                            update.setTxid(txid);
                            update.setTime(new Date().getTime());
                            update.setToken(amt);
                            update.setAddress(tokenAssetss.get(0).getAddress());
                            tokenAssetsService.insertTokenAssets(update);

                        }
                    }

                    if (!flag && tokenAssetss != null) {            //销毁

                        if (tokenAssetss != null) {

                            for (TokenAssets tokenAssets : tokenAssetss) {

                                TokenDestruction tokenDestruction = new TokenDestruction();
                                tokenDestruction.setAddress(tokenAssets.getAddress());
                                tokenDestruction.setTxid(txid);
                                tokenDestruction.setN(tokenAssets.getVout());
                                tokenDestructionService.insertTokenDestruction(tokenDestruction);
                                TokenAssets update = new TokenAssets();
                                update.setTokenId(tokenAssets.getTokenId());
                                update.setStatus(3);
                                update.setTxid(txid);
                                update.setTime(new Date().getTime());
                                update.setToken(tokenAssets.getToken());
                                update.setAddress(tokenAssets.getAddress());
                                tokenAssetsService.insertTokenAssets(update);

                            }

                        } else if (scriptTokenLink != null) {

                            for (ScriptTokenLink scriptToken : scriptTokenLink) {

                                ScriptTokenDestruction tokenDestruction = new ScriptTokenDestruction();
                                tokenDestruction.setScript(scriptToken.getScript());
                                tokenDestruction.setTxid(txid);
                                tokenDestruction.setN(scriptToken.getVout());
                                scriptTokenDestructionService.insert(tokenDestruction);
                                ScriptTokenLink update = new ScriptTokenLink();
                                update.setTokenId(scriptToken.getTokenId());
                                update.setStatus(3);
                                update.setTxid(txid);
                                update.setToken(scriptToken.getToken());
                                update.setScript(scriptToken.getScript());
                                scriptTokenLinkService.insert(update);

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }




    //解析发行
    @Transactional(rollbackFor=Exception.class)
    public boolean decodeGenesistoken(Integer type, String content, Map<Integer, String> map, String hexStr, String tokenId, String txid, Integer n, String vouthex, String value, List<String> addressList) {

        try {

            String token_ticker_hex = content.substring(0, 2);
            Integer token_ticker = UnicodeUtil.decodeHEX(token_ticker_hex);
            content = content.replaceFirst(token_ticker_hex, "");
            String token_ticker_str = content.substring(0, token_ticker * 2);

            content = content.replaceFirst(token_ticker_str, "");				// 清空掉token_ticker
            String tokenTicker = UnicodeUtil.hexStringToString(token_ticker_str);

            String token_name_hex = content.substring(0, 2);
            Integer token_name = UnicodeUtil.decodeHEX(token_name_hex);
            content = content.replaceFirst(token_name_hex, "");
            String token_name_str = content.substring(0, token_name * 2);

            content = content.replaceFirst(token_name_str, "");				// 清空掉token_name
            String tokenName = UnicodeUtil.hexStringToString(token_name_str);

            String token_document_url_hex = content.substring(0, 2);
            Integer token_document_url = UnicodeUtil.decodeHEX(token_document_url_hex);
            content = content.replaceFirst(token_document_url_hex, "");
            String token_document_url_str = content.substring(0, token_document_url * 2);

            content = content.replaceFirst(token_document_url_str, "");		// 清空掉url
            String tokenUrl = UnicodeUtil.hexStringToString(token_document_url_str);

            String token_document_hash_hex = content.substring(0, 2);
            Integer token_document_hash = UnicodeUtil.decodeHEX(token_document_hash_hex);
            content = content.replaceFirst(token_document_hash_hex, "");
            String token_document_hash_str = content.substring(0, token_document_hash * 2);

            content = content.replaceFirst(token_document_hash_str, "");		// 清空掉hash


            String byte_length_hex = content.substring(0, 2);
            Integer byte_length = UnicodeUtil.decodeHEX(byte_length_hex);
            content = content.replaceFirst(byte_length_hex, "");
            String byte_length_str = content.substring(0, byte_length * 2);

            if (byte_length < 0 || byte_length > 9) {
                // 超出范围
                return false;
            }
            content = content.replaceFirst(byte_length_str, "");			    // 清空掉精度precition
            Integer precition = new BigInteger(byte_length_str, 16).intValue();

            String mint_baton_vout_hex = content.substring(0, 2);
            Integer mint_baton_vout = UnicodeUtil.decodeHEX(mint_baton_vout_hex);
            content = content.replaceFirst(mint_baton_vout_hex, "");
            String mint_baton_vout_str = content.substring(0, mint_baton_vout * 2);

            if (mint_baton_vout < 1 || mint_baton_vout > 255) {
                // 超出范围
                return false;
            }
            content = content.replaceFirst(mint_baton_vout_str, "");			//清空掉vout
            Integer mintVout = new BigInteger(mint_baton_vout_str, 16).intValue();

            String initial_token_mint_quantity_hex = content.substring(0, 2);
            Integer initial_token_mint_quantity = UnicodeUtil.decodeHEX(initial_token_mint_quantity_hex);
            content = content.replaceFirst(initial_token_mint_quantity_hex, "");
            String initial_token_mint_quantity_str = content.substring(0, initial_token_mint_quantity * 2);
            BigInteger quantity = new BigInteger(initial_token_mint_quantity_str, 16);

            if (quantity.compareTo(new BigInteger("0")) < 0|| quantity.compareTo(new BigInteger("18446744073709551999")) > 0) {
                // 超出范围
                return false;
            }

            content = content.replaceFirst(initial_token_mint_quantity_str, "");				// 清空掉mintQuantity
            if (!"".equals(content)) {
                // 格式错误
                return false;
            }


            if (type == 1) {    // 第一种情况

                String mintAddress = map.get(mintVout).replaceFirst("76a914", "").replaceFirst("88ac", "");   //增发权限地址
                Slp slp = new Slp();
                slp.setTokenTicker(tokenTicker);
                slp.setTokenName(tokenName);
                slp.setTokenDocumentUrl(tokenUrl);
                slp.setTokenDocumentHash(token_document_hash_str);
                slp.setTokenDecimal(precition);
                slp.setMintBatonVout(mintVout);
                slp.setTransactionType("GENESIS");
                slp.setOriginalAddress(hexStr);                                // 发行地址
                slp.setInitIssueAddress(mintAddress);                // 增发权限地址
                slp.setInitialTokenMintQuantity(quantity.toString());
                slp.setTxid(tokenId);
                slpService.insertSlp(slp);

                GenesisAddress genesisAddress = new GenesisAddress();
                genesisAddress.setTxid(tokenId);
                genesisAddress.setRaiseVout(mintVout);
                genesisAddress.setIssueAddress(hexStr);
                genesisAddress.setRaiseAddress(mintAddress);
                genesisAddress.setIssueVout(n);
                genesisAddress.setRaiseTxid(txid);
                genesisAddressService.insertGenesisAddress(genesisAddress);


                TokenAssets tokenAssets = new TokenAssets();
                tokenAssets.setAddress(hexStr);
                tokenAssets.setTokenId(tokenId);
                tokenAssets.setTxid(txid);
                tokenAssets.setVout(n);
                tokenAssets.setTime(new Date().getTime());
                tokenAssets.setToken(quantity);
                tokenAssets.setStatus(0);

                tokenAssetsService.insertTokenAssets(tokenAssets);

                UtxoToken UtxoToken = new UtxoToken();
                UtxoToken.setAddress(hexStr);
                UtxoToken.setN(n);
                UtxoToken.setScript(vouthex);
                UtxoToken.setTxid(txid);
                UtxoToken.setValue(value);
                utxoTokenService.insertUtxoToken(UtxoToken);

            } else if (type == 2) {                             //多个地址情况

                String mintAddress = map.get(mintVout).replaceFirst("76a914", "").replaceFirst("88ac", "");   //增发权限地址
                ScriptSlp scriptSlp = new ScriptSlp();
                scriptSlp.setTokenTicker(tokenTicker);
                scriptSlp.setTokenName(tokenName);
                scriptSlp.setTokenDocumentUrl(tokenUrl);
                scriptSlp.setTokenDocumentHash(token_document_hash_str);
                scriptSlp.setTokenDecimal(precition);
                scriptSlp.setMintBatonVout(mintVout);
                scriptSlp.setTransactionType("GENESIS");
                scriptSlp.setOriginalScrpit(hexStr);                                // 发行的脚本
                scriptSlp.setInitIssueAddress(mintAddress);                // 增发权限地址
                scriptSlp.setInitialTokenMintQuantity(quantity.toString());
                scriptSlp.setTxid(tokenId);
                scriptSlpService.insertSlp(scriptSlp);

                GenesisAddress genesisAddress = new GenesisAddress();
                genesisAddress.setTxid(tokenId);
                genesisAddress.setRaiseVout(mintVout);
                genesisAddress.setIssueAddress(hexStr);
                genesisAddress.setRaiseAddress(mintAddress);
                genesisAddress.setIssueVout(n);
                genesisAddress.setRaiseTxid(txid);
                genesisAddressService.insertGenesisAddress(genesisAddress);


                ScriptTokenLink tokenAssets = new ScriptTokenLink();
                tokenAssets.setScript(hexStr);
                tokenAssets.setTokenId(tokenId);
                tokenAssets.setTxid(txid);
                tokenAssets.setVout(n);
                tokenAssets.setToken(quantity);
                tokenAssets.setStatus(0);
                scriptTokenLinkService.insert(tokenAssets);


                for (String address: addressList) {

                    ScriptUtxoTokenLink UtxoToken = new ScriptUtxoTokenLink();
                    UtxoToken.setAddress(address);
                    UtxoToken.setN(n);
                    UtxoToken.setScript(vouthex);
                    UtxoToken.setTxid(txid);
                    UtxoToken.setValue(value);
                    UtxoToken.setScript(hexStr);
                    scriptUtxoTokenLinkService.insert(UtxoToken);

                    int count = addressScriptLinkService.findCount(address, hexStr);
                    if (count < 1) {
                        AddressScriptLink asl = new AddressScriptLink();
                        asl.setAddress(address);
                        asl.setScript(hexStr);
                        addressScriptLinkService.insert(asl);
                    }
                }


            }

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        }

        return true;

    }

    // 解析增发
    @Transactional(rollbackFor=Exception.class)
    public boolean decodeMinttoken(Integer type, JSONArray vins, String content, Map<Integer, String> map, String hexStr, String tx, Integer n, String vouthex, String value, List<String> addressList) {

        try {

            String token_id_hex = content.substring(0, 2);
            Integer token_id = UnicodeUtil.decodeHEX(token_id_hex);

            content = content.replaceFirst(token_id_hex, "");
            String token_id_str = content.substring(0, token_id * 2);				//token_id

            content = content.replaceFirst(token_id_str, "");				// 清空掉token_id

            String mint_baton_vout_hex = content.substring(0, 2);
            Integer mint_baton_vout = UnicodeUtil.decodeHEX(mint_baton_vout_hex);
            content = content.replaceFirst(mint_baton_vout_hex, "");
            String mint_baton_vout_str = content.substring(0, mint_baton_vout * 2);		// vout

            if (mint_baton_vout < 1 || mint_baton_vout > 255) {
                // 超出范围
                return false;
            }
            content = content.replaceFirst(mint_baton_vout_str, "");			//清空掉vout

            Integer mintVout = new BigInteger(mint_baton_vout_str, 16).intValue();

            String initial_token_mint_quantity_hex = content.substring(0, 2);
            Integer initial_token_mint_quantity = UnicodeUtil.decodeHEX(initial_token_mint_quantity_hex);
            content = content.replaceFirst(initial_token_mint_quantity_hex, "");
            String initial_token_mint_quantity_str = content.substring(0, initial_token_mint_quantity * 2);
            BigInteger quantity = new BigInteger(initial_token_mint_quantity_str, 16);

            if (quantity.compareTo(new BigInteger("0")) < 0|| quantity.compareTo(new BigInteger("18446744073709551999")) > 0) {
                // 超出范围
                return false;
            }

            content = content.replaceFirst(initial_token_mint_quantity_str, "");				// 清空掉mintQuantity
            if (!"".equals(content)) {
                // 格式错误
                return false;
            }

            String mintAddress = map.get(mintVout).replaceFirst("76a914","").replaceFirst("88ac","");   //增发权限地址


            if (type == 1) {

                Slp slp = slpService.findByTokenId(token_id_str);

                if (slp == null)
                    return false;   // 不存在token

                boolean f = false;

                try {
                    f = mintVins(vins, token_id_str);         // 判断有没有增发权限
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!f) {
                    return false;
                }

                SlpMint slpMint = new SlpMint();
                slpMint.setTransactionType("mint");
                slpMint.setTokenId(token_id_str);
                slpMint.setMintBatonVout(mintVout);
                slpMint.setAdditionalTokenQuantity(quantity.toString());
                slpMint.setAddress(hexStr);         //增发地址
                slpMint.setMinterAddress(mintAddress);
                slpMintService.insertSlpMint(slpMint);

                GenesisAddress genesisAddress = new GenesisAddress();
                genesisAddress.setRaiseAddress(mintAddress);
                genesisAddress.setTxid(token_id_str);
                genesisAddress.setRaiseVout(mintVout);
                genesisAddress.setRaiseTxid(tx);
                genesisAddressService.updateGensisAddress(genesisAddress);

                TokenAssets tokenAssets = new TokenAssets();
                tokenAssets.setAddress(hexStr);
                tokenAssets.setTokenId(token_id_str);
                tokenAssets.setTxid(tx);
                tokenAssets.setVout(n);
                tokenAssets.setTime(new Date().getTime());
                tokenAssets.setToken(quantity);
                tokenAssets.setStatus(1);
                tokenAssetsService.insertTokenAssets(tokenAssets);

                UtxoToken UtxoToken = new UtxoToken();
                UtxoToken.setAddress(hexStr);
                UtxoToken.setN(n);
                UtxoToken.setScript(vouthex);
                UtxoToken.setTxid(tx);
                UtxoToken.setValue(value);
                utxoTokenService.insertUtxoToken(UtxoToken);

            } else if (type == 2) {

                ScriptSlp scriptSlp = scriptSlpService.findByTokenId(token_id_str);

                if (scriptSlp == null)
                    return false;   // 不存在token

                boolean f = false;

                try {
                    f = mintVins(vins, token_id_str);         // 判断有没有增发权限
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!f) {
                    return false;
                }

                ScriptSlpMint slpMint = new ScriptSlpMint();
                slpMint.setTransactionType("mint");
                slpMint.setTokenId(token_id_str);
                slpMint.setMintBatonVout(mintVout);
                slpMint.setAdditionalTokenQuantity(quantity.toString());
                slpMint.setScript(hexStr);         //增发脚本
                slpMint.setMinterAddress(mintAddress);
                scriptSlpMintService.insertSlpMint(slpMint);

                GenesisAddress genesisAddress = new GenesisAddress();
                genesisAddress.setRaiseAddress(mintAddress);
                genesisAddress.setTxid(token_id_str);
                genesisAddress.setRaiseVout(mintVout);
                genesisAddress.setRaiseTxid(tx);
                genesisAddressService.updateGensisAddress(genesisAddress);

                ScriptTokenLink tokenAssets = new ScriptTokenLink();
                tokenAssets.setScript(hexStr);
                tokenAssets.setTokenId(token_id_str);
                tokenAssets.setTxid(tx);
                tokenAssets.setVout(n);
                tokenAssets.setToken(quantity);
                tokenAssets.setStatus(1);
                scriptTokenLinkService.insert(tokenAssets);

                for (String address: addressList) {

                    ScriptUtxoTokenLink UtxoToken = new ScriptUtxoTokenLink();
                    UtxoToken.setAddress(address);
                    UtxoToken.setN(n);
                    UtxoToken.setScript(vouthex);
                    UtxoToken.setTxid(tx);
                    UtxoToken.setValue(value);
                    UtxoToken.setScript(hexStr);
                    scriptUtxoTokenLinkService.insert(UtxoToken);

                    int count = addressScriptLinkService.findCount(address, hexStr);
                    if (count < 1) {
                        AddressScriptLink asl = new AddressScriptLink();
                        asl.setAddress(address);
                        asl.setScript(hexStr);
                        addressScriptLinkService.insert(asl);
                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;

    }

    public Map<Integer, String> vouts(JSONArray vouts){

        Map<Integer, String> map = new HashedMap();

        for (Object v : vouts) {

            JSONObject vout = (JSONObject) v;
            JSONObject scriptPubKey = vout.getJSONObject("scriptPubKey");
            Integer n = vout.getInteger("n");
            String xsvaddressHash = scriptPubKey.getString("hex");
            map.put(n, xsvaddressHash);

        }

        return map;

    }

    public boolean mintVins(JSONArray vins, String tokenId) throws Exception {

        for (Object v: vins) {

            JSONObject vin = (JSONObject) v;

            JSONObject json = Api.GetRawTransaction(vin.getString("txid"));
            JSONArray vout = json.getJSONArray("vout");
            JSONObject vv = vout.getJSONObject(vin.getInteger("vout"));
            String xsvaddressHash = vv.getJSONObject("scriptPubKey").getString("hex").replaceFirst("76a914","").replaceFirst("88ac","");

            GenesisAddress genesisAddress = genesisAddressService.findRaiseAddress(xsvaddressHash, tokenId);

            if (genesisAddress != null)
                return true;

        }

        return false;

    }

    @Transactional(rollbackFor=Exception.class)
    public List<TokenAssets> tokenvins(JSONArray vins) {

        try {
            List<TokenAssets> tokenAssetsList = new ArrayList<>();

            for (Object v : vins) {

                JSONObject vin = (JSONObject) v;
                String txid = vin.getString("txid");
                Integer vout = vin.getInteger("vout");
                utxoTokenService.deleteUtxoToken(txid, vout);
                TokenAssets tokenAssets = tokenAssetsService.findByTokenAssetsStatus(txid, vout, 3);
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

    @Transactional(rollbackFor=Exception.class)
    public List<ScriptTokenLink> scripttokenvins(JSONArray vins) {

        try {
            List<ScriptTokenLink> tokenAssetsList = new ArrayList<>();

            for (Object v : vins) {

                JSONObject vin = (JSONObject) v;
                String txid = vin.getString("txid");
                Integer vout = vin.getInteger("vout");
                scriptUtxoTokenLinkService.deleteUtxoToken(txid, vout);
                ScriptTokenLink tokenAssets = scriptTokenLinkService.findByTokenAssetsStatus(txid, vout, 3);
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

    @Transactional(rollbackFor=Exception.class)
    public void addressHash(JSONObject transaction) {

        try {

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
                        AddressHashLink addressHashLink = addressHashLinkService.findByAddress(ad);
                        if (addressHashLink != null)
                            continue;
                        AddressHashLink insert = new AddressHashLink();
                        insert.setAddress(ad);
                        insert.setAddressHash(addressHash.replaceFirst("88ac", ""));
                        addressHashLinkService.insertAddressHashLink(insert);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    //解析发送
    public boolean decodeSnedToken(Integer type, String content, String toAddressHash, Integer n, JSONArray vins, String tx, Map<Integer, BigInteger> hashmap, List<Object> SlpSendList,
                                   List<Object> TokenAssetsList, String vouthex, String value,  List<Object> UtxoTokenList, List<String> addressList, List<AddressScriptLink> addressScriptLink) {

        String token_id_hex = content.substring(0, 2);
        Integer token_id = UnicodeUtil.decodeHEX(token_id_hex);				// 获取token
        content = content.replaceFirst(token_id_hex, "");
        String token_id_str = content.substring(0, token_id * 2);
        content = content.replaceFirst(token_id_str, "");

        String quantity_hex = content.substring(0, 2);

        Integer quantity = UnicodeUtil.decodeHEX(quantity_hex);

        content = content.replaceFirst(quantity_hex, "");
        String quantity_hex_str = content.substring(0, quantity * 2);

        BigInteger quantity_int = new BigInteger(quantity_hex_str, 16);

        if (quantity_int.compareTo(new BigInteger("0")) < 0|| quantity_int.compareTo(new BigInteger("18446744073709551999")) > 0) {
            // 超出范围
            return false;
        }

        List<Object> assetsList = new ArrayList<>();

        Map<String, Integer> ty = new HashedMap();

        for (Object v : vins) {
            JSONObject vin = (JSONObject) v;
            String txid = vin.getString("txid");
            Integer vout = vin.getInteger("vout");

            TokenAssets assets = tokenAssetsService.findByTokenAssets(token_id_str, txid, vout);

            if (assets != null) {
                assetsList.add(assets);
                ty.put("tokenAssets", 1);
            }

            ScriptTokenLink ScriptAssets = scriptTokenLinkService.findByTokenAssets(token_id_str, txid, vout);
            if (ScriptAssets != null) {
                assetsList.add(ScriptAssets);
                ty.put("scriptAssets", 1);
            }

        }

        String fromAddress = null;
        BigInteger newBig = new BigInteger("0");

        if (assetsList != null && assetsList.size() > 0) {

            for (Object tokenAssets : assetsList) {
                BigInteger fromToken = null;
                if (tokenAssets instanceof TokenAssets) {
                    TokenAssets t = (TokenAssets)tokenAssets;
                    fromToken = tokenAssetsService.selectFAToken(token_id_str, t.getTxid(), t.getVout());                     // 查询地址的
                    if (StringUtils.isEmpty(fromAddress))
                        fromAddress = t.getAddress();
                } else if (tokenAssets instanceof  ScriptTokenLink) {
                    ScriptTokenLink t = (ScriptTokenLink)tokenAssets;
                    fromToken = scriptTokenLinkService.selectFAToken(token_id_str, t.getTxid(), t.getVout());                 // 查询脚本的
                    if (StringUtils.isEmpty(fromAddress))
                        fromAddress = t.getScript();
                }
                if (fromToken != null) {
                    newBig = newBig.add(fromToken);
                }
            }

        } else
            return false;

        hashmap.put(0, newBig);          //vin的所有钱
        BigInteger voutBig = hashmap.get(1);
        hashmap.put(1, voutBig.add(quantity_int));

        if (newBig.compareTo(quantity_int) < 0)
            return false;                               //钱不够，返回


        if (type == 1) {

            SlpSend slpSend = new SlpSend();
            slpSend.setTokenId(token_id_str);
            slpSend.setTokenOutputQuantity(quantity.toString());
            slpSend.setVout(n);
            slpSend.setAddress(toAddressHash);                  // 向这个地址打钱
            slpSend.setTxid(tx);
            SlpSendList.add(slpSend);


            TokenAssets tokenAssets = new TokenAssets();
            if (fromAddress.equals(toAddressHash)) {
                tokenAssets.setStatus(4);
            } else {
                tokenAssets.setStatus(2);
            }
            tokenAssets.setAddress(toAddressHash);
            tokenAssets.setTokenId(token_id_str);
            tokenAssets.setTxid(tx);
            tokenAssets.setVout(n);
            tokenAssets.setToken(quantity_int);
            tokenAssets.setFromAddress(fromAddress);
            tokenAssets.setTime(new Date().getTime());
            TokenAssetsList.add(tokenAssets);

            Integer scriptAssets = ty.get("scriptAssets");
            if (scriptAssets != null && scriptAssets == 1) {
                ScriptTokenLink scriptTokenLink = new ScriptTokenLink();
                if (fromAddress.equals(toAddressHash)) {                    // 脚本相同，给自己找零
                    scriptTokenLink.setStatus(4);
                } else {
                    scriptTokenLink.setStatus(2);
                }
                scriptTokenLink.setScript(toAddressHash);
                scriptTokenLink.setTokenId(token_id_str);
                scriptTokenLink.setTxid(tx);
                scriptTokenLink.setVout(n);
                scriptTokenLink.setToken(quantity_int);
                scriptTokenLink.setFromScript(fromAddress);
                TokenAssetsList.add(scriptTokenLink);
            }

            UtxoToken UtxoToken = new UtxoToken();
            UtxoToken.setAddress(toAddressHash);
            UtxoToken.setN(n);
            UtxoToken.setScript(vouthex);
            UtxoToken.setTxid(tx);
            UtxoToken.setValue(value);
            UtxoTokenList.add(UtxoToken);

        } else if (type == 2) {

            ScriptSlpSend scriptSlpSend = new ScriptSlpSend();
            scriptSlpSend.setTokenId(token_id_str);
            scriptSlpSend.setTokenOutputQuantity(quantity.toString());
            scriptSlpSend.setVout(n);
            scriptSlpSend.setScript(toAddressHash);                  // 向这个脚本打钱
            scriptSlpSend.setTxid(tx);
            SlpSendList.add(scriptSlpSend);

            ScriptTokenLink scriptTokenLink = new ScriptTokenLink();
            if (fromAddress.equals(toAddressHash)) {                    // 脚本相同，给自己找零
                scriptTokenLink.setStatus(4);
            } else {
                scriptTokenLink.setStatus(2);
            }
            scriptTokenLink.setScript(toAddressHash);
            scriptTokenLink.setTokenId(token_id_str);
            scriptTokenLink.setTxid(tx);
            scriptTokenLink.setVout(n);
            scriptTokenLink.setToken(quantity_int);
            scriptTokenLink.setFromScript(fromAddress);
            TokenAssetsList.add(scriptTokenLink);
            Integer tokenAssetsMap = ty.get("tokenAssets");
            if (tokenAssetsMap != null && tokenAssetsMap == 1) {
                TokenAssets tokenAssets = new TokenAssets();
                if (fromAddress.equals(toAddressHash)) {
                    tokenAssets.setStatus(4);
                } else {
                    tokenAssets.setStatus(2);
                }
                tokenAssets.setAddress(toAddressHash);
                tokenAssets.setTokenId(token_id_str);
                tokenAssets.setTxid(tx);
                tokenAssets.setVout(n);
                tokenAssets.setToken(quantity_int);
                tokenAssets.setFromAddress(fromAddress);
                tokenAssets.setTime(new Date().getTime());
                TokenAssetsList.add(tokenAssets);
            }


            for (String address: addressList) {
                ScriptUtxoTokenLink scriptUtxoTokenLink = new ScriptUtxoTokenLink();
                scriptUtxoTokenLink.setAddress(address);               // 地址
                scriptUtxoTokenLink.setN(n);
                scriptUtxoTokenLink.setScript(vouthex);
                scriptUtxoTokenLink.setTxid(tx);
                scriptUtxoTokenLink.setValue(value);
                scriptUtxoTokenLink.setScript(toAddressHash);         // 脚本
                UtxoTokenList.add(scriptUtxoTokenLink);
                int count = addressScriptLinkService.findCount(address, toAddressHash);
                if (count < 1) {
                    AddressScriptLink asl = new AddressScriptLink();
                    asl.setAddress(address);
                    asl.setScript(toAddressHash);
                    addressScriptLink.add(asl);
                }
            }
        }

        return true;

    }


}


