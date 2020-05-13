package com.xyz.slppp.app.modular.system.scheduler;

import com.xyz.slppp.app.core.rpc.Api;
import com.xyz.slppp.app.modular.system.model.Utxo;
import com.xyz.slppp.app.modular.system.service.UtxoService;
import com.xyz.slppp.app.core.common.annotion.TimeStat;
import com.xyz.slppp.app.modular.system.model.KycAddress;
import com.xyz.slppp.app.modular.system.service.KycAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "guns.scheduler-switch", name = "utxo", havingValue = "true")
@Slf4j
public class UtxoScheduler {

    @Autowired
    private UtxoScheduler self;

    @Autowired
    private KycAddressService kycAddressService;

    @Autowired
    private UtxoService utxoService;

//    @Scheduled(cron = "0/59 * * * * ?")
    public void work() {
        self.start();
    }

    @TimeStat
    public void start(){

        try {

            List<KycAddress> kycAddressList = kycAddressService.selectKycAddress();

            for (KycAddress kycAddress : kycAddressList) {

                String address = kycAddress.getAddress();

                List<Utxo> utxoList = utxoService.findByAddress(address);

                BigDecimal sumValue = new BigDecimal("0");
                for (Utxo utxo : utxoList) {
                    sumValue = sumValue.add(new BigDecimal(utxo.getValue()));
                }

                if (sumValue.subtract(new BigDecimal("0.00005")).compareTo(new BigDecimal("0")) <= 0) {

                    Api.SendToAddress(address, new BigDecimal("0.0001"));

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
