package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.SendVoutMapper;
import com.xyz.slppp.app.modular.system.model.SendVout;
import com.xyz.slppp.app.modular.system.service.SendVoutService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SendVoutServiceImpl implements SendVoutService {

    @Resource
    private SendVoutMapper sendVoutMapper;


    @Override
    public int insertSendVout(SendVout sendVout) {
        return sendVoutMapper.insertSendVout(sendVout);
    }

}
