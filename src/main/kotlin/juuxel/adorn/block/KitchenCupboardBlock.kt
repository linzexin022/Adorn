@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.entity.BETypeProvider
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class KitchenCupboardBlock(
    variant: BlockVariant
) : AbstractKitchenCounterBlock(variant), BETypeProvider, BaseInventoryBlockEntity.InventoryProviderImpl {
    override val blockEntityType = AdornBlockEntities.KITCHEN_CUPBOARD

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): ActionResult {
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
        return ActionResult.SUCCESS
    }

    override fun createScreenHandlerFactory(state: BlockState, world: World, pos: BlockPos) =
        world.getBlockEntity(pos) as? NamedScreenHandlerFactory

    override fun onStateReplaced(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is BaseInventoryBlockEntity) {
                ItemScatterer.spawn(world, pos, getInventory(state1, world, pos))
                world.updateComparators(pos, this)
            }

            super.onStateReplaced(state1, world, pos, state2, b)
        }
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? KitchenCupboardBlockEntity)?.customName = stack.name
        }
    }

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
}
