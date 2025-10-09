package com.sanolink.techev_additions.recipes.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TechevFanRecipeSerializer extends ProcessingRecipeSerializer<TechevFanRecipe> {

    public TechevFanRecipeSerializer() {
        super(TechevFanRecipe::new);
    }

    @Override
    protected @NotNull TechevFanRecipe readFromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        TechevFanRecipe recipe = super.readFromJson(recipeId, json);

        if (json.has("catalysts")) {
            JsonArray array = json.getAsJsonArray("catalysts");
            List<TechevFanRecipe.Catalyst> catalysts = new ArrayList<>();

            for (JsonElement el : array) {
                JsonObject catObj = el.getAsJsonObject();
                ResourceLocation fluid = catObj.has("fluid") ? ResourceLocation.parse(catObj.get("fluid").getAsString()) : null;
                ResourceLocation block = catObj.has("block") ? ResourceLocation.parse(catObj.get("block").getAsString()) : null;

                int color = 0xFFFFFF;
                if (catObj.has("color")) {
                    String colorStr = catObj.get("color").getAsString().replace("#", "");
                    try {
                        color = (int) Long.parseLong(colorStr, 16);
                    } catch (NumberFormatException ignored) {}
                }

                catalysts.add(new TechevFanRecipe.Catalyst(fluid, block, color));
            }

            recipe.setCatalysts(catalysts);
        }

        return recipe;
    }
}
