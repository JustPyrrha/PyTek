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

@file:Suppress("MagicNumber")
package gay.pyrrha.demontech.datagen.provider

import gay.pyrrha.demontech.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeCategory
import java.util.function.Consumer

class ModRecipeProvider(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generateRecipes(exporter: Consumer<RecipeJsonProvider>) {
        // thermal_pad
        ShapedRecipeJsonFactory.create(RecipeCategory.MISC, ModItems.THERMAL_PAD, 8)
            .pattern("oi")
            .pattern("go")
            .ingredient('o', Items.OBSIDIAN)
            .ingredient('i', Items.IRON_INGOT)
            .ingredient('g', Items.GOLD_NUGGET)
            .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
            .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
            .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET))
            .offerTo(exporter)

        // adaptive_thermal_pad
        ShapedRecipeJsonFactory.create(RecipeCategory.MISC, ModItems.ADAPTIVE_THERMAL_PAD, 8)
            .pattern("ppp")
            .pattern("pcp")
            .pattern("ppp")
            .ingredient('p', ModItems.THERMAL_PAD)
            .ingredient('c', Items.COPPER_INGOT)
            .criterion(hasItem(ModItems.THERMAL_PAD), conditionsFromItem(ModItems.THERMAL_PAD))
            .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
            .offerTo(exporter)
    }
}
