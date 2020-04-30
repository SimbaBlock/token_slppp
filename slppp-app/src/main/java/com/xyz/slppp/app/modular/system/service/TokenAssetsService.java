package com.xyz.slppp.app.modular.system.service;

import com.xyz.slppp.app.modular.system.model.TokenAssets;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface TokenAssetsService {

    int insertTokenAssets(TokenAssets tokenAssets);

    List<TokenAssets> selectToken(String tokenId, String address);

    BigInteger selectAddressToken(String tokenId, String address);

    int selectAddressCount(String address);

    TokenAssets findByTokenAssets(String txid, Integer vout);

    BigInteger selectFromAddressToken(String tokenId, String address);

    List<TokenAssets> selectByTxid(String txid);

    BigInteger selectFAToken(String txid, Integer vout);
}
