package com.xyz.slppp.app.modular.api;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.xyz.slppp.app.core.common.exception.BizExceptionEnum;
import com.xyz.slppp.app.core.exception.ParamException;
import com.xyz.slppp.app.core.rpc.Api;
import com.xyz.slppp.app.core.rpc.TxInputDto;
import com.xyz.slppp.app.core.rpc.TxOutputDto;
import com.xyz.slppp.app.core.util.HttpUtil;
import com.xyz.slppp.app.core.util.JsonResult;
import com.xyz.slppp.app.core.util.UnicodeUtil;
import com.xyz.slppp.app.modular.system.model.*;
import com.xyz.slppp.app.modular.system.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.Block;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Controller
@RequestMapping("/rest/Api")
public class ApiController {

	@Autowired
	private SendRawTypeService sendRawTypeService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private VinService vinService;

	@Autowired
	private VoutService voutService;

	@Autowired
	private VoutAddressService voutAddressService;

	@Autowired
	private SlpService slpService;

	@Autowired
	private GenesisAddressService genesisAddressService;

	@Autowired
	private SlpMintService slpMintService;

	@Autowired
	private TokenAssetsService tokenAssetsService;

	@Autowired
	private SlpSendService slpSendService;

	@Autowired
	private KycAddressService kycAddressService;

	@Autowired
    private TokenAssetsLogService tokenAssetsLogService;

	@Autowired
	private AddressHashLinkService addressHashLinkService;

	@Value("${system-address}")
	private String systemAddress;

	@Value("${utxo-url}")
	private String utxoUrl;

	@Value("${sp-token-url}")
	private String spTokenUrl;

	/**
	 * 验证并发送裸交易到P2P网络
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("broadcastTx")
	public JsonResult SendRawTransaction(@RequestBody JSONObject JSONObject) {

		try {

			String hex = JSONObject.getString("hex");

			String txid = sendRawTransaction(hex);

			return new JsonResult().addData("txid", txid);

		} catch (Exception e) {

			e.printStackTrace();
			return new JsonResult(BizExceptionEnum.SERVER_ERROR.getCode(), BizExceptionEnum.SERVER_ERROR.getMessage());

		}

	}




	public void vouts(JSONObject v, Map<Integer, List<String>> map){

		JSONObject scriptPubKey = v.getJSONObject("scriptPubKey");
		Integer n = v.getInteger("n");
		JSONArray addresses = scriptPubKey.getJSONArray("addresses");
		List<String> address = new ArrayList<>();

		for (int j = 0; j < addresses.size(); j++) {
			address.add(addresses.get(j).toString());
		}
		map.put(n, address);

	}

	public Map<Integer, String> contents(String content, Map<Integer, String> map, Integer i){

			String mint_baton_vout_hex = content.substring(0, 2);

			Integer mint_baton_vout = UnicodeUtil.decodeHEX(mint_baton_vout_hex);

			if (mint_baton_vout > 256 || mint_baton_vout < 1) {
				// 格式错误
			}

			content = content.replaceFirst(mint_baton_vout_hex, "");

			String mint_baton_vout_str = content.substring(0, mint_baton_vout * 2);

			content = content.replaceFirst(mint_baton_vout_str, "");

			i++;
			map.put(i, mint_baton_vout_str);
			if (!"".equals(content)) {
				contents(content, map, i);
			}

			return  map;

	}


	public String sendRawTransaction(String hex) throws Exception {

		String json = null;

		try {
//			 json = Api.SendRawTransaction(hex);
			 json = "b5c30921ef188dd2907f0895c7de8a8cf85be71780491b940f3fc501aaca311f";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		JSONObject data = Api.GetRawTransaction(json);

		String txid = data.getString("txid");				// 交易txid

		Transaction transaction = new Transaction();
		transaction.setTxid(txid);
		transactionService.insertTransaction(transaction);

		JSONArray vins = data.getJSONArray("vin");

		for(int i = 0; i < vins.size(); i++) {

			JSONObject vin = (JSONObject) vins.get(i);

			String vinTxid = vin.getString("txid");
			Integer vout = vin.getInteger("vout");
			Vin insertVin = new Vin();
			insertVin.setTxid(txid);
			insertVin.setVout(vout);
			insertVin.setVinTxid(vinTxid);
			vinService.insertVin(insertVin);

		}

		JSONArray vouts = data.getJSONArray("vout");

		for(int i = 0; i < vouts.size(); i++) {

			JSONObject vout = (JSONObject) vouts.get(i);

			BigDecimal value = vout.getBigDecimal("value");
			Integer n = vout.getInteger("n");
			JSONObject scriptPubKey = vout.getJSONObject("scriptPubKey");
			JSONArray jsons = scriptPubKey.getJSONArray("addresses");

			if (jsons != null) {

				Vout insertVout = new Vout();
				insertVout.setN(n);
				insertVout.setTxid(txid);
				insertVout.setValue(value);

				voutService.insertVout(insertVout);

				for (int j = 0; j < jsons.size(); j++) {

					String address = (String) jsons.get(j);
					VoutAddress voutAddress = new VoutAddress();
					voutAddress.setAddress(address);
					voutAddress.setTxid(txid);
					voutAddress.setN(n);
					voutAddressService.insertVoutAddress(voutAddress);

				}

			}


		}

		return txid;

	}




	/**
	 * 返回指定的裸交易
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("GetRawTransaction")
	public JSONObject GetRawTransaction(String hex) throws Exception {

		JSONObject json = new JSONObject();

		json = Api.GetRawTransaction(hex);

		return json;

	}

	/**
	 * 签名消息
	 * @param address
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("SignMessage")
	public String SignMessage(String address, String message) throws Exception {

		String json = Api.SignMessage(address, message);

		return json;

	}

	/**
	 * 验证签名的消息
	 * @param address
	 * @param sign
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("VerifyMessage")
	public Boolean VerifyMessage(String address, String sign, String message) throws Exception {

		Boolean json = Api.VerifyMessage(address, sign, message);

		return json;

	}


/*	@ResponseBody
	@RequestMapping("GetBlock")
	public JSONObject GetBlock(String blockHash) throws Exception {

		Block block = Api.GetBlock(blockHash);
		JSONObject ob = new JSONObject();

		ob.put("hash", block.hash());
		ob.put("confirmations", block.confirmations());
		ob.put("size", block.size());
		ob.put("height", block.height());
		ob.put("version", block.version());
		ob.put("merkleRoot", block.merkleRoot());
		ob.put("time", block.time());
		ob.put("bits", block.bits());
		ob.put("difficulty", block.difficulty());
		ob.put("previousHash", block.previousHash());
		ob.put("nextHash", block.nextHash());
		ob.put("chainwork", block.chainwork());
		ob.put("previous", block.previous());
		ob.put("next", block.next());
		ob.put("tx", block.tx());

		return ob;

	}*/

