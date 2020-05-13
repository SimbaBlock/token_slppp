package com.slppp.app.modular.system.model;

import java.math.BigInteger;

public class TokenAssets {

    private String tokenId;

    private String address;

    private BigInteger token;

    private String txid;

    private Integer vout;

    private String fromAddress;

    private Long time;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public BigInteger getToken() {
        return token;
    }

    public void setToken(BigInteger token) {
        this.token = token;
    }
}
