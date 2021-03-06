package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.UtxoTokenMapper;
import com.slppp.app.modular.system.model.UtxoToken;
import com.slppp.app.modular.system.service.UtxoTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UtxoTokenServiceImpl implements UtxoTokenService {

    @Resource
    private UtxoTokenMapper utxoTokenMapper;

    @Override
    public List<UtxoToken> findByAddress(String address) {
        return utxoTokenMapper.findByAddress(address);
    }

    @Override
    public int insertUtxoToken(UtxoToken utxoToken) {
        return utxoTokenMapper.insertUtxoToken(utxoToken);
    }

    @Override
    public int deleteUtxoToken(String txid, Integer n) {
        return utxoTokenMapper.deleteUtxoToken(txid, n);
    }

}
