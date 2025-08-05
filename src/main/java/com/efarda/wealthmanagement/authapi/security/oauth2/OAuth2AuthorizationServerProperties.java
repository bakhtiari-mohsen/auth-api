package com.efarda.wealthmanagement.authapi.security.oauth2;

import jakarta.annotation.Nonnull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ConfigurationProperties(prefix = "oauth2.authorization.server")
public record OAuth2AuthorizationServerProperties(@Nonnull JWKRSAKeys jwkRsaKeys) {

    public record JWKRSAKeys(@Nonnull String privateKey, @Nonnull String publicKey, @Nonnull String keyId) {

        public KeyPair keyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
            final var keyFactory = KeyFactory.getInstance("RSA");
            return new KeyPair(readPublicKey(keyFactory), readPrivateKey(keyFactory));
        }

        private PublicKey readPublicKey(KeyFactory keyFactory) throws InvalidKeySpecException {
            final var publicBytes = Base64.getDecoder().decode(publicKey);
            final var keySpec = new X509EncodedKeySpec(publicBytes);

            return keyFactory.generatePublic(keySpec);
        }

        private PrivateKey readPrivateKey(KeyFactory keyFactory) throws InvalidKeySpecException {
            final var privateBytes = Base64.getDecoder().decode(privateKey);
            final var keySpec = new PKCS8EncodedKeySpec(privateBytes);

            return keyFactory.generatePrivate(keySpec);
        }
    }
}
