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
import gay.pyrrha.sync.converter.BooleanNbtConverter
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtCompound
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BooleanNbtConverterTests {

    @Test
    fun `is valid for kotlin Boolean`() {
        // Given
        val name = Boolean::class.qualifiedName!!

        // When
        val isValid = BooleanNbtConverter.validFor(name)

        // Then
        assertThat(isValid).isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `can read booleans`(value: Boolean) {
        // Given
        val name = "testBoolean"
        val compound = NbtCompound()
        compound.putBoolean(name, value)

        // When
        val testBoolean = BooleanNbtConverter.read(compound, name)

        // Then
        assertThat(testBoolean).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `can write booleans`(value: Boolean) {
        // Given
        val name = "testBoolean"
        val compound = NbtCompound()

        // When
        BooleanNbtConverter.write(compound, name, value)

        // Then
        assertThat(compound.contains(name, NbtByte.BYTE_TYPE.toInt())).isTrue()
        assertThat(compound.getBoolean(name)).isEqualTo(value)
    }
}
