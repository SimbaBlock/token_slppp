package com.xyz.slppp.app.modular.system.model;

public class SendVin {

    private String txid;

    private Integer vinvout;

    private String vintxid;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getVinvout() {
        return vinvout;
    }

    public void setVinvout(Integer vinvout) {
        this.vinvout = vinvout;
    }

    public String getVintxid() {
        return vintxid;
    }

    public void setVintxid(String vintxid) {
        this.vintxid = vintxid;
    }
}
