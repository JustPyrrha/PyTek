/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.neoforge

import com.mojang.datafixers.types.Type
import gay.pyrrha.pytek.ModRuntime
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader

public class ModRuntimeNeoForge : ModRuntime {
    override val runtimeName: String
        get() = "NeoForge"

    override fun isModLoaded(modId: String): Boolean =
        ModList.get().isLoaded(modId)

    override fun isDevEnv(): Boolean =
        FMLLoader.isProduction().not()

    override fun <T : BlockEntity> createBlockEntityType(
        vararg blocks: Block,
        dfuType: Type<*>?,
        factory: (BlockPos, BlockState) -> T
    ): BlockEntityType<T> {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") // for dfuType
        return BlockEntityType.Builder.of(factory, *blocks).build(dfuType)
    }
}
