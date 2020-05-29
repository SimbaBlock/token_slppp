package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.AddressScriptLinkMapper;
import com.slppp.app.modular.system.model.AddressScriptLink;
import com.slppp.app.modular.system.service.AddressScriptLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AddressScriptLinkServiceImpl implements AddressScriptLinkService {

    @Resource
    private AddressScriptLinkMapper addressScriptLinkMapper;

    @Override
    public int insert(AddressScriptLink addressScriptLink) {
        return addressScriptLinkMapper.insert(addressScriptLink);
    }

    @Override
    public int findCount(String address, String script) {
        return addressScriptLinkMapper.findCount(address, script);
    }

}
