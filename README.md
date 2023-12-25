# Minecart View Fix
This mod fixes [MC-201](https://bugs.mojang.com/browse/MC-201), a near decade old bug.\
Also adds a command to disable it per-player.

## Semi-Archived
This project is on hold while I find another way to fix the bug.
Here are a number of issues I've encountered in this project:

* After crossing diagonal tracks or turns the carts rotates incredibly weirdly, most of the time snapping to the nearest 45-degree angle, usually in the opposite signage.
  * 180 > -45 > -90 are the rough angles when crossing a T intersection.
  * Using `target.setYaw(this.getYaw());`, while smooth and quite nice feeling, it tends to be buggy/too snappy due to this issue.
* `-0` is a valid rotation value, for some reason? I'm guessing its due to some kind of floating point error but im not entirely sure what's going on there.
  * Checking for specific values is nearly impossible due to this
  * The `getDirectionVector()` method doesn't always return the correct value, outputting `1.22467991473532E-16` or similar when stopped on a rail, which makes absolutely zero sense.

Due to this, I'm not sure how to continue this project.\
My original plan was to use the rotation, speed, and `Vec3d` of the minecart to set the target's yaw value.\
Considering 2 out of 3 of those are broken/not always functional, im not sure how to approach this bug anymore.\
If someone has an idea of how to fix this issue, feel free to fork this and make a PR.\
In the meantime, I'll be trying to find another way to do this. No ETA for this, and possibly never.
