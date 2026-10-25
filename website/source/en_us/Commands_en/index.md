## commandNoteblockChunkloader | commandPistonChunkloader | commandBellChunkloader

Note blocks, pistons and bells share `/blockChunkloader`. Enable the corresponding rule, set conditions, duration and range, then use `enabled true` to enable the loader.

| `<type>` | Block | Corresponding rule |
| --- | --- | --- |
| `noteblock` | Note block | `commandNoteblockChunkloader` |
| `piston` | Piston | `commandPistonChunkloader` |
| `bell` | Bell | `commandBellChunkloader` |

### Command syntax

Replace `<arguments>` with actual values. `|` separates alternatives, do not type these symbols. `/blockChunkloader help` shows help, and `/blockChunkloader list` shows loaders you can configure.

Prefix each command below with `/blockChunkloader <type>`.

| Subcommand | Usage |
| --- | --- |
| `show` | Show settings and conditions |
| `enabled <true\|false>` | Enable or disable the loader |
| `time <time>` | Set loading duration |
| `range <range>` | Set loading range |
| `test <x> <y> <z>` | Check conditions at the specified position |
| `reset` | Restore defaults and disable the loader |
| `group create <name>` | Create a condition group |
| `group list` | List all condition groups |
| `group show <name>` | Show a group and its conditions |
| `group mode <name> <all\|any>` | Require all conditions or at least one to match |
| `group enabled <name> <true\|false>` | Enable or disable a group while keeping its conditions |
| `group remove <name>` | Delete a group and its conditions |
| `group clear` | Delete all groups and create an empty `default` group |
| `condition add <group> direction <direction> <distance> <is\|not> <block>` | Add a condition using direction and distance |
| `condition add <group> offset <dx> <dy> <dz> <is\|not> <block>` | Add a condition using coordinate offsets |
| `condition list <group>` | Show conditions and current IDs |
| `condition remove <group> <id>` | Delete a condition by ID |
| `condition clear <group>` | Remove all conditions in a group |

### Arguments

| Argument | Meaning |
| --- | --- |
| `<time>` | Duration in game ticks, 1–300, default `300` |
| `<range>` | Loading area in chunks: `1x1`, `3x3`, `5x5`, `7x7` or `9x9`, default `3x3` |
| `<name>` | Name of the group to create or manage, such as `default` or `my_group` |
| `<group>` | Name of the existing group containing the conditions, use `default` for the default group |
| `all`, `any` | All conditions must match, or at least one must match, default `all` requires all conditions to match |
| `<direction>` | `up`, `down`, `north`, `south`, `east`, `west`, `northeast`, `northwest`, `southeast` or `southwest` |
| `<distance>` | 1–32 blocks, diagonal distance applies to each axis, for example `northeast 2` means 2 diagonal steps northeast |
| `<dx>`, `<dy>`, `<dz>` | World coordinate offsets from the loader, each from -32 to 32, positive X is east, Y is up and Z is south |
| `is`, `not` | Require the specified position to match or not match the block condition |
| `<block>` | Block ID, such as `minecraft:bone_block` |
| `<id>` | Condition ID, IDs are automatically renumbered after condition edits, so always use `condition list` to check and confirm before deleting |
| `<x>`, `<y>`, `<z>` | Loader position to test, such as `~ ~ ~` |

Group names may contain letters, digits, underscores `_` and hyphens `-`, and must be 1–32 characters long.

All enabled, nonempty groups must match. Empty and disabled groups are ignored, no active condition groups means unrestricted triggering. Conditions are relative to the note block, piston base or bell. Loading is centered on the note block or bell chunk, or the piston head chunk.

### Example

When a bone block is above the note block, a gold block is one diagonal step northeast at the same height, a diamond block is one block northeast and above, and stone is one block northwest and above, triggering loads a `5x5` area for `200` game ticks:

Run each command below, lines starting with `#` are comments:

