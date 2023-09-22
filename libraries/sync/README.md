# Sync
A Kotlin Symbol Processing (KSP) plugin for generating helpers with syncing BlockEntity data.

## Usage
### Setup
1. Add the Kotlin Symbol Processing (KSP) Gradle plugin.
   ```kotlin
   plugins {
       id("com.google.devtools.ksp") version "1.9.0-1.0.13" // https://github.com/google/ksp
   }
   ```
2. Depend on the `sync` project.
   > [!IMPORTANT]
   > Because loom remaps built projects to intermediary you must set `targetConfiguration = "namedElements"`
   > so Gradle uses the variant that matches your mappings.
   ```kotlin
   dependencies {
       include(implementation(projects.libraries.sync) { targetConfiguration = "namedElements" })
       ksp(projects.libraries.sync) { targetConfiguration = "namedElements" }
   }
   ```

### Use in a BlockEntity
1. Tag your BlockEntity and data fields with `@Sync`
   ```kotlin
   import gay.pyrrha.sync.Sync
   import my.mod.MyMod
   import net.minecraft.block.BlockState
   import net.minecraft.block.entity.BlockEntity

   @Sync
   class ExampleBlockEntity(
       pos: BlockPos,
       state: BlockState
   ) : BlockEntity(MyMod.EXAMPLE_BLOCK_ENTITY, pos, state) {
       @Sync
       var example: Boolean = false
   }
   ```
2. Either build or run the `kpsKotlin` Gradle task.
3. Sync will generate two classes per BlockEntity with `@Sync`
   1. `<Class>Sync` - The sync helper class for your BlockEntity (e.g. `ExampleBlockEntitySync`)
   2. `<Class>SyncData` - A data class with fields specific for your BlockEntity's `@Sync` fields. (e.g. `ExampleBlockEntitySyncData`)
4. Update your BlockEntity to use the new generated classes.
   ```kotlin
   import net.minecraft.nbt.NbtCompound
   ...
   override fun writeNbt(nbt: NbtCompound) {
       super.writeNbt(
           ExampleBlockEntitySync.write(
               nbt,
               ExampleBlockEntitySyncData(
                   example
               )
           )
       )
   }
   
   override fun readNbt(nbt: NbtCompound) {
       super.readNbt(nbt)
       ExampleBlockEntitySync.read(nbt) {
           this@ExampleBlockEntity.example = example
       }
   }
   ```
5. Profit (hopefully :3)
