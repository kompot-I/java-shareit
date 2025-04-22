package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(Long userId, ItemDto entity) {
        userStorage.getItem(userId);
        Item item = itemStorage.create(userId, ItemMapper.toModel(entity));
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto entity) {
        userStorage.getItem(userId);
        Item item = itemStorage.update(userId, itemId, ItemMapper.toModel(entity));
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        Item item = itemStorage.getItem(userId, itemId);
        return ItemMapper.toDto(item);
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        return itemStorage.getItems(userId).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getSearch(Long userId, String text) {
        return itemStorage.getSearch(userId, text).stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
