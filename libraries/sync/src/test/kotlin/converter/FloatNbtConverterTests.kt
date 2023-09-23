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
import gay.pyrrha.sync.converter.FloatNbtConverter
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtFloat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class FloatNbtConverterTests {

    @Test
    fun `is valid for kotlin Double`() {
        // Given
        val name = Float::class.qualifiedName!!

        // When
        val isValid = FloatNbtConverter.validFor(name)

        // Then
        assertThat(isValid).isTrue()
    }

    @ParameterizedTest
    @ValueSource(floats = [0f, 1f, -1f, 1.5f, 1.5f, Float.MAX_VALUE, Float.MIN_VALUE])
    fun `can read floats`(value: Float) {
        // Given
        val name = "testFloat"
        val compound = NbtCompound()
        compound.putFloat(name, value)

        // When
        val testFloat = FloatNbtConverter.read(compound, name)

        // Then
        assertThat(testFloat).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(floats = [0f, 1f, -1f, 1.5f, 1.5f, Float.MAX_VALUE, Float.MIN_VALUE])
    fun `can write floats`(value: Float) {
        // Given
        val name = "testFloat"
        val compound = NbtCompound()

        // When
        FloatNbtConverter.write(compound, name, value)

        // Then
        assertThat(compound.contains(name, NbtFloat.FLOAT_TYPE.toInt())).isTrue()
        assertThat(compound.getFloat(name)).isEqualTo(value)
    }
}
