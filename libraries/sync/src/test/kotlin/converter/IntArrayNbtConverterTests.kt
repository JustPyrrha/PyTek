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
import gay.pyrrha.sync.converter.IntArrayNbtConverter
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIntArray
import org.junit.jupiter.api.Test

class IntArrayNbtConverterTests {

    @Test
    fun `is valid for kotlin IntArray`() {
        // Given
        val name = IntArray::class.qualifiedName!!

        // When
        val isValid = IntArrayNbtConverter.validFor(name)

        // Then
        assertThat(isValid).isTrue()
    }

    @Test
    fun `can read int array`() {
        // Given
        val name = "testIntArray"
        val value = intArrayOf(0, -1, 1, Int.MAX_VALUE, Int.MIN_VALUE)
        val compound = NbtCompound()
        compound.putIntArray(name, value)

        // When
        val testInt = IntArrayNbtConverter.read(compound, name)

        // Then
        assertThat(testInt).isEqualTo(value)
    }

    @Test
    fun `can write int array`() {
        // Given
        val name = "testIntArray"
        val value = intArrayOf(0, -1, 1, Int.MAX_VALUE, Int.MIN_VALUE)
        val compound = NbtCompound()

        // When
        IntArrayNbtConverter.write(compound, name, value)

        // Then
        assertThat(compound.contains(name, NbtIntArray.INT_ARRAY_TYPE.toInt())).isTrue()
        assertThat(compound.getIntArray(name)).isEqualTo(value)
    }
}
