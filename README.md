# Fab's Server Tweaks

`Fab's Server Tweaks` is a modular gameplay-tweaks mod focused on server-side quality-of-life changes, vanilla logic overrides, optional enchantment extensions, and data-driven recipe packs.

The mod is designed to work server-side, with optional client installation for matching visuals and a few client-only extras.

## Mod Requirements

### Dependencies

- Architectury API (required) – Allows the mod to run on Fabric or NeoForge.
- Cloth Config (required) - Makes configuring the mod possible.
- Fabric or NeoForge (required) – Needed for the mod to run on Minecraft.
  - Fabric additionally requires:
    - Fabric API
- Polymer (optional) [Fabric-only] – Allows the mod to add some server side content (read more below).
- Lootr (optional, requires Polymer) [Fabric-only] - Compatibility layer to make Lootr completely server side (read more below).
- Universal Ores (optional, requires Polymer) [Fabric-only] - Compatibility layer to make Universal Ores completely server side (read more below).
- Mod Menu (optional) [Fabric-only] – Allows the mod to add a configuration screen in game on clients.

## How Features Are Controlled

The mod is intentionally split across several control layers:

- `Config`: global mod settings such as trampling rules, leash behavior, seed sharing, and piston block-entity movement.
- `Gamerules`: per-world runtime toggles for most gameplay and vanilla-behavior overrides.
- `Built-in datapacks`: optional feature packs for enchantments, recipes, cauldron conversions, and durability smelting.
- `Tags and data files`: used to define targets, exceptions, item groups, stack overrides, leash categories, piston overrides, and more.

This makes it possible to run the mod in a very vanilla-like mode or enable a much broader tweak set.

## Client and Server Expectations

The mod works server-side only. Vanilla clients can connect and play normally.

For the best visual experience, installing it on the client is recommended since some custom piston and presentation logic may otherwise be shown using vanilla client visuals.

To easily find the recipes, it's recommended to install a recipe viewer like JEI or REI on both the server and client.

## Config Features

These settings are stored in the mod config and can be viewed or changed with `/fabs config`.

- `cropTrampleMode`: controls farmland trampling. Supports vanilla behavior, no trampling, or trampling immunity with Feather Falling.
- `eggTrampleMode`: the same idea for turtle eggs.
- `difficulty`: controls how the custom hostile mob potion-effect system scales. Can either behave the same on all difficulties or scale with world difficulty.
- `shareSeed`: shares the world seed with players on join and allows non-moderators to use `/fabs seed`.
- `overrideNormalLead`: overrides vanilla lead interaction and allows expanded leash targets.
- `canLeashAnimals`: allows or blocks leashing animals through the modded lead logic.
- `canLeashMonsters`: allows or blocks leashing hostile mobs.
- `canLeashBosses`: allows or blocks leashing boss mobs.
- `canLeashVillagerTypes`: allows or blocks leashing villagers and related types.
- `canLeashGolems`: allows or blocks leashing golems.
- `canLeashPets`: allows or blocks leashing tameable pets.
- `canPistonsPushBlockEntities`: enables the custom piston logic for moving block entities with their full data.
- `plantRainGrowthChance`: the percent chance that a plant will grow an extra step during a growth tick when it's raining.

## Gamerule Features

Most of the gameplay tweaks are controlled with gamerules.

### Mob Behavior and Drops

- `mob_grief_enderman`: toggles Endermen picking up blocks.
- `mob_grief_creeper`: toggles Creeper block griefing while still allowing explosion handling.
- `mob_grief_zombie`: toggles zombies breaking doors.
- `mob_drop_equipable`: toggles equipment, tools, and weapon drops from mobs.
- `mob_drop_full_durability`: restores dropped mob gear to full durability.
- `mob_drops_require_player_kill`: prevents mob drops unless the mob was killed by a player attack or player-caused explosion.
- `shulker_random_color`: randomizes natural shulker colors.
- `shulkers_can_teleport`: toggles shulker teleporting.
- `shulker_shell_drop_amount`: fixed shell drop amount for shulkers.
- `villager_always_turn_into_zombies`: zombies always convert villagers instead of only sometimes.
- `mobs_spawn_with_effects`: hostile mobs can spawn with dimension- and difficulty-based potion effects.
- `friendly_phantoms`: phantoms orbit but do not attack players.

