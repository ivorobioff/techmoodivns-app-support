package eu.techmoodivns.support.security;

import eu.techmoodivns.support.security.authenticator.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import static eu.techmoodivns.support.security.authenticator.Authenticator.Cleanable;

@Component
public class SecurityCron {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Authenticator authenticator;

    @Scheduled(cron = "${eu.techmoodivns.support.security.cron.cleanUp:0 0 4 * * *}") // everyday at 04:00:00
    public void cleanUp() {

        if (authenticator instanceof Cleanable) {

            logger.info("Started cleaning up authenticator...");

            ((Cleanable) authenticator).cleanUp();

            logger.info("Finished cleaning up authenticator!");
        }
    }
}
