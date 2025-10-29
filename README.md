# Trofers [![CurseForge](https://img.shields.io/curseforge/dt/482265?label=CurseForge&color=F16436&style=flat&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/trofers) [![Modrinth](https://img.shields.io/modrinth/dt/trofers?color=00AF5C&label=Modrinth&style=flat)](https://modrinth.com/mod/trofers) [![](https://discordapp.com/api/guilds/298798089068609537/widget.png?style=shield)](https://discord.gg/87pXJadaRr)

⚠️ *This README explains the data pack format for Trofers.
The data pack format and this README can be subject to change between Minecraft updates.
If you're using an older Minecraft version, 
make sure you're reading this README on the branch for that version.*

*The 1.20.1 version of this README can be found [here](https://github.com/ochotonida/trofers/blob/1.20.1/README.md).*

---

Trofers is a Minecraft mod that adds customizable trophies, which can be added using data packs.
The data pack that comes with the mod includes a trophy for every vanilla mob, and supports some modded mobs as well. 
Entities have a small chance to drop these when killed by a player.
The drop rate for the trophies that come with the mod can be changed in the config.

![image](https://user-images.githubusercontent.com/37985539/134405190-2076a728-fb77-4232-9936-42a4a8307bdd.png)
## Customizing Trophies

Trophy JSONs are placed in the `data/<namespace>/trofers/trophies` folder. The following fields can be customized 
(all fields are optional unless stated otherwise):

* `name`: The name of the trophy as a text component (can be a string). 
  Information on how to format these can be found [here](https://minecraft.wiki/w/Raw_JSON_text_format).
* `tooltip`: Either a single text component, or a list of text components, one for each tooltip line
* `item`: An object describing the item the trophy should display. Contains the following fields:
  * `id`: (_required_) The item ID
  * `count`: The size of the item stack
  * `components`: The components item stack
* `entity`: An object describing the entity the trophy should display. Contains the following fields:
  * `id`: (_required_) The entity ID
  * `tag`: The NBT tag of the entity
* `display`: An object containing information about how to display the item or entity
  * `offset`: (_default = [0,0,0]_) Offset from the center of the trophy, as a list of 3 numbers. Distance is measured in 1/16-ths of a block
  * `rotation`: (_default = [0,0,0]_ ) The rotation of the item or entity, as a list of 3 numbers. Each element is the rotation in degrees around that axis.
  * `scale`: (_default = 1_) The size of the item/entity
* `animation`: An object describing the animation of the item/entity
  * `type`: (_default = "fixed"_) The animation type, either "fixed", "spinning" or "tumbling"
  * `speed`: (_default = 1_) Affects the speed of the animation
* `colors`: An object describing the colors of the trophy base
  * `base`: (_default = "#FFFFFF"_) A color, either in hexadecimal as a string (`"#RRGGBB"`), 
    or as a list of 3 integers between 0 and 255
  * `accent` A color, formatted the same way as `base`
* `effects`: An object describing effects that should apply when the trophy is right-clicked
  * `sound`: A sound to play when the trophy is right-clicked
    * `id`: A sound event identifier
    * `volume`: (_default = 1_) the volume to play the sound event at
    * `pitch`: (_default = 1_) the pitch to play the sound event at
  * `rewards`: Rewards given to the player when the trophy is right-clicked
    * `loot_table`: A loot table ID to generate loot from
    * `mob_effect`: A potion effect to apply to the player
      * `id`: (_required_) The effect ID
      * `duration`: (_required_) The amount of time in ticks the effect should last
      * `amplifier`: (_default = 0_) The amplifier of the effect (effect level - 1)
      * `show_particles`: (_default = true_) Whether the effect should spawn particles around the player
      * `show_icon`: (_default = true_) Whether the effect's icon should be shown in the HUD
    * `cooldown`: (_default = 0_) The amount of time it takes in ticks before the reward(s) can be claimed again
* `is_hidden`: (_default = false_) Whether the trophy should be hidden from trophy selection screen in creative mode

For example trophies see the [default data pack](https://github.com/ochotonida/trofers/tree/HEAD/common/src/generated/resources/data/trofers/trofers).

## Adding a trophy to a trophy base
Trofers currently adds 6 trophy bases. 
Placing one down and right-clicking it while in creative will open a menu that allows you to pick any existing trophy.
You can also set the trophy by setting the item's trophy component: `/give @a trofers:small_pillar[trofers:trophy="namespace:trophy_name"]`.
Changes made to your data pack will also apply to any existing trophies.

## Making entities drop trophies
Because overriding loot tables can be annoying, 
Trofers adds a data pack resource type which can be used
to make entities drop trophies.
These are placed under `data/<namespace>/trofers/entity_drops`.

The file should have the following structure:

* `conditions`: A list of loot conditions that determine when to apply the loot modifier. 
  Trofers uses a `minecraft:killed_by_player` and a `trofers:random_trophy_chance` condition here. 
  The latter is a loot condition added by Trofers that succeeds if 
  a randomly generated number is smaller than the trophy chance value set in the config.
* `trophy_base`: The ID of an item to use as the trophy base. The available options are:
  * `trofers:<size>_plate`
  * `trofers:<size>_pillar`

  where `<size>` can be either `small`, `medium` or `large`.
   
* `trophies`: An object with multiple key-value pairs. 
  Each key should correspond with an entity type id, and its value the id of the trophy it should drop. (Note: for entities that can drop multiple trophies you will need multiple files)

If all trophies have the same drop conditions and trophy base, you only need a single file.

Example:
```json5
{
  "conditions": [
    {
      "condition": "minecraft:killed_by_player"
    },
    {
      "condition": "trofers:random_trophy_chance"
    }
  ],
  "trophy_base": "trofers:small_pillar",
  "trophies": {
    "minecraft:axolotl": "my_namespace:custom_axolotl_trophy",
    "minecraft:creeper": "trofers:creeper",
    "quark:crab": "trofers:quark/crab",
  }
}
```

## Making trophies drop from advancements

It's also possible to make trophies drop from advancements.
The files for these should be placed under `data/<namespace>/trofers/advancement_drops`,
and use the following format:

* `conditions`: Same as with entity drops.
  Trofers uses the `trofers:advancement_drops_enabled` loot condition here,
  which checks whether advancement drops are enabled in the config.
* `trophy_base`: Same as with entity drops.
* `trophies`: An object with multiple key-value pairs. Each key should correspond with
  an advancement id, and its value with the id of the trophy it should drop.

Example:

```json5
{
  "conditions": [
    "trofers:advancement_drops_enabled"
  ],
  "trophy_base": "trofers:large_plate",
  "trophies": {
    "minecraft:nether/uneasy_alliance" : "my_namespace:some_custom_trophy",
    "minecraft:story/cure_zombie_villager" : "trofers:villager"
  }
}
```