### World and Block Behavior

- `no_cobble_gen`: alters lava and water interaction to prevent standard renewable cobblestone-style generation.
- `better_hoes`: enables area tilling and area harvesting with hoes.
- `dripstone_fill_cauldron_water`: toggles water dripstone filling cauldrons.
- `dripstone_fill_cauldron_lava`: toggles lava dripstone filling cauldrons.
- `rain_fills_cauldron`: toggles rain filling cauldrons.
- `snow_fills_cauldron`: toggles snow filling cauldrons.
- `better_rail_placement`: improves rail placement and shape retention.
- `snow_golems_survive_on_ice`: prevents snow golems from melting while standing on ice.
- `snow_golems_generate_snow`: toggles snow trail generation.
- `no_soliciting_sign`: prevents wandering trader spawns near signs reading `No Soliciting`.
- `silk_touchable_amethyst_nodes`: allows budding amethyst to be harvested.
- `silk_touchable_spawners`: allows spawners to drop with their block entity data.
- `silk_touchable_trial_spawners`: allows trial spawners to drop with their data.
- `silk_touchable_trial_vaults`: allows vaults to drop with their data.
- `expanded_bone_meal`: adds new bonemeal interactions.
- `sleeping_bags_enabled`: enables sleeping-bag behavior for specially named beds.
- `safe_cant_sleep`: prevents sleeping if the world currently cannot skip the night.
- `pet_friendly_fire`: when disabled, prevents players from hurting their own tameables.
- `fortune_ancient_debris`: changes ancient debris drops to support direct scrap-style fortune scaling.
- `torch_flowers_glow`: toggles torch flowers glowing.
- `cactus_grow_height`: sets the max height of cactus growth. (Setting to `3` disables the modded growth, and `1` disables growth completely)
- `sugar_cane_grow_height`: sets the max height of sugar cane growth. (Setting to `3` disables the modded growth, and `1` disables growth completely)
- `plants_grow_faster_in_rain`: gives plants a chance to grow an extra step for every growth tick when it's raining (see config for chance config).

### Projectile and Item Recovery

- `trident_with_loyalty_returns_from_void`: loyalty tridents can return instead of being lost to the void.

## Major Gameplay Tweaks

### Pistons

The piston system is one of the largest logic overrides in the mod.

- Block entities can be pushed and pulled while preserving their data.
- Inventories, furnace progress, custom NBT, and stored components are transferred to the destination block.
- Double chests are handled specially so they do not split incorrectly.
- Single chests can move as singles without auto-merging unexpectedly.
- Double chests only move if both halves move.
- Two parallel pistons moving both halves in the same tick and direction are supported.
- Piston behavior is further controlled through tags:
  - `fabsservertweaks:piston_push_blacklist`
  - `fabsservertweaks:piston_push_whitelist`
  - `fabsservertweaks:piston_move_override`

`piston_move_override` specifically allows blocks with a normal `DESTROY` push reaction to move instead of break.

### Leads and Mob Storage

- Leads can leash expanded categories of entities depending on config.
- `Ender`-enchanted leads can store a mob in the lead item and place it again later.
- This applies entity data storage rather than just converting the mob to an item placeholder.

### Spider Climbing

Spiders in Minecraft can usually climb on anything. This mod adds a tag to blacklist certain blocks.

- `fabsservertweaks:spider_not_climbable` can be used to blacklist spiders being able to climb on blocks.
- By default, the tag blocks spiders from climbing on:
  - All `ice` blocks, this is applied through the `#minecraft:ice` tag, so it includes modded ice blocks if the tag is used by the mod author.
  - All `glazed terracotta` blocks

