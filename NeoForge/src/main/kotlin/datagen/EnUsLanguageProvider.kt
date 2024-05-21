/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.neoforge.datagen

import gay.pyrrha.pytek.MOD_ID
import gay.pyrrha.pytek.datagen.lang.EnUsLanguageProvider
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

public class EnUsLanguageProvider(output: PackOutput) : LanguageProvider(output, MOD_ID, "en_us") {
    override fun addTranslations() {
        EnUsLanguageProvider.provide(::add, ::add, ::add, ::add, ::add, ::add, ::add, ::add)
    }
}
