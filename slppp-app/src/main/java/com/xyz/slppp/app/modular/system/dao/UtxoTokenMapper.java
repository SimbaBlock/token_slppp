package com.xyz.slppp.app.modular.system.dao;

import com.xyz.slppp.app.modular.system.model.UtxoToken;

import java.util.List;

public interface UtxoTokenMapper {

    List<UtxoToken> findByAddress(String address);

}
