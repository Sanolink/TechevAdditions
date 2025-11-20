package com.sanolink.techev_additions.recipes.create;

import com.sanolink.techev_additions.recipes.TechevRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public enum TechevCreateRecipeTypes implements IRecipeTypeInfo {

    TECHEV_FAN(TechevFanRecipeSerializer::new);

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    private final @Nullable RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    private TechevCreateRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(this.name());
        this.id = Create.asResource(name);
        this.serializerObject = TechevRecipeTypes.SERIALIZERS.register(name, serializerSupplier);
        this.typeObject = TechevRecipeTypes.TYPES.register(name, () -> RecipeType.simple(this.id));
        this.type = this.typeObject;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }
}
