package security;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import play.Configuration;
import utils.Const;

import javax.crypto.KeyGenerator;
import javax.inject.Inject;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JWTProvider {
    private static String sharedSecret = "A71A2E1D7C317B801D05C321CEF52754096963DF55B4DB2BAA607CA07D8804DB";
    private static String issuer = "fpt.mola";


    public static String generateJWT(String username) throws NoSuchAlgorithmException, JOSEException, ParseException {

        System.out.println(new String(Const.sharedSecret));
        JWSSigner signer = new MACSigner(Const.sharedSecret);

        long expirationTime = TimeUnit.MILLISECONDS.convert(Const.TOKEN_EXPIRATION_DAYS, TimeUnit.DAYS);

        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer(Const.issuer)
                .expirationTime(new Date(new Date().getTime() + expirationTime))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        // Apply the HMAC protection
        signedJWT.sign(signer);

        // Serialize to compact form, produces something like
        // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        String s = signedJWT.serialize();

        signedJWT = SignedJWT.parse(s);

        JWSVerifier verifier = new MACVerifier(Const.sharedSecret);

        System.out.println(signedJWT.verify(verifier));
        System.out.println(signedJWT.getJWTClaimsSet().getSubject());




        return s;
    }

    public static boolean verifyJWT(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(Const.sharedSecret);

        System.out.println(signedJWT.verify(verifier));

        return false;
    }
}
