package ca.kaxx.items;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * The KaxxItemHandler class manages KaxxItem objects.
 */
@Getter
public final class KaxxItemHandler {


    /**
     * The {@code KAXX_ITEM_UUID} is a constant string that represents the key used in an NBT tag to store the UUID associated with a specific ItemStack.
     * It is used in the KaxxItemManager class to manage KaxxItem objects.
     * The UUID is generated when a KaxxItem is created and stored in the NBT tag of the ItemStack.
     * The UUID can be retrieved from the ItemStack using the {@link KaxxItemHandler#getKaxxItemUUID(ItemStack)} method.
     * <p>
     * Usage example:
     * <p>
     * String myKaxxItemUUID = KAXX_ITEM_UUID;
     * <p>
     * Note: This constant should not be modified or accessed directly outside of the KaxxItemManager class.
     */
    public static final String KAXX_ITEM_UUID = "kaxx_item_uuid";

    /**
     * The KaxxItemManager class manages KaxxItem objects.
     */
    @Getter
    private static KaxxItemHandler instance;


    /**
     * This class represents a private final variable "plugin" of type JavaPlugin.
     * The variable is used to store an instance of the JavaPlugin class.
     */
    private final JavaPlugin plugin;

    /**
     * The items variable is a private final List of KaxxItem objects.
     * It is a field in the KaxxItemManager class and is used to store
     * instances of KaxxItem that are registered with the manager. The
     * list allows for easy access and manipulation of the items.
     */
    private final Collection<KaxxItem> items;

    /**
     * The KaxxItemManager class manages KaxxItem objects.
     */
    public KaxxItemHandler(final JavaPlugin plugin) {
        if (instance != null) {
            throw new IllegalStateException("KaxxItemManager is already initialized!");
        }

        instance = this;

        this.plugin = plugin;

        this.items = Lists.newArrayList();

        Bukkit.getPluginManager().registerEvents(new KaxxItemListener(this), plugin);
    }


    /**
     * Initializes the KaxxItemManager for a given JavaPlugin.
     *
     * @param plugin The JavaPlugin instance to initialize the KaxxItemManager for.
     */
    public static void init(final JavaPlugin plugin) {
        new KaxxItemHandler(plugin);
    }

    /**
     * Clears the list of items in the KaxxItemManager.
     */
    public void clean() {
        items.clear();
    }

    /**
     * Registers an item to the collection of items.
     *
     * @param item The item to be registered.
     */
    public void registerItem(final KaxxItem item) {

        if (items.contains(item)) {
            throw new IllegalArgumentException("Item is already registered!");
        }

        items.add(item);
    }

    /**
     * Finds a KaxxItem in the list of items based on the provided player and stack.
     *
     * @param stack the stack to search for the KaxxItem
     * @return an Optional containing the found KaxxItem, or empty if not found
     */
    public Optional<KaxxItem> findKaxxItem(final ItemStack stack) {
        return getKaxxItemUUID(stack).flatMap(uuid -> items.stream()
                .filter(item -> item.getUniqueId().equals(uuid))
                .findFirst());
    }

    /**
     * This method retrieves the UUID associated with a given ItemStack.
     * If the ItemStack has a NBT tag that contains the {@code KAXX_ITEM_UUID} key, the method will return an Optional containing the UUID value.
     * Otherwise, it will return an empty Optional.
     *
     * @param stack The ItemStack to retrieve the UUID from.
     * @return An Optional containing the UUID value, or an empty Optional.
     */
    private Optional<UUID> getKaxxItemUUID(final ItemStack stack) {
        net.minecraft.server.v1_8_R3.ItemStack item = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = item.getTag();

        if (tag != null && tag.hasKey(KAXX_ITEM_UUID)) {
            return Optional.of(UUID.fromString(tag.getString(KAXX_ITEM_UUID)));
        }

        return Optional.empty();
    }
}
