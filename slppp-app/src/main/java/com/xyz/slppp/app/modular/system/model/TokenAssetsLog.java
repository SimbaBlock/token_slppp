package com.xyz.slppp.app.modular.system.model;

import java.math.BigInteger;

public class TokenAssetsLog {

    private String txid;

    private String tokenId;

    private BigInteger quantity;

    private Integer precition;

    private String fromAddress;

    private String toAddress;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    public Integer getPrecition() {
        return precition;
    }

    public void setPrecition(Integer precition) {
        this.precition = precition;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
}
