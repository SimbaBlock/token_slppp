package com.xyz.slppp.app.modular.system.dao;

import com.xyz.slppp.app.modular.system.model.KycAddress;

import java.util.List;

public interface KycAddressMapper {

   int insertKycAddress(KycAddress kycAddress);

   KycAddress selectAddress(String address);

   List<KycAddress> selectKycAddress();

}
