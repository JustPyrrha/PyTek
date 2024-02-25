/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.server

import gay.pyrrha.pytek.utils.SERVER_TAG
import gay.pyrrha.pytek.utils.logTag
import io.github.oshai.kotlinlogging.KotlinLogging
import org.quiltmc.loader.api.ModContainer
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger { }
private val TAG = logTag(SERVER_TAG)

public object PyTekServer {
    public fun onInitializeServer(mod: ModContainer) {
        logger.info { "$TAG Initializing..." }
        val initTime = measureTimeMillis {

        }
        logger.info { "$TAG Initialized. (Took ${initTime}ms)" }
    }
}
