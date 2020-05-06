package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.GenesisAddressMapper;
import com.xyz.slppp.app.modular.system.model.GenesisAddress;
import com.xyz.slppp.app.modular.system.service.GenesisAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GenesisAddressServiceImpl implements GenesisAddressService {

    @Resource
    private GenesisAddressMapper genesisAddressMapper;


    @Override
    public int insertGenesisAddress(GenesisAddress genesisAddress) {
        return genesisAddressMapper.insertGenesisAddress(genesisAddress);
    }

    @Override
    public GenesisAddress findByTxidAndRaiseVout(String txid, Integer raiseVout) {
        return genesisAddressMapper.findByTxidAndRaiseVout(txid, raiseVout);
    }

    @Override
    public int updateGensisAddress(GenesisAddress genesisAddress) {
        return genesisAddressMapper.updateGensisAddress(genesisAddress);
    }

    @Override
    public GenesisAddress findRaiseAddress(String raiseAddress) {
        return genesisAddressMapper.findRaiseAddress(raiseAddress);
    }

}
