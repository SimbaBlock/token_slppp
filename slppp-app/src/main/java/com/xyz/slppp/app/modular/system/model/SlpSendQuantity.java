package com.xyz.slppp.app.modular.system.model;

public class SlpSendQuantity {

    private String txid;

    private String address;

    private String tokenQuantity;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTokenQuantity() {
        return tokenQuantity;
    }

    public void setTokenQuantity(String tokenQuantity) {
        this.tokenQuantity = tokenQuantity;
    }

}
