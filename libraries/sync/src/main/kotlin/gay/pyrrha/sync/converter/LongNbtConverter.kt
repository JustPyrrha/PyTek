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

package gay.pyrrha.sync.converter

import net.minecraft.nbt.NbtCompound

public object LongNbtConverter : NbtConverter<Long> {
    override fun validFor(qualifiedName: String): Boolean =
        Long::class.qualifiedName!! == qualifiedName

    override fun read(nbt: NbtCompound, name: String): Long =
        nbt.getLong(name)

    override fun write(nbt: NbtCompound, name: String, value: Long) {
        nbt.putLong(name, value)
    }
}
