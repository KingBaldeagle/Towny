package com.baldeagle.towny;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Towny.MODID)
public class Towny {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "towny";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "towny" namespace
    public static final DeferredRegister.Blocks BLOCKS =
        DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "towny" namespace
    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "towny" namespace
}
