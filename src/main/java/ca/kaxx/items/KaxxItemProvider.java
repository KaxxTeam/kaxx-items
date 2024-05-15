package ca.kaxx.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

/**
 * The KaxxItemProvider interface defines the methods that a provider class for KaxxItem must implement.
 */
public interface KaxxItemProvider {

    /**
     * Retrieves the base item for a KaxxItem.
     *
     * @return The base item as an ItemStack.
     * <p>
     * Note: This is the common item that will be used as the base for all KaxxItems for every player.
     */
    ItemStack getBaseItem();

    /**
     * The init method initializes a KaxxItem by setting the player and base item.
     *
     * @param player   The player associated with the KaxxItem.
     * @param baseItem The base item of the KaxxItem.
     */
    default void init(final Player player, final ItemStack baseItem) {
    }

    /**
     * Handles the drop item event for players.
     *
     * @param player The player who dropped the item.
     * @param event  The PlayerDropItemEvent instance.
     */
    default void onDrop(final Player player, final PlayerDropItemEvent event) {
    }

    /**
     * This method is called when a player picks up an item.
     *
     * @param player The player who picked up the item
     * @param event  The PlayerPickupItemEvent triggered when the item is picked up
     */
    default void onPickup(final Player player, final PlayerPickupItemEvent event) {
    }

    /**
     * Handles the player interaction event.
     *
     * @param player The player interacting with the item.
     * @param event  The PlayerInteractEvent object representing the interaction event.
     */
    default void onInteract(final Player player, final PlayerInteractEvent event) {
    }

    /**
     * This method is called when a player's item breaks.
     *
     * @param player The player whose item broke.
     * @param event  The event representing the item break.
     */
    default void onBreak(final Player player, final PlayerItemBreakEvent event) {
    }

    /**
     * Handles the durability change event for a player's item.
     *
     * @param player The player whose item's durability has changed.
     * @param event  The PlayerItemDamageEvent that represents the durability change event.
     */
    default void onDurabilityChange(final Player player, final PlayerItemDamageEvent event) {
    }

    /**
     * Handles the InventoryClickEvent. This method is an event handler that is called when a player clicks on an item within an inventory.
     *
     * @param player The player who triggered the event.
     * @param event  The InventoryClickEvent to handle.
     */
    default void onInventoryClick(final Player player, final InventoryClickEvent event) {
    }
}
