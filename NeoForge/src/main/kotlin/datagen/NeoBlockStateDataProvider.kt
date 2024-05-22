/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.neoforge.datagen

import gay.pyrrha.pytek.MOD_ID
import gay.pyrrha.pytek.blocks.ModBlocks
import gay.pyrrha.pytek.rl
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

// todo(py, 21/05/2024): make this platform non-specific
public class NeoBlockStateDataProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        horizontalBlock(
            ModBlocks.REACTOR_CONTROLLER,
            rl("block/reactor_controller_side"),
            rl("block/reactor_controller_front"),
            rl("block/reactor_controller_top")
        )

        simpleBlock(ModBlocks.CABLE)
    }
}
