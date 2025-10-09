package com.sanolink.techev_additions.recipes.create;

import com.mojang.math.Vector3f;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TechevFanProcessingTypes extends AllFanProcessingTypes {

    public static final TechevFanType TECHEV_FAN = register("techev_fan", new TechevFanType());

    private static final Map<String, FanProcessingType> LEGACY_NAME_MAP;

    static {
        Object2ReferenceOpenHashMap<String, FanProcessingType> map = new Object2ReferenceOpenHashMap<>();
        map.put("TECHEV_FAN", TECHEV_FAN);
        map.trim();
        LEGACY_NAME_MAP = map;
    }

    private static <T extends FanProcessingType> T register(String id, T type) {
        FanProcessingTypeRegistry.register(Create.asResource(id), type);
        return type;
    }

    public static void register() {
    }

    @Nullable
    public static FanProcessingType ofLegacyName(String name) {
        return LEGACY_NAME_MAP.get(name);
    }


    public static class TechevFanType implements FanProcessingType {
        private static final TechevFanRecipe.TechevFanWrapper TECHEV_FAN_WRAPPER = new TechevFanRecipe.TechevFanWrapper();

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            FluidState fluidState = level.getFluidState(pos);
            BlockState blockState = level.getBlockState(pos);

            List<TechevFanRecipe> recipes = level.getRecipeManager()
                    .getAllRecipesFor(TechevCreateRecipeTypes.TECHEV_FAN.getType());

            for (TechevFanRecipe recipe : recipes) {
                for (TechevFanRecipe.Catalyst cat : recipe.getCatalysts()) {
                    if (cat.fluid() != null && fluidState.getType() == ForgeRegistries.FLUIDS.getValue(cat.fluid())) {
                        return true;
                    }
                    if (cat.block() != null && blockState.is(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(cat.block())))) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public int getPriority() {
            return 500;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            TECHEV_FAN_WRAPPER.setItem(0, stack);
            TechevFanRecipe.Catalyst currentCatalyst = CatalystContext.get();

            Optional<TechevFanRecipe> recipe = findRecipeWithCatalyst(level, currentCatalyst);
            return recipe.isPresent();
        }

        @Override
        @Nullable
        public List<ItemStack> process(ItemStack stack, Level level) {
            TECHEV_FAN_WRAPPER.setItem(0, stack);
            TechevFanRecipe.Catalyst currentCatalyst = CatalystContext.get();

            Optional<TechevFanRecipe> recipe = findRecipeWithCatalyst(level, currentCatalyst);
            return recipe.map(techevFanRecipe -> RecipeApplier.applyRecipeOn(stack, techevFanRecipe)).orElse(null);
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {

            TechevFanRecipe.Catalyst catalyst = CatalystContext.get();
            if (catalyst != null) {
                Vector3f color = new Color(catalyst.color()).asVectorF();
                level.addParticle(new DustParticleOptions(color, 1),
                        pos.x + (level.random.nextFloat() - 0.5f) * 0.5f,
                        pos.y + 0.5f,
                        pos.z + (level.random.nextFloat() - 0.5f) * 0.5f,
                        0, 1/8f, 0);
            } else {
                level.addParticle(new DustParticleOptions(new Vector3f(1f, 1f, 1f), 1),
                        pos.x + (level.random.nextFloat() - 0.5f) * 0.5f,
                        pos.y + 0.5f,
                        pos.z + (level.random.nextFloat() - 0.5f) * 0.5f,
                        0, 1/8f, 0);
            }
        }

        @Override
        public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {

            TechevFanRecipe.Catalyst catalyst = getCatalystFromContext(particleAccess);

            if (catalyst != null) {
                particleAccess.setColor(Color.mixColors(catalyst.color(), catalyst.color(), random.nextFloat()));
                particleAccess.setAlpha(1f);
            } else {
                particleAccess.setColor(0xFFFFFF);
                particleAccess.setAlpha(0.5f);
            }
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
        }

        @Nullable
        private TechevFanRecipe.Catalyst getCatalystFromContext(AirFlowParticleAccess particleAccess) {
            return CatalystContext.get();
        }

        private Optional<TechevFanRecipe> findRecipeWithCatalyst(
                Level level,
                @Nullable TechevFanRecipe.Catalyst currentCatalyst) {

            if (currentCatalyst == null) {
                return Optional.empty();
            }

            List<TechevFanRecipe> allRecipes = level.getRecipeManager()
                    .getAllRecipesFor(TechevCreateRecipeTypes.TECHEV_FAN.getType());

            for (TechevFanRecipe recipe : allRecipes) {
                if (!recipe.matches(TechevFanType.TECHEV_FAN_WRAPPER, level)) {
                    continue;
                }

                for (TechevFanRecipe.Catalyst catalyst : recipe.getCatalysts()) {
                    if (catalystsMatch(catalyst, currentCatalyst)) {
                        return Optional.of(recipe);
                    }
                }
            }

            return Optional.empty();
        }

        private boolean catalystsMatch(TechevFanRecipe.Catalyst a, TechevFanRecipe.Catalyst b) {
            if (a.fluid() != null && b.fluid() != null) {
                return a.fluid().equals(b.fluid());
            }
            if (a.block() != null && b.block() != null) {
                return a.block().equals(b.block());
            }
            return false;
        }
    }

    public static class CatalystContext {
        private static final ThreadLocal<TechevFanRecipe.Catalyst> CURRENT = new ThreadLocal<>();

        public static void set(@Nullable TechevFanRecipe.Catalyst catalyst) {
            if (catalyst == null) {
                CURRENT.remove();
            } else {
                CURRENT.set(catalyst);
            }
        }

        @Nullable
        public static TechevFanRecipe.Catalyst get() {
            return CURRENT.get();
        }

        public static void clear() {
            CURRENT.remove();
        }
    }
}
