package com.xb.hutool;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UtilDemoTest - 工具类综合单元测试
 *
 * 覆盖 UUID/NanoId 生成、MD5/AES 加解密、数据脱敏、类型转换、Base64 和空值判断。
 *
 * @author ibqy
 */
@DisplayName("Hutool 工具类综合测试")
class UtilDemoTest {

    @Test
    @DisplayName("UUID 生成")
    void uuidGeneration() {
        String uuid = IdUtil.fastSimpleUUID();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
    }

    @Test
    @DisplayName("NanoId 生成指定长度")
    void nanoIdGeneration() {
        String nanoId = IdUtil.nanoId(12);
        assertNotNull(nanoId);
        assertEquals(12, nanoId.length());
    }

    @Test
    @DisplayName("MD5 加密")
    void md5Encrypt() {
        String md5 = SecureUtil.md5("hello");
        assertNotNull(md5);
        assertEquals(32, md5.length());
        assertEquals("5d41402abc4b2a76b9719d911017c592", md5);
    }

    @Test
    @DisplayName("AES 加密解密")
    void aesEncryptDecrypt() {
        String key = "1234567890123456";
        AES aes = SecureUtil.aes(key.getBytes());

        String original = "Hello Hutool";
        String encrypted = aes.encryptBase64(original);
        String decrypted = aes.decryptStr(encrypted);

        assertNotEquals(original, encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    @DisplayName("手机号脱敏")
    void mobilePhoneDesensitize() {
        String result = DesensitizedUtil.mobilePhone("13812345678");
        assertEquals("138****5678", result);
    }

    @Test
    @DisplayName("邮箱脱敏")
    void emailDesensitize() {
        String result = DesensitizedUtil.email("ibqy@example.com");
        assertEquals("i***@example.com", result);
    }

    @Test
    @DisplayName("密码脱敏")
    void passwordDesensitize() {
        String result = DesensitizedUtil.password("secret123");
        assertNotNull(result);
        assertTrue(result.matches("\\*+"));
    }

    @Test
    @DisplayName("类型转换 - String to Int")
    void convertToInt() {
        int result = Convert.toInt("42");
        assertEquals(42, result);
    }

    @Test
    @DisplayName("类型转换 - 默认值")
    void convertWithDefault() {
        int result = Convert.toInt("invalid", 0);
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Base64 编解码")
    void base64EncodeDecode() {
        String original = "Hello Hutool";
        String encoded = Base64.encode(original);
        String decoded = Base64.decodeStr(encoded);

        assertEquals(original, decoded);
        assertNotEquals(original, encoded);
    }

    @Test
    @DisplayName("ObjectUtil - 空值判断")
    void objectUtilIsEmpty() {
        assertTrue(ObjectUtil.isEmpty(null));
        assertTrue(ObjectUtil.isEmpty(""));
        assertFalse(ObjectUtil.isEmpty("hello"));
    }

    @Test
    @DisplayName("ObjectUtil - 默认值")
    void objectUtilDefaultIfNull() {
        String result = ObjectUtil.defaultIfNull(null, "default");
        assertEquals("default", result);

        String result2 = ObjectUtil.defaultIfNull("value", "default");
        assertEquals("value", result2);
    }
}
