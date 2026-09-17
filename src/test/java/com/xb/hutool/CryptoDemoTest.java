package com.xb.hutool;

import com.xb.hutool.crypto.CryptoDemo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RSA 加密 & 签名测试")
class CryptoDemoTest {

    private static Map<String, String> keys;

    @BeforeAll
    static void generateKeys() {
        keys = CryptoDemo.generateKeyPair();
    }

    @Nested
    @DisplayName("密钥对")
    class KeyPair {

        @Test
        @DisplayName("生成非空公私钥")
        void generateNonEmptyKeys() {
            assertNotNull(keys.get("publicKey"));
            assertNotNull(keys.get("privateKey"));
            assertFalse(keys.get("publicKey").isEmpty());
            assertFalse(keys.get("privateKey").isEmpty());
        }

        @Test
        @DisplayName("公私钥不相同")
        void keysAreDifferent() {
            assertNotEquals(keys.get("publicKey"), keys.get("privateKey"));
        }
    }

    @Nested
    @DisplayName("加解密")
    class EncryptDecrypt {

        @Test
        @DisplayName("公钥加密 → 私钥解密还原")
        void encryptAndDecrypt() {
            String original = "Hello RSA";
            String encrypted = CryptoDemo.encryptByPublic(keys.get("publicKey"), original);
            String decrypted = CryptoDemo.decryptByPrivate(keys.get("privateKey"), encrypted);
            assertEquals(original, decrypted);
        }

        @Test
        @DisplayName("加密后内容不等于原文")
        void encryptedDiffersFromOriginal() {
            String original = "Secret Data";
            String encrypted = CryptoDemo.encryptByPublic(keys.get("publicKey"), original);
            assertNotEquals(original, encrypted);
        }

        @Test
        @DisplayName("中文内容加解密")
        void chineseContentEncryptDecrypt() {
            String original = "订单号: 20260917001, 金额: 99.00元";
            String encrypted = CryptoDemo.encryptByPublic(keys.get("publicKey"), original);
            String decrypted = CryptoDemo.decryptByPrivate(keys.get("privateKey"), encrypted);
            assertEquals(original, decrypted);
        }
    }

    @Nested
    @DisplayName("数字签名")
    class Signature {

        @Test
        @DisplayName("私钥签名 → 公钥验签通过")
        void signAndVerify() {
            String data = "重要数据";
            String signature = CryptoDemo.sign(keys.get("privateKey"), data);
            assertTrue(CryptoDemo.verify(keys.get("publicKey"), data, signature));
        }

        @Test
        @DisplayName("篡改数据后验签失败")
        void tamperedDataFailsVerify() {
            String data = "原始数据";
            String signature = CryptoDemo.sign(keys.get("privateKey"), data);
            assertFalse(CryptoDemo.verify(keys.get("publicKey"), data + "X", signature));
        }

        @Test
        @DisplayName("错误公钥验签失败")
        void wrongPublicKeyFailsVerify() {
            Map<String, String> otherKeys = CryptoDemo.generateKeyPair();
            String data = "测试数据";
            String signature = CryptoDemo.sign(keys.get("privateKey"), data);
            assertFalse(CryptoDemo.verify(otherKeys.get("publicKey"), data, signature));
        }
    }
}