```text
# Enable the note block loader rule, require permission level 2 to configure it, and save the rule setting
/carpet setDefault commandNoteblockChunkloader ops

# Restore default note block loader settings, the loader is disabled
/blockChunkloader noteblock reset

# Set loading duration to 200 game ticks
/blockChunkloader noteblock time 200

# Set loading range to 5x5 chunks
/blockChunkloader noteblock range 5x5

# Add a condition to default: a bone block one block above the note block
/blockChunkloader noteblock condition add default direction up 1 is minecraft:bone_block

# Add a condition to default: a gold block one diagonal step northeast at the same height
/blockChunkloader noteblock condition add default direction northeast 1 is minecraft:gold_block

# Add a condition to default: a diamond block one block northeast and above
/blockChunkloader noteblock condition add default offset 1 1 -1 is minecraft:diamond_block

# Add a condition to default: stone one block northwest and above, all conditions must match by default
/blockChunkloader noteblock condition add default offset -1 1 -1 is minecraft:stone

# Enable the note block loader, triggering the note block loads chunks when the conditions match
/blockChunkloader noteblock enabled true
```

## commandPlayerChunkLoadController

| Command | Function |
| --- | --- |
| `/playerChunkLoading [true/false]` | Control chunk loading of player; can only manipulate yourself |

## commandAnvilInteractionDisabled

| Command | Function |
| --- | --- |
| `/anvilInteractionDisabled [true/false]` | Controls whether the player can interact with the anvil |

## commandCustomBlockBlastResistance

| Command | Function |
| --- | --- |
| `/customBlockBlastResistance set <block> <resistance>` | Add or modify blocks and their blast resistance |
| `/customBlockBlastResistance remove  <block>` | Remove a block from the list |
| `/customBlockBlastResistance removeAll` | Remove all blocks from the list |
| `/customBlockBlastResistance list` | Display all added blocks |
| `/customBlockBlastResistance help` | View the usage instructions |

## commandHere

| Command | Function |
| --- | --- |
| `/here` | send the current dimension, coordinates, and corresponding overworld/nether coordinates of your current location and give oneself a 30s glowing effect |

## commandWhere

| Command | Function |
| --- | --- |
| `/where` | get the specified player's dimension, coordinates, and corresponding Overworld/Nether coordinates and give the target player a 30s glowing effect |

## commandPlayerLeader

| Command | Function |
| --- | --- |
| `/leader add <player>` | Add a tour guide |
| `/leader remove <player>` | remove a tour guide |
| `/leader removeAll` | Remove all tour guide |
| `/leader broadcastLeaderPos <player> interval <tick>` | Broadcast the coordinates of a tour guide at regular intervals, The time is set to a negative number to stop broadcasting |
| `/leader list` | View tour guide list |
| `/leader help` | View the usage instructions |

## commandPacketInternetGroper

| Command | Function |
| --- | --- |
| `/pings <targetIpOrDomainName> <pingQuantity>` | Ping a specific host |
| `/pings stop` | Interrupt the ping operation |

## commandGetSaveSize

| Command | Function |
| --- | --- |
| `/getSaveSize` | Get save size |

## commandGetSystemInfo

| Command | Function |
| --- | --- |
| `/getSystemInfo` | Get the system information of the server |

## commandGoto

| Command | Function |
| --- | --- |
| `/goto <dimension> <x> <y> <z>` | teleport to the specified dimension and coordinates |
| `/goto <dimension>` | Teleport to the corresponding coordinates of the current player location |

## commandCustomCommandPermissionLevel

| Command | Function |
| --- | --- |
| `/customCommandPermissionLevel set <command> <permissionLevel>` | Set the permission level of a command |
| `/customCommandPermissionLevel remove <command>` | Remove a modified command |
| `/customCommandPermissionLevel removeAll` | Remove all modified commands |
| `/customCommandPermissionLevel refresh` | Refresh command tree |
| `/customCommandPermissionLevel list` | View the list of modified commands |
| `/customCommandPermissionLevel help` | View the usage instructions |

## commandGetPlayerSkull

