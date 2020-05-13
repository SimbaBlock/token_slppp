package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.Utxo;

import java.util.List;

public interface UtxoMapper {

    List<Utxo> findByAddress(String address);

}
