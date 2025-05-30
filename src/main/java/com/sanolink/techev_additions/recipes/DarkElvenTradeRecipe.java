package com.sanolink.techev_additions.recipes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sanolink.techev_additions.block.TechevBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.crafting.RecipeSerializerBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DarkElvenTradeRecipe implements IDarkElvenTradeRecipe {
    private final ResourceLocation id;
    private final ImmutableList<ItemStack> outputs;
    private final NonNullList<Ingredient> inputs;

    public DarkElvenTradeRecipe(ResourceLocation id, ItemStack[] outputs, Ingredient... inputs) {
        this.id = id;
        this.outputs = ImmutableList.copyOf(outputs);
        this.inputs = NonNullList.create();
        this.inputs.addAll(Arrays.asList(inputs));
    }

    @Override
    public Optional<List<ItemStack>> match(List<ItemStack> stacks) {
        List<Ingredient> inputsMissing = new ArrayList<>(inputs);
        List<ItemStack> stacksToRemove = new ArrayList<>();

        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                continue;
            }
            if (inputsMissing.isEmpty()) {
                break;
            }

            int stackIndex = -1;

            for (int i = 0; i < inputsMissing.size(); i++) {
                Ingredient ingr = inputsMissing.get(i);
                if (ingr.test(stack)) {
                    if (!stacksToRemove.contains(stack)) {
                        stacksToRemove.add(stack);
                    }
                    stackIndex = i;
                    break;
                }
            }

            if (stackIndex != -1) {
                inputsMissing.remove(stackIndex);
            }
        }

        return inputsMissing.isEmpty() ? Optional.of(stacksToRemove) : Optional.empty();
    }

    @Override
    public boolean containsItem(ItemStack stack) {
        for (Ingredient input : inputs) {
            if (input.test(stack)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return TechevRecipeTypes.DARK_ELVEN_TRADE_SERIALIZER.get();
    }

    @NotNull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

    @NotNull
    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(TechevBlocks.SVARTALFPORTAL.get());
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<ItemStack> getOutputs() {
        return outputs;
    }

    @Override
    public List<ItemStack> getOutputs(List<ItemStack> inputs) {
        return getOutputs();
    }

    public static class Serializer extends RecipeSerializerBase<DarkElvenTradeRecipe> {

        @NotNull
        @Override
        public DarkElvenTradeRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            JsonElement output = json.get("output");
            List<ItemStack> outputStacks = new ArrayList<>();
            if (output.isJsonArray()) {
                for (JsonElement e : output.getAsJsonArray()) {
                    JsonObject o = GsonHelper.convertToJsonObject(e, "output stack");
                    outputStacks.add(ShapedRecipe.itemStackFromJson(o));
                }
            } else {
                JsonObject o = GsonHelper.convertToJsonObject(output, "output stack");
                outputStacks.add(ShapedRecipe.itemStackFromJson(o));
            }

            List<Ingredient> inputs = new ArrayList<>();
            for (JsonElement e : GsonHelper.getAsJsonArray(json, "ingredients")) {
                Ingredient ing = Ingredient.fromJson(e);
                if (!ing.isEmpty()) {
                    inputs.add(ing);
                }
            }

            return new DarkElvenTradeRecipe(id, outputStacks.toArray(new ItemStack[0]), inputs.toArray(new Ingredient[0]));
        }

        @Override
        public DarkElvenTradeRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient[] inputs = new Ingredient[buf.readVarInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.fromNetwork(buf);
            }
            ItemStack[] outputs = new ItemStack[buf.readVarInt()];
            for (int i = 0; i < outputs.length; i++) {
                outputs[i] = buf.readItem();
            }
            return new DarkElvenTradeRecipe(id, outputs, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DarkElvenTradeRecipe recipe) {
            buf.writeVarInt(recipe.getIngredients().size());
            for (Ingredient input : recipe.getIngredients()) {
                input.toNetwork(buf);
            }
            buf.writeVarInt(recipe.getOutputs().size());
            for (ItemStack output : recipe.getOutputs()) {
                buf.writeItem(output);
            }
        }
    }
}
