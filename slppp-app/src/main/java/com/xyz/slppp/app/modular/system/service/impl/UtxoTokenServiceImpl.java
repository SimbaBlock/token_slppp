package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.UtxoTokenMapper;
import com.xyz.slppp.app.modular.system.model.UtxoToken;
import com.xyz.slppp.app.modular.system.service.UtxoTokenService;
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

}
