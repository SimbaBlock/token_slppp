package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.SendVinMapper;
import com.xyz.slppp.app.modular.system.model.SendVin;
import com.xyz.slppp.app.modular.system.service.SendVinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SendVinServiceImpl implements SendVinService {

    @Resource
    private SendVinMapper sendVinMapper;

    @Override
    public int insertSendVin(SendVin sendVin) {
        return sendVinMapper.insertSendVin(sendVin);
    }

}
