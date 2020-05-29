package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.ScriptTokenDestructionMapper;
import com.slppp.app.modular.system.model.ScriptTokenDestruction;
import com.slppp.app.modular.system.service.ScriptTokenDestructionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ScriptTokenDestructionServiceImpl implements ScriptTokenDestructionService {

    @Resource
    private ScriptTokenDestructionMapper scriptTokenDestructionMapper;

    @Override
    public int insert(ScriptTokenDestruction scriptTokenDestruction) {
        return scriptTokenDestructionMapper.insert(scriptTokenDestruction);
    }

}
