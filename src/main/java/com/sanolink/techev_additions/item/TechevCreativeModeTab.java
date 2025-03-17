package com.sanolink.techev_additions.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TechevCreativeModeTab {

    public static final CreativeModeTab TECHEV_TAB = new CreativeModeTab("techev_tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(TechevItems.TECHEVIUM.get());
        }
    };
}
