package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.VinMapper;
import com.xyz.slppp.app.modular.system.model.Vin;
import com.xyz.slppp.app.modular.system.service.VinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VinServiceImpl implements VinService {

    @Resource
    private VinMapper vinMapper;

    @Override
    public int insertVin(Vin vin) {
        return vinMapper.insertVin(vin);
    }

}
