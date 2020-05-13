package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.AddressHashLink;

public interface AddressHashLinkMapper {

    int insertAddressHashLink(AddressHashLink addressHash);

    AddressHashLink findByAddress(String address);

    AddressHashLink findByAddressHash(String addressHash);

}