### Beds and Sleeping Bags

- Beds named `Sleeping Bag` act as sleeping bags instead of spawn-setting beds.
- Sleeping in one does not change respawn position.
- When used, the bed is broken and dropped back as the named item.

### Seed Sharing and Debugging

- The server seed can optionally be shared to joining players.
- A slime chunk debug entry exists on the client when the seed is known.

### Item and Utility Tweaks

- Lodestone compasses can teleport the player to the lodestone by consuming an ender pearl.
- Using a glass bottle on an enchanting table stores one level as a reusable XP bottle.
- Right-clicking that XP bottle gives the stored experience back.
- Snowballs deal proper damage to water-sensitive mobs.
- Aqua Affinity underwater mining is improved.
- Tagged items can become cactus-immune.
- Tagged items can also have stack sizes overridden to `16` or `64`.
- Creative players can instantly kill mobs with non-weapon attacks.
- Nicknames can override the displayed chat sender name.

## Enchantments

The mod includes built-in enchantment packs. Some are handled entirely through data, while others also have Java-side behavior.

### Custom Enchantments Pack

Built-in pack: `CUSTOM_ENCHANTMENTS`

This pack is enabled by default and requires restart.

- `Tree Chopper`:
  Breaks an entire tree, giant mushroom, or supported fungus-style structure using whitelist and attachment tags.
- `Capturing I-III`:
  Melee weapons gain a chance to make mobs drop their spawn eggs.
- `Ender I`:
  Applied to leads. Lets a lead capture and redeploy a mob.
- `Hammer I-III`:
  Applied to pickaxes. Enables area mining.
- `Longsword I-III`:
  Increases entity interaction range.
- `Long Legs I`:
  Increases step height.
- `Reach I-III`:
  Increases block reach. Works on chest armor and Elytra.
- `Springy I-III`:
  Increases jump strength.
- `Swiftness I-III`:
  Increases movement speed.

### Ore Miner Pack

Built-in pack: `ORE_MINER`

This pack is disabled by default and requires restart.

- `Ore Miner I`:
  Vein mines connected tagged ores and clusters.

### Vanilla Enchantment Overrides Pack

Built-in pack: `VANILLA_ENCHANTMENTS_OVERRIDES`

This pack is disabled by default and requires restart.

It overrides several vanilla enchantments:

- `Efficiency` up to level `X`
- `Fortune` up to level `V`
- `Looting` up to level `V`
- `Sharpness` up to level `X`

These are not just higher caps. Their data definitions also adjust scaling behavior.

## Recipe and Datapack Features

The mod ships a number of built-in datapacks that can be toggled with `/fabs feature <pack>`.

### `CUSTOM_RECIPES`

Enabled by default. Does not require restart.

Adds a small collection of utility recipes, including:

- coral block crafting
- additional dye conversions
- red sand recoloring
- clay and calcite recipes
- a larger `mass calcite` recipe
- rotten flesh to leather

### `CAULDRON_CONVERSIONS`

Enabled by default. Does not require restart.

Adds item-to-item conversion when items sit inside a full layered cauldron.

Current conversions include:

- all 16 concrete powder colors into concrete
- dirt into mud
- campfire to unlit campfire (works with soul campfire as well)

### `DURABILITY_SMELTING`

Enabled by default. Does not require restart.

Adds data-driven smelting behavior for used tools and armor. The result can vary by durability tier and can also adjust cook time and XP. 

**NOTE**: Because of how furnaces work, it's not enough to just add the durability-smelting recipe. The recipe must be added as a vanilla smelting recipe as well.

Current data includes entries for:

- chainmail armor
- iron and gold gear
- copper gear
- custom horse armor, nautilus armor, spears, and related items

