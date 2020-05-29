package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.ScriptTokenLinkMapper;
import com.slppp.app.modular.system.model.ScriptTokenLink;
import com.slppp.app.modular.system.service.ScriptTokenLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class ScriptTokenLinkServiceImpl implements ScriptTokenLinkService {

    @Resource
    private ScriptTokenLinkMapper scriptTokenLinkMapper;

    @Override
    public int insert(ScriptTokenLink scriptTokenLink) {
        return scriptTokenLinkMapper.insert(scriptTokenLink);
    }

    @Override
    public ScriptTokenLink findByTokenAssets(String txid, Integer vout) {
        return scriptTokenLinkMapper.findByTokenAssets(txid, vout);
    }

    @Override
    public BigInteger selectFAToken(String txid, Integer vout) {
        return scriptTokenLinkMapper.selectFAToken(txid, vout);
    }

    @Override
    public ScriptTokenLink findByTokenAssetsStatus(String txid, Integer vout, Integer status) {
        return scriptTokenLinkMapper.findByTokenAssetsStatus(txid, vout, status);
    }

    @Override
    public List<ScriptTokenLink> selectByTxid(String txid) {
        return scriptTokenLinkMapper.selectByTxid(txid);
    }

}
