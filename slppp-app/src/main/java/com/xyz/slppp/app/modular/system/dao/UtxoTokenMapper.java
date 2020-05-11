package com.xyz.slppp.app.modular.system.dao;

import com.xyz.slppp.app.modular.system.model.UtxoToken;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UtxoTokenMapper {

    List<UtxoToken> findByAddress(String address);

    int insertUtxoToken(UtxoToken utxoToken);

    int deleteUtxoToken(@Param("txid") String txid, @Param("n") Integer n);

}
