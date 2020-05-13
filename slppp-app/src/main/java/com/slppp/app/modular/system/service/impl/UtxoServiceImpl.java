package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.UtxoMapper;
import com.slppp.app.modular.system.service.UtxoService;
import com.slppp.app.modular.system.model.Utxo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UtxoServiceImpl implements UtxoService {

    @Resource
    private UtxoMapper utxoMapper;

    @Override
    public List<Utxo> findByAddress(String address) {
        return utxoMapper.findByAddress(address);
    }


}
