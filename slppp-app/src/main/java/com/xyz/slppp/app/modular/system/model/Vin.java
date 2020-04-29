package com.xyz.slppp.app.modular.system.model;

public class Vin {

    private String txid;

    private String vinTxid;

    private Integer vout;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getVinTxid() {
        return vinTxid;
    }

    public void setVinTxid(String vinTxid) {
        this.vinTxid = vinTxid;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }
}
