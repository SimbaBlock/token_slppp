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

    @Scheduled(cron = "0/5 * * * * ?")
    public void work() throws Exception {
        self.start();
    }

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


    @Transactional(rollbackFor=Exception.class)
    public void block(JSONArray jsonArray) {

        try {

            if (jsonArray.size() > 0) {

                for (Object j : jsonArray) {

                    String txid = (String) j;

                    List<TokenAssets> tokenAssetsList = tokenAssetsService.selectByTxid(txid);


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
                    List<Boolean> sendFlagList = new ArrayList<>();
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
                                    continue;

                            } else if ("4d".equals(leng_hex)) {

                                String length_hex = first.substring(0, 4);
                                Integer length = UnicodeUtil.decodeHEX(length_hex);
                                first = first.replaceFirst(length_hex, "");
                                OP_RETURN = first.substring(0, length*2);
                                first = first.replaceFirst(OP_RETURN,"");
                                if (!"".equals(first))
                                    continue;

                            } else if ("4e".equals(leng_hex)) {

                                String length_hex = first.substring(0, 6);
                                Integer length = UnicodeUtil.decodeHEX(length_hex);
                                first = first.replaceFirst(length_hex, "");
                                OP_RETURN = first.substring(0, length*2);
                                first = first.replaceFirst(OP_RETURN,"");
                                if (!"".equals(first))
                                    continue;

                            } else {

                                Integer length = UnicodeUtil.decodeHEX(leng_hex);
                                first = first.replaceFirst(leng_hex, "");
                                OP_RETURN = first.substring(0, length*2);
                                first = first.replaceFirst(OP_RETURN,"");
                                if (!"".equals(first))
                                    continue;

                            }

                            OP_RETURN = OP_RETURN.replaceFirst("06534c502b2b00020201", "");

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
                                    String nn = UnicodeUtil.intToHex(n);
                                    tokenid = UnicodeUtil.getSHA256(txid + nn);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String vouthex = scriptPubKey.getString("hex");
                                String value = vout.getBigDecimal("value").toString();

                                boolean bl = false;

                                try {
                                    bl = decodeGenesistoken(OP_RETURN, map, hexStr, tokenid, txid, n, time, vouthex, value);
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

                                boolean f = false;

                                try {
                                    f = mintVins(vins);         //判断有没有增发权限
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (f) {
                                    String vouthex = scriptPubKey.getString("hex");
                                    String value = vout.getBigDecimal("value").toString();
                                    boolean bl = false;

                                    try {
                                        bl = decodeMinttoken(OP_RETURN, map, hexStr, txid, n, time, vouthex, value);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (bl)
                                        flag = true;
                                    else {
                                        flag = false;
                                        break;
                                    }
                                }

                            } else if ("53454e44".equals(token_type_str)) {

                                String vouthex = scriptPubKey.getString("hex");
                                String value = vout.getBigDecimal("value").toString();
                                Boolean f = decodeSnedToken(OP_RETURN, hexStr, n, vins, txid, time, hashmap, SlpSendList, TokenAssetsList, vouthex, value, utxoTokenList);

                                if (f || sendFlag)
                                    sendFlag = true;

                            }
                        }

                    }

                    if (sendFlag && hashmap.get(0).compareTo(hashmap.get(1)) >=0) {

                        flag = true;
                        if (SlpSendList != null) {
                            for (SlpSend s : SlpSendList) {
                                slpSendService.insertSlpSend(s);
                            }
                        }

                        if (TokenAssetsList != null) {
                            for (TokenAssets t : TokenAssetsList) {
                                tokenAssetsService.insertTokenAssets(t);
                            }
                        }

                        if (utxoTokenList != null) {
                            for (UtxoToken ut : utxoTokenList) {
                                utxoTokenService.insertUtxoToken(ut);
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
    public boolean decodeGenesistoken(String content, Map<Integer, String> map, String hexStr, String tokenId, String txid, Integer n, Long time, String vouthex, String value) {

        try {

            String token_ticker_hex = content.substring(0, 2);
            Integer token_ticker = UnicodeUtil.decodeHEX(token_ticker_hex);
            content = content.replaceFirst(token_ticker_hex, "");
            String token_ticker_str = content.substring(0, token_ticker * 2);

//            if (token_ticker_str == null) {
//                // 不能为空
//                return false;
//            }
            content = content.replaceFirst(token_ticker_str, "");				// 清空掉token_ticker
            String tokenTicker = UnicodeUtil.hexStringToString(token_ticker_str);



            String token_name_hex = content.substring(0, 2);
            Integer token_name = UnicodeUtil.decodeHEX(token_name_hex);
            content = content.replaceFirst(token_name_hex, "");
            String token_name_str = content.substring(0, token_name * 2);

//            if (token_name_str == null) {
//                // 不能为空
//                return false;
//            }
            content = content.replaceFirst(token_name_str, "");				// 清空掉token_name
            String tokenName = UnicodeUtil.hexStringToString(token_name_str);

            String token_document_url_hex = content.substring(0, 2);
            Integer token_document_url = UnicodeUtil.decodeHEX(token_document_url_hex);
            content = content.replaceFirst(token_document_url_hex, "");
            String token_document_url_str = content.substring(0, token_document_url * 2);

//            if (token_document_url_str == null) {
//                // 不能为空
//                return false;
//            }

            content = content.replaceFirst(token_document_url_str, "");		// 清空掉url
            String tokenUrl = UnicodeUtil.hexStringToString(token_document_url_str);

            String token_document_hash_hex = content.substring(0, 2);
            Integer token_document_hash = UnicodeUtil.decodeHEX(token_document_hash_hex);
            content = content.replaceFirst(token_document_hash_hex, "");
            String token_document_hash_str = content.substring(0, token_document_hash * 2);

//            if (token_document_hash_str == null) {
//                // 不能为空
//                return false;
//            }
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


            String mintAddress = map.get(mintVout).replaceFirst("76a914","").replaceFirst("88ac","");   //增发权限地址
            Slp slp = new Slp();
            slp.setTokenTicker(tokenTicker);
            slp.setTokenName(tokenName);
            slp.setTokenDocumentUrl(tokenUrl);
            slp.setTokenDocumentHash(token_document_hash_str);
            slp.setTokenDecimal(precition);
            slp.setMintBatonVout(mintVout);
            slp.setTransactionType("GENESIS");
            slp.setOriginalAddress(hexStr);					            // 发行地址
            slp.setInitIssueAddress(mintAddress);				// 增发权限地址
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

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        }

        return true;

    }

    // 解析增发
    @Transactional(rollbackFor=Exception.class)
    public boolean decodeMinttoken(String content, Map<Integer, String> map, String hexStr, String tx, Integer n, Long time, String vouthex, String value) {

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

            Slp slp = slpService.findByTokenId(token_id_str);

            if (slp == null)
                return false;   // 不存在token


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
            String addressHash = scriptPubKey.getString("hex");
            map.put(n, addressHash);

        }

        return map;

    }

    public boolean mintVins(JSONArray vins) throws Exception {


        for (Object v: vins) {

            JSONObject vin = (JSONObject) v;

            JSONObject json = Api.GetRawTransaction(vin.getString("txid"));
            JSONArray vout = json.getJSONArray("vout");
            JSONObject vv = vout.getJSONObject(vin.getInteger("vout"));
            String addressHash = vv.getJSONObject("scriptPubKey").getString("hex").replaceFirst("76a914","").replaceFirst("88ac","");

            GenesisAddress genesisAddress = genesisAddressService.findRaiseAddress(addressHash);

            if (genesisAddress != null)
                return true;

        }

        return false;

    }

    @Transactional(rollbackFor=Exception.class)
    public List<TokenAssets> vins(JSONArray vins) {

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
    public boolean decodeSnedToken(String content, String toAddressHash, Integer n, JSONArray vins, String tx, Long time, Map<Integer, BigInteger> hashmap, List<SlpSend> SlpSendList,
                                   List<TokenAssets> TokenAssetsList, String vouthex, String value,  List<UtxoToken> UtxoTokenList) {

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

        List<TokenAssets> assetsList = new ArrayList<>();
        for (Object v : vins) {
            JSONObject vin = (JSONObject) v;
            String txid = vin.getString("txid");
            Integer vout = vin.getInteger("vout");
            TokenAssets assets = tokenAssetsService.findByTokenAssets(txid, vout);
            if (assets != null)
                assetsList.add(assets);
        }


        BigInteger newBig = new BigInteger("0");
        if (assetsList != null && assetsList.size() > 0) {
            for (TokenAssets tokenAssets : assetsList) {
                BigInteger fromToken = tokenAssetsService.selectFAToken(tokenAssets.getTxid(), tokenAssets.getVout());
                if (fromToken != null) {
                    newBig = newBig.add(fromToken);
                }
            }
        } else
            return false;

        hashmap.put(0,newBig);          //vin的所有钱
        BigInteger voutBig = hashmap.get(1);
        hashmap.put(1, voutBig.add(quantity_int));

        if (newBig.compareTo(quantity_int) < 0)
            return false;                               //钱不够，返回


        SlpSend slpSend = new SlpSend();
        slpSend.setTokenId(token_id_str);
        slpSend.setTokenOutputQuantity(quantity.toString());
        slpSend.setVout(n);
        slpSend.setAddress(toAddressHash);                  // 向这个地址打钱
        slpSend.setTxid(tx);
        SlpSendList.add(slpSend);

        String fromAddress = assetsList.get(0).getAddress();
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
        tokenAssets.setFromAddress(assetsList.get(0).getAddress());
        tokenAssets.setTime(new Date().getTime());

        TokenAssetsList.add(tokenAssets);

        UtxoToken UtxoToken = new UtxoToken();
        UtxoToken.setAddress(toAddressHash);
        UtxoToken.setN(n);
        UtxoToken.setScript(vouthex);
        UtxoToken.setTxid(tx);
        UtxoToken.setValue(value);
        UtxoTokenList.add(UtxoToken);

        return true;

    }


}


