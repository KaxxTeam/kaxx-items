package ca.kaxx.items;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * The KaxxItemListener class implements Listener and handles events related to KaxxItem objects.
 */
public final class KaxxItemListener implements Listener {


    /**
     * Represents a variable manager for KaxxItem objects.
     */
    private final KaxxItemHandler manager;

    /**
     * The KaxxItemListener class implements Listener and handles events related to KaxxItem objects.
     */
    public KaxxItemListener(final KaxxItemHandler manager) {
        this.manager = manager;
    }

    /**
     * Handles the PlayerInteractEvent.
     *
     * @param event The PlayerInteractEvent to handle.
     */
    @EventHandler
    public void onItemInteract(final PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        handleEvent(event, event.getItem(), (t, e) -> t.onInteract(e.getPlayer(), e));
    }


    /**
     * Handles the drop item event for players.
     *
     * @param event The PlayerDropItemEvent instance.
     */
    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        if(event.getItemDrop() == null) return; //should never happen
        handleEvent(event, event.getItemDrop().getItemStack(), (t, e) -> t.onDrop(e.getPlayer(), e));
    }

    /**
     * This method is called when a player picks up an item.
     *
     * @param event The PlayerPickupItemEvent triggered when the item is picked up
     */
    @EventHandler
    public void onPickupItem(final PlayerPickupItemEvent event) {
        if(event.getItem() == null) return; //should never happen
        handleEvent(event, event.getItem().getItemStack(), (t, e) -> t.onPickup(e.getPlayer(), e));
    }

    /**
     * Handles the PlayerItemDamageEvent.
     *
     * @param event The PlayerItemDamageEvent to handle.
     */
    @EventHandler
    public void onDurabilityChange(final PlayerItemDamageEvent event) {
        if(event.getItem() == null) return; //should never happen
        handleEvent(event, event.getItem(), (t, e) -> t.onDurabilityChange(e.getPlayer(), e));
    }

    /**
     * Handles the onItemBreak event.
     *
     * @param event The PlayerItemBreakEvent.
     */
    @EventHandler
    public void onItemBreak(final PlayerItemBreakEvent event) {
        if(event.getBrokenItem() == null) return; //should never happen
        handleEvent(event, event.getBrokenItem(), (t, e) -> t.onBreak(e.getPlayer(), e));
    }

    /**
     * Handles the InventoryClickEvent. This method is an event handler that is called when a player clicks on an item within an inventory.
     *
     * @param event The InventoryClickEvent to handle.
     */
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!(event.getWhoClicked() instanceof Player)) return; //should never happen
        handleEvent(event, event.getCurrentItem(), (kaxxItemProvider, event1) -> kaxxItemProvider.onInventoryClick((Player) event.getWhoClicked(), event1));
    }

    /**
     * Handles events related to KaxxItem objects.
     *
     * @param <T>       the type of event to handle
     * @param event     the event to handle
     * @param itemStack the ItemStack associated with the event
     * @param action    the action to perform on the KaxxItemProvider
     */
    private <T extends Event> void handleEvent(final @Nonnull T event, final @Nonnull ItemStack itemStack, @Nonnull final BiConsumer<KaxxItemProvider, T> action) {
        this.manager.findKaxxItem(itemStack).ifPresent(item -> {
            if (event instanceof Cancellable) {
                if (event instanceof PlayerPickupItemEvent && !item.isPickable()) {
                    ((Cancellable) event).setCancelled(true);
                }

                if (event instanceof PlayerDropItemEvent && !item.isDroppable()) {
                    ((Cancellable) event).setCancelled(true);
                }

                if (event instanceof PlayerItemBreakEvent && !item.isBreakable()) {
                    ((Cancellable) event).setCancelled(true);
                }
            }

            action.accept(item.getProvider(), event);
        });
    }
}
