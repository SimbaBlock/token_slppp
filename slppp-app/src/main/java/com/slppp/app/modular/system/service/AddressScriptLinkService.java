package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.AddressScriptLink;

public interface AddressScriptLinkService {

    int insert(AddressScriptLink addressScriptLink);

    int findCount(String address, String script);

}
