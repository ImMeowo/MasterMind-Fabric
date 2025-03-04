package de.snowii.mastermind.module.modules.player

import de.snowii.mastermind.module.Module
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ToolMaterial
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.world.ServerWorld

object InvManager : Module("InvManager", "Manages your Inventory", Category.PLAYER) {

    private val bestTools: MutableMap<Item, MutableMap<Int, Float>> = HashMap()

    init {
        toggle(true)
        ScreenEvents.AFTER_INIT.register(ScreenEvents.AfterInit { client, screen, scaledWidth, scaledHeight ->
            if (!this.isToggled) return@AfterInit
            run {
                if (screen is InventoryScreen) {
                    ScreenEvents.afterTick(screen)
                        .register(ScreenEvents.AfterTick { screen ->
                            bestTools.clear()

                            /**
                            // Find best Tools
                            for (slot in 0..PlayerInventory.MAIN_SIZE) {
                                val stack = mc.player!!.inventory.getStack(slot)
                                if (stack == null || stack.item !is ToolIte) continue
                                val item = stack.item as ToolItem
                                val toolValue = getToolValue(item, stack)
                                if (toolValue > bestTools.getOrPut(item) { HashMap() }.getOrPut(slot) { 0.0F }) {
                                    bestTools[item] = HashMap(slot, toolValue)
                                }
                            }

                            // Throw other tools out
                            for (slot in 0..PlayerInventory.MAIN_SIZE) {
                                val stack = mc.player!!.inventory.getStack(slot)
                                if (stack == null || stack.item !is PickaxeItem) continue
                                val item = stack.item as PickaxeItem
                                val toolValue = getToolValue(item, stack)
                                val bestItemMap = bestTools[item]
                                if (bestItemMap != null) {
                                    val toolValueMap = bestItemMap[slot]
                                    if (toolValueMap != null && toolValue < toolValueMap) {
                                        mc.interactionManager!!.clickSlot(
                                            mc.player!!.currentScreenHandler.syncId,
                                            slot,
                                            0,
                                            SlotActionType.THROW,
                                            mc.player
                                        )
                                    }
                                }
                            }
                            **/

                        })
                }
            }
        })
    }

    private fun getToolValue(material: ToolMaterial, stack: ItemStack): Float {
        val itemPoints = material.durability + material.speed + material.enchantmentValue
        val itemDamage = material.attackDamageBonus

        val dmgSource =
            mc.player!!.damageSources.playerAttack(mc.player!!)
    //    val prtPoints = EnchantmentHelper.getProtectionAmount(mc.player!!.world!! as ServerWorld?, mc.player, dmgSource)

        return itemPoints + /* prtPoints */ + itemDamage
    }
}

