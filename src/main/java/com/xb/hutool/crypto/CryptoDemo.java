package com.xb.hutool.crypto;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 非对称加密 + 数字签名 —— 高阶实战
 *
 * <p>作者：xb | 日期：2026-09-17</p>
 *
 * <p><b>高阶知识点</b>：
 * <ul>
 *     <li>RSA 密钥对生成：公钥加密、私钥解密</li>
 *     <li>数字签名：私钥签名、公钥验签，保证数据完整性和不可否认性</li>
 *     <li>典型场景：API 接口签名验证、敏感数据传输</li>
 * </ul>
 *
 * <p><b>生产场景</b>：密钥对由 KMS 或证书管理，此处动态生成用于教学。</p>
 *
 * @author ibqy
 */
public class CryptoDemo {

    /**
     * 生成 RSA 密钥对
     *
     * @return 包含 publicKey 和 privateKey 的 Map（Base64 编码）
     */
    public static Map<String, String> generateKeyPair() {
        RSA rsa = new RSA();
        return Map.of(
                "publicKey", rsa.getPublicKeyBase64(),
                "privateKey", rsa.getPrivateKeyBase64()
        );
    }

    /**
     * 使用公钥加密数据
     *
     * @param publicKey Base64 编码的公钥
     * @param data      待加密的明文
     * @return 加密后的字符串
     */
    public static String encryptByPublic(String publicKey, String data) {
        RSA rsa = new RSA(null, publicKey);
        return rsa.encryptBcd(data, KeyType.PublicKey);
    }

    /**
     * 使用私钥解密数据
     *
     * @param privateKey Base64 编码的私钥
     * @param encrypted  加密后的字符串
     * @return 解密后的明文
     */
    public static String decryptByPrivate(String privateKey, String encrypted) {
        RSA rsa = new RSA(privateKey, null);
        return rsa.decryptStr(encrypted, KeyType.PrivateKey);
    }

    /**
     * 使用私钥对数据签名（SHA256withRSA）
     *
     * @param privateKey Base64 编码的私钥
     * @param data       待签名数据
     * @return Base64 编码的签名
     */
    public static String sign(String privateKey, String data) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, privateKey, null);
        return cn.hutool.core.codec.Base64.encode(sign.sign(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 使用公钥验证签名
     *
     * @param publicKey Base64 编码的公钥
     * @param data      原始数据
     * @param signature Base64 编码的签名
     * @return 验签通过返回 true
     */
    public static boolean verify(String publicKey, String data, String signature) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, null, publicKey);
        return sign.verify(data.getBytes(StandardCharsets.UTF_8),
                cn.hutool.core.codec.Base64.decode(signature));
    }

    /**
     * RSA 加密与签名演示入口：展示公钥加密、私钥解密、私钥签名、公钥验签的完整流程
     */
    public static void demo() {
        System.out.println("═══ RSA 加密 & 签名 ═══");

        Map<String, String> keys = generateKeyPair();
        System.out.println("  公钥 → " + keys.get("publicKey").substring(0, 40) + "…");
        System.out.println("  私钥 → " + keys.get("privateKey").substring(0, 40) + "…");

        String original = "订单号: 20260917001, 金额: 99.00";
        String encrypted = encryptByPublic(keys.get("publicKey"), original);
        System.out.println("  公钥加密 → " + encrypted.substring(0, 40) + "…");

        String decrypted = decryptByPrivate(keys.get("privateKey"), encrypted);
        System.out.println("  私钥解密 → " + decrypted);
        System.out.println("  解密一致 → " + original.equals(decrypted));

        String signature = sign(keys.get("privateKey"), original);
        System.out.println("  私钥签名 → " + signature.substring(0, 40) + "…");

        boolean verified = verify(keys.get("publicKey"), original, signature);
        System.out.println("  公钥验签 → " + (verified ? "通过" : "失败"));

        boolean tampered = verify(keys.get("publicKey"), original + "X", signature);
        System.out.println("  篡改后验签 → " + (tampered ? "通过（异常）" : "失败（正确）"));

        System.out.println();
    }
}
