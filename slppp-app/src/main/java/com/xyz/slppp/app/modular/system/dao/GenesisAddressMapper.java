package com.xyz.slppp.app.modular.system.dao;

import com.xyz.slppp.app.modular.system.model.GenesisAddress;
import org.apache.ibatis.annotations.Param;

public interface GenesisAddressMapper {

    int insertGenesisAddress(GenesisAddress genesisAddress);

    GenesisAddress findByTxidAndRaiseVout(@Param("txid") String txid, @Param("raiseVout") Integer raiseVout);

    int updateGensisAddress(GenesisAddress genesisAddress);

    GenesisAddress findRaiseAddress(String raiseAddress);

}
