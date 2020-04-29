package com.xyz.slppp.app.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xyz.slppp.app.modular.system.model.Token;

public interface TokenService extends IService<Token> {

    int insertToken(Token token);



}
