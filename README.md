# Trofers [![CurseForge](http://cf.way2muchnoise.eu/full_482265_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/trofers)

Trofers is a minecraft mod that adds customizable trophies. Trophies can be added using data packs.
The mod's data pack includes a trophy for every non-boss mob, entities have a small chance to drop these when killed by a player.
(This can be disabled in the config)

![image](https://user-images.githubusercontent.com/37985539/134157534-8d5308a9-b96d-4f2f-afe1-9e44bb15bd29.png)
## Customizing Trophies

Trophy JSONs are placed in the `data/<namespace>/trofers` folder. The following fields can be customized 
(all fields are optional unless stated otherwise):

* `name`: The name of the trophy as a text component (can be a string). 
  Information on how to format these can be found [here](https://minecraft.fandom.com/wiki/Raw_JSON_text_format).
* `item`: An object describing the item the trophy should display. Contains the following fields:
  * `item`: (_required_) The item ID
  * `count`: The size of the item stack
  * `nbt`: The NBT of the item stack, either as a JSON object or stringified NBT
* `entity`: An object describing the entity the trophy should display. Contains the following fields:
  * `type`: (_required_) The entity ID
  * `nbt`: The NBT of the entity, either as a JSON object or stringified NBT
  * `animated` (_default = true_) Whether the entity's idle animation should be played. 
    Note that some animations may not work because the entity is not being ticked.
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
* `hidden`: (_default = false_) Whether the trophy should be hidden from trophy selection screen in creative mode

## Adding a trophy to a trophy base
Trofers currently adds 6 trophy bases. 
Placing one down and right-clicking it while in creative will open a menu that allows you to pick any existing trophy.
You can also set the trophy by changing the item's NBT: `{BlockEntityTag:{Trophy:"namespace:path"}}`.
Changes made to your data pack will apply to any existing trophies.
