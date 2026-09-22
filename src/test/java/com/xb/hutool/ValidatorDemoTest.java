package com.xb.hutool;

import com.xb.hutool.validation.ValidatorDemo;
import com.xb.hutool.validation.ValidatorDemo.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValidatorDemoTest - 数据校验器单元测试
 *
 * 覆盖静态校验方法（手机号/邮箱/身份证/中文名）和链式校验器的错误收集行为。
 *
 * @author ibqy
 */
@DisplayName("数据校验器测试")
class ValidatorDemoTest {

    @Nested
    @DisplayName("静态校验方法")
    class StaticValidators {

        @Test
        @DisplayName("手机号校验：合法通过，非法拒绝")
        void phoneValidation() {
            assertTrue(ValidatorDemo.isPhone("13812345678"));
            assertTrue(ValidatorDemo.isPhone("19900001111"));
            assertFalse(ValidatorDemo.isPhone("12345"));
            assertFalse(ValidatorDemo.isPhone("23812345678"));
            assertFalse(ValidatorDemo.isPhone(""));
        }

        @Test
        @DisplayName("邮箱校验：合法通过，非法拒绝")
        void emailValidation() {
            assertTrue(ValidatorDemo.isEmail("ibqy@example.com"));
            assertTrue(ValidatorDemo.isEmail("test.user@domain.org"));
            assertFalse(ValidatorDemo.isEmail("not-email"));
            assertFalse(ValidatorDemo.isEmail("@no-local.com"));
            assertFalse(ValidatorDemo.isEmail(""));
        }

        @Test
        @DisplayName("身份证校验：18 位合法通过")
        void idCardValidation() {
            assertTrue(ValidatorDemo.isIdCard("110101199001011234"));
            assertTrue(ValidatorDemo.isIdCard("44030119950101123X"));
            assertFalse(ValidatorDemo.isIdCard("12345"));
            assertFalse(ValidatorDemo.isIdCard(""));
        }

        @Test
        @DisplayName("中文名校验：2-20 个汉字通过")
        void chineseNameValidation() {
            assertTrue(ValidatorDemo.isChineseName("张三"));
            assertTrue(ValidatorDemo.isChineseName("欧阳修文"));
            assertFalse(ValidatorDemo.isChineseName("abc"));
            assertFalse(ValidatorDemo.isChineseName("张"));
            assertFalse(ValidatorDemo.isChineseName(""));
        }
    }

    @Nested
    @DisplayName("链式校验器")
    class ChainValidator {

        @Test
        @DisplayName("所有规则通过时 isValid 返回 true")
        void allRulesPass() {
            Validator result = ValidatorDemo.newValidator()
                    .notBlank("张三", "用户名")
                    .match("13812345678", "手机号", ValidatorDemo::isPhone, "格式不正确")
                    .match("test@example.com", "邮箱", ValidatorDemo::isEmail, "格式不正确");

            assertTrue(result.isValid());
            assertTrue(result.getErrors().isEmpty());
            assertNull(result.firstError());
        }

        @Test
        @DisplayName("多条规则失败时收集所有错误")
        void collectAllErrors() {
            Validator result = ValidatorDemo.newValidator()
                    .notBlank("", "用户名")
                    .match("12345", "手机号", ValidatorDemo::isPhone, "格式不正确")
                    .match("not-email", "邮箱", ValidatorDemo::isEmail, "格式不正确")
                    .lengthBetween("ab", "密码", 6, 20)
                    .range(200, "年龄", 1, 150);

            assertFalse(result.isValid());
            assertEquals(5, result.getErrors().size());
            assertEquals("用户名不能为空", result.firstError());
        }

        @Test
        @DisplayName("getErrors 返回不可变列表")
        void errorsListIsImmutable() {
            Validator result = ValidatorDemo.newValidator()
                    .notBlank("", "字段");

            assertThrows(UnsupportedOperationException.class, () -> result.getErrors().add("hack"));
        }
    }
}
