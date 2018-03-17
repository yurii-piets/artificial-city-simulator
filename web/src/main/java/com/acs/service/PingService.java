package com.acs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.acs.service.PingService.PING_PROFILE;

@Service
@Profile(PING_PROFILE)
public class PingService {

    public final static String PING_PROFILE = "ping";

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Value("${azure.basic.url}")
    private String azureUrl;

    @Scheduled(fixedDelay = 1 * 1000 * 60)
    public void ping() {
        RestTemplate restTemplate = new RestTemplate();
        String pingUrl = azureUrl + "/ping";
        ResponseEntity responseEntity = restTemplate.getForEntity(pingUrl, String.class);

        logger.info("Ping response: " + responseEntity.getStatusCode());
    }

}
