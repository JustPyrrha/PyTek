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

package gay.pyrrha.demontech.client

import gay.pyrrha.demontech.DemonTech.LOGGER
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.minecraft.ClientOnly
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer

@ClientOnly
object DemonTechClient : ClientModInitializer {
    override fun onInitializeClient(mod: ModContainer) {
        LOGGER.info("[DemonTech|Client] Initializing...")
        val startTime = System.currentTimeMillis()



        LOGGER.info("[DemonTech|Client] Initialized. (Took {}ms)", System.currentTimeMillis()-startTime)
    }
}
