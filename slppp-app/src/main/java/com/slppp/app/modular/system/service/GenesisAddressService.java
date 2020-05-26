package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.GenesisAddress;

public interface GenesisAddressService {

    int insertGenesisAddress(GenesisAddress genesisAddress);

    GenesisAddress findByTxidAndRaiseVout(String txid, Integer raiseVout);

    int updateGensisAddress(GenesisAddress genesisAddress);

    GenesisAddress findRaiseAddress(String raiseAddress, String tokenId);

    GenesisAddress findByRaiseTxidAndRaiseVout(String raiseTxid, Integer raiseVout);


}
