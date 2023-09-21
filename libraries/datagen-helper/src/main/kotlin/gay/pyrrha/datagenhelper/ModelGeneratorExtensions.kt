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

package gay.pyrrha.datagenhelper

import net.minecraft.block.Block
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Texture
import net.minecraft.data.client.model.TextureKey

fun BlockStateModelGenerator.registerHorizontalRotatable(block: Block) {
     registerNorthDefaultHorizontalRotatable(
          block,
          Texture()
               .put(TextureKey.PARTICLE, Texture.getSubId(block, "_front"))
               .put(TextureKey.TOP, Texture.getSubId(block, "_top"))
               .put(TextureKey.BOTTOM, Texture.getSubId(block, "_bottom"))
               .put(TextureKey.SIDE, Texture.getSubId(block, "_side"))
               .put(TextureKey.FRONT, Texture.getSubId(block, "_front"))
               .put(TextureKey.BACK, Texture.getSubId(block, "_back"))
     )
}
