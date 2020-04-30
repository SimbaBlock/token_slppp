package com.xyz.slppp.app.modular.system.model;

public class Utxo {

    private String txid;

    private Integer n;

    private String address;

    private Integer addresspos;

    private String value;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAddresspos() {
        return addresspos;
    }

    public void setAddresspos(Integer addresspos) {
        this.addresspos = addresspos;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
