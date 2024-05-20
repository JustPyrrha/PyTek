/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek

import java.util.*

public interface ModRuntime {
    public val runtimeName: String
    public fun isModLoaded(modId: String): Boolean
    public fun isDevEnv(): Boolean

    public companion object {
        public val instance: ModRuntime = ServiceLoader.load(ModRuntime::class.java).stream().toList().firstOrNull()?.get()
            ?: throw IllegalStateException("Could not find an implementation of ${ModRuntime::class.java.name}")
    }
}
