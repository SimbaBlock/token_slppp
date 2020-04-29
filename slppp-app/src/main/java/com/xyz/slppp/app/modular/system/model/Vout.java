package com.xyz.slppp.app.modular.system.model;

import java.math.BigDecimal;

public class Vout {

    private String txid;

    private BigDecimal value;

    private Integer n;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }
}
