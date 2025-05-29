package hr.fer.gymmanagment.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import hr.fer.gymmanagment.security.entity.pojo.JwtGenerationResult;
import hr.fer.gymmanagment.security.entity.pojo.JwtValidationResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.secret-key:secret-key-refresh}")
    private String secretKeyRefresh;

    @Value("${security.jwt.token.validity-duration:3600000}")
    private Integer validityDuration;

    @Value("${security.jwt.token.validity-duration-refresh:7200000}")
    private Integer validityDurationRefresh;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private Date generateExpirationDate(String usage) {
        Date Now = new Date();
        Date expires;

        if(Objects.equals(usage, "refresh")) {
            expires = new Date(Now.getTime() + validityDurationRefresh);
        } else {
            expires = new Date(Now.getTime() + validityDuration);
        }

        return expires;
    }

    private Algorithm generateAlgorithm(String usage) {
        Algorithm algorithm;

        if(Objects.equals(usage, "refresh")) {
            algorithm = Algorithm.HMAC256(secretKeyRefresh);
        } else {
            algorithm = Algorithm.HMAC256(secretKey);
        }

        return algorithm;
    }

    public JwtGenerationResult generateToken(String username, String usage) {
        Date now = new Date();
        Date expires = generateExpirationDate(usage);
        Algorithm algorithm = generateAlgorithm(usage);

        return new JwtGenerationResult(
                JWT.create()
                        .withSubject(username)
                        .withIssuedAt(now)
                        .withExpiresAt(expires)
                        .sign(algorithm),
                expires.getTime());
    }

    public JwtGenerationResult generateToken(String username, String usage, boolean tacacsFallback) {
        Date now = new Date();
        Date expires = generateExpirationDate(usage);
        Algorithm algorithm = generateAlgorithm(usage);

        return new JwtGenerationResult(
                JWT.create()
                        .withClaim("tacacsFallback", tacacsFallback)
                        .withSubject(username)
                        .withIssuedAt(now)
                        .withExpiresAt(expires)
                        .sign(algorithm),
                expires.getTime());
    }

    public JwtGenerationResult generateToken(String username, String usage,
                                             boolean usingPrimaryTacacs, boolean usingAccounting) {
        Date now = new Date();
        Date expires = generateExpirationDate(usage);
        Algorithm algorithm = generateAlgorithm(usage);

        return new JwtGenerationResult(
                JWT.create()
                        .withClaim("usingPrimaryTacacs", usingPrimaryTacacs)
                        .withClaim("usingAccounting", usingAccounting)
                        .withSubject(username)
                        .withIssuedAt(now)
                        .withExpiresAt(expires)
                        .sign(algorithm),
                expires.getTime());
    }

    public JwtValidationResult validateToken(String token, String usage) {

        Algorithm algorithm;
        if(Objects.equals(usage, "refresh")) {
            algorithm = Algorithm.HMAC256(secretKeyRefresh);
        } else {
            algorithm = Algorithm.HMAC256(secretKey);
        }

        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return new JwtValidationResult(true, decodedJWT);
        } catch (AlgorithmMismatchException e) {
            log.error("Algorithm mismatch: The algorithm in the token's header does not match the expected algorithm.");
            return new JwtValidationResult(false, null);
        } catch (SignatureVerificationException e) {
            log.error("Signature verification failed: The signature of the token is invalid.");
            return new JwtValidationResult(false, null);
        } catch (TokenExpiredException e) {
            log.error("Token expired: The token has expired.");
            return new JwtValidationResult(false, null);
        } catch (InvalidClaimException e) {
            log.error("Invalid claim: A claim contained a different value than the expected one.");
            return new JwtValidationResult(false, null);
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed: " + e.getMessage());
            return new JwtValidationResult(false, null);
        } catch (Exception e) {
            log.error("An unexpected exception occurred: " + e.getMessage());
            return new JwtValidationResult(false, null);
        }
    }

}


