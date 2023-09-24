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

package gay.pyrrha.demontech.gametest

import gay.pyrrha.demontech.block.ModBlocks
import gay.pyrrha.demontech.gametest.util.checkBlock
import net.minecraft.test.GameTest
import net.minecraft.util.math.BlockPos
import org.quiltmc.qsl.testing.api.game.QuiltGameTest
import org.quiltmc.qsl.testing.api.game.QuiltTestContext

class DemonTechGameTestSuite : QuiltGameTest {
    @GameTest(structureName = QuiltGameTest.EMPTY_STRUCTURE)
    fun simpleTest(context: QuiltTestContext) {
        val pos = BlockPos(0, 2, 0)
        context.setBlockState(pos, ModBlocks.THERMAL_GENERATOR)

        context.succeedWhen {
            context.checkBlock(pos, "Expected Thermal Generator") { it == ModBlocks.THERMAL_GENERATOR }
        }
    }
}
