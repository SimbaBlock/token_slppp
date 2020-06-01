package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.ScriptSlpMint;
import com.slppp.app.modular.system.model.SlpMint;

public interface ScriptSlpMintService {

    int insertSlpMint(ScriptSlpMint slpMint);

    ScriptSlpMint findByToken(String tokenId, String address);

}
