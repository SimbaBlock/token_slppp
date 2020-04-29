package com.xyz.slppp.app.modular.api.vo;

import java.math.BigDecimal;

public class Utxo {

    private String txid;

    private Integer vout;

    private BigDecimal vaule;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public BigDecimal getVaule() {
        return vaule;
    }

    public void setVaule(BigDecimal vaule) {
        this.vaule = vaule;
    }
}
