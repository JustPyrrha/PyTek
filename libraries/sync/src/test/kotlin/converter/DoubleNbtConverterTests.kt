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
import gay.pyrrha.sync.converter.DoubleNbtConverter
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtDouble
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DoubleNbtConverterTests {

    @Test
    fun `is valid for kotlin Double`() {
        // Given
        val name = Double::class.qualifiedName!!

        // When
        val isValid = DoubleNbtConverter.validFor(name)

        // Then
        assertThat(isValid).isTrue()
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 1.0, -1.0, 1.5, -1.5, Double.MAX_VALUE, Double.MIN_VALUE])
    fun `can read doubles`(value: Double) {
        // Given
        val name = "testDouble"
        val compound = NbtCompound()
        compound.putDouble(name, value)

        // When
        val testDouble = DoubleNbtConverter.read(compound, name)

        // Then
        assertThat(testDouble).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 1.0, -1.0, 1.5, -1.5, Double.MAX_VALUE, Double.MIN_VALUE])
    fun `can write doubles`(value: Double) {
        // Given
        val name = "testDouble"
        val compound = NbtCompound()

        // When
        DoubleNbtConverter.write(compound, name, value)

        // Then
        assertThat(compound.contains(name, NbtDouble.DOUBLE_TYPE.toInt())).isTrue()
        assertThat(compound.getDouble(name)).isEqualTo(value)
    }
}
