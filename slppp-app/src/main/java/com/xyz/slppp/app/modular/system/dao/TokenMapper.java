package com.xyz.slppp.app.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xyz.slppp.app.modular.system.model.Token;

public interface TokenMapper extends BaseMapper<Token> {

    int insertToken(Token token);

}
