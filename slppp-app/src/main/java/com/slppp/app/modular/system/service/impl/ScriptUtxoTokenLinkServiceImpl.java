package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.ScriptUtxoTokenLinkMapper;
import com.slppp.app.modular.system.model.ScriptUtxoTokenLink;
import com.slppp.app.modular.system.service.ScriptUtxoTokenLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ScriptUtxoTokenLinkServiceImpl implements ScriptUtxoTokenLinkService {

    @Resource
    private ScriptUtxoTokenLinkMapper scriptUtxoTokenLinkMapper;


    @Override
    public int insert(ScriptUtxoTokenLink scriptUtxoTokenLink) {
        return scriptUtxoTokenLinkMapper.insert(scriptUtxoTokenLink);
    }

    @Override
    public int deleteUtxoToken(String txid, Integer n) {
        return scriptUtxoTokenLinkMapper.deleteUtxoToken(txid, n);
    }

}
