/*
 * Copyright 2023 Pyrrha Wills
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gay.pyrrha.demontech.gametest.util

import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import org.quiltmc.qsl.testing.api.game.QuiltTestContext

fun QuiltTestContext.checkBlock(pos: BlockPos, errorMessage: String, predicate: (Block) -> Boolean): Unit =
    checkBlock(pos, predicate, errorMessage)
