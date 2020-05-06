package com.xyz.slppp.app.modular.system.dao;

import com.xyz.slppp.app.modular.api.vo.TokenHistory;
import com.xyz.slppp.app.modular.system.model.TokenAssets;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface TokenAssetsMapper {

    int insertTokenAssets(TokenAssets tokenAssets);

    List<TokenAssets> selectToken(@Param("tokenId") String tokenId, @Param("address") String address);

    BigInteger selectAddressToken(@Param("tokenId") String tokenId, @Param("address") String address);

    int selectAddressCount(String address);

    TokenAssets findByTokenAssets(@Param("txid") String txid, @Param("vout") Integer vout);

    BigInteger selectFromAddressToken(@Param("tokenId") String tokenId, @Param("address") String address);

    List<TokenAssets> selectByTxid(@Param("txid") String txid);

    BigInteger selectFAToken(@Param("txid")String txid, @Param("vout") Integer vout);

    List<TokenHistory> selectHistory(Map<String, Object> query);

    Long selectHistoryCount(Map<String, Object> query);

}
