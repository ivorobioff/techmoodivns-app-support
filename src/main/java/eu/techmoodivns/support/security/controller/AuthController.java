package eu.techmoodivns.support.security.controller;

import eu.techmoodivns.support.security.authenticator.Authenticator;
import eu.techmoodivns.support.validation.InvalidOperationException;
import eu.techmoodivns.support.validation.InvalidPayloadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import eu.techmoodivns.support.security.authenticator.Authenticator.Resolution;
import eu.techmoodivns.support.security.authenticator.Authenticator.Credentials;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
)
public class AuthController {

    public final static String LOGIN_ENDPOINT = "/api/v1.0/auth/login";
    public final static String LOGOUT_ENDPOINT = "/api/v1.0/auth/logout";
    public final static String REFRESH_ENDPOINT = "/api/v1.0/auth/refresh";

    @Autowired
    private Authenticator authenticator;

    @RequestMapping(path = LOGIN_ENDPOINT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Resolution login(@RequestBody @Valid Credentials credentials, Errors errors) {

        if (errors.hasErrors()) {
            throw new InvalidPayloadException(errors);
        }

        Resolution resolution = authenticator.login(credentials);

        if (resolution == null) {
            throw new InvalidOperationException("Incorrect username or password!");
        }

        return resolution;
    }

    @RequestMapping(path = REFRESH_ENDPOINT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Resolution refresh(@Valid @RequestBody SecretHolder secretHolder, Errors errors) throws InvalidOperationException, InvalidPayloadException {

        if (errors.hasErrors()) {
            throw new InvalidPayloadException(errors);
        }

        Resolution resolution = authenticator.refresh(secretHolder.secret);

        if (resolution == null) {
            throw new InvalidOperationException("Token can't be refreshed");
        }

        return resolution;
    }

    @RequestMapping(path = LOGOUT_ENDPOINT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void logout(@Valid @RequestBody SecretHolder secretHolder, Errors errors) throws InvalidPayloadException {

        if (errors.hasErrors()) {
            throw new InvalidPayloadException(errors);
        }

        authenticator.logout(secretHolder.secret);
    }

    public static class SecretHolder {
        @NotBlank
        private String secret;
    }
}
