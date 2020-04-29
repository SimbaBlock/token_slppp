package com.xyz.slppp.app.modular.system.service;

import com.xyz.slppp.app.modular.system.model.KycAddress;

public interface KycAddressService {

    int insertKycAddress(KycAddress kycAddress);

    KycAddress selectAddress(String address);

}
