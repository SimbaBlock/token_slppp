package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.VoutMapper;
import com.xyz.slppp.app.modular.system.model.Vout;
import com.xyz.slppp.app.modular.system.service.VoutService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VoutServiceImpl implements VoutService {

    @Resource
    private VoutMapper voutMapper;

    @Override
    public int insertVout(Vout vout) {
        return voutMapper.insertVout(vout);
    }

}
