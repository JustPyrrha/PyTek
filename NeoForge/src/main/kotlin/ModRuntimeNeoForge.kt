/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.neoforge

import gay.pyrrha.pytek.ModRuntime
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader

public class ModRuntimeNeoForge : ModRuntime {
    override val runtimeName: String
        get() = "NeoForge"

    override fun isModLoaded(modId: String): Boolean =
        ModList.get().isLoaded(modId)

    override fun isDevEnv(): Boolean =
        FMLLoader.isProduction().not()
}
