package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(Long userId, ItemDto entity) {
        getUser(userId);
        Item item = ItemMapper.toModel(entity);
        item.setOwner(userId);
        Item createdItem = itemStorage.create(item);
        return ItemMapper.toDto(createdItem);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto entity) {
        Item item = itemStorage.getItemByUserId(userId, itemId);
        if (!item.getOwner().equals(userId)) {
            throw new NotFoundException("User with id " + userId + " is not the owner of item with id " + itemId);
        }

        Item updated = itemStorage.update(userId, itemId, ItemMapper.toModel(entity));
        return ItemMapper.toDto(updated);
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        Item item = itemStorage.getItemByUserId(userId, itemId);
        return ItemMapper.toDto(item);
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        return ItemMapper.toDto(itemStorage.getItemsByUserId(userId));
    }

    @Override
    public Collection<ItemDto> searchItems(Long userId, String text) {
        return ItemMapper.toDto(itemStorage.searchItemByText(userId, text));
    }

    private User getUser(Long userId) {
        return userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }
}
