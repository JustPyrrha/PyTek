/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.neoforge.datagen

import gay.pyrrha.pytek.MOD_ID
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public object NeoForgeDataGenerator {
    @SubscribeEvent
    private fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val output = generator.packOutput
        val disabledExistingFileHelper = ExistingFileHelper(emptyList(), emptySet(), false, null, null)

        generator.addProvider(event.includeServer(), NeoUSEnglishLanguageDataProvider(output))
        generator.addProvider(event.includeServer(), NeoBlockStateDataProvider(output, disabledExistingFileHelper))
    }
}
