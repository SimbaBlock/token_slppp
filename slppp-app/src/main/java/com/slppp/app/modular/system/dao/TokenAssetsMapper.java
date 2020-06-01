package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.TokenAssets;
import com.slppp.app.modular.api.vo.TokenHistory;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface TokenAssetsMapper {

    int insertTokenAssets(TokenAssets tokenAssets);

    List<TokenAssets> selectToken(@Param("tokenId") String tokenId, @Param("address") String address);

    BigInteger selectAddressToken(@Param("tokenId") String tokenId, @Param("address") String address);

    int selectAddressCount(String address);

    TokenAssets findByTokenAssets(@Param("tokenId") String tokenId, @Param("txid") String txid, @Param("vout") Integer vout);

    BigInteger selectFromAddressToken(@Param("tokenId") String tokenId, @Param("address") String address);

    List<TokenAssets> selectByTxid(@Param("txid") String txid);

    BigInteger selectFAToken(@Param("tokenId") String tokenId, @Param("txid") String txid, @Param("vout") Integer vout);

    List<TokenHistory> selectHistory(Map<String, Object> query);

    Long selectHistoryCount(Map<String, Object> query);

    int updateTokenAssets(TokenAssets tokenAssets);

    TokenAssets findByTokenAssetsStatus(@Param("txid") String txid, @Param("vout") Integer vout, @Param("status") Integer status);

    BigInteger selectFromAddressTokenStatus(@Param("tokenId") String tokenId, @Param("address") String address);

}
