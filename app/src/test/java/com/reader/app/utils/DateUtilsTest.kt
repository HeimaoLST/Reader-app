package com.reader.app.utils

import org.junit.Assert.*
import org.junit.Test

/**
 * DateUtils 单元测试示例
 * 测试日期时间工具类的时间转换功能
 */
class DateUtilsTest {

    @Test
    fun `parseIsoToLong should parse valid ISO string correctly`() {
        // Given
        val isoString = "2024-01-15T10:00:00Z"

        // When
        val result = DateUtils.parseIsoToLong(isoString)

        // Then
        val expected = 1705310400000L // 2024-01-15T10:00:00Z in milliseconds
        assertEquals(expected, result)
    }

    @Test
    fun `parseIsoToLong should handle different timezones`() {
        // Given
        val isoString = "2024-01-15T10:00:00+08:00"

        // When
        val result = DateUtils.parseIsoToLong(isoString)

        // Then - 转换为 UTC 时间戳
        val expected = 1705281600000L
        assertEquals(expected, result)
    }

    @Test
    fun `parseIsoToLong should return current time on invalid input`() {
        // Given
        val invalidString = "invalid-date"

        // When
        val result = DateUtils.parseIsoToLong(invalidString)
        val before = System.currentTimeMillis()
        val after = System.currentTimeMillis()

        // Then - 应该返回当前时间戳
        assertTrue(result in before..after)
    }

    @Test
    fun `parseIsoToLong should handle empty string`() {
        // Given
        val emptyString = ""

        // When
        val result = DateUtils.parseIsoToLong(emptyString)
        val before = System.currentTimeMillis()
        val after = System.currentTimeMillis()

        // Then
        assertTrue(result in before..after)
    }
}
