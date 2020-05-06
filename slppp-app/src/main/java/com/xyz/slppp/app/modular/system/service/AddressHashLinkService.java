package com.xyz.slppp.app.modular.system.service;

import com.xyz.slppp.app.modular.system.model.AddressHashLink;

public interface AddressHashLinkService {

    int insertAddressHashLink(AddressHashLink addressHash);

    AddressHashLink findByAddress(String address);

    AddressHashLink findByAddressHash(String addressHash);

}
