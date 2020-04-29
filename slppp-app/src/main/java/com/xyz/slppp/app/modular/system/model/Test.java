package com.xyz.slppp.app.modular.system.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xyz.slppp.app.core.rpc.Api;
import com.xyz.slppp.app.core.rpc.TxInputDto;
import com.xyz.slppp.app.core.rpc.TxOutputDto;
import com.xyz.slppp.app.core.util.HttpUtil;
import com.xyz.slppp.app.core.util.UnicodeUtil;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.classification.InterfaceAudience;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.ScriptBuilder;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Test implements Serializable {

    private String lokad_id;

    private String token_type;

    private String transaction_type;

    private String token_ticker;

    private String token_name;

    private String token_document_url;

    private String token_document_hash;

    private Integer decimals;

    private String mint_baton_vout;

    private String initial_token_mint_quantity;

    public Test() {}

    public Test(String lokad_id, String token_type, String transaction_type, String token_ticker, String token_name, String token_document_url, String token_document_hash, Integer decimals, String mint_baton_vout,
                String initial_token_mint_quantity) {

        this.lokad_id = lokad_id;
        this.token_type = token_type;
        this.transaction_type = transaction_type;
        this.token_ticker = token_ticker;
        this.token_name = token_name;
        this.token_document_url = token_document_url;
        this.token_document_hash = token_document_hash;
        this.decimals = decimals;
        this.mint_baton_vout = mint_baton_vout;
        this.initial_token_mint_quantity = initial_token_mint_quantity;

    }

    public String getLokad_id() {
        return lokad_id;
    }

    public void setLokad_id(String lokad_id) {
        this.lokad_id = lokad_id;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getToken_ticker() {
        return token_ticker;
    }

    public void setToken_ticker(String token_ticker) {
        this.token_ticker = token_ticker;
    }

    public String getToken_name() {
        return token_name;
    }

    public void setToken_name(String token_name) {
        this.token_name = token_name;
    }

    public String getToken_document_url() {
        return token_document_url;
    }

    public void setToken_document_url(String token_document_url) {
        this.token_document_url = token_document_url;
    }

    public String getToken_document_hash() {
        return token_document_hash;
    }

    public void setToken_document_hash(String token_document_hash) {
        this.token_document_hash = token_document_hash;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getMint_baton_vout() {
        return mint_baton_vout;
    }

    public void setMint_baton_vout(String mint_baton_vout) {
        this.mint_baton_vout = mint_baton_vout;
    }

    public String getInitial_token_mint_quantity() {
        return initial_token_mint_quantity;
    }

    public void setInitial_token_mint_quantity(String initial_token_mint_quantity) {
        this.initial_token_mint_quantity = initial_token_mint_quantity;
    }


    public static int returnActualLength(byte[] data) {
        int i = 0;
        for (; i < data.length; i++) {
            if (data[i] == '\0')
                break;
        }
        return i;
    }

//    public static String createTokenHex(Integer tokenType, String transactionType, String tokenTicker, String tokenName, String tokenUrl, String tokenHash,
//                                        Integer precition, BigInteger mintQuantity, Integer mintBatoVout) throws Exception {

//        String tokenTypeHex = UnicodeUtil.intToHex(tokenType);
//        String transactionTypeHex = UnicodeUtil.str2HexStr(transactionType);
//
//        String tokenTickerHex = UnicodeUtil.str2HexStr(tokenTicker);
//        String tokenNameHex = UnicodeUtil.str2HexStr(tokenName);
//        String tokenUrlHex = UnicodeUtil.str2HexStr(tokenUrl);
//        String tokenHashHex = UnicodeUtil.str2HexStr(tokenHash);
//        String precitionHex = UnicodeUtil.intToHex(precition);
//        String mintBatoVoutHex = UnicodeUtil.intToHex(mintBatoVout);
//        String mintQuantityHex = new BigInteger(mintQuantity.toString(),10).toString(16);
//
//
//        String tokenTypeInt = UnicodeUtil.intToHex(tokenTypeHex.getBytes().length / 2);
//        String transactionTypeInt = UnicodeUtil.intToHex(transactionTypeHex.getBytes().length / 2);
//        String tokenTickerInt = UnicodeUtil.intToHex(tokenTickerHex.getBytes().length / 2);
//        String tokenNameInt = UnicodeUtil.intToHex(tokenNameHex.getBytes().length / 2);
//        String tokenUrlInt = UnicodeUtil.intToHex(tokenUrlHex.getBytes().length / 2);
//        String tokenHashInt = UnicodeUtil.intToHex(tokenHashHex.getBytes().length / 2);
//        String precitionInt = UnicodeUtil.intToHex(precitionHex.getBytes().length / 2);
//        String mintBatoVoutInt = UnicodeUtil.intToHex(mintBatoVoutHex.getBytes().length / 2);
//        String mintQuantityInt = UnicodeUtil.intToHex(mintQuantityHex.getBytes().length / 2);
//
//
//        String a = tokenTypeInt+tokenTypeHex+transactionTypeInt+transactionTypeHex+tokenTickerInt+tokenTickerHex+tokenNameInt+tokenNameHex+tokenUrlInt+tokenUrlHex+tokenHashInt+tokenHashHex+precitionInt+precitionHex+
//                mintBatoVoutInt+mintBatoVoutHex+mintQuantityInt+mintQuantityHex;

//        if (tokenType < 0 || tokenType > 9999) {
//
//            // 超出范围
//
//        }
//
//
//        if (tokenTicker == null) {
//           // 不能为空
//        }
//
//        if (tokenName == null) {
//            // 不能为空
//        }
//
//        if (tokenUrl == null) {
//            // 不能为空
//        }
//
//        if (precition < 0 || precition > 9) {
//            // 超出范围
//        }
//
//        if (mintBatoVout < 2 || mintBatoVout > 255) {
//            // 超出范围
//        }
//
//        if (mintQuantity.compareTo(new BigInteger("0")) < 0|| mintQuantity.compareTo(new BigInteger("9999999999999999")) > 0) {
//            // 超出范围
//        }
//
//
//        String tokenTypeHex = UnicodeUtil.intToHex(tokenType);
//        String transactionTypeHex = UnicodeUtil.str2HexStr(transactionType);
//        String tokenTickerHex = UnicodeUtil.str2HexStr(tokenTicker);
//        String tokenNameHex = UnicodeUtil.str2HexStr(tokenName);
//        String tokenUrlHex = UnicodeUtil.str2HexStr(tokenUrl);
//        String tokenHashHex = UnicodeUtil.str2HexStr(tokenHash);
//        String precitionHex = UnicodeUtil.intToHex(precition);
//        String mintBatoVoutHex = UnicodeUtil.intToHex(mintBatoVout);
//        String mintQuantityHex = new BigInteger(mintQuantity.toString(),10).toString(16);
//
//
//        String aa = Integer.toHexString(tokenType);
//        String bb = Integer.toHexString(precition);
//        String cc = Integer.toHexString(mintBatoVout);
//
//
//        String tokenTypeInt = UnicodeUtil.intToHex(aa.getBytes().length);
//        String transactionTypeInt = UnicodeUtil.intToHex(transactionType.getBytes().length);
//        String tokenTickerInt = UnicodeUtil.intToHex(tokenTicker.getBytes().length);
//        String tokenNameInt = UnicodeUtil.intToHex(tokenName.getBytes().length);
//        String tokenUrlInt = UnicodeUtil.intToHex(tokenUrl.getBytes().length);
//        String tokenHashInt = UnicodeUtil.intToHex(tokenHash.getBytes().length);
//        String precitionInt = UnicodeUtil.intToHex(bb.getBytes().length);
//        String mintBatoVoutInt = UnicodeUtil.intToHex(cc.getBytes().length);
//        if (mintQuantityHex.length()%2 > 0) {
//            mintQuantityHex = "0"+mintQuantityHex;
//        }
//        String dd = Integer.toHexString(mintBatoVout);
//        String mintQuantityInt = UnicodeUtil.intToHex(mintQuantityHex.length()/2);
//
//
//        String a = transactionTypeInt+transactionTypeHex+tokenTickerInt+tokenTickerHex+tokenNameInt+tokenNameHex
//                +tokenUrlInt+tokenUrlHex+tokenHashInt+tokenHashHex+precitionInt+precitionHex+mintBatoVoutInt+mintBatoVoutHex+mintQuantityInt+mintQuantityHex;
//        System.out.println(a);
//        List<TxInputDto> input = new ArrayList<>();
//
//        JSONArray systemAddressJons = new JSONArray();
//        systemAddressJons.add("1FnAoPfBqreWkcqS6StXhZcCKCwCks1GKo");
//        systemAddressJons.add(0.00000551);
//        JSONArray addresss = new JSONArray();
//        addresss.add(systemAddressJons);
//        JSONObject results = getUxto(addresss);
//        BigDecimal value1 = ApiController.txInput(input, results, "1FnAoPfBqreWkcqS6StXhZcCKCwCks1GKo");
//
//
//        List<TxOutputDto> output = new ArrayList<>();
//        TxOutputDto d = new TxOutputDto(a);
//        output.add(d);
//        String createRawTransaction = Api.CreateRawTransaction(input, output);
//
//        return createRawTransaction;

//    }

//    public static JSONObject getUxto(JSONArray address) throws Exception {
//
//        JSONObject params = new JSONObject();
//        params.put("jsonrpc","1.0");
//        params.put("id","curltest");
//        params.put("method","getutxo");
//        params.put("params", address);
//
//        System.out.println(params);
//        String utxos = HttpUtil.doPost("http://8.209.113.233:8666/", params.toJSONString());
//        System.out.println(utxos);
//        JSONObject data = (JSONObject) JSONObject.parse(utxos);
//        JSONObject result = data.getJSONObject("result");
//
//        return result;
//
//    }





//<token_type: 1> (1 to 2 byte integer)           1到2个字节整数，如最小1，最大99
//<transaction_type: 'GENESIS'> (7 bytes, ascii)  7个btye
//<token_ticker> (0 to ∞ bytes, suggested utf-8)  0到无限
//<token_name> (0 to ∞ bytes, suggested utf-8)    0到无限
//<token_document_url> (0 to ∞ bytes, suggested ascii)   0到无限
//<token_document_hash> (0 bytes or 32 bytes)    0字节或者 32btyes
//<decimals> (1 byte in range 0x00-0x09)      0到9
//<mint_baton_vout> (0 bytes, or 1 byte in range 0x02-0xff) 0或者 2到255
//<initial_token_mint_quantity> (8 byte integer)        8byte, 0到9999999999999999



    public static void main(String args[]) throws Exception {

//       String b = createTokenHex(1,"GENESIS","1000000000000000000","bbb","wwww.baidu.com", "askhdaksjdhjaskhd", 1000000, new BigInteger("1000000000000000000"), 2);

//       String a = createTokenHex(9999,"GENESIS","9999999999999999","bbb","wwww.baidu.com", "askhdaksjdhjaskhd", 8, new BigInteger("9999999999999999"), 2);
//       System.out.println(a);


//        String a = decodeGenesistoken("04534c500001010747454e455349531331303030303030303030303030303030303030036262620e777777772e62616964752e636f6d1161736b6864616b736a64686a61736b686401080102080de0b6b3a7640000");


    }

//    public static int decodeHEX(String hexs){
//        BigInteger bigint=new BigInteger(hexs, 16);
//        int numb=bigint.intValue();
//        return numb;
//    }
//
//    public static String decodeGenesistoken(String opturn) {
//
//        String OP_RETURN = opturn.replaceFirst("0 OP_RETURN ", "");
//        String genesis = OP_RETURN.substring(0, 30);
//
//        if (!"04534c500001010747454e45534953".equals(genesis)) {
//            // 格式错误
//        }
//        String content = OP_RETURN.replaceFirst("04534c500001010747454e45534953", "");
//        String token_ticker_hex = content.substring(0, 2);
//        Integer token_ticker = decodeHEX(token_ticker_hex);
//        content = content.replaceFirst(token_ticker_hex, "");
//        String token_ticker_str = content.substring(0, token_ticker * 2);
//
//        if (token_ticker_str == null) {
//            // 不能为空
//        }
//        content = content.replaceFirst(token_ticker_str, "");				// 清空掉token_ticker
//        String tokenTicker = UnicodeUtil.hexStringToString(token_ticker_str);
//
//
//
//        String token_name_hex = content.substring(0, 2);
//        Integer token_name = decodeHEX(token_name_hex);
//        content = content.replaceFirst(token_name_hex, "");
//        String token_name_str = content.substring(0, token_name * 2);
//
//        if (token_name_str == null) {
//            // 不能为空
//        }
//        content = content.replaceFirst(token_name_str, "");				// 清空掉token_name
//        String tokenName = UnicodeUtil.hexStringToString(token_name_str);
//
//        String token_document_url_hex = content.substring(0, 2);
//        Integer token_document_url = decodeHEX(token_document_url_hex);
//        content = content.replaceFirst(token_document_url_hex, "");
//        String token_document_url_str = content.substring(0, token_document_url * 2);
//
//        if (token_document_url_str == null) {
//            // 不能为空
//        }
//        content = content.replaceFirst(token_document_url_str, "");		// 清空掉url
//        String tokenUrl = UnicodeUtil.hexStringToString(token_document_url_str);
//
//        String token_document_hash_hex = content.substring(0, 2);
//        Integer token_document_hash = decodeHEX(token_document_hash_hex);
//        content = content.replaceFirst(token_document_hash_hex, "");
//        String token_document_hash_str = content.substring(0, token_document_hash * 2);
//
//        if (token_document_hash_str == null) {
//            // 不能为空
//        }
//        content = content.replaceFirst(token_document_hash_str, "");		// 清空掉hash
//        String tokenHash = UnicodeUtil.hexStringToString(token_document_hash_str);
//
//
//        String byte_length_hex = content.substring(0, 2);
//        Integer byte_length = decodeHEX(byte_length_hex);
//        content = content.replaceFirst(byte_length_hex, "");
//        String byte_length_str = content.substring(0, byte_length * 2);
//
//        if (byte_length < 0 || byte_length > 9) {
//            // 超出范围
//        }
//        content = content.replaceFirst(byte_length_str, "");			    // 清空掉精度precition
//        Integer precition = new BigInteger(byte_length_str, 16).intValue();
//
//        String mint_baton_vout_hex = content.substring(0, 2);
//        Integer mint_baton_vout = decodeHEX(mint_baton_vout_hex);
//        content = content.replaceFirst(mint_baton_vout_hex, "");
//        String mint_baton_vout_str = content.substring(0, mint_baton_vout * 2);
//
//        if (mint_baton_vout < 2 || mint_baton_vout > 255) {
//            // 超出范围
//        }
//        content = content.replaceFirst(mint_baton_vout_str, "");			//清空掉vout
//        Integer mintVout = new BigInteger(mint_baton_vout_str, 16).intValue();
//
//        String initial_token_mint_quantity_hex = content.substring(0, 2);
//        Integer initial_token_mint_quantity = decodeHEX(initial_token_mint_quantity_hex);
//        content = content.replaceFirst(initial_token_mint_quantity_hex, "");
//        String initial_token_mint_quantity_str = content.substring(0, initial_token_mint_quantity * 2);
//        BigInteger quantity = new BigInteger(initial_token_mint_quantity_str, 16);
//
//		if (quantity.compareTo(new BigInteger("0")) < 0|| quantity.compareTo(new BigInteger("9999999999999999")) > 0) {
//			// 超出范围
//		}
//
//		content = content.replaceFirst(initial_token_mint_quantity_str, "");				// 清空掉mintQuantity
//		if (!"".equals(content)) {
//			// 格式错误
//		}
//
//		System.out.println("tokenTicker=========="+tokenTicker);
//        System.out.println("tokenName=========="+tokenName);
//        System.out.println("tokenUrl=========="+tokenUrl);
//        System.out.println("tokenHash=========="+tokenHash);
//        System.out.println("precition=========="+precition);
//        System.out.println("mintVout=========="+mintVout);
//        System.out.println("quantity=========="+quantity);
//
//        System.out.println("==================================================================");
//        return null;
//
//    }

}

