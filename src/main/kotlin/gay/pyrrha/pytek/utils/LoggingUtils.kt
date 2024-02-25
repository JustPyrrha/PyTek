/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.utils

public const val BASE_TAG: String = "PyTek"
public const val CLIENT_TAG: String = "Client"
public const val SERVER_TAG: String = "Server"

public fun logTag(subTag: String? = null): String =
    StringBuilder().apply {
        append("[")
        append(BASE_TAG)
        if (subTag != null) {
            append("|")
            append(subTag)
        }
        append("]")
    }.toString()
