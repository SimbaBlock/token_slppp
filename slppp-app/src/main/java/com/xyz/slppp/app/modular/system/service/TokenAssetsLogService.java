package com.xyz.slppp.app.modular.system.service;

import com.xyz.slppp.app.modular.system.model.TokenAssetsLog;

import java.util.List;

public interface TokenAssetsLogService {

   int insertTokenAssetsLog(TokenAssetsLog tokenAssetsLog);

   List<TokenAssetsLog> findByAddress(TokenAssetsLog tokenAssetsLog);

}
