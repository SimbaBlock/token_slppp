package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.ScriptSlpMint;

public interface ScriptSlpMintService {

    int insertSlpMint(ScriptSlpMint slpMint);

    ScriptSlpMint findByToken(String tokenId, String address);

}
