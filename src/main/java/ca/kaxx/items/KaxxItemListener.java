package ca.kaxx.items;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * The KaxxItemListener class implements Listener and handles events related to KaxxItem objects.
 */
public class KaxxItemListener implements Listener {


    /**
     * Represents a variable manager for KaxxItem objects.
     */
    private final KaxxItemManager manager;

    /**
     * The KaxxItemListener class implements Listener and handles events related to KaxxItem objects.
     */
    public KaxxItemListener(KaxxItemManager manager) {
        this.manager = manager;
    }

    /**
     * Handles the PlayerInteractEvent.
     *
     * @param event   The PlayerInteractEvent to handle.
     */
    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        handleEvent(event, event.getItem(), event.getPlayer(), (t, e) -> t.onInteract(e.getPlayer(), e));
    }


    /**
     * Handles the drop item event for players.
     *
     * @param event The PlayerDropItemEvent instance.
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        handleEvent(event, event.getItemDrop().getItemStack(), event.getPlayer(), (t, e) -> t.onDrop(e.getPlayer(), e));
    }

    /**
     * This method is called when a player picks up an item.
     *
     * @param event The PlayerPickupItemEvent triggered when the item is picked up
     */
    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        handleEvent(event, event.getItem().getItemStack(), event.getPlayer(), (t, e) -> t.onPickup(e.getPlayer(), e));
    }

    /**
     * Handles the PlayerItemDamageEvent.
     *
     * @param event The PlayerItemDamageEvent to handle.
     */
    @EventHandler
    public void onDurabilityChange(PlayerItemDamageEvent event) {
        handleEvent(event, event.getItem(), event.getPlayer(), (t, e) -> t.onDurabilityChange(e.getPlayer(), e));
    }

    /**
     * Handles the onItemBreak event.
     *
     * @param event The PlayerItemBreakEvent.
     */
    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent event) {
        handleEvent(event, event.getBrokenItem(), event.getPlayer(), (t, e) -> t.onBreak(e.getPlayer(), e));
    }

    /**
     * Handles the InventoryClickEvent. This method is an event handler that is called when a player clicks on an item within an inventory.
     *
     * @param event    The InventoryClickEvent to handle.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getCurrentItem() == null) return;
        if(!(event.getWhoClicked() instanceof Player)) return; //should never happen
        handleEvent(event, event.getCurrentItem(), (Player) event.getWhoClicked(), (t, e) -> t.onInventoryClick((Player) e.getWhoClicked(), e));
    }

    /**
     * Handles events related to KaxxItem objects.
     *
     * @param <T> the type of event to handle
     * @param event the event to handle
     * @param itemStack the ItemStack associated with the event
     * @param player the Player associated with the event
     * @param action the action to perform on the KaxxItemProvider
     */
    private <T extends Event> void handleEvent(T event, ItemStack itemStack, Player player, BiConsumer<KaxxItemProvider, T> action) {
        this.manager.findKaxxItem(player, itemStack).ifPresent(item -> {
            if(event instanceof Cancellable) {
                ((Cancellable) event).setCancelled(true);
            }
            action.accept(item.getProvider(), event);
        });
    }
}