	@ResponseBody
	@RequestMapping("GetBlockHash")
	public String GetBlockHash(Integer height) throws Exception {
		String json = Api.GetBlockHash(height);
		return json;
	}


	@ResponseBody
	@RequestMapping("GetBlockCount")
	public Integer GetBlockCount() throws Exception {
		Integer json = Api.GetBlockCount();
		return json;
	}

	public JSONObject getUxto(JSONArray address) throws Exception {

		JSONObject params = new JSONObject();
		params.put("jsonrpc","1.0");
		params.put("id","curltest");
		params.put("method","getutxo");
		params.put("params", address);

		System.out.println(params);
		String utxos = HttpUtil.doPost(utxoUrl, params.toJSONString());
		System.out.println(utxos);
		JSONObject data = (JSONObject) JSONObject.parse(utxos);
		JSONObject result = data.getJSONObject("result");

		return result;

	}

	public JSONObject getTxidUtxo(JSONArray address) throws Exception {

		JSONObject params = new JSONObject();
		params.put("jsonrpc","1.0");
		params.put("id","curltest");
		params.put("method","gettxidutxo");
		params.put("params", address);

		System.out.println(params);
		String utxos = HttpUtil.doPost(utxoUrl, params.toJSONString());
		System.out.println(utxos);
		JSONObject data = (JSONObject) JSONObject.parse(utxos);
		JSONObject result = data.getJSONObject("result");

		return result;

	}


	public JSONArray getTokenUtxo(JSONArray address) throws Exception {

		JSONObject params = new JSONObject();
		params.put("jsonrpc","1.0");
		params.put("id","curltest");
		params.put("method","gettokenutxo");
		params.put("params", address);

		System.out.println(params);
		String utxos = HttpUtil.doPost(utxoUrl, params.toJSONString());
		System.out.println(utxos);
		JSONObject data = (JSONObject) JSONObject.parse(utxos);
		JSONArray result = data.getJSONArray("result");

		return result;

	}


	public void isLength(String param, Integer length) throws Exception {

		if (param.length() > length)
			throw new ParamException.ParamLengthException(BizExceptionEnum.PARAM_LENGTH_ERROR.getCode(), BizExceptionEnum.PARAM_LENGTH_ERROR.getMessage()+ param );

	}



