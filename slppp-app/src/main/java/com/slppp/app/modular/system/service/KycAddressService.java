package com.slppp.app.modular.system.service;

import com.slppp.app.modular.system.model.KycAddress;

import java.util.List;

public interface KycAddressService {

    int insertKycAddress(KycAddress kycAddress);

    KycAddress selectAddress(String address);

    List<KycAddress> selectKycAddress();

}
