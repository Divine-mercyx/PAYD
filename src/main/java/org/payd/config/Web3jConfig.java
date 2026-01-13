package org.payd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    private String polygonUrl = "https://polygon-rpc.com";

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(polygonUrl));
    }
}
