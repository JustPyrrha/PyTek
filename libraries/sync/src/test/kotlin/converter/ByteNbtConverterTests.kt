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
import gay.pyrrha.sync.converter.ByteNbtConverter
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtCompound
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ByteNbtConverterTests {

    @Test
    fun `is valid for kotlin Byte`() {
        // Given
        val name = Byte::class.qualifiedName!!

        // When
        val isValid = ByteNbtConverter.validFor(name)

        // Then
        assertThat(isValid).isTrue()
    }

    @ParameterizedTest
    @ValueSource(bytes = [0, 1, -1, Byte.MAX_VALUE, Byte.MIN_VALUE])
    fun `can read bytes`(value: Byte) {
        // Given
        val name = "testByte"
        val compound = NbtCompound()
        compound.putByte(name, value)

        // When
        val testByte = ByteNbtConverter.read(compound, name)

        // Then
        assertThat(testByte).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(bytes = [0, 1, -1, Byte.MAX_VALUE, Byte.MIN_VALUE])
    fun `can write bytes`(value: Byte) {
        // Given
        val name = "testByte"
        val compound = NbtCompound()

        // When
        ByteNbtConverter.write(compound, name, value)

        // Then
        assertThat(compound.contains(name, NbtByte.BYTE_TYPE.toInt())).isTrue()
        assertThat(compound.getByte(name)).isEqualTo(value)
    }
}
