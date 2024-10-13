package com.hana.service.Utils;

import com.hana.service.Common.AppConfig;
import com.hana.service.Common.Const;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.SystemConfigRepository;
import com.hana.service.DAO.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    @Autowired
    private SystemConfigRepository systemConfigRepository;

    public String getUserValidJWT(UserRepository userRepository,
                                  JwtTokenUtil jwtTokenUtil, UserEntity user) {
        String oldToken = user.getLoginToken();
        String userAccount = user.getAccount();
        String loginToken = "";
        if (jwtTokenUtil == null) {
            jwtTokenUtil = new JwtTokenUtil();
        }
        if (oldToken == null || "".equals(oldToken)) {
            Instant tokenExpiredTime = Instant.now().plus(5, ChronoUnit.HOURS);
            // generate an access token
            // get token validTime from database
            loginToken = jwtTokenUtil.generateToken(userAccount, tokenExpiredTime.getEpochSecond());

            // write token and expire date into DB
            user.setLoginToken(loginToken);
            user.setTokenExpiredTime(LocalDateTime.ofInstant(tokenExpiredTime, ZoneId.of("UTC")));
            userRepository.save(user);
        } else {
            loginToken = oldToken;
        }
        return loginToken;
    }

    // generate token for user
    private String generateToken(String userName, long expiredTime) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userName, expiredTime);
    }
    private String doGenerateToken(Map<String, Object> claims, String subject,
                                   long expiredTime) {
        initSecretKeyForJWT();
        return Jwts
                .builder()
                .setIssuer("Keyper Backend-" + Const.BACKEND_VERSION_CODE)
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))

                .setExpiration(
                        new Date(System.currentTimeMillis() + expiredTime
                                * 1000))
                .signWith(SignatureAlgorithm.HS512, Const.secretKeyForJWT).compact();
    }

    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private static <T> T getClaimFromToken(String token,
                                   Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public static Claims getAllClaimsFromToken(String token) {
        initSecretKeyForJWT();
        return Jwts.parser().setSigningKey(Const.secretKeyForJWT).parseClaimsJws(token)
                .getBody();
    }

    private static void initSecretKeyForJWT() {
        if (Const.secretKeyForJWT == null || "".equals(Const.secretKeyForJWT)) {
            AppConfig appConfig = new AppConfig();
            appConfig.configLoginTokenKey();
        }
    }

}
