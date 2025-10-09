package com.sanolink.techev_additions.accessors.create;

import com.sanolink.techev_additions.recipes.create.TechevFanRecipe;
import javax.annotation.Nullable;

public interface AirCurrentCatalystAccess {
    void techevAdditions$setCurrentCatalyst(@Nullable TechevFanRecipe.Catalyst catalyst);
    @Nullable
    TechevFanRecipe.Catalyst techevAdditions$getCurrentCatalyst();
}