package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.AddressHashLinkMapper;
import com.xyz.slppp.app.modular.system.model.AddressHashLink;
import com.xyz.slppp.app.modular.system.service.AddressHashLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AddressHashLinkServiceImpl implements AddressHashLinkService {

    @Resource
    private AddressHashLinkMapper addressHashLinkMapper;

    @Override
    public int insertAddressHashLink(AddressHashLink addressHash) {
        return addressHashLinkMapper.insertAddressHashLink(addressHash);
    }

    @Override
    public AddressHashLink findByAddress(String address) {
        return addressHashLinkMapper.findByAddress(address);
    }

    @Override
    public AddressHashLink findByAddressHash(String addressHash) {
        return addressHashLinkMapper.findByAddressHash(addressHash);
    }

}
