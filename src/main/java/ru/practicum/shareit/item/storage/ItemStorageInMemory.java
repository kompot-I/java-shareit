package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemStorageInMemory implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item entity) {
        entity.setId(getNextId());
        items.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Item update(Long userId, Long itemId, Item entity) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Item with id " + itemId + " not found");
        }
        Item oldItem = items.get(itemId);
        String itemName = entity.getName();
        String itemDescription = entity.getDescription();
        Boolean itemAvailable = entity.getAvailable();
        if (itemName != null && !itemName.isBlank()) {
            oldItem.setName(itemName);
        }
        if (itemDescription != null && !itemDescription.isBlank()) {
            oldItem.setDescription(itemDescription);
        }
        if (itemAvailable != null) {
            oldItem.setAvailable(itemAvailable);
        }
        return oldItem;
    }

    @Override
    public Item getItemByUserId(Long userId, Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Item with id " + itemId + " not found");
        }
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getItemsByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .toList();
    }

    @Override
    public Collection<Item> searchItemByText(Long userId, String text) {
        if (text.isEmpty() || text.isBlank()) {
            return List.of();
        }
        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getDescription().toLowerCase().contains(searchText) || item.getName().toLowerCase().contains(searchText))
                .filter(item -> item.getAvailable().equals(true))
                .toList();
    }

    private long getNextId() {

        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}