### `MOUNT_ARMOR_RECIPES`

Enabled by default. Does not require restart.

Adds crafting recipes for:

- copper, iron, gold, and diamond horse armor
- copper, iron, gold, and diamond nautilus armor

### `RAW_BLOCK_SMELTING`

Enabled by default. Does not require restart.

Adds furnace and blasting support for raw ore blocks.

### `STONE_STAIRS_RECIPES`

Enabled by default. Does not require restart.

Adds a large set of stair recipes for stone-like and copper-like materials, including normal and smaller recipe variants.

- The `normal` stair recipes are now mathematically correct.
- The `small` stair recipes are the amount vanilla normally gives.

### `WOOD_OVERRIDE_RECIPES`

Enabled by default. Does not require restart.

Adds alternate wood recipes, especially for stairs (including normal and small recipe variants) and trapdoors (with increased output).

- The `normal` stair recipes are now mathematically correct.
- The `small` stair recipes are the amount vanilla normally gives.

### `COMBINE_SLABS_RECIPES`

Enabled by default. Does not require restart.

Adds recipes to recombine slab pairs back into their full blocks.

### `POTTERY_SHERD_DUPLICATION`

Enabled by default. Does not require restart.

Adds duplication recipes for pottery sherds.

- The original pottery sherd is required for duplication.
- The recipe uses the original sherd with two clay and returns two sherds.

## Commands

### `/fabs`

Primary admin and utility command.

- `/fabs config <option>`:
  reads a config value.
- `/fabs config <option> <value>`:
  writes a config value.
- `/fabs seed`:
  shows the world seed and makes it easy to copy.
- `/fabs enchant <enchantment> [level]`:
  directly enchants the held item.
- `/fabs enchant <enchantment> remove`:
  removes an enchantment from the held item.
- `/fabs feature <pack>`:
  toggles one of the built-in datapacks or resourcepacks.

### `/preset <name>`

Applies a predefined ruleset.

Current presets:

- `default`
- `vanilla`
- `fabs_choice`
- `no_renewable`
- `creative_defaults`

### `/nickname`

- `/nickname <name>`
- `/nickname <name> <color>`
- `/nickname clear`

Stores persistent nicknames and rebroadcasts chat with the configured display name.

### `/goto <dimension>`

Moderator command for safe cross-dimension teleportation.

## Data-Driven Tags

A lot of the mod is powered by tags.

Examples include:

- `fabsservertweaks:ore_miner_whitelist`
- `fabsservertweaks:tree_chopper_whitelist`
- `fabsservertweaks:tree_chopper_attachments`
- `fabsservertweaks:scythe-able`
- `fabsservertweaks:immune_to_cactus`
- `fabsservertweaks:stack_16`
- `fabsservertweaks:stack_64`
- `fabsservertweaks:spider_not_climbable`
- `fabsservertweaks:piston_push_blacklist`
- `fabsservertweaks:piston_push_whitelist`
- `fabsservertweaks:piston_move_override`

Entity tags are also used for leash permissions and mob-effect blacklists.

## Plugins

### Just Enough Items (JEI)
The mod includes a JEI plugin that adds support for the mod's custom recipes.

### Roughly Enough Items (REI)
The mod includes a REI plugin that adds support for the mod's custom recipes.

### Mod Menu (Fabric Only)
Mod Menu is a client-side mod that lists mods installed on the client and allows for opening configuration screens in game.

### Polymer (Fabric Only)

Allows the mod to add server-side content like custom blocks and items. It also allows the mod to display textures for things that would normally be client-sided.

Current features:
- Chunk Loader (force loads a single chunk).
- Textures and item models for the unlit campfire items.
- Lootr Compatibility
  - Allows the Lootr mod to work server-side only by registering the content through Polymer and sending the resources to the client.
  - Note: Requires Lootr to be installed on the server.
  - Note 2: Display of Lootr's custom decorated pots doesn't work at the moment.
