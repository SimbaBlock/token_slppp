package com.xyz.slppp.app.modular.system.scheduler;

import com.xyz.slppp.app.core.common.annotion.TimeStat;
import com.xyz.slppp.app.core.rpc.Api;
import com.xyz.slppp.app.core.rpc.TxInputDto;
import com.xyz.slppp.app.core.rpc.TxOutputDto;
import com.xyz.slppp.app.modular.system.model.KycAddress;
import com.xyz.slppp.app.modular.system.model.Utxo;
import com.xyz.slppp.app.modular.system.service.KycAddressService;
import com.xyz.slppp.app.modular.system.service.UtxoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Scheduled(cron = "0/5 * * * * ?")
    public void work() {
        self.start();
    }

    static String systemnAddress = "";

    @TimeStat
    public void start(){

        try {

            List<KycAddress> kycAddressList = kycAddressService.selectKycAddress();

            BigDecimal amount = new BigDecimal("0.00001");
            for (KycAddress kycAddress : kycAddressList) {

                String address = kycAddress.getAddress();
                List<Utxo> utxoList = utxoService.findByAddress(address);

                BigDecimal sumValue = new BigDecimal("0");
                for (Utxo utxo : utxoList) {
                    sumValue = sumValue.add(new BigDecimal(utxo.getValue()));
                }

                if (sumValue.subtract(new BigDecimal("0.000005")).compareTo(new BigDecimal("0")) <= 0) {
                    //钱不够，打XSV
                    List<TxInputDto> inputList = new ArrayList<>();
                    List<TxOutputDto > outputList = new ArrayList<>();

                    List<Utxo> systemnAddressUtxoList = utxoService.findByAddress(systemnAddress);
                    BigDecimal sysSumValue = new BigDecimal("0");
                    for (Utxo utxo : systemnAddressUtxoList) {
                        TxInputDto input = new TxInputDto(utxo.getTxid(), utxo.getN(),"");
                        inputList.add(input);
                        sysSumValue = sysSumValue.add(new BigDecimal(utxo.getValue()));
                    }


                    TxOutputDto output1 = new TxOutputDto(systemnAddress, sysSumValue.subtract(amount).subtract(new BigDecimal("0.000005")));
                    TxOutputDto output2 = new TxOutputDto(address, amount);
                    outputList.add(output1);
                    outputList.add(output2);
                    String hex = Api.CreateRawTransaction(inputList, outputList);

                    String signHex = Api.SignRawTransaction(hex);

                    Api.SendRawTransaction(signHex);


                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
