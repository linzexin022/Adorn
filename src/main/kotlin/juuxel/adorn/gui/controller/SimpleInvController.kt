package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.gui.painter.Painters
import juuxel.adorn.gui.widget.WColorableLabel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.container.BlockContext
import net.minecraft.container.PropertyDelegate
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

open class SimpleInvController(
    syncId: Int,
    playerInv: PlayerInventory,
    context: BlockContext,
    invWidth: Int,
    invHeight: Int,
    private val paletteId: Identifier,
    blockInventory: Inventory = getBlockInventoryOrCreate(context, invWidth * invHeight),
    propertyDelegate: PropertyDelegate = getBlockPropertyDelegate(context)
) : BaseAdornController(syncId, playerInv, context, blockInventory, propertyDelegate) {
    private val slot: WItemSlot

    init {
        (rootPanel as WGridPanel).apply {
            add(createTitle(context), 0, 0)

            slot = WItemSlot.of(blockInventory, 0, invWidth, invHeight)
            add(slot, (9 - invWidth) / 2, 1)

            if (invHeight > 0) {
                add(playerInvPanel, 0, 2 + invHeight)
            }

            validate(this@SimpleInvController)
        }
    }

    @Environment(EnvType.CLIENT)
    override fun addPainters() {
        super.addPainters()
        slot.setBackgroundPainter(Painters.LIBGUI_STYLE_SLOT)
    }

    protected open fun createTitle(context: BlockContext): WWidget {
        val block = getBlock(context).get()
        val blockId = Registry.BLOCK.getId(block)

        return WColorableLabel(
            TranslatableText(
                block.translationKey
            ), paletteId, blockId
        )
    }
}