- Universal Ores Compatibility
  - Allows the Universal Ores mod to work server-side only by registering the content through Polymer and sending the resources to the client.
  - Note: Requires Universal Ores to be installed on the server.

### Universal Ores (Fabric Only)

Allows Universal Ores to function server-side by registering the content through Polymer and sending the resources to the client.

### Lootr (Fabric Only)

Allows Lootr to function server-side by registering the content through Polymer and sending the resources to the client.

#### What works:

- Lootr's native functionality of allowing separate loot for each player and preventing containers from being broken unless crouching.
- Statistics
- Commands*
- Block models**
- Block textures**
- Item models**
- Item textures**
- Displaying "opened" texture for each player on containers that have been looted***.


- *Commands do work, but some arguments may be displayed as "invalid" on vanilla clients, ignore the red text and run the command anyway.
- **The decorated pots do not work at the moment.
- ***Works by displaying a Display Entity on top of the block as there are no good way to change the texture of the block from the server side.

#### What does not work:

- Advancements
- Decorated pot models and textures (The function of the block still works. It can still be looted by each player separately. It just doesn't display anything.)


## Preset Philosophy

The included presets make it easier to swing between:

- a mostly vanilla setup, this essentially disables most of the mod's features.
- a default tweak-heavy setup, this is the default setup for the mod.
- a personal `Fab's choice` setup as I like to play the game.
- a restricted renewable-resource setup, for those who want to play with a limited resource pool.
  - Disables cobblestone generation.
  - Prevents cauldrons from filling.
  - Disables snow trail generation.
- a creative-building setup, for a simple creative mode setup.
  - Disables `advance_time` gamerule.
  - Disables `advance_weather` gamerule.
  - Disables `spawn_mobs` gamerule.
  - Disables `spawn_wandering_trader` gamerule.
  - Disables `fire_damage` gamerule.
  - Enables `keep_inventory` gamerule.

This is one of the mod's main strengths: it is not meant to force one single ruleset.

## Limitations and In-Progress Areas

Some features are more mature than others.

- The piston block-entity system is significant and functional, but vanilla clients may still show incorrect visuals in some custom piston cases.
  - Blocks that normally don't move; blink away and re-appear where they are moved.
  - Block-breaking particles still show up when moving blocks that normally break.
  - This is due to vanilla running its own logic for piston movement for the visual side. The server has authority of the movement.
- Torch flower glow is simulated with invisible LIGHT blocks placed in the six adjacent spaces. 
Because vanilla torch flowers cannot be given dynamic light emission directly, this implementation uses block placement and neighbor updates instead of replacing the flower itself. 
This keeps the feature lightweight and compatible with normal gameplay interactions, but it is not retroactive: 
toggling the gamerule does not immediately add or remove "glow" from already placed torch flowers. 
Existing flowers must be updated, typically by breaking and replacing them, for the change to take effect.
  - The reason for this is that scanning the entire world for torch flowers adds complexity and is expensive on system resources for very little gain.
- The block outline rendering code exists, but it's not currently implemented.
  - This will be implemented in the future.
- Some features depend on custom item data or compatibility with other content packs and should be validated in the target modpack.
- If a built-in datapack is marked as restart-required, treat that as authoritative even if the command can toggle it immediately.
  - Meaning that enabling the datapack will succeed and the command will prompt the player to restart the server.
  - This is a limitation in vanilla not rebuilding the registries related on reload.

## Recommended Documentation Note

If you are using the mod in your modpack or on your server, this is a good short summary:

`This mod works server-side only. Vanilla clients can connect and play normally. For the best visual experience, installing it on the client is recommended, as some piston-related visuals may be inaccurate on vanilla clients.`


## For modpack authors

Using this mod in your modpack is encouraged. If you do, please follow these requirements:
- The modpack must be distributed on CurseForge or Modrinth.
- I don't allow distributing the mod from a 3rd party source.
