/*
 * Copyright 2023 Pyrrha Wills
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package converter

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import gay.pyrrha.sync.converter.LongArrayNbtConverter
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtLongArray
import org.junit.jupiter.api.Test

class LongArrayNbtConverterTests {
    @Test
    fun `is valid for kotlin LongArray`() {
        // Given
        val kClass = LongArray::class.qualifiedName!!

        // When
        val isValid = LongArrayNbtConverter.validFor(kClass)

        // Then
        assertThat(isValid).isTrue()
    }

    @Test
    fun `can read long array`() {
        // Given
        val name = "testLongArray"
        val value = longArrayOf(0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE)
        val compound = NbtCompound()
        compound.putLongArray(name, value)

        // When
        val testLong = LongArrayNbtConverter.read(compound, name)

        // Then
        assertThat(testLong).isEqualTo(value)
    }

    @Test
    fun `can write long array`() {
        // Given
        val name = "testLongArray"
        val value = longArrayOf(0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE)
        val compound = NbtCompound()

        // When
        LongArrayNbtConverter.write(compound, name, value)

        // Then
        assertThat(compound.contains(name, NbtLongArray.LONG_ARRAY_TYPE.toInt())).isTrue()
        assertThat(compound.getLongArray(name)).isEqualTo(value)
    }
}
