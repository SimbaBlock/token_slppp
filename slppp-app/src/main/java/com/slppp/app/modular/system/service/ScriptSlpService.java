package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.ScriptSlp;
import com.slppp.app.modular.system.model.Slp;

import java.util.List;
import java.util.Map;

public interface ScriptSlpService {

    int insertSlp(ScriptSlp slp);

    ScriptSlp findByTokenId(String tokenId);

    List<ScriptSlp> queryTokenInfoList(Map<String, Object> query);

    Long queryTokenInfoCount(Map<String, Object> query);

}
