package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.ScriptUtxoTokenLink;

public interface ScriptUtxoTokenLinkService {

    int insert(ScriptUtxoTokenLink scriptUtxoTokenLink);

    int deleteUtxoToken(String txid, Integer n);

}
