package com.xyz.slppp.app.modular.system.dao;

import com.xyz.slppp.app.modular.system.model.TokenAssetsLog;

import java.util.List;

public interface TokenAssetsLogMapper {

    int insertTokenAssetsLog(TokenAssetsLog tokenAssetsLog);

    List<TokenAssetsLog> findByAddress(TokenAssetsLog tokenAssetsLog);


}
