package com.xyz.slppp.app.modular.system.service;

import com.xyz.slppp.app.modular.system.model.UtxoToken;

import java.util.List;

public interface UtxoTokenService {

    List<UtxoToken> findByAddress(String address);
    
}
