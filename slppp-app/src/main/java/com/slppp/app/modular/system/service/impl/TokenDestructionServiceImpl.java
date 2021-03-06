package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.TokenDestructionMapper;
import com.slppp.app.modular.system.model.TokenDestruction;
import com.slppp.app.modular.system.service.TokenDestructionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TokenDestructionServiceImpl implements TokenDestructionService {

    @Resource
    private TokenDestructionMapper tokenDestructionMapper;

    @Override
    public int insertTokenDestruction(TokenDestruction tokenDestruction) {
        return tokenDestructionMapper.insertTokenDestruction(tokenDestruction);
    }

}
