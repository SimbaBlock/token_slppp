package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.TokenAssetsMapper;
import com.xyz.slppp.app.modular.system.model.TokenAssets;
import com.xyz.slppp.app.modular.system.service.TokenAssetsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

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

}
