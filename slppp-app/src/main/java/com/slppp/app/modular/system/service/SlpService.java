package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.Slp;

import java.util.List;
import java.util.Map;

public interface SlpService {

    int insertSlp(Slp slp);

    Slp findByTokenId(String tokenId);

    List<Slp> queryTokenInfoList(Map<String, Object> query);

    Long queryTokenInfoCount(Map<String, Object> query);

}
