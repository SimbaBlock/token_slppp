package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.SlpMint;

public interface SlpMintService {

    int insertSlpMint(SlpMint slpMint);

    SlpMint findByToken(String tokenId, String address);

}
