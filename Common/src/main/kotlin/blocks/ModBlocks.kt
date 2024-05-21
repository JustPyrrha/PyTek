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
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour

public object ModBlocks {

    public val REACTOR_CONTROLLER: Block by lazy { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)) }

    public fun init(register: (rl: ResourceLocation, block: Block) -> Unit) {
        register(rl("reactor_controller"), REACTOR_CONTROLLER)
    }

    public fun initBlockItems(register: (rl: ResourceLocation, item: Item) -> Unit) {
        val defaultProps = Item.Properties()
        register(BuiltInRegistries.BLOCK.getKey(REACTOR_CONTROLLER), BlockItem(REACTOR_CONTROLLER, defaultProps))
    }
}
