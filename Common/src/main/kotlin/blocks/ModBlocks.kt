/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.blocks

import gay.pyrrha.pytek.rl
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

public object ModBlocks {
    public val REACTOR_CONTROLLER: ReactorControllerBlock by lazy { ReactorControllerBlock() }
    public val CABLE: CableBlock by lazy { CableBlock() }

    public fun init(register: (rl: ResourceLocation, block: Block) -> Unit) {
        register(rl("reactor_controller"), REACTOR_CONTROLLER)
        register(rl("cable"), CABLE)
    }

    public fun initBlockItems(register: (rl: ResourceLocation, item: Item) -> Unit) {
        val defaultProps = Item.Properties()

        fun registerDefault(block: Block) =
            register(BuiltInRegistries.BLOCK.getKey(block), BlockItem(block, defaultProps))

        registerDefault(REACTOR_CONTROLLER)
        registerDefault(CABLE)
    }
}
