package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.SlpMint;
import org.apache.ibatis.annotations.Param;

public interface SlpMintMapper {

   int insertSlpMint(SlpMint slpMint);

   SlpMint findByToken(@Param("tokenId") String tokenId, @Param("address") String address);

}
