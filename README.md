# Trofers [![CurseForge](http://cf.way2muchnoise.eu/full_482265_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/trofers) [![](https://discordapp.com/api/guilds/298798089068609537/widget.png?style=shield)](https://discord.gg/87pXJadaRr)

For information about Trofers 1.16.5-1.1.0 and below, click [here](https://github.com/ochotonida/trofers/blob/1.16-legacy/README.md).

---

Trofers is a minecraft mod that adds customizable trophies, which can be added using data packs.
The data pack that comes with the mod includes a trophy for every non-boss mob, entities have a small chance to drop these when killed by a player.
(The drop rate can be changed in the config)

![image](https://user-images.githubusercontent.com/37985539/134405190-2076a728-fb77-4232-9936-42a4a8307bdd.png)
## Customizing Trophies

Trophy JSONs are placed in the `data/<namespace>/trofers` folder. The following fields can be customized 
(all fields are optional unless stated otherwise):

* `name`: The name of the trophy as a text component (can be a string). 
  Information on how to format these can be found [here](https://minecraft.fandom.com/wiki/Raw_JSON_text_format).
* `tooltip`: A list of text components, one component per line
* `item`: An object describing the item the trophy should display. Contains the following fields:
  * `item`: (_required_) The item ID
  * `count`: The size of the item stack
  * `nbt`: The NBT of the item stack, either as a JSON object or stringified NBT
* `entity`: An object describing the entity the trophy should display. Contains the following fields:
  * `type`: (_required_) The entity ID
  * `nbt`: The NBT of the entity, either as a JSON object or stringified NBT
* `display`: An object containing information about how to display the item/entity
  * `offset`: An object describing the position of the item/entity
    * `x`/`y`/`z`: Offset in 1/16-ths of a block
  * `rotation`: An object describing the rotation of the item/entity
    * `x`/`y`/`z`: Rotation in degrees
  * `scale`: (_default = 1_) The size of the item/entity
* `animation`: An object describing the animation of the item/entity
  * `type`: (_default = "fixed"_) The animation type, either "fixed", "spinning" or "tumbling"
  * `speed`: Affects the speed of the animation
* `colors`: An object describing the colors of the trophy base
  * `base`/`accent`: A color, either in hexadecimal as a string (`"#RRGGBB"`), 
    or as an object with `red`/`green`/`blue` fields between 0 and 255.
* `effects`: An object describing effects that should apply when the trophy is right-clicked
  * `sound`: A sound to play when the trophy is right-clicked
    * `soundEvent`: A sound event ID
    * `volume`: (_default = 1_) the volume to play the sound event at
    * `pitch`: (_default = 1_) the pitch to play the sound event at
  * `rewards`: Rewards given to the player when the trophy is right-clicked
    * `lootTable`: A loot table ID to generate loot from
    * `statusEffect`: A potion effect to apply to the player
      * `effect`: (_required_) The effect ID
      * `duration`: (_required_) The amount of time in ticks the effect should last
      * `amplifier`: (_default = 0_) The amplifier of the effect (effect level - 1)
    * `cooldown`: The amount of time it takes in ticks before the reward(s) can be claimed again
* `hidden`: (_default = false_) Whether the trophy should be hidden from trophy selection screen in creative mode

For example trophies see the [default data pack](https://github.com/ochotonida/trofers/tree/HEAD/src/generated/resources/data/trofers/trofers).

## Adding a trophy to a trophy base
Trofers currently adds 6 trophy bases. 
Placing one down and right-clicking it while in creative will open a menu that allows you to pick any existing trophy.
You can also set the trophy by changing the item's NBT: `{BlockEntityTag:{Trophy:"namespace:path"}}`.
Changes made to your data pack will apply to any existing trophies.

## Making entities drop trophies
Because overriding loot tables can be annoying, Trofers adds a loot modifier which can be used to make entities drop trophies.

The loot modifier should be placed in the `data/<namespace>/loot_modifiers` folder and uses the following structure:

* `"type": "trofers:add_entity_trophy"`: required, this tells forge which loot modifier type to use
* `conditions`: A list of loot conditions that determine when to apply the loot modifier. Trofers uses a `minecraft:killed_by_player` and a `trofers:random_trophy_chance` condition here. The `random_trophy_chance` condition ensures the loot modifier is applied with the trophy chance value specified in the config.
* `trophyBase`: The ID of an item to use as a trophy base (e.g. "trofers:small_plate")
* `trophies`: A list of objects. When loot is generated for an entity and the entity matches one of the entries in this list, the corresponding trophy will be added to the generated loot. (Note: duplicate entity types are not allowed, for entities that can drop multiple trophies you will need multiple loot modifier files)
  * `trophy`: A trophy ID
  * `entity`: An entity type ID

If all trophies have the same drop conditions, you only need a single file.

[Example loot modifiers](https://github.com/ochotonida/trofers/tree/HEAD/src/generated/resources/data/trofers/loot_modifiers)

After creating your loot modifier you need to register it to forge, more information on how to register a loot modifier (and loot modifiers in general) found [here](https://forge.gemwire.uk/wiki/Dynamic_Loot_Modification). (The wiki page is mostly aimed at mod developers, you can ignore the last section)

[Example registration](https://github.com/ochotonida/trofers/blob/HEAD/src/generated/resources/data/forge/loot_modifiers/global_loot_modifiers.json)
