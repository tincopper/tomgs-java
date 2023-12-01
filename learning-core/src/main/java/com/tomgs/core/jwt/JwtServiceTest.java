/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomgs.core.jwt;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * JwtServiceTest
 *
 * @author tomgs
 * @since 1.0
 */
public class JwtServiceTest {

    // "Fdh9u8rINxfivbrianbbVT1u232VQBZYKx1HGAGPt2I="
    public static final String KEY = "11C41BE62AB0989D7C8B5979CE6174639B0505DD942DBD4F7EADDB7D6880E954";

    @Test
    public void testGenerateKey() {
        try {
            // 选择HMAC算法
            String algorithm = "HmacSHA256";
            // 创建KeyGenerator实例
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            // 生成随机密钥
            SecretKey secretKey = keyGenerator.generateKey();
            // 获取密钥的字节数组形式
            byte[] keyBytes = secretKey.getEncoded();
            // 打印生成的密钥
            System.out.println("Generated HMAC key: " + bytesToHex(keyBytes));
            final String encodedToString = Base64.getEncoder().encodeToString(keyBytes);
            System.out.println(encodedToString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecoder() {
        final byte[] result = Base64.getDecoder().decode("nxANtSAR7KafF7k2xCH300KiwPpkcSNqUO1mw+b4GMQ=".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(result));
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    @Test
    public void testCreateJwt() throws JoseException {
        // 创建JWT声明
        JwtClaims claims = new JwtClaims();
        //claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setSubject("example_subject");
        claims.setAudience("YOUR_AUDIENCE");
        claims.setExpirationTimeMinutesInTheFuture(10);
        // 添加自定义参数
        claims.setClaim("userId", 123456);

        // 创建签名算法
        Key key = new HmacKey(KEY.getBytes(StandardCharsets.UTF_8));
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(key); // 设置共享密钥
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256); // 设置算法
        jws.setHeader("typ", "JWT"); // 设置头部信息
        String jwt = jws.getCompactSerialization();
        System.out.println("JWT: " + jwt);
    }

    @Test
    public void testVerifyToken() throws InvalidJwtException {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MDE0MTQyMTksInN1YiI6ImV4YW1wbGVfc3ViamVjdCIsImF1ZCI6IllPVVJfQVVESUVOQ0UiLCJleHAiOjE3MDE0MTQ4MTksInVzZXJJZCI6MTIzNDU2fQ.j_LE8qSoF0rnue_NopDBrzVMBZh8Lu8L2ooaeEOhvgA";
        Key key = new HmacKey(KEY.getBytes(StandardCharsets.UTF_8));
        JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                //.setMaxFutureValidityInMinutes(5256000)
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                //.setExpectedIssuer("")
                .setExpectedAudience("YOUR_AUDIENCE")
                .setVerificationKey(key)
                .setRelaxVerificationKeyValidation()
                .build();
        JwtClaims claims = consumer.processToClaims(token);
        if (claims != null) {
            System.out.println("认证通过！");
            Long account = (Long) claims.getClaimValue("userId");
            System.out.println("token payload携带的自定义内容:用户账号account=" + account);
        }
    }
}
