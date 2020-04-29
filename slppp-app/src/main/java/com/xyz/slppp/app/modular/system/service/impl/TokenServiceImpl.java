package com.xyz.slppp.app.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xyz.slppp.app.modular.system.dao.TokenMapper;
import com.xyz.slppp.app.modular.system.model.Token;
import com.xyz.slppp.app.modular.system.service.TokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token> implements TokenService {

    @Resource
    private TokenMapper tokenMapper;

    @Override
    public int insertToken(Token token) {
        return tokenMapper.insertToken(token);
    }

}
