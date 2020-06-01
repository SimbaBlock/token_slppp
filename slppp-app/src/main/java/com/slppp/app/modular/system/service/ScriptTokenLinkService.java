package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.ScriptTokenLink;

import java.math.BigInteger;
import java.util.List;

public interface ScriptTokenLinkService {

    int insert(ScriptTokenLink scriptTokenLink);

    ScriptTokenLink findByTokenAssets(String tokenId, String txid, Integer vout);

    BigInteger selectFAToken(String tokenId, String txid, Integer vout);

    ScriptTokenLink findByTokenAssetsStatus(String txid, Integer vout, Integer status);

    List<ScriptTokenLink> selectByTxid(String txid);

}
