package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.KycAddressMapper;
import com.xyz.slppp.app.modular.system.model.KycAddress;
import com.xyz.slppp.app.modular.system.service.KycAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KycAddressServiceImpl implements KycAddressService {

    @Resource
    private KycAddressMapper kycAddressMapper;

    @Override
    public int insertKycAddress(KycAddress kycAddress) {
        return kycAddressMapper.insertKycAddress(kycAddress);
    }

    @Override
    public KycAddress selectAddress(String address) {
        return kycAddressMapper.selectAddress(address);
    }

    @Override
    public List<KycAddress> selectKycAddress() {
        return kycAddressMapper.selectKycAddress();
    }

}
