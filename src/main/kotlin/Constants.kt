/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package gay.pyrrha.pytek

import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

public const val MOD_ID: String = "pytek"
public const val TAG: String = "[PyTek]"

public fun ident(path: String): Identifier = Identifier.of(MOD_ID, path)

@JvmName("identPayload")
public fun <T : CustomPayload> ident(path: String): CustomPayload.Id<T> =
    CustomPayload.Id<T>(ident(path))
