package com.xyz.slppp.app.modular.system.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyz.slppp.app.core.common.annotion.TimeStat;
import com.xyz.slppp.app.core.rpc.Api;
import com.xyz.slppp.app.core.util.UnicodeUtil;
import com.xyz.slppp.app.modular.system.model.*;
import com.xyz.slppp.app.modular.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private TransactionService transactionService;

    @Autowired
    private TokenDestructionService tokenDestructionService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void work() {
        self.start();
    }

    @TimeStat
    public void start(){

        try {

            Integer count = blockCountService.findBlockCount();
            Integer json = Api.GetBlockCount();

            if (count < json + 1) {

                String blockHash = Api.GetBlockHash(count);
                JSONObject block = Api.GetBlock(blockHash);

                JSONArray jsonArray = block.getJSONArray("tx");

                block(jsonArray);
                count ++;
                blockCountService.updateBlock(count);

            } else {

                List<String> txList = Api.GetRawMemPool();
                JSONArray jsonArray = new JSONArray();
                for (String tx: txList) {
                    jsonArray.add(tx);
                }
                block(jsonArray);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void block(JSONArray jsonArray) throws Exception {

        if (jsonArray.size() > 0) {

            for (Object j : jsonArray) {

                String txid = (String) j;

                List<TokenAssets> tokenAssetsList = tokenAssetsService.selectByTxid(txid);

                if (tokenAssetsList != null && tokenAssetsList.size() > 0)
                    continue;

                addressHash(j);

                JSONObject txHex = Api.GetRawTransaction(txid);

                JSONArray vouts = txHex.getJSONArray("vout");
                JSONArray vins = txHex.getJSONArray("vin");
                TokenAssets tokenAssets = vins(vins);


                Long time = txHex.getLong("time");

                Map<Integer, String> map = vouts(vouts);

                boolean flag = false;

                for (Object v : vouts) {

                    JSONObject vout = (JSONObject) v;
                    JSONObject scriptPubKey = vout.getJSONObject("scriptPubKey");
                    String open_hex = scriptPubKey.getString("hex");
                    Integer n = vout.getInteger("n");


                    if (open_hex.contains("76a914")) {
                        String hex = open_hex.replaceFirst("76a914", "");
                        String hexStr = hex.substring(0, 40);
                        String first = hex.replaceFirst(hexStr + "88ac", "");

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
                            String tokenid = UnicodeUtil.getSHA256(open_hex);
//                               if (map.size() < 2)
//                                   continue;

                            boolean bl = decodeGenesistoken(OP_RETURN, map, hexStr, tokenid, txid, n, time);

                            if (bl)
                                flag = true;

                        } else if ("4d494e54".equals(token_type_str)) {

                            boolean f = mintVins(vins);         //判断有没有增发权限

                            if (f) {
                                boolean bl = decodeMinttoken(OP_RETURN, map, hexStr, txid, n, time);
                                if (bl)
                                    flag = true;
                            }

                        } else if ("53454e44".equals(token_type_str)) {

                            boolean bl = decodeSnedToken(OP_RETURN, hexStr, n, vins, txid, time);

                            if (bl)
                                flag = true;

                        }
                    }

                }

                if (!flag && tokenAssets != null) {

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


        } else {
            for (Object j : jsonArray) {
                addressHash(j);
            }
        }



    }



    //解析发行
    public boolean decodeGenesistoken(String content, Map<Integer, String> map, String hexStr, String tokenId, String txid, Integer n, Long time) {

        String token_ticker_hex = content.substring(0, 2);
        Integer token_ticker = UnicodeUtil.decodeHEX(token_ticker_hex);
        content = content.replaceFirst(token_ticker_hex, "");
        String token_ticker_str = content.substring(0, token_ticker * 2);

        if (token_ticker_str == null) {
            // 不能为空
            return false;
        }
        content = content.replaceFirst(token_ticker_str, "");				// 清空掉token_ticker
        String tokenTicker = UnicodeUtil.hexStringToString(token_ticker_str);



        String token_name_hex = content.substring(0, 2);
        Integer token_name = UnicodeUtil.decodeHEX(token_name_hex);
        content = content.replaceFirst(token_name_hex, "");
        String token_name_str = content.substring(0, token_name * 2);

        if (token_name_str == null) {
            // 不能为空
            return false;
        }
        content = content.replaceFirst(token_name_str, "");				// 清空掉token_name
        String tokenName = UnicodeUtil.hexStringToString(token_name_str);

        String token_document_url_hex = content.substring(0, 2);
        Integer token_document_url = UnicodeUtil.decodeHEX(token_document_url_hex);
        content = content.replaceFirst(token_document_url_hex, "");
        String token_document_url_str = content.substring(0, token_document_url * 2);

        if (token_document_url_str == null) {
            // 不能为空
            return false;
        }
        content = content.replaceFirst(token_document_url_str, "");		// 清空掉url
        String tokenUrl = UnicodeUtil.hexStringToString(token_document_url_str);

        String token_document_hash_hex = content.substring(0, 2);
        Integer token_document_hash = UnicodeUtil.decodeHEX(token_document_hash_hex);
        content = content.replaceFirst(token_document_hash_hex, "");
        String token_document_hash_str = content.substring(0, token_document_hash * 2);

        if (token_document_hash_str == null) {
            // 不能为空
            return false;
        }
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

        return true;

    }

    // 解析增发
    public boolean decodeMinttoken(String content, Map<Integer, String> map, String hexStr, String tx, Integer n, Long time) {

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


//        GenesisAddress genesisaddress = genesisAddressService.findRaiseAddress(hexStr);
//        if (genesisaddress == null)
//            return false;   // 无权限增发

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

    public boolean mintVins(JSONArray vins) {

        for (Object v: vins) {

            JSONObject vin = (JSONObject) v;

            GenesisAddress genesisAddress = genesisAddressService.findByRaiseTxidAndRaiseVout(vin.getString("txid"), vin.getInteger("vout"));

            if (genesisAddress != null)
                return true;

        }

        return false;

    }

    public TokenAssets vins(JSONArray vins) {

        for (Object v: vins) {

            JSONObject vin = (JSONObject) v;

            TokenAssets tokenAssets = tokenAssetsService.findByTokenAssetsStatus(vin.getString("txid"), vin.getInteger("vout"), 2);

            if (tokenAssets != null)
                return tokenAssets;

        }

        return null;

    }


    public void addressHash(Object j) throws Exception {

        String tx = (String) j;
        JSONObject transaction = Api.GetRawTransaction(tx);
        System.out.println(transaction.toJSONString());
        JSONArray vouts = transaction.getJSONArray("vout");

        for (Object vout : vouts) {

            JSONObject voutJSON = (JSONObject) vout;
            JSONObject scriptPubKey = voutJSON.getJSONObject("scriptPubKey");
            JSONArray addresss = scriptPubKey.getJSONArray("addresses");
            if (addresss != null) {
                for (Object address : addresss) {
                    String ad = address.toString();
                    String addressHash = Api.ValidateAddress(ad).getString("scriptPubKey").replaceFirst("76a914", "");
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

    }

    //解析发送
    public boolean decodeSnedToken(String content, String toAddressHash, Integer n, JSONArray vins, String tx, Long time) {

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

        if (newBig.compareTo(quantity_int) < 0)
            return false;                               //钱不够，返回


        SlpSend slpSend = new SlpSend();
        slpSend.setTokenId(token_id_str);
        slpSend.setTokenOutputQuantity(quantity.toString());
        slpSend.setVout(n);
        slpSend.setAddress(toAddressHash);                  // 向这个地址打钱
        slpSend.setTxid(tx);

        slpSendService.insertSlpSend(slpSend);


//        for (TokenAssets ta : assetsList) {
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

        tokenAssetsService.insertTokenAssets(tokenAssets);

//        }

        return true;

    }


}


