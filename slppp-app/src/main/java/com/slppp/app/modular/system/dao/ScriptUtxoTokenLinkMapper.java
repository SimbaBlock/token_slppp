package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.ScriptUtxoTokenLink;
import org.apache.ibatis.annotations.Param;

public interface ScriptUtxoTokenLinkMapper {

    int insert(ScriptUtxoTokenLink scriptUtxoTokenLink);

    int deleteUtxoToken(@Param("txid") String txid, @Param("n") Integer n);

}
