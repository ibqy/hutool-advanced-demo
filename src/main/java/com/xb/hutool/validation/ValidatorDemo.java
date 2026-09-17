package com.xb.hutool.validation;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ReUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 数据校验器 —— 链式校验 + 错误收集
 *
 * <p>作者：xb | 日期：2026-09-17</p>
 *
 * <p><b>高阶知识点</b>：
 * <ul>
 *     <li>利用 Hutool Validator / ReUtil 做手机号、邮箱、身份证、中文名校验</li>
 *     <li>链式校验器：收集所有错误而非遇到第一个就中断，适合表单提交场景</li>
 *     <li>自定义规则扩展：Predicate 函数式接口，一行代码添加新规则</li>
 * </ul>
 */
public class ValidatorDemo {

    public static boolean isPhone(String value) {
        return ReUtil.isMatch("^1[3-9]\\d{9}$", value);
    }

    public static boolean isEmail(String value) {
        return ReUtil.isMatch("^[\\w.-]+@[\\w.-]+\\.\\w+$", value);
    }

    public static boolean isIdCard(String value) {
        return ReUtil.isMatch("^\\d{17}[\\dXx]$", value);
    }

    public static boolean isChineseName(String value) {
        return ReUtil.isMatch("^[\\u4e00-\\u9faa]{2,20}$", value);
    }

    public static class Validator {
        private final List<String> errors = new ArrayList<>();

        public Validator notBlank(String value, String fieldName) {
            if (StrUtil.isBlank(value)) {
                errors.add(fieldName + "不能为空");
            }
            return this;
        }

        public Validator match(String value, String fieldName, Predicate<String> rule, String errorMsg) {
            if (StrUtil.isNotBlank(value) && !rule.test(value)) {
                errors.add(fieldName + errorMsg);
            }
            return this;
        }

        public Validator lengthBetween(String value, String fieldName, int min, int max) {
            if (StrUtil.isNotBlank(value)) {
                int len = value.length();
                if (len < min || len > max) {
                    errors.add(fieldName + "长度应在" + min + "-" + max + "之间");
                }
            }
            return this;
        }

        public Validator range(int value, String fieldName, int min, int max) {
            if (value < min || value > max) {
                errors.add(fieldName + "应在" + min + "-" + max + "之间");
            }
            return this;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return List.copyOf(errors);
        }

        public String firstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }
    }

    public static Validator newValidator() {
        return new Validator();
    }

    public static void demo() {
        System.out.println("═══ 数据校验器 ═══");

        System.out.println("  手机号校验：");
        System.out.println("    13812345678 → " + isPhone("13812345678"));
        System.out.println("    12345 → " + isPhone("12345"));

        System.out.println("  邮箱校验：");
        System.out.println("    ibqy@example.com → " + isEmail("ibqy@example.com"));
        System.out.println("    not-email → " + isEmail("not-email"));

        System.out.println("  中文名校验：");
        System.out.println("    张三 → " + isChineseName("张三"));
        System.out.println("    abc → " + isChineseName("abc"));

        System.out.println("  链式校验：");
        Validator result = newValidator()
                .notBlank("", "用户名")
                .match("12345", "手机号", ValidatorDemo::isPhone, "格式不正确")
                .match("not-email", "邮箱", ValidatorDemo::isEmail, "格式不正确")
                .lengthBetween("ab", "密码", 6, 20)
                .range(200, "年龄", 1, 150);

        System.out.println("    是否通过 → " + result.isValid());
        System.out.println("    错误列表：");
        for (String err : result.getErrors()) {
            System.out.println("      - " + err);
        }

        System.out.println();
    }
}
