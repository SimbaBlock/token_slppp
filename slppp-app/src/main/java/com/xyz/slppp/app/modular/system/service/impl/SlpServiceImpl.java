package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.SlpMapper;
import com.xyz.slppp.app.modular.system.model.Slp;
import com.xyz.slppp.app.modular.system.service.SlpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SlpServiceImpl implements SlpService {

    @Resource
    private SlpMapper slpMapper;

    @Override
    public int insertSlp(Slp slp) {
        return slpMapper.insertSlp(slp);
    }

    @Override
    public Slp findByTokenId(String tokenId) {
        return slpMapper.findByTokenId(tokenId);
    }

    @Override
    public List<Slp> queryTokenInfoList(Map<String, Object> query) {
        return slpMapper.queryTokenInfoList(query);
    }

    @Override
    public Long queryTokenInfoCount(Map<String, Object> query) {
        return slpMapper.queryTokenInfoCount(query);
    }

}
