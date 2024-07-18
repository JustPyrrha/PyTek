/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package gay.pyrrha.pytek

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.system.measureTimeMillis
import net.fabricmc.api.ModInitializer

internal val LOGGER: KLogger = KotlinLogging.logger {}

public object PyTek : ModInitializer {
    override fun onInitialize() {
        LOGGER.info { "$TAG Initializing..." }
        val initTimeMillis = measureTimeMillis {}

        LOGGER.info { "$TAG Initialized. (Took ${initTimeMillis}ms)" }
    }
}
