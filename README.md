# PacketEntityAPI

FadeCloud Trial Project! spawn entities and do all kinds of things with them.
## Features
- Easy to use.
- Well documented.
- Highly-packed lightweight API.
- Entities can be equipped with items.
- Only dependency is ProtocolLib.
- Entity teleport.
- Entity move to a location.
- Entities can play animations.
- Metadata 100% supported. Entities can have Poses like sneaking/swimming.
  - You can also apply glowing effects and set the entity on fire.
- Viewers list. You can decide to hide/show entities for whoever you want.
- Entity Manager with useful methods to help you in your development.
- TAB auto complete.
- Reload config with a command.
- Highly-packed lightweight plugin.
- Easily extendable.
## API Usage
You'll want to use the manager in order to store all the custom entities on a list. This is for later use in your development, that way you can retrieve these whenever you want to. 
Everytime the create method is called, it will store the CustomEntity object into its list. 

The way we spawn an entity is the following:
```java
CustomEntity entity = CustomEntityManager.getManager().createEntity(PacketEntityType type, Location location, UUID uuid);
entity.addToViewerList(Player player);
```

- **PacketEntityType** is a custom enum class. This was decided because Mojang likes to change entity ID's every update. I've grabbed the 1.18.2 ones and put them in a compact enum class. You can use it in the constructor like this:  `PacketEntityType.BLAZE`. The other arguments are self-explanatory, the only note I have is that the UUID is set by us for later retrieval if we want to.
- **CustomEntity#addToViewerList**. This allows you to add a player to the list of players that can see this entity. You can call **CustomEntity#removeFromViewersList** in order to remove the player from the viewers list. This does invoke the destroy packet to the entity but the CustomEntity object is still stored in the manager.

We can go ahead and destroy the entity for good using: `entity.destroy()`.

## Additional features
There are multiple useful methods to use with the entity. They are all documented and tested! As a disclaimer though, some methods won't work for certain specific types of entities. For instance, you cannot put a helmet in a bee as that is not possible due to Minecraft limitations.

Some of the entity features are:
- **addDisplayName** Adds a display name to the entity.
- **hide** Hides the entity. This only makes the entity invisible.
- **unhide** Unhides the entity. 
- **teleport** Teleports the entity.
- **moveHere** Moves the entity to a specific location.
- **moveHead** Moves the head of the entity to a specific location.
- **addEquipment** Adds equipment to the entity and to a specific equipment slot. 
- **playAnimation** Plays an animation such as Crouching, Swimming, Elytra Flying, Sleeping.
- **setMetadata** Sets the metadata for an entity. Mostly internal use but can be used if you want to add a missing metadata value.
## How it works?

All of the magic is taken care of in the **CustomEntity** and **PacketWrapper** classes. This is where the entities, methods and useful stuff is inside. We have the wrappers package that contains all the PacketWrapper children, these represent a different packet that is needed to be sent to the client in order to get what we need for the entity to work as we expect to.

## Extending PacketWrapper
As mentioned before, all you need to do is create a class that extends PacketWrapper. This will give you a CustomEntity object that you can use to execute the packets to.

It is pretty easy and straightforward. More stuff can be added such as entity hit, and what not.
## Extra info and dependencies.

- Tested against Java 17, Minecraft 1.18.2 though it should work from 1.15 - 1.18.2 as some fields in the move packet were changed from integers to shorts.
- [ProtocolLib ](https://www.spigotmc.org/resources/protocollib.1997/) is the only dependency. It made my life easier. 
- Little to no external libraries were used besides a borrowed class from [IllusionTheDev](https://gist.github.com/IllusionTheDev/8b0761be3b699fcfc0c082b753e6f063)  in order to add display names.

I hope this API is good for your needs, a good effort (and a can of coke) went into it. ¡Adiós!

Total coding time including battle-testing it: 12 hours.