package com.slppp.app.modular.api;

import com.google.common.collect.Maps;
import com.slppp.app.config.shiro.security.JwtUtil;
import com.slppp.app.core.constant.SecurityConsts;
import com.slppp.app.modular.api.vo.TokenHistory;
import com.slppp.app.modular.system.model.*;
import com.slppp.app.modular.system.service.*;
import com.slppp.app.core.common.exception.BizExceptionEnum;
import com.slppp.app.core.rpc.Api;
import com.slppp.app.core.util.JsonResult;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;


import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Controller
@RequestMapping("/rest/Api")
public class ApiController {

	String uploadPath =  "/java/upay/upload-images/";


	@Autowired
	private SlpService slpService;

	@Autowired
	private TokenAssetsService tokenAssetsService;

	@Autowired
	private KycAddressService kycAddressService;

	@Autowired
	private AddressHashLinkService addressHashLinkService;

	@Autowired
	private UtxoTokenService utxoTokenService;

	@Autowired
	private UtxoService utxoService;

	@Autowired
	private MemberService memberService;


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
	public JsonResult SendRawTransaction(@RequestBody JSONObject JSONObject) throws Exception {

			String hex = JSONObject.getString("hex");
			String txid = Api.SendRawTransaction(hex);
			return new JsonResult().addData("txid", txid);

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

		JSONObject json = Api.GetRawTransaction(hex);
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



	/**
	 * 获取文件后缀名
	 *
	 * @param fileName
	 * @return
	 */
	private String getFileType(String fileName) {
		if (fileName != null && fileName.indexOf(".") >= 0) {
			return fileName.substring(fileName.lastIndexOf("."), fileName.length());
		}
		return "";
	}

	private Boolean isImageFile(String fileName) {
		String[] img_type = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
		if (fileName == null) {
			return false;
		}
		fileName = fileName.toLowerCase();
		for (String type : img_type) {
			if (fileName.endsWith(type)) {
				return true;
			}
		}
		return false;
	}

	@ResponseBody
	@RequestMapping(value="/pidImgUpload", method = RequestMethod.POST)
	public JsonResult pidImgUpload(@RequestParam(value = "pidImg") MultipartFile pidImg) {

		String img2 = null;

		File uploadDirectory = new File(uploadPath);

		if (uploadDirectory.exists()) {
			if (!uploadDirectory.isDirectory()) {
				uploadDirectory.delete();
			}
		} else {
			uploadDirectory.mkdir();
		}

		BufferedOutputStream bw = null;

		try {

			String fileName2 = pidImg.getOriginalFilename();
			//判断是否有文件且是否为图片文件
			if(fileName2!=null && !"".equalsIgnoreCase(fileName2.trim()) && isImageFile(fileName2)) {
				//创建输出文件对象
				String name = UUID.randomUUID().toString()+ getFileType(fileName2);
				File outFile = new File(uploadPath+name);
				//拷贝文件到输出文件对象
				FileUtils.copyInputStreamToFile(pidImg.getInputStream(), outFile);
				img2 = "http://47.110.137.123:8433/upload-images/"+name;
			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new JsonResult().addData("pidImg", img2);
	}

	@ResponseBody
	@RequestMapping(value="/bidImgUpload", method = RequestMethod.POST)
	public JsonResult bidImgUpload(@RequestParam(value = "bidImg") MultipartFile bidImg) {

		String img3 = null;

		File uploadDirectory = new File(uploadPath);

		if (uploadDirectory.exists()) {
			if (!uploadDirectory.isDirectory()) {
				uploadDirectory.delete();
			}
		} else {
			uploadDirectory.mkdir();
		}

		BufferedOutputStream bw = null;

		try {

			String fileName3 = bidImg.getOriginalFilename();
			//判断是否有文件且是否为图片文件
			if(fileName3!=null && !"".equalsIgnoreCase(fileName3.trim()) && isImageFile(fileName3)) {
				//创建输出文件对象
				String name = UUID.randomUUID().toString()+ getFileType(fileName3);
				File outFile = new File(uploadPath+name);
				//拷贝文件到输出文件对象
				FileUtils.copyInputStreamToFile(bidImg.getInputStream(), outFile);
				img3 = "http://47.110.137.123:8433/upload-images/"+name;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new JsonResult().addData("bidImg", img3);
	}


	/**
	 * 认证kyc
	 * @param body
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("kyc")
	public JsonResult addressKYC(@RequestBody String body) throws Exception {

		JSONObject jsonData = (JSONObject) JSONObject.parse(body);
		String address = jsonData.getString("address");
		String name = jsonData.getString("name");
		String IDnumber = jsonData.getString("ID_number");
		String positiveImg = jsonData.getString("positive_img");
		String backImg = jsonData.getString("back_img");
		String phone = jsonData.getString("phone");

		Member member = memberService.findByUserName(JwtUtil.getClaim(SecurityUtils.getSubject().getPrincipal().toString(), SecurityConsts.ACCOUNT));

		KycAddress kycAddress = kycAddressService.selectAddress(address);

		if (kycAddress != null)
			return new JsonResult(BizExceptionEnum.ADDRESS_REPEAT_ERROR.getCode(), BizExceptionEnum.ADDRESS_REPEAT_ERROR.getMessage());

		KycAddress KycAddress = new KycAddress();
		KycAddress.setAddress(address);
		KycAddress.setName(name);
		KycAddress.setIdNumber(IDnumber);
		KycAddress.setPositiveImg(positiveImg);
		KycAddress.setBackImg(backImg);
		KycAddress.setPhone(phone);
		KycAddress.setMemberId(member.getId());

		kycAddressService.insertKycAddress(KycAddress);
		Member upmember = new Member();
		upmember.setStatus(1);
		upmember.setId(member.getId());
		memberService.updateStatus(upmember);

		String addressHash = Api.ValidateAddress(address).getString("scriptPubKey").replaceFirst("76a914", "").replaceFirst("88ac", "");

		AddressHashLink addressHashLink = new AddressHashLink();
		addressHashLink.setAddressHash(addressHash);
		addressHashLink.setAddress(address);

		addressHashLinkService.insertAddressHashLink(addressHashLink);

		Api.SendToAddress(address, new BigDecimal("0.0001"));

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
				BigInteger destructionToken = tokenAssetsService.selectFromAddressTokenStatus(tokenId, addressHashLink.getAddressHash());
				if (destructionToken != null)
					token = token.subtract(destructionToken);

				if (fromToken != null) {
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


	@ResponseBody
	@RequestMapping("getUtxo")
	public JsonResult getUtxoApi(String address) throws Exception {

		List<Utxo> utxoList = utxoService.findByAddress(address);

		return new JsonResult().addData("utxo",utxoList);


	}

	@ResponseBody
	@RequestMapping("getTokenUtxo")
	public JsonResult getTokenUtxo(String address) throws Exception {

		List<UtxoToken> utxoTokenList = utxoTokenService.findByAddress(address);

		return new JsonResult().addData("utxo",utxoTokenList);

	}

	@ResponseBody
	@RequestMapping("getTokenHistory")
	public JsonResult getTokenHistory(String address, Integer page) {

		page = page - 1;
		Integer limit = 10;
		Integer offset = page * limit;

		AddressHashLink addressHashLink = addressHashLinkService.findByAddress(address);

		if (addressHashLink == null)
			return new JsonResult(BizExceptionEnum.ADDRESS_HASH_ERROR.getCode(), BizExceptionEnum.ADDRESS_HASH_ERROR.getMessage());

		Map<String,Object> params = Maps.newHashMap();
		params.put("offset",offset);
		params.put("limit",limit);
		params.put("address",addressHashLink.getAddressHash());

		List<TokenHistory> tokenHistoryList = tokenAssetsService.selectHistory(params);
		long total = tokenAssetsService.selectHistoryCount(params);

		long size;
		if (total % limit == 0){
			size = total/limit;
		} else {
			size = total/limit + 1;
		}

		return new JsonResult().addData("history",tokenHistoryList).addData("total",String.valueOf(total)).addData("page",String.valueOf(size));

	}


}