| Command | Function |
| --- | --- |
| `/getPlayerSkull <player> <count>` | Get the skull of a specified player |

## commandCustomMovableBlock

| Command | Function |
| --- | --- |
| `/customMovableBlock add <block>` | Added a movable block |
| `/customMovableBlock remove <block>` | Delete a movable block |
| `/customMovableBlock removeAll` | Removes all added movable blocks |
| `/customMovableBlock list` | Check out the list of movable blocks |
| `/customMovableBlock help` | View the usage instructions |

## commandGetHeldItemID

| Command | Function |
| --- | --- |
| `/getHeldItemID` | Get item ID of the player main hand item |

## commandCustomAntiFireItems

| Command | Function |
| --- | --- |
| `/customAntiFireItems add <item>` | Added a anti fire item |
| `/customAntiFireItems  remove <block>` | Delete a anti fire item |
| `/customAntiFireItems removeAll` | Removes all added anti fire items |
| `/customAntiFireItems  list` | Check out the list of anti fire item |
| `/customAntiFireItems help` | View the usage instructions |

## commandCarpetExtensionModWikiHyperlink

| Command | Function |
| --- | --- |
| `/carpetExtensionModWikiHyperlink <ExtensionName>` | Get the Wiki link for a carpet extension mod |

## commandCustomBlockHardness

| Command | Function |
| --- | --- |
| `/customBlockHardness set <block> <hardness>` | Add or modify blocks and their hardness |
| `/customBlockHardness remove <block>` | Remove a block from the list |
| `/customBlockHardness removeAll` | Remove all blocks from the list |
| `/customBlockHardness getDefaultHardness` | Query the default hardness of blocks |
| `/customBlockHardness list` | Display all added blocks |
| `/customBlockHardness help` | View the usage instructions |

## commandAtSomeOnePlayer

| Command | Function |
| --- | --- |
| `/@ <player>` | @ some one player |

## commandGetClientPlayerFps

| Command | Function |
| --- | --- |
| `/getClientPlayerFps <player>` | Get the FPS of the specified player |
| `/getClientPlayerFps help` | View the usage instructions |

## commandSetPlayerPose

| Command | Function |
| --- | --- |
| `/playerPose <player> set <pose>` | Set player pose |
| `/playerPose <player> stop` | Stop posing |
| `/playerPose help` | View the usage instructions |

## commandPlayerNoNetherPortalTeleport

| Command | Function |
| --- | --- |
| `/playerNoNetherPortalTeleport globalMode [true/false]` | Set global mode |
| `/playerNoNetherPortalTeleport globalMode` | View the global mode status |
| `/playerNoNetherPortalTeleport add <player>` | Add a player to the blacklist |
| `/playerNoNetherPortalTeleport remove <player>` | Remove a player from the blacklist |
| `/playerNoNetherPortalTeleport clear` | Clear the blacklist |
| `/playerNoNetherPortalTeleport list` | View the blacklist |
| `/playerNoNetherPortalTeleport help` | View the usage instructions |

## commandLocationTag

On the front of a regular oak sign, enter `amsloc` on the first line (the sign type and marker can be modified via commands), and the name on lines two through four. Trim leading and trailing whitespace from each line, then concatenate them in order without spaces, line breaks, or other separators. Blank lines do not affect concatenation; only the front side is read.

| Command | Function |
| --- | --- |
| `/amsloc` or `/amsloc help` | Help, current recognition settings and documentation link |
| `/amsloc list [page]` | Ten entries per page, sorted by name, dimension and coordinates |
| `/amsloc list dimension <dimension> [page]` | Filter by dimension |
| `/amsloc info <id>` | Show name, full dimension ID and coordinates |
| `/amsloc config` | Show settings |
| `/amsloc config marker <text>` | Change the first-line marker |
| `/amsloc config sign <type>` | Change sign type |
| `/amsloc config reset` | Restore default recognition settings |
| `/amsloc rescan` | Check loaded chunks in batches |