	/**
	 *
	 * 创建token
	 * @param body
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("issueToken")
	public JsonResult createToken(@RequestBody String body) throws Exception {

			String tokenTicker = null;
			String tokenName = null;
			String tokenUrl = null;
			String tokenHash = null;
			String precition = null;
			String mintQuantity = null;
			String minterAddress = null;
			String address = null;

			try {

				JSONObject jsonData = (JSONObject) JSONObject.parse(body);
				tokenTicker = jsonData.getString("short_name");
				tokenName = jsonData.getString("full_name");
				tokenUrl = jsonData.getString("url");
				tokenHash = jsonData.getString("hash");
				precition = jsonData.getString("precition");
				mintQuantity = jsonData.getString("quantity");
				minterAddress = jsonData.getString("minter_address");
				address = jsonData.getString("issuer_address");

			} catch (Exception e) {

				e.printStackTrace();
				return new JsonResult(BizExceptionEnum.REQUEST_INVALIDATE.getCode(), BizExceptionEnum.REQUEST_INVALIDATE.getMessage());

			}

			if (!StringUtils.isNotBlank(tokenTicker) || !StringUtils.isNotBlank(tokenName) || !StringUtils.isNotBlank(tokenUrl) || !StringUtils.isNotBlank(tokenHash) ||
					!StringUtils.isNotBlank(precition) || !StringUtils.isNotBlank(mintQuantity) || !StringUtils.isNotBlank(minterAddress) || !StringUtils.isNotBlank(address))
				return new JsonResult(BizExceptionEnum.REQUEST_INVALIDATE.getCode(), BizExceptionEnum.REQUEST_INVALIDATE.getMessage());

			isLength(tokenTicker, 32);
			isLength(tokenName, 255);
			isLength(tokenUrl, 1024);
			isLength(tokenHash, 255);
			isLength(precition, 2);

			BigInteger TotalLong = null;

			if (!StringUtils.isNumeric(precition) || !StringUtils.isNumeric(mintQuantity)) {
				return new JsonResult(BizExceptionEnum.NOT_INT_ERROR.getCode(), BizExceptionEnum.NOT_INT_ERROR.getMessage());
			}

			Integer decimals = Integer.valueOf(precition);

			try {
				TotalLong = new BigInteger(mintQuantity);
			} catch (Exception e) {
				return new JsonResult(BizExceptionEnum.LONG_ERROR.getCode(), BizExceptionEnum.LONG_ERROR.getMessage());
			}

			// 查询发行地址的钱
            JSONArray selectKycaddressJons = new JSONArray();
            selectKycaddressJons.add(address);
            selectKycaddressJons.add(0.0000055);
            JSONArray selectKycaddress = new JSONArray();
            selectKycaddress.add(selectKycaddressJons);
			JSONObject selectToken = getUxto(selectKycaddress);

			JSONArray tokenDatas = selectToken.getJSONArray(address);
			BigDecimal value3 = new BigDecimal("0");
			for (Object d : tokenDatas) {
				JSONArray dd = (JSONArray) d;
				value3 = value3.add(dd.getBigDecimal(2));
			}

			// 如果发行地址没有钱
			String tx = null;
			if (selectToken == null || value3.compareTo(new BigDecimal("0.0000055")) < 0) {

				// 系统地址向该发行地址打一笔钱用来该地址发行时使用
				tx = createTransaction(address);
				if (tx == null) {
					return new JsonResult(BizExceptionEnum.TOKEN_RECHARGE_ERROR.getCode(), BizExceptionEnum.TOKEN_RECHARGE_ERROR.getMessage());
				}

			}


			List<TxInputDto> input = new ArrayList<>();
			List<TxOutputDto> output = new ArrayList<>();

            JSONArray addressJons = new JSONArray();
            addressJons.add(address);
			addressJons.add(0.00000550);
            JSONArray systemAddressJons = new JSONArray();
            systemAddressJons.add(systemAddress);
            systemAddressJons.add(0.00001550);

            JSONArray addresss = new JSONArray();
            addresss.add(addressJons);
            addresss.add(systemAddressJons);

			JSONObject results = getUxto(addresss);

            txInput(input, results, address);						// 拿取当前发行地址的钱

			JSONArray systemDatas = results.getJSONArray(systemAddress);
			if (systemDatas == null)
				return new JsonResult(BizExceptionEnum.SYSTEM_ADDRESS_UTXO_ERROR.getCode(), BizExceptionEnum.SYSTEM_ADDRESS_UTXO_ERROR.getMessage());


            BigDecimal value2 = txInput(input, results, systemAddress);									 // 查询系统地址的钱
			TxOutputDto da1 = new TxOutputDto(address, new BigDecimal("0.00000550"));                // 向发行地址打钱
			TxOutputDto da2 = new TxOutputDto(minterAddress, new BigDecimal("0.00000550"));          // 给增发权限地址打钱
			BigDecimal v = value2.subtract(new BigDecimal("0.00001")).subtract(new BigDecimal("0.00000550"));			//系统地址找零
			TxOutputDto da3 = new TxOutputDto(systemAddress, v);                // 找零系统地址

            String opretrun = creategenesistoken(1, "GENESIS", tokenTicker, tokenName, tokenUrl, tokenHash, decimals, 2, TotalLong);
			TxOutputDto d = new TxOutputDto(opretrun);
			output.add(da1);
			output.add(da2);
			output.add(da3);
			output.add(d);

			String createRawTransaction = Api.CreateRawTransaction(input, output);
			if (createRawTransaction == null)
				return new JsonResult(BizExceptionEnum.CREATE_RAW_TRANSACTION_ERROR.getCode(), BizExceptionEnum.CREATE_RAW_TRANSACTION_ERROR.getMessage());

			String sign = Api.SignRawTransaction(createRawTransaction);
			if (sign == null)
				return new JsonResult(BizExceptionEnum.SIGN_RAW_TRANSACTION_ERROR.getCode(), BizExceptionEnum.SIGN_RAW_TRANSACTION_ERROR.getMessage());

			return new JsonResult().addData("hex", sign);

	}

	/**
	 * 增发token
	 * @param body
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("mintToken")
	public JsonResult createMintToken(@RequestBody String body) {

		try {

			String tokenId = null;
			String tokenMintTotal = null;
			String issueAddress = null;
			String address = null;

			try {

				JSONObject jsonData = (JSONObject) JSONObject.parse(body);
				tokenId = jsonData.getString("token_id");
				tokenMintTotal = jsonData.getString("mint_total");
				issueAddress = jsonData.getString("minter_address");
				address = jsonData.getString("issuer_address");

			} catch (Exception e) {
				e.printStackTrace();
				return new JsonResult(BizExceptionEnum.REQUEST_INVALIDATE.getCode(), BizExceptionEnum.REQUEST_INVALIDATE.getMessage());
			}

			if (!StringUtils.isNotBlank(tokenId) || !StringUtils.isNotBlank(tokenMintTotal) || !StringUtils.isNotBlank(issueAddress) || !StringUtils.isNotBlank(address))
				return new JsonResult(BizExceptionEnum.REQUEST_INVALIDATE.getCode(), BizExceptionEnum.REQUEST_INVALIDATE.getMessage());

			BigInteger TotalLong;

			try {
				TotalLong = new BigInteger(tokenMintTotal);
			} catch (Exception e) {
				return new JsonResult(BizExceptionEnum.LONG_ERROR.getCode(), BizExceptionEnum.LONG_ERROR.getMessage());
			}


			GenesisAddress genesisAddress = genesisAddressService.findByTxidAndRaiseVout(tokenId, 2);

			if (genesisAddress == null) {
				return new JsonResult(BizExceptionEnum.NOT_TOKEN_ERROR.getCode(), BizExceptionEnum.NOT_TOKEN_ERROR.getMessage());
			}

			if (!address.equals(genesisAddress.getRaiseAddress())) {
				return new JsonResult(BizExceptionEnum.TOKEN_NO_AUTHORITY_ERROR.getCode(), BizExceptionEnum.TOKEN_NO_AUTHORITY_ERROR.getMessage());
			}

           String opretrun = minttoken(1,"MINT", tokenId, 2, TotalLong);

			List<TxInputDto> input = new ArrayList<>();
			List<TxOutputDto> output = new ArrayList<>();

            JSONArray addressJons = new JSONArray();
            addressJons.add(address);
            addressJons.add(genesisAddress.getRaiseTxid());
			JSONArray mintTokenUtxo = new JSONArray();
			mintTokenUtxo.add(addressJons);
			JSONObject mintTokenResults = getTxidUtxo(mintTokenUtxo);
			if (mintTokenResults.getString(address) == null)
				return new JsonResult(BizExceptionEnum.MINT_ADD_TOKEN_LOSS_ERROR.getCode(), BizExceptionEnum.MINT_ADD_TOKEN_LOSS_ERROR.getMessage());
			txInput(input, mintTokenResults, address);


            JSONArray systemAddressJons = new JSONArray();
            systemAddressJons.add(systemAddress);
            systemAddressJons.add(0.00001550);
            JSONArray addresss = new JSONArray();
            addresss.add(systemAddressJons);
			JSONObject results = getUxto(addresss);
			JSONArray systemDatas = results.getJSONArray(systemAddress);
			if (systemDatas == null)
				return new JsonResult(BizExceptionEnum.SYSTEM_ADDRESS_UTXO_ERROR.getCode(), BizExceptionEnum.SYSTEM_ADDRESS_UTXO_ERROR.getMessage());

            BigDecimal value2 = txInput(input, results, systemAddress);


			TxOutputDto da1 = new TxOutputDto(address, new BigDecimal("0.00000550"));                // 自身地址

			TxOutputDto da2 = new TxOutputDto(issueAddress, new BigDecimal("0.00000550"));            // 固定的增发地址

			BigDecimal v = value2.subtract(new BigDecimal("0.00001")).subtract(new BigDecimal("0.00000550"));

			TxOutputDto da3 = new TxOutputDto(systemAddress, v);                // 找零系统地址

			TxOutputDto d = new TxOutputDto(opretrun);

			output.add(da1);
			output.add(da2);
			output.add(da3);
			output.add(d);

			String createRawTransaction = Api.CreateRawTransaction(input, output);

			if (createRawTransaction == null)
				return new JsonResult(BizExceptionEnum.CREATE_RAW_TRANSACTION_ERROR.getCode(), BizExceptionEnum.CREATE_RAW_TRANSACTION_ERROR.getMessage());

			String sign = Api.SignRawTransaction(createRawTransaction);

			if (sign == null)
				return new JsonResult(BizExceptionEnum.SIGN_RAW_TRANSACTION_ERROR.getCode(), BizExceptionEnum.SIGN_RAW_TRANSACTION_ERROR.getMessage());

			return new JsonResult().addData("hex", sign);

		} catch (Exception e){

			e.printStackTrace();
			return new JsonResult(BizExceptionEnum.SERVER_ERROR.getCode(), BizExceptionEnum.SERVER_ERROR.getMessage());

		}

	}

	/**
	 * 发送token
	 * @param body
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("sendToken")
	public JsonResult createSendToken(@RequestBody String body) {

		try {

			JSONObject jsonData = (JSONObject) JSONObject.parse(body);

			String address = jsonData.getString("transfer_from_address");

			String tokenId = jsonData.getString("token_id");
			JSONArray dataJSONArray = jsonData.getJSONArray("transfer_to");

			if (address == null || tokenId == null)
				return new JsonResult(BizExceptionEnum.REQUEST_INVALIDATE.getCode(), BizExceptionEnum.REQUEST_INVALIDATE.getMessage());


			Slp slp = slpService.findByTokenId(tokenId);

			if (slp == null)
				return new JsonResult(BizExceptionEnum.TOKEN_ISNULL_ERROR.getCode(), BizExceptionEnum.TOKEN_ISNULL_ERROR.getMessage());

			BigInteger q = new BigInteger("0");
			String opretrun = snedToken(1, "sned", tokenId, dataJSONArray, q);
			if (opretrun == null)
				return new JsonResult(BizExceptionEnum.MONEY_IS_NOT_ENOUGH_ERROR.getCode(), BizExceptionEnum.MONEY_IS_NOT_ENOUGH_ERROR.getMessage());


			List<TokenAssets> tokenAssetsList = tokenAssetsService.selectToken(tokenId, address);
			List<TokenAssets> addLis = new ArrayList<>();
			for (TokenAssets tokenAssets: tokenAssetsList) {
				if (tokenAssets.getToken().compareTo(q) >= 0) {
					addLis.add(tokenAssets);
					break;
				} else {
					addLis.add(tokenAssets);
				}
			}



			List<TxInputDto> input = new ArrayList<>();
			List<TxOutputDto> output = new ArrayList<>();

			JSONArray addressJons = new JSONArray();
			JSONArray sendTokenUtxo = new JSONArray();
			for (TokenAssets tokenAssets: addLis) {
				addressJons.add(address);
				addressJons.add(tokenAssets.getTxid());
				sendTokenUtxo.add(addressJons);
			}

			JSONObject sendTokenUtxoResults = getTxidUtxo(sendTokenUtxo);
			if (sendTokenUtxoResults.getString(address) == null)
				return new JsonResult(BizExceptionEnum.MINT_ADD_TOKEN_LOSS_ERROR.getCode(), BizExceptionEnum.MINT_ADD_TOKEN_LOSS_ERROR.getMessage());
			BigDecimal value1 = txInput(input, sendTokenUtxoResults, address);
			System.out.println("发送地址value="+value1);


			BigDecimal f1 = new BigDecimal("0.00000550");
			BigDecimal f2 = new BigDecimal("0");

			TxOutputDto da4 = new TxOutputDto(address, new BigDecimal("0.00000550"));			// 给打钱地址找零
			output.add(da4);

			for (int i = 0; i < dataJSONArray.size(); i++) {
				JSONObject dd = (JSONObject) dataJSONArray.get(i);
				KycAddress inputAddress = kycAddressService.selectAddress(dd.getString("account_address"));
                f2 = f2.add(f1);
				TxOutputDto da = new TxOutputDto(inputAddress.getAddress(), f1);                // 向这地址打钱
				output.add(da);

			}

			JSONArray systemAddressJons = new JSONArray();
			systemAddressJons.add(systemAddress);
			systemAddressJons.add(0.00001);
			JSONArray addresss = new JSONArray();
			addresss.add(systemAddressJons);
			JSONObject results = getUxto(addresss);
			JSONArray systemDatas = results.getJSONArray(systemAddress);
			if (systemDatas == null)
				return new JsonResult(BizExceptionEnum.SYSTEM_ADDRESS_UTXO_ERROR.getCode(), BizExceptionEnum.SYSTEM_ADDRESS_UTXO_ERROR.getMessage());
			BigDecimal value2 = txInput(input, results, systemAddress);
			System.out.println("系统地址value="+value2);

			BigDecimal v = value1.add(value2).subtract(f2).subtract(new BigDecimal("0.00001")).subtract(new BigDecimal("0.00000550"));

			TxOutputDto da3 = new TxOutputDto(systemAddress, v);                          // 找零系统地址
			output.add(da3);
			TxOutputDto d = new TxOutputDto(opretrun);
			output.add(d);

			String createRawTransaction = Api.CreateRawTransaction(input, output);

			if (createRawTransaction == null)
				return new JsonResult(BizExceptionEnum.CREATE_RAW_TRANSACTION_ERROR.getCode(), BizExceptionEnum.CREATE_RAW_TRANSACTION_ERROR.getMessage());

			String sign = Api.SignRawTransaction(createRawTransaction);

			if (sign == null)
				return new JsonResult(BizExceptionEnum.SIGN_RAW_TRANSACTION_ERROR.getCode(), BizExceptionEnum.SIGN_RAW_TRANSACTION_ERROR.getMessage());

			return new JsonResult().addData("hex", sign);

		} catch (Exception e) {

			e.printStackTrace();
			return new JsonResult(BizExceptionEnum.SERVER_ERROR.getCode(), BizExceptionEnum.SERVER_ERROR.getMessage());

		}

	}


    public static BigDecimal txInput(List<TxInputDto> input, JSONObject results, String address) {

        JSONArray datas = results.getJSONArray(address);

        BigDecimal value = new BigDecimal("0");

        for (Object d : datas) {

            JSONArray dd = (JSONArray) d;

            String txid = (String) dd.get(0);
            Integer vout = (Integer) dd.get(1);
            value = value.add(dd.getBigDecimal(2));
            TxInputDto txinputDto = new TxInputDto(txid, vout, address);
            input.add(txinputDto);

        }

        return value;
    }




	/**
	 * 认证kyc
	 * @param body
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("kyc")
	public JsonResult addressKYC(@RequestBody String body) {

		JSONObject jsonData = (JSONObject) JSONObject.parse(body);
		String address = jsonData.getString("address");
		String name = jsonData.getString("name");
		String IDnumber = jsonData.getString("ID_number");

		KycAddress kycAddress = kycAddressService.selectAddress(address);

		if (kycAddress != null)
			return new JsonResult(BizExceptionEnum.ADDRESS_REPEAT_ERROR.getCode(), BizExceptionEnum.ADDRESS_REPEAT_ERROR.getMessage());

		KycAddress KycAddress = new KycAddress();
		KycAddress.setAddress(address);
		KycAddress.setName(name);
		KycAddress.setIdNumber(IDnumber);
		kycAddressService.insertKycAddress(KycAddress);

		return new JsonResult();

	}


	/**
	 * 查询用户tonken
	 * @param tokenId
	 * @param address
	 * @return
	 */
	@ResponseBody
	@RequestMapping("queryToken")
	public JsonResult selectToken(String tokenId, String address) {

		if (tokenId != null && address != null) {

			AddressHashLink addressHashLink = addressHashLinkService.findByAddress(address);

			if (addressHashLink == null)
				return new JsonResult(BizExceptionEnum.ADDRESS_HASH_ERROR.getCode(), BizExceptionEnum.ADDRESS_HASH_ERROR.getMessage());

			BigInteger token = tokenAssetsService.selectAddressToken(tokenId, addressHashLink.getAddressHash());

			Slp slp = slpService.findByTokenId(tokenId);
			if (token != null && !"".equals(token) && slp != null) {
				BigInteger fromToken = tokenAssetsService.selectFromAddressToken(tokenId, addressHashLink.getAddressHash());
				if (fromToken != null && !"".equals(token)) {
					BigInteger newToken = token.subtract(fromToken);
					return new JsonResult().addData("token", newToken).addData("precition", slp.getTokenDecimal());
				} else {
					return new JsonResult().addData("token", token).addData("precition", slp.getTokenDecimal());
				}
			} else
				return new JsonResult().addData("token", 0).addData("precition", 0);
		} else {
			return new JsonResult(BizExceptionEnum.REQUEST_INVALIDATE.getCode(), BizExceptionEnum.REQUEST_INVALIDATE.getMessage());
		}

	}

