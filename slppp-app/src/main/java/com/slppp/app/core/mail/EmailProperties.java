package com.slppp.app.core.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "sender-email")
public class EmailProperties {

    public Map<String, Map<String, String>> getMail() {
        return mail;
    }

    public void setMail(Map<String, Map<String, String>> mail) {
        this.mail = mail;
    }

    private Map<String, Map<String, String>> mail;


}
