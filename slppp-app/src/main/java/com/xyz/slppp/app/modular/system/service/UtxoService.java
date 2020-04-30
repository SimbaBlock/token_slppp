package com.xyz.slppp.app.modular.system.service;

import com.xyz.slppp.app.modular.system.model.Utxo;

import java.util.List;

public interface UtxoService {

    List<Utxo> findByAddress(String address);

}
