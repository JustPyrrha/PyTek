/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.neoforge

import gay.pyrrha.pytek.MOD_ID
import gay.pyrrha.pytek.PyTekCore
import gay.pyrrha.pytek.blocks.ModBlocks
import gay.pyrrha.pytek.blocks.entity.ModBlockEntities
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.registries.RegisterEvent

@Mod(MOD_ID)
public class PyTekNeoForge(private val modEventBus: IEventBus, modContainer: ModContainer) {

    private fun <T> bindRegistry(registry: ResourceKey<Registry<T>>, source: ((ResourceLocation, T) -> Unit) -> Unit) {
        modEventBus.addListener<RegisterEvent> { event ->
            if (event.registryKey == registry) {
                source { rl, t -> event.register(registry, rl) { t } }
            }
        }
    }

    init {
        registryInit()
        modEventBus.addListener(::commonSetup)
    }

    private fun registryInit() {
        bindRegistry(Registries.BLOCK, ModBlocks::init)
        bindRegistry(
            Registries.ITEM,
            ModBlocks::initBlockItems
        ) // todo(py, 20/05/2024): Add BlockItems to a creative tab.
        bindRegistry(Registries.BLOCK_ENTITY_TYPE, ModBlockEntities::init)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        PyTekCore.init { }
    }
}
