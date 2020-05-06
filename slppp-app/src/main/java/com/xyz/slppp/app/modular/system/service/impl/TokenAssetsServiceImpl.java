package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.api.vo.TokenHistory;
import com.xyz.slppp.app.modular.system.dao.TokenAssetsMapper;
import com.xyz.slppp.app.modular.system.model.TokenAssets;
import com.xyz.slppp.app.modular.system.service.TokenAssetsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class TokenAssetsServiceImpl implements TokenAssetsService {

    @Resource
    private TokenAssetsMapper tokenAssetsMapper;


    @Override
    public int insertTokenAssets(TokenAssets tokenAssets) {
        return tokenAssetsMapper.insertTokenAssets(tokenAssets);
    }

    @Override
    public List<TokenAssets> selectToken(String tokenId, String address) {
        return tokenAssetsMapper.selectToken(tokenId, address);
    }

    @Override
    public BigInteger selectAddressToken(String tokenId, String address) {
        return tokenAssetsMapper.selectAddressToken(tokenId, address);
    }

    @Override
    public int selectAddressCount(String address) {
        return tokenAssetsMapper.selectAddressCount(address);
    }

    @Override
    public TokenAssets findByTokenAssets(String txid, Integer vout) {
        return tokenAssetsMapper.findByTokenAssets(txid, vout);
    }

    @Override
    public BigInteger selectFromAddressToken(String tokenId, String address) {
        return tokenAssetsMapper.selectFromAddressToken(tokenId, address);
    }

    @Override
    public List<TokenAssets> selectByTxid(String txid) {
        return tokenAssetsMapper.selectByTxid(txid);
    }

    @Override
    public BigInteger selectFAToken(String txid, Integer vout) {
        return tokenAssetsMapper.selectFAToken(txid, vout);
    }

    @Override
    public List<TokenHistory> selectHistory(Map<String, Object> query) {
        return tokenAssetsMapper.selectHistory(query);
    }

    @Override
    public Long selectHistoryCount(Map<String, Object> query) {
        return tokenAssetsMapper.selectHistoryCount(query);
    }

}