	/**
	 * 查询所有tonken
	 * @param
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("queryTokenInfoList")
	public JsonResult queryTokenInfoList(Integer page) {

		page = page - 1;
		Integer limit = 10;
		Integer offset = page * limit;

		Map<String,Object> params = Maps.newHashMap();
		params.put("offset",offset);
		params.put("limit",limit);

		List<Slp> slpLisst = slpService.queryTokenInfoList(params);
		long total = slpService.queryTokenInfoCount(params);

		long size;
		if (total % limit == 0){
			size = total/limit;
		} else {
			size = total/limit + 1;
		}

		return new JsonResult().addData("list", slpLisst).addData("total",String.valueOf(total)).addData("page",String.valueOf(size));

	}


	public String createTransaction(String toAddress) throws Exception {

			List<TxInputDto> input = new ArrayList<>();
			List<TxOutputDto> output = new ArrayList<>();

            JSONArray systemAddressJons = new JSONArray();
            systemAddressJons.add(systemAddress);
            systemAddressJons.add(0.0000155);

			JSONArray addresss = new JSONArray();
            addresss.add(systemAddressJons);
			JSONObject results = getUxto(addresss);

            BigDecimal value2 = txInput(input, results, systemAddress);

			BigDecimal v = value2.subtract(new BigDecimal("0.0000155"));
			TxOutputDto da3 = new TxOutputDto(systemAddress, v);                // 找零系统地址
			output.add(da3);
			TxOutputDto da4 = new TxOutputDto(toAddress, new BigDecimal("0.00000550"));                // 给发行地址打钱
			output.add(da4);

			String createRawTransaction = Api.CreateRawTransaction(input, output);

			String hex = Api.SignRawTransaction(createRawTransaction);

			String txid = Api.SendRawTransaction(hex);

		return txid;

	}


	// 创建发行
	public String creategenesistoken(Integer tokenType, String transactionType, String tokenTicker, String tokenName, String tokenUrl, String tokenHash,
								 Integer precition,  Integer mintBatoVout, BigInteger mintQuantity) {

		if (tokenType < 0 || tokenType > 9999) {
			// 超出范围
		} else if (tokenTicker == null) {
			// 不能为空
		} else if (tokenName == null) {
			// 不能为空
		} else if (tokenUrl == null) {
			// 不能为空
		} else if (precition < 0 || precition > 9) {
			// 超出范围
		} else if (mintBatoVout < 2 || mintBatoVout > 255) {
			// 超出范围
		} else if (mintQuantity.compareTo(new BigInteger("0")) < 0|| mintQuantity.compareTo(new BigInteger("9999999999999999")) > 0) {
			// 超出范围
		}

		String tokenTypeHex = UnicodeUtil.intToHex(tokenType);
		String transactionTypeHex = UnicodeUtil.str2HexStr(transactionType);
		String tokenTickerHex = UnicodeUtil.str2HexStr(tokenTicker);
		String tokenNameHex = UnicodeUtil.str2HexStr(tokenName);
		String tokenUrlHex = UnicodeUtil.str2HexStr(tokenUrl);
		String tokenHashHex = UnicodeUtil.str2HexStr(tokenHash);
		String precitionHex = UnicodeUtil.intToHex(precition);
		String mintBatoVoutHex = UnicodeUtil.intToHex(mintBatoVout);
		String mintQuantityHex = new BigInteger(mintQuantity.toString(),10).toString(16);

		String aa = Integer.toHexString(tokenType);
		String bb = Integer.toHexString(precition);
		String cc = Integer.toHexString(mintBatoVout);

		String tokenTypeInt = UnicodeUtil.intToHex(aa.getBytes().length);
		String transactionTypeInt = UnicodeUtil.intToHex(transactionType.getBytes().length);
		String tokenTickerInt = UnicodeUtil.intToHex(tokenTicker.getBytes().length);
		String tokenNameInt = UnicodeUtil.intToHex(tokenName.getBytes().length);
		String tokenUrlInt = UnicodeUtil.intToHex(tokenUrl.getBytes().length);
		String tokenHashInt = UnicodeUtil.intToHex(tokenHash.getBytes().length);
		String precitionInt = UnicodeUtil.intToHex(bb.getBytes().length);
		String mintBatoVoutInt = UnicodeUtil.intToHex(cc.getBytes().length);
		if (mintQuantityHex.length()%2 > 0) {
			mintQuantityHex = "0"+mintQuantityHex;
		}

		String mintQuantityInt = UnicodeUtil.intToHex(mintQuantityHex.length()/2);


		String a = "04534c5000"+tokenTypeInt+tokenTypeHex+transactionTypeInt+transactionTypeHex+tokenTickerInt+tokenTickerHex+tokenNameInt+tokenNameHex
				+tokenUrlInt+tokenUrlHex+tokenHashInt+tokenHashHex+precitionInt+precitionHex+mintBatoVoutInt+mintBatoVoutHex+mintQuantityInt+mintQuantityHex;


		return a;

	}

	//解析发行
	public Slp decodeGenesistoken(String opturn, Map<Integer, List<String>> map) {

		String OP_RETURN = opturn.replaceFirst("0 OP_RETURN ", "");
		String genesis = OP_RETURN.substring(0, 30);

		if (!"04534c500001010747454e45534953".equals(genesis)) {
			// 格式错误
		}
		String content = OP_RETURN.replaceFirst("04534c500001010747454e45534953", "");
		String token_ticker_hex = content.substring(0, 2);
		Integer token_ticker = UnicodeUtil.decodeHEX(token_ticker_hex);
		content = content.replaceFirst(token_ticker_hex, "");
		String token_ticker_str = content.substring(0, token_ticker * 2);

		if (token_ticker_str == null) {
			// 不能为空
		}
		content = content.replaceFirst(token_ticker_str, "");				// 清空掉token_ticker
		String tokenTicker = UnicodeUtil.hexStringToString(token_ticker_str);



		String token_name_hex = content.substring(0, 2);
		Integer token_name = UnicodeUtil.decodeHEX(token_name_hex);
		content = content.replaceFirst(token_name_hex, "");
		String token_name_str = content.substring(0, token_name * 2);

		if (token_name_str == null) {
			// 不能为空
		}
		content = content.replaceFirst(token_name_str, "");				// 清空掉token_name
		String tokenName = UnicodeUtil.hexStringToString(token_name_str);

		String token_document_url_hex = content.substring(0, 2);
		Integer token_document_url = UnicodeUtil.decodeHEX(token_document_url_hex);
		content = content.replaceFirst(token_document_url_hex, "");
		String token_document_url_str = content.substring(0, token_document_url * 2);

		if (token_document_url_str == null) {
			// 不能为空
		}
		content = content.replaceFirst(token_document_url_str, "");		// 清空掉url
		String tokenUrl = UnicodeUtil.hexStringToString(token_document_url_str);

		String token_document_hash_hex = content.substring(0, 2);
		Integer token_document_hash = UnicodeUtil.decodeHEX(token_document_hash_hex);
		content = content.replaceFirst(token_document_hash_hex, "");
		String token_document_hash_str = content.substring(0, token_document_hash * 2);

		if (token_document_hash_str == null) {
			// 不能为空
		}
		content = content.replaceFirst(token_document_hash_str, "");		// 清空掉hash
		String tokenHash = UnicodeUtil.hexStringToString(token_document_hash_str);


		String byte_length_hex = content.substring(0, 2);
		Integer byte_length = UnicodeUtil.decodeHEX(byte_length_hex);
		content = content.replaceFirst(byte_length_hex, "");
		String byte_length_str = content.substring(0, byte_length * 2);

		if (byte_length < 0 || byte_length > 9) {
			// 超出范围
		}
		content = content.replaceFirst(byte_length_str, "");			    // 清空掉精度precition
		Integer precition = new BigInteger(byte_length_str, 16).intValue();

		String mint_baton_vout_hex = content.substring(0, 2);
		Integer mint_baton_vout = UnicodeUtil.decodeHEX(mint_baton_vout_hex);
		content = content.replaceFirst(mint_baton_vout_hex, "");
		String mint_baton_vout_str = content.substring(0, mint_baton_vout * 2);

		if (mint_baton_vout < 2 || mint_baton_vout > 255) {
			// 超出范围
		}
		content = content.replaceFirst(mint_baton_vout_str, "");			//清空掉vout
		Integer mintVout = new BigInteger(mint_baton_vout_str, 16).intValue();

		String initial_token_mint_quantity_hex = content.substring(0, 2);
		Integer initial_token_mint_quantity = UnicodeUtil.decodeHEX(initial_token_mint_quantity_hex);
		content = content.replaceFirst(initial_token_mint_quantity_hex, "");
		String initial_token_mint_quantity_str = content.substring(0, initial_token_mint_quantity * 2);
		BigInteger quantity = new BigInteger(initial_token_mint_quantity_str, 16);

		if (quantity.compareTo(new BigInteger("0")) < 0|| quantity.compareTo(new BigInteger("9999999999999999")) > 0) {
			// 超出范围
		}

		content = content.replaceFirst(initial_token_mint_quantity_str, "");				// 清空掉mintQuantity
		if (!"".equals(content)) {
			// 格式错误
		}


		Slp slp = new Slp();
		slp.setTokenTicker(tokenTicker);
		slp.setTokenName(tokenName);
		slp.setTokenDocumentUrl(tokenUrl);
		slp.setTokenDocumentHash(tokenHash);
		slp.setTokenDecimal(precition);
		slp.setMintBatonVout(mintVout);
		slp.setTransactionType("GENESIS");
		slp.setOriginalAddress(map.get(0).get(0));							// 增发地址
		slp.setInitIssueAddress(map.get(mintVout-1).get(0));				// 增发权限地址
		slp.setInitialTokenMintQuantity(quantity.toString());

		return slp;

	}

	// 创建增发
	public String minttoken(Integer tokenType, String transactionType, String tokenId, Integer mintBatoVout, BigInteger mintQuantity) {

		if (tokenType < 0 || tokenType > 9999) {
			// 超出范围
		}  else if (mintBatoVout < 2 || mintBatoVout > 255) {
			// 超出范围
		} else if (mintQuantity.compareTo(new BigInteger("0")) < 0|| mintQuantity.compareTo(new BigInteger("9999999999999999")) > 0) {
			// 超出范围
		}

		String tokenTypeHex = UnicodeUtil.intToHex(tokenType);
		String transactionTypeHex = UnicodeUtil.str2HexStr(transactionType);
		String mintBatoVoutHex = UnicodeUtil.intToHex(mintBatoVout);
		String mintQuantityHex = new BigInteger(mintQuantity.toString(),10).toString(16);

		String aa = Integer.toHexString(tokenType);
		String cc = Integer.toHexString(mintBatoVout);

		String tokenTypeInt = UnicodeUtil.intToHex(aa.getBytes().length);
		String transactionTypeInt = UnicodeUtil.intToHex(transactionType.getBytes().length);
		String tokenIdInt = UnicodeUtil.intToHex(tokenId.getBytes().length/2);
		String mintBatoVoutInt = UnicodeUtil.intToHex(cc.getBytes().length);
		if (mintQuantityHex.length()%2 > 0) {
			mintQuantityHex = "0"+mintQuantityHex;
		}

		String mintQuantityInt = UnicodeUtil.intToHex(mintQuantityHex.length()/2);
		String a = "04534c5000"+tokenTypeInt+tokenTypeHex+transactionTypeInt+transactionTypeHex+tokenIdInt+tokenId+mintBatoVoutInt+mintBatoVoutHex+mintQuantityInt+mintQuantityHex;

		return a;

	}


	public SlpMint decodeMinttoken(String opturn) {

		String OP_RETURN = opturn.replaceFirst("0 OP_RETURN ", "");
		String mint = OP_RETURN.substring(0, 24);

		if (!"04534c50000101044d494e54".equals(mint)) {
			// 格式错误
		}
		String content = OP_RETURN.replaceFirst("04534c50000101044d494e54", "");

		String token_id_hex = content.substring(0, 2);
		Integer token_id = UnicodeUtil.decodeHEX(token_id_hex);

		content = content.replaceFirst(token_id_hex, "");
		String token_id_str = content.substring(0, token_id * 2);				//token_id

		content = content.replaceFirst(token_id_str, "");				// 清空掉token_id


		String mint_baton_vout_hex = content.substring(0, 2);
		Integer mint_baton_vout = UnicodeUtil.decodeHEX(mint_baton_vout_hex);
		content = content.replaceFirst(mint_baton_vout_hex, "");
		String mint_baton_vout_str = content.substring(0, mint_baton_vout * 2);		// vout

		if (mint_baton_vout < 2 || mint_baton_vout > 255) {
			// 超出范围
		}
		content = content.replaceFirst(mint_baton_vout_str, "");			//清空掉vout

		Integer mintVout = new BigInteger(mint_baton_vout_str, 16).intValue();

		String initial_token_mint_quantity_hex = content.substring(0, 2);
		Integer initial_token_mint_quantity = UnicodeUtil.decodeHEX(initial_token_mint_quantity_hex);
		content = content.replaceFirst(initial_token_mint_quantity_hex, "");
		String initial_token_mint_quantity_str = content.substring(0, initial_token_mint_quantity * 2);
		BigInteger quantity = new BigInteger(initial_token_mint_quantity_str, 16);

		if (quantity.compareTo(new BigInteger("0")) < 0|| quantity.compareTo(new BigInteger("9999999999999999")) > 0) {
			// 超出范围
		}

		content = content.replaceFirst(initial_token_mint_quantity_str, "");				// 清空掉mintQuantity
		if (!"".equals(content)) {
			// 格式错误
		}

		SlpMint slpMint = new SlpMint();
		slpMint.setTransactionType("mint");
		slpMint.setTokenId(token_id_str);
		slpMint.setMintBatonVout(mintVout);
		slpMint.setAdditionalTokenQuantity(quantity.toString());
		return slpMint;

	}


	// 创建发送交易
	public String snedToken(Integer tokenType, String transactionType, String tokenId, JSONArray quantitys, BigInteger q) {

		if (tokenType < 0 || tokenType > 9999) {
			// 超出范围
		}

		String tokenTypeHex = UnicodeUtil.intToHex(tokenType);
		String transactionTypeHex = UnicodeUtil.str2HexStr(transactionType);

		String aa = Integer.toHexString(tokenType);
		String tokenTypeInt = UnicodeUtil.intToHex(aa.getBytes().length);
		String transactionTypeInt = UnicodeUtil.intToHex(transactionType.getBytes().length);
		String tokenIdInt = UnicodeUtil.intToHex(tokenId.getBytes().length/2);

		String quantitysStr = "";
		for (int i = 0; i < quantitys.size(); i++) {
			JSONObject data = (JSONObject) quantitys.get(i);
			BigInteger quantity = data.getBigInteger("amount");

			String mintQuantityHex = new BigInteger(quantity.toString(),10).toString(16);
			if (mintQuantityHex.length()%2 > 0) {
				mintQuantityHex = "0"+mintQuantityHex;
			}
			q = q.add(quantity);
			String mintQuantityInt = UnicodeUtil.intToHex(mintQuantityHex.length()/2);
			quantitysStr += mintQuantityInt+mintQuantityHex;

		}

		String a = "04534c5000"+tokenTypeInt+tokenTypeHex+transactionTypeInt+transactionTypeHex+tokenIdInt+tokenId+quantitysStr;

		return a;

	}


	//解析发行
	public List<SlpSend> decodeSnedToken(String opturn, Map<Integer, List<String>> map) {

		String OP_RETURN = opturn.replaceFirst("0 OP_RETURN ", "");
		String send = OP_RETURN.substring(0, 24);

		if (!"04534c5000010104736e6564".equals(send)) {
			// 格式错误
		}
		String content = OP_RETURN.replaceFirst("04534c5000010104736e6564", "");

		String token_id_hex = content.substring(0, 2);

		Integer token_id = UnicodeUtil.decodeHEX(token_id_hex);				// 获取token

		content = content.replaceFirst(token_id_hex, "");
		String token_id_str = content.substring(0, token_id * 2);
		content = content.replaceFirst(token_id_str, "");

		Map<Integer, String> maps = contents(content, new HashedMap(), 0);

		List<SlpSend> snedVoutList = new ArrayList<>();
		for(Map.Entry<Integer, String> entry : maps.entrySet()){
			Integer mapKey = entry.getKey();
			BigInteger quantity = new BigInteger(entry.getValue(), 16);
			if (quantity.compareTo(new BigInteger("0")) < 0|| quantity.compareTo(new BigInteger("9999999999999999")) > 0) {
				// 超出范围
			}

			SlpSend slpSend = new SlpSend();
			slpSend.setTokenId(token_id_str);
			slpSend.setTokenOutputQuantity(quantity.toString());
			slpSend.setVout(mapKey);
			slpSend.setAddress(map.get(mapKey).get(0));
			snedVoutList.add(slpSend);

		}


		return snedVoutList;

	}


	@ResponseBody
	@RequestMapping("getUtxo")
	public JsonResult getUtxoApi(String address) throws Exception {

		JSONArray jsons = new JSONArray();
		JSONArray js = new JSONArray();
		js.add(address);
		js.add(-1);
		jsons.add(js);
		JSONObject jsonObject = getUxto(jsons);
		JSONArray uxs = jsonObject.getJSONArray(address);

		if (uxs == null) {
			return new JsonResult().addData("utxo","");
		} else {
			return new JsonResult().addData("utxo",uxs.toJSONString());
		}

	}

	@ResponseBody
	@RequestMapping("getTokenUtxo")
	public JsonResult getTokenUtxo(String address) throws Exception {


		JSONArray js = new JSONArray();
		js.add(address);

		JSONArray jsonObject = getTokenUtxo(js);

		if (jsonObject == null) {
			return new JsonResult().addData("utxo","");
		} else {
			return new JsonResult().addData("utxo",jsonObject.toJSONString());
		}

	}



}
