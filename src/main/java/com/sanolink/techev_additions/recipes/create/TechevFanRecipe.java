package com.sanolink.techev_additions.recipes.create;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class TechevFanRecipe extends ProcessingRecipe<TechevFanRecipe.TechevFanWrapper> {

    protected List<Catalyst> catalysts = new ArrayList<>();

    public TechevFanRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TechevCreateRecipeTypes.TECHEV_FAN, params);
    }

    @Override
    public boolean matches(TechevFanRecipe.TechevFanWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }

    public List<Catalyst> getCatalysts() {
        return catalysts;
    }

    @Nullable
    public Catalyst getFirstCatalyst() {
        return catalysts.isEmpty() ? null : catalysts.get(0);
    }

    public void setCatalysts(List<Catalyst> catalysts) {
        this.catalysts.clear();
        this.catalysts.addAll(catalysts);
    }

    protected int getMaxInputCount() {
        return 1;
    }

    protected int getMaxOutputCount() {
        return 12;
    }

    public static class TechevFanWrapper extends RecipeWrapper {
        public TechevFanWrapper() { super(new ItemStackHandler(1)); }
    }

    public record Catalyst(@Nullable ResourceLocation fluid, @Nullable ResourceLocation block, int color) {
    }

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeVarInt(catalysts.size());

        for (Catalyst catalyst : catalysts) {
            buffer.writeBoolean(catalyst.fluid() != null);
            if (catalyst.fluid() != null)
                buffer.writeResourceLocation(catalyst.fluid());

            buffer.writeBoolean(catalyst.block() != null);
            if (catalyst.block() != null)
                buffer.writeResourceLocation(catalyst.block());

            buffer.writeInt(catalyst.color());
        }
    }

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        catalysts.clear();

        int count = buffer.readVarInt();
        for (int i = 0; i < count; i++) {
            ResourceLocation fluid = null;
            if (buffer.readBoolean())
                fluid = buffer.readResourceLocation();

            ResourceLocation block = null;
            if (buffer.readBoolean())
                block = buffer.readResourceLocation();

            int color = buffer.readInt();

            catalysts.add(new Catalyst(fluid, block, color));
        }
    }
}
