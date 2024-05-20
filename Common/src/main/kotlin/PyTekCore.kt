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

public val logger: KLogger = KotlinLogging.logger {  }
public const val TAG: String = "[PyTek]"
public const val MOD_ID: String = "pytek"

public object PyTekCore {
    public fun init() {
        logger.info { "$TAG Initializing..." }
        val initMs = measureTimeMillis {
            logger.info { "$TAG Running on ${ModRuntime.instance.runtimeName} in ${if(ModRuntime.instance.isDevEnv()) "dev" else "prod"}" }
        }
        logger.info { "$TAG Initialized. (Took ${initMs}ms)" }
    }
}
