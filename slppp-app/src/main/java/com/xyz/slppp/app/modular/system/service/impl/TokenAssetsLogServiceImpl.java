package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.TokenAssetsLogMapper;
import com.xyz.slppp.app.modular.system.model.TokenAssetsLog;
import com.xyz.slppp.app.modular.system.service.TokenAssetsLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TokenAssetsLogServiceImpl implements TokenAssetsLogService {

    @Resource
    private TokenAssetsLogMapper tokenAssetsLogMapper;


    @Override
    public int insertTokenAssetsLog(TokenAssetsLog tokenAssetsLog) {
        return tokenAssetsLogMapper.insertTokenAssetsLog(tokenAssetsLog);
    }

    @Override
    public List<TokenAssetsLog> findByAddress(TokenAssetsLog tokenAssetsLog) {
        return tokenAssetsLogMapper.findByAddress(tokenAssetsLog);
    }
}
