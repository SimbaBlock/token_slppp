package com.xyz.slppp.app.modular.system.dao;

import com.xyz.slppp.app.modular.system.model.Slp;

import java.util.List;
import java.util.Map;

public interface SlpMapper {

    int insertSlp(Slp slp);

    Slp findByTokenId(String tokenId);

    List<Slp> queryTokenInfoList(Map<String, Object> query);

    Long queryTokenInfoCount(Map<String, Object> query);

}
