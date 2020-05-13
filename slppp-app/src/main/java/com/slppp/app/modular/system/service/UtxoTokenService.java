package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.UtxoToken;

import java.util.List;

public interface UtxoTokenService {

    List<UtxoToken> findByAddress(String address);

    int insertUtxoToken(UtxoToken utxoToken);

    int deleteUtxoToken(String txid, Integer n);

}
