package com.rama.bohio.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Pure logic, no Android dependency at all, so this runs as a plain JVM
 * test with no Robolectric needed.
 */
class IdUtilsTest {

    @Test
    fun `zero is padded to the default length`() {
        assertThat(IdUtils.toBase36Fixed(0L)).isEqualTo("00000000000")
    }

    @Test
    fun `small value is padded and uppercased`() {
        // 35 in base36 is "z", uppercased to "Z"
        assertThat(IdUtils.toBase36Fixed(35L)).isEqualTo("0000000000Z")
    }

    @Test
    fun `result always matches the requested length`() {
        val id = IdUtils.toBase36Fixed(123456789L)
        assertThat(id).hasLength(11)
    }

    @Test
    fun `custom length is respected`() {
        assertThat(IdUtils.toBase36Fixed(1L, length = 2)).isEqualTo("01")
    }

    @Test
    fun `large timestamp-like value does not overflow the fixed length`() {
        // Real usage: IdUtils.toBase36Fixed(System.currentTimeMillis())
        val big = 9_999_999_999_999L
        val id = IdUtils.toBase36Fixed(big)
        assertThat(id).hasLength(11)
        assertThat(id).isEqualTo(id.uppercase())
    }

    @Test
    fun `two different inputs never collide`() {
        val a = IdUtils.toBase36Fixed(1000L)
        val b = IdUtils.toBase36Fixed(1001L)
        assertThat(a).isNotEqualTo(b)
    }
}
