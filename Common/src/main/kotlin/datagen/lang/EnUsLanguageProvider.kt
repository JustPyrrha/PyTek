/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.datagen.lang

import gay.pyrrha.pytek.MOD_ID
import gay.pyrrha.pytek.blocks.ModBlocks
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.block.Block

public object EnUsLanguageProvider : CustomLanguageProvider {
    override fun provide(
        addBlock: AddFunc<Block>,
        addItem: AddFunc<Item>,
        addItemStack: AddFunc<ItemStack>,
        addEnchantment: AddFunc<Enchantment>,
        addEffect: AddFunc<MobEffect>,
        addEntity: AddFunc<EntityType<*>>,
        addTag: AddFunc<TagKey<*>>,
        addString: AddFunc<String>
    ) {
        // Blocks
        addBlock(ModBlocks.REACTOR_CONTROLLER, "Reactor Controller")
        addBlock(ModBlocks.CABLE, "Cable")
    }
}
