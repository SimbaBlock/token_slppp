package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.SlpMintMapper;
import com.xyz.slppp.app.modular.system.model.SlpMint;
import com.xyz.slppp.app.modular.system.service.SlpMintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SlpMintServiceImpl implements SlpMintService {

    @Resource
    private SlpMintMapper slpMintMapper;

    @Override
    public int insertSlpMint(SlpMint slpMint) {
        return slpMintMapper.insertSlpMint(slpMint);
    }

    @Override
    public SlpMint findByToken(String tokenId, String address) {
        return slpMintMapper.findByToken(tokenId, address);
    }

}
