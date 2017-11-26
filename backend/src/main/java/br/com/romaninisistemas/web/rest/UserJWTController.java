package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.security.SecurityUtils;
import br.com.romaninisistemas.security.jwt.JWTConfigurer;
import br.com.romaninisistemas.security.jwt.TokenProvider;
import br.com.romaninisistemas.web.rest.vm.LoginVM;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            String currentUserLogin = SecurityUtils.getCurrentUserLogin();
            return ResponseEntity.ok(new JWTToken(jwt, currentUserLogin));
        } catch (AuthenticationException ae) {
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",
                ae.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String token;
        private String username;
        private String permissions;

        JWTToken(String idToken, String username) {
            this.token = idToken;
            this.username = username;
        }

        @JsonProperty("username")
        String username () {
            return username;
        }
        void setUsername(String username) {
            this.username = username;
        }

        @JsonProperty("token")
        String getIdToken() {
            return token;
        }

        @JsonProperty("permissions")
        String getPermissions() {
            return permissions;
        }

        void setIdToken(String idToken) {
            this.token = idToken;
        }

        @Override
        public String toString() {
            return "JWTToken{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", permissions='" + permissions + '\'' +
                '}';
        }
    }
}
