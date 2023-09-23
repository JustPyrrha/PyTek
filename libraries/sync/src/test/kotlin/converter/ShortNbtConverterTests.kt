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
import gay.pyrrha.sync.converter.ShortNbtConverter
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtShort
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ShortNbtConverterTests {
    @Test
    fun `is valid for kotlin Short`() {
        // Given
        val kClass = Short::class.qualifiedName!!

        // When
        val isValid = ShortNbtConverter.validFor(kClass)

        // Then
        assertThat(isValid).isTrue()
    }

    @ParameterizedTest
    @ValueSource(shorts = [0, -1, 1, Short.MAX_VALUE, Short.MIN_VALUE])
    fun `can read shorts`(value: Short) {
        // Given
        val name = "testShort"
        val compound = NbtCompound()
        compound.putShort(name, value)

        // When
        val testShort = ShortNbtConverter.read(compound, name)

        // Then
        assertThat(testShort).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(shorts = [0, -1, 1, Short.MAX_VALUE, Short.MIN_VALUE])
    fun `can write shorts`(value: Short) {
        // Given
        val name = "testShort"
        val compound = NbtCompound()

        // When
        ShortNbtConverter.write(compound, name, value)

        // Then
        assertThat(compound.contains(name, NbtShort.SHORT_TYPE.toInt())).isTrue()
        assertThat(compound.getShort(name)).isEqualTo(value)
    }
}
