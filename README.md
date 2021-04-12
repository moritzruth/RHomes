# RHomes
> A Spigot plugin for defining home locations and teleporting to them.

## Commands
- `/home` - Teleport yourself to your home (Permission: `rhomes.use.own`).
- `/home <Player>` - Teleport yourself to the guest home of `<Player>` (Permission: `rhomes.use.guest`).
- `/home set` - Set your own home (Permission: `rhomes.set.own`).
- `/home setguest` - Set your own guest home (Permission: `rhomes.set.guest`).

## Configuration
```yaml
# Language of chat messages. Allowed values: en, de
language: en

# The time until you will be teleported
teleportDelay: 3
```
