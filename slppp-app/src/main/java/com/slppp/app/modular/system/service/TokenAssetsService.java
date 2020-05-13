package com.slppp.app.modular.system.service;

import com.slppp.app.modular.api.vo.TokenHistory;
import com.slppp.app.modular.system.model.TokenAssets;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface TokenAssetsService {

    int insertTokenAssets(TokenAssets tokenAssets);

    List<TokenAssets> selectToken(String tokenId, String address);

    BigInteger selectAddressToken(String tokenId, String address);

    int selectAddressCount(String address);

    TokenAssets findByTokenAssets(String txid, Integer vout);

    BigInteger selectFromAddressToken(String tokenId, String address);

    List<TokenAssets> selectByTxid(String txid);

    BigInteger selectFAToken(String txid, Integer vout);

    List<TokenHistory> selectHistory(Map<String, Object> query);

    Long selectHistoryCount(Map<String, Object> query);

    int updateTokenAssets(TokenAssets tokenAssets);

    TokenAssets findByTokenAssetsStatus(String txid, Integer vout, Integer status);

    BigInteger selectFromAddressTokenStatus(String tokenId, String address);
}
