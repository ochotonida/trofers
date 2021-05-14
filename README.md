# Trofers

Trofers is a minecraft mod that adds customizable trophies, mostly intended for use by modpack makers. The trophies can be customized either by
spawning them with custom nbt, or by interacting with them after placing them down in creative mode.

![image](https://user-images.githubusercontent.com/37985539/118311814-af3b9380-b4f0-11eb-8e90-714ea53f2dcb.png)

## Customizing Trophies

Trofers currently adds 3 trophy bases of different sizes, which can be customized by setting the following tags in the item's `BlockEntityTag`:

* `Name`: The name of the trophy while in the player's inventory. Can be set in creative mode by right-clicking the block with a name tag.

* `Colors`: A compound tag used to change the colors of the trophy's model parts. This tag consists of the compound tags `Bottom`, `Middle` and `Top`.
  Each one of these tags has subtags `Red`, `Green` and `Blue`, which are integers between 0 and 255. Colors can be customized in creative mode by
  right-clicking the trophy with dyes.

* `Item`: The item to be displayed on the trophy. Can be any item stack, including count and NBT. See the
  [Minecraft wiki](https://minecraft.fandom.com/wiki/Tutorials/Command_NBT_tags#Items) for more info. Right-clicking a trophy with an item in creative
  mode will also set the item. Right-clicking with an empty hand will remove it.

* `Animation`: Controls the animation of the item. There are currently 3 animations:
  `"Static"`, `"Spinning"` and `"Tumbling"`. While in creative mode, right-clicking a trophy with a stick will cycle through the animations.

* `AnimationSpeed`: Controls the speed of the animation (Default: 1). Animation speed can currently only be changed by editing NBT.

* `DisplayScale`: The size of the displayed item (Default: 1 for large trophies, 0.75 for normal trophies and 0.5 for small trophies). Display scale
  can currently only be changed by editing NBT.

* `DisplayHeight`: Controls the height (in 1/16th's of a block) of the displayed item, starting from the top of the trophy (a small offset gets added
  when using the spinning/tumbling animations). Display height can currently only be changed by editing NBT.
