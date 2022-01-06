package eu.techmoodivns.support.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCron {

    @Autowired
    private TokenManager tokenManager;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "${eu.techmoodivns.support.token.cron.cleanUp:0 0 3 * * *}") // everyday at 03:00:00
    public void cleanUp() {

        logger.info("Started cleaning up...");

        tokenManager.cleanUp();

        logger.info("Finished cleaning up!");
    }
}
