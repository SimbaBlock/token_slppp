package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.VoutAddressMapper;
import com.xyz.slppp.app.modular.system.model.VoutAddress;
import com.xyz.slppp.app.modular.system.service.VoutAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VoutAddressServiceImpl implements VoutAddressService {

    @Resource
    private VoutAddressMapper voutAddressMapper;

    @Override
    public int insertVoutAddress(VoutAddress voutaddress) {
        return voutAddressMapper.insertVoutAddress(voutaddress);
    }

}
