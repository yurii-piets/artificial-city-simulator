package com.acs;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PingService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Value("${azure.basic.url}")
    private String azureUrl;

    @Scheduled(fixedDelay = 5 * 1000 * 60)
    public void ping() {
        RestTemplate restTemplate = new RestTemplate();
        String pingUrl = azureUrl + "/ping";
        ResponseEntity responseEntity = restTemplate.getForEntity(pingUrl, String.class);

        logger.info("Ping response: " + responseEntity.getStatusCode());
    }

}
