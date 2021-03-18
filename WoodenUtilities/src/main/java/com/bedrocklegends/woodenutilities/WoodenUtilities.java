package com.bedrocklegends.woodenutilities;

import com.bedrocklegends.woodenutilities.config.WoodenConfig;
import com.bedrocklegends.woodenutilities.setup.*;
import net.minecraftforge.fml.common.Mod;

@Mod(WoodenUtilities.ID)
public class WoodenUtilities {
    /**
     * @deprecated Use the constant in the WoodenConstants class
     */
    @Deprecated
    public static final String ID = "woodenutilities";


    public WoodenUtilities() {
        WoodenConfig.init();
        WoodenItems.register();
        WoodenBlocks.register();
        WoodenTiles.register();
        WoodenFluids.register();
        WoodenTags.register();
    }
}