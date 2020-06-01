package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.ScriptSlpSendMapper;
import com.slppp.app.modular.system.model.ScriptSlpSend;
import com.slppp.app.modular.system.service.ScriptSlpSendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ScriptSlpSendServiceImpl implements ScriptSlpSendService {

    @Resource
    private ScriptSlpSendMapper scriptSlpSendMapper;


    @Override
    public int insertSlpSend(ScriptSlpSend slpSend) {
        return scriptSlpSendMapper.insertSlpSend(slpSend);
    }
}
