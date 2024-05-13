package ca.kaxx.items;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The KaxxItem class represents an item in the Kaxx system.
 * Each KaxxItem has a provider, a unique identifier, and various methods to interact with the item.
 */
@Getter
public class KaxxItem {

    /**
     * The KaxxItemProvider interface defines the methods that a provider class for KaxxItem must implement.
     */
    private final KaxxItemProvider provider;
    /**
     * The uniqueId variable represents the unique identifier of a KaxxItem.
     * It is a private final field of type UUID and is used to uniquely identify each KaxxItem object.
     * The uniqueId is generated using the UUID.randomUUID() method when a KaxxItem is created.
     * It is set in the constructor of the KaxxItem class and cannot be modified thereafter.
     * The uniqueId is stored in the NBT tag of the ItemStack associated with the KaxxItem using the setUniqueId(ItemStack) method in the KaxxItem class.
     * The UUID can be retrieved from the ItemStack using the getKaxxItemUUID(ItemStack) method in the KaxxItemManager class.
     */
    private final UUID uniqueId;

    /**
     * The constructor for the KaxxItem class.
     * Each KaxxItem has a provider, a unique identifier, and various methods to interact with the item.
     *
     * @param provider The provider for the KaxxItem.
     */
    public KaxxItem(final KaxxItemProvider provider){
        this.provider = provider;
        this.uniqueId = UUID.randomUUID();
        KaxxItemManager.getInstance().registerItem(this);
    }

    /**
     * Retrieves the item associated with a player.
     *
     * @param player The player to retrieve the item for.
     * @return The item as an ItemStack.
     */
    public ItemStack get(Player player){
        ItemStack baseItem = provider.getBaseItem();
        initializeItemForPlayer(player, baseItem);
        return baseItem;
    }

    /**
     * Initializes a KaxxItem for a specific player by invoking the provider's init method and setting a unique identifier.
     *
     * @param player The player associated with the KaxxItem.
     * @param baseItem The base item of the KaxxItem.
     */
    private void initializeItemForPlayer(Player player, ItemStack baseItem) {
        provider.init(player, baseItem);
        setUniqueId(baseItem);
    }

    /**
     * Sets the unique ID for the given ItemStack.
     *
     * @param stack The ItemStack to set the unique ID for.
     */
    private void setUniqueId(ItemStack stack){
        net.minecraft.server.v1_8_R3.ItemStack item = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = item.getTag() == null ? new NBTTagCompound() : item.getTag();
        tag.setString(KaxxItemManager.KAXX_ITEM_UUID, uniqueId.toString());
        item.setTag(tag);
        stack.setItemMeta(CraftItemStack.asBukkitCopy(item).getItemMeta());
    }

}
