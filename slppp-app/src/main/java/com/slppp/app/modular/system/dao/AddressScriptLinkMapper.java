package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.AddressScriptLink;
import org.apache.ibatis.annotations.Param;

public interface AddressScriptLinkMapper {

    int insert(AddressScriptLink addressScriptLink);

    int findCount(@Param("address") String address, @Param("script") String script);

}
