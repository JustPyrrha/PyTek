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

package gay.pyrrha.demontech.block.entity

import gay.pyrrha.sync.Sync
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

@Sync
class ThermalGeneratorBlockEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(ModBlockEntities.THERMAL_GENERATOR, pos, state) {

    @Sync
    var active: Boolean = false
        internal set

    @Sync
    private var energyBuffer: Int = 0

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(
            ThermalGeneratorBlockEntitySync.write(
                nbt,
                ThermalGeneratorBlockEntitySyncData(
                    active,
                    energyBuffer
                )
            )
        )
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        ThermalGeneratorBlockEntitySync.read(nbt) {
            this@ThermalGeneratorBlockEntity.active = active
            this@ThermalGeneratorBlockEntity.energyBuffer = energyBuffer
        }
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> =
        BlockEntityUpdateS2CPacket.of(this)

    override fun toSyncedNbt(): NbtCompound {
        val nbt = NbtCompound()
        writeNbt(nbt)
        return nbt
    }

    companion object {
        fun tick(world: World, entity: ThermalGeneratorBlockEntity) {
            if (!world.isClient && entity.active) {
                entity.energyBuffer++
            }
        }
    }
}
