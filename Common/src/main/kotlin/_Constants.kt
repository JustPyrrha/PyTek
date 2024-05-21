/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek

import net.minecraft.resources.ResourceLocation

public const val MOD_ID: String = "pytek"
public fun rl(path: String): ResourceLocation = ResourceLocation(MOD_ID, path)
