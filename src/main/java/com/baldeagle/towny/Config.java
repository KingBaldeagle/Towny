package com.baldeagle.towny;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue TOWN_BLOCK_SIZE = BUILDER
        .comment("Town block size in blocks.")
        .defineInRange("townBlockSize", 16, 1, 1024);

    public static final ModConfigSpec.IntValue TOWN_BLOCK_RATIO = BUILDER
        .comment("Town blocks per resident.")
        .defineInRange("townBlockRatio", 8, 1, 1024);

    public static final ModConfigSpec.IntValue MAX_TOWN_BLOCKS = BUILDER
        .comment("Maximum town blocks. 0 means unlimited.")
        .defineInRange("maxTownBlocks", 0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.LongValue PRICE_NEW_TOWN_IN_COPPER = BUILDER
        .comment("New town creation price in copper.")
        .defineInRange("priceNewTownInCopper", 10_000L, 0L, Long.MAX_VALUE);

    public static final ModConfigSpec.LongValue PRICE_NEW_NATION_IN_COPPER = BUILDER
        .comment("New nation creation price in copper.")
        .defineInRange("priceNewNationInCopper", 100_000L, 0L, Long.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> PERSISTENCE_BACKEND = BUILDER
        .comment("Persistence backend: json_snapshot or directory_json.")
        .define("persistenceBackend", "directory_json");

    public static final ModConfigSpec.ConfigValue<String> DATA_DIRECTORY = BUILDER
        .comment("Relative data directory for Towny persistence.")
        .define("dataDirectory", "config/towny");

    public static final ModConfigSpec.ConfigValue<String> ECONOMY_PROVIDER = BUILDER
        .comment("Economy provider: lightmans_currency or closed.")
        .define("economyProvider", "lightmans_currency");

    public static final ModConfigSpec.IntValue TAX_COLLECTION_INTERVAL_TICKS = BUILDER
        .comment("How often to run Towny tax collection tick tasks.")
        .defineInRange("taxCollectionIntervalTicks", 24_000, 20, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue NATION_TAX_PERCENT = BUILDER
        .comment("Nation tax percentage collected from each member town balance per cycle.")
        .defineInRange("nationTaxPercent", 3, 0, 100);

    public static final ModConfigSpec.BooleanValue HEALTH_REGEN_ENABLED = BUILDER
        .comment("Enable Phase-6 bonus health regeneration in owned or allied territory.")
        .define("healthRegenEnabled", true);

    public static final ModConfigSpec.IntValue HEALTH_REGEN_INTERVAL_TICKS = BUILDER
        .comment("Tick interval for Phase-6 health regeneration checks.")
        .defineInRange("healthRegenIntervalTicks", 40, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue HEALTH_REGEN_HEARTS = BUILDER
        .comment("Hearts restored when Phase-6 regen applies.")
        .defineInRange("healthRegenHearts", 1.0d, 0.0d, 20.0d);

    public static final ModConfigSpec.BooleanValue PLOT_REGEN_ENABLED = BUILDER
        .comment("Enable Phase-6 plot regeneration timer checks for unowned plots.")
        .define("plotRegenEnabled", false);

    public static final ModConfigSpec.IntValue PLOT_REGEN_INTERVAL_TICKS = BUILDER
        .comment("Tick interval for Phase-6 plot regeneration scans.")
        .defineInRange("plotRegenIntervalTicks", 24_000, 20, Integer.MAX_VALUE);

    public static final ModConfigSpec.LongValue DAILY_UPKEEP_PER_TOWN_BLOCK_COPPER = BUILDER
        .comment("Phase-6 daily upkeep charged per claimed town block each tax cycle.")
        .defineInRange("dailyUpkeepPerTownBlockCopper", 0L, 0L, Long.MAX_VALUE);

    public static final ModConfigSpec.IntValue PLOT_REGEN_GRACE_CYCLES = BUILDER
        .comment("Phase-6 cycles an unowned plot must remain idle before being regenerated.")
        .defineInRange("plotRegenGraceCycles", 1, 1, Integer.MAX_VALUE);


    public static final ModConfigSpec.IntValue TP_WARMUP_SECONDS = BUILDER
        .comment("Teleport warmup duration in seconds.")
        .defineInRange("tpWarmupSeconds", 5, 0, 300);

    public static final ModConfigSpec.BooleanValue TP_CANCEL_ON_MOVE = BUILDER
        .comment("Cancel warmup teleport when the player moves.")
        .define("tpCancelOnMove", true);

    public static final ModConfigSpec.BooleanValue TP_CANCEL_ON_DAMAGE = BUILDER
        .comment("Cancel warmup teleport when the player is damaged.")
        .define("tpCancelOnDamage", true);

    public static final ModConfigSpec.IntValue TP_COOLDOWN_SECONDS = BUILDER
        .comment("Teleport cooldown after successful teleport in seconds.")
        .defineInRange("tpCooldownSeconds", 30, 0, 3600);

    static final ModConfigSpec SPEC = BUILDER.build();
}
