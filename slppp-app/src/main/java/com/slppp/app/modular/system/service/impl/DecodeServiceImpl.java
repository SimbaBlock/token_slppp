package com.slppp.app.modular.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slppp.app.core.util.UnicodeUtil;
import com.slppp.app.modular.system.dao.*;
import com.slppp.app.modular.system.model.*;
import com.slppp.app.modular.system.service.DecodeService;
import com.slppp.app.modular.system.service.TokenAnalyzingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DecodeServiceImpl implements DecodeService {

    @Resource
    private SlpMapper slpMapper;

    @Resource
    private GenesisAddressMapper genesisAddressMapper;

    @Resource
    private TokenAssetsMapper tokenAssetsMapper;

    @Resource
    private UtxoTokenMapper utxoTokenMapper;

    @Resource
    private SlpMintMapper slpMintMapper;

    @Resource
    private TokenAnalyzingService tokenAnalyzingService;

    @Override
    public Boolean decodeGenesistoken(String content, Map<Integer, String> map, String hexStr, String tokenId, String txid, Integer n, String vouthex, String value) {
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
            slpMapper.insertSlp(slp);

            GenesisAddress genesisAddress = new GenesisAddress();
            genesisAddress.setTxid(tokenId);
            genesisAddress.setRaiseVout(mintVout);
            genesisAddress.setIssueAddress(hexStr);
            genesisAddress.setRaiseAddress(mintAddress);
            genesisAddress.setIssueVout(n);
            genesisAddress.setRaiseTxid(txid);
            genesisAddressMapper.insertGenesisAddress(genesisAddress);


            TokenAssets tokenAssets = new TokenAssets();
            tokenAssets.setAddress(hexStr);
            tokenAssets.setTokenId(tokenId);
            tokenAssets.setTxid(txid);
            tokenAssets.setVout(n);
            tokenAssets.setTime(new Date().getTime());
            tokenAssets.setToken(quantity);
            tokenAssets.setStatus(0);

            tokenAssetsMapper.insertTokenAssets(tokenAssets);

            UtxoToken UtxoToken = new UtxoToken();
            UtxoToken.setAddress(hexStr);
            UtxoToken.setN(n);
            UtxoToken.setScript(vouthex);
            UtxoToken.setTxid(txid);
            UtxoToken.setValue(value);
            utxoTokenMapper.insertUtxoToken(UtxoToken);

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        }

        return true;
    }

    @Override
    public Boolean decodeMinttoken(String content, Map<Integer, String> map, String hexStr, String tx, Integer n, String vouthex, String value, JSONArray vins) {
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

            Slp slp = slpMapper.findByTokenId(token_id_str);

            if (slp == null)
                return false;   // 不存在token


            boolean f = false;

            try {
                f = tokenAnalyzingService.mintVins(vins, token_id_str);         //判断有没有增发权限
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
            slpMintMapper.insertSlpMint(slpMint);

            GenesisAddress genesisAddress = new GenesisAddress();
            genesisAddress.setRaiseAddress(mintAddress);
            genesisAddress.setTxid(token_id_str);
            genesisAddress.setRaiseVout(mintVout);
            genesisAddress.setRaiseTxid(tx);
            genesisAddressMapper.updateGensisAddress(genesisAddress);


            TokenAssets tokenAssets = new TokenAssets();
            tokenAssets.setAddress(hexStr);
            tokenAssets.setTokenId(token_id_str);
            tokenAssets.setTxid(tx);
            tokenAssets.setVout(n);
            tokenAssets.setTime(new Date().getTime());
            tokenAssets.setToken(quantity);
            tokenAssets.setStatus(1);
            tokenAssetsMapper.insertTokenAssets(tokenAssets);

            UtxoToken UtxoToken = new UtxoToken();
            UtxoToken.setAddress(hexStr);
            UtxoToken.setN(n);
            UtxoToken.setScript(vouthex);
            UtxoToken.setTxid(tx);
            UtxoToken.setValue(value);
            utxoTokenMapper.insertUtxoToken(UtxoToken);

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        }

        return true;

    }

    @Override
    public Boolean decodeSnedToken(String content, String toAddressHash, Integer n, JSONArray vins, String tx, Map<Integer, BigInteger> hashmap, List<SlpSend> SlpSendList,
                                   List<TokenAssets> TokenAssetsList, String vouthex, String value, List<UtxoToken> UtxoTokenList) {

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
            TokenAssets assets = tokenAssetsMapper.findByTokenAssets(token_id_str, txid, vout);
            if (assets != null)
                assetsList.add(assets);
        }


        BigInteger newBig = new BigInteger("0");
        if (assetsList != null && assetsList.size() > 0) {
            for (TokenAssets tokenAssets : assetsList) {
                BigInteger fromToken = tokenAssetsMapper.selectFAToken(token_id_str, tokenAssets.getTxid(), tokenAssets.getVout());
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
