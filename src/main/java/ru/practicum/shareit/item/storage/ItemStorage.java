package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item create(Item item);

    Item update(Long userId, Long itemId, Item item);

    Item getItemByUserId(Long userId, Long itemId);

    Collection<Item> getItemsByUserId(Long userId);

    Collection<Item> searchItemByText(Long userId, String text);
}
