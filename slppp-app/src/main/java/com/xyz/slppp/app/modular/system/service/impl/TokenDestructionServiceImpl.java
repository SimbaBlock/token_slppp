package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.TokenDestructionMapper;
import com.xyz.slppp.app.modular.system.model.TokenDestruction;
import com.xyz.slppp.app.modular.system.service.TokenDestructionService;
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
