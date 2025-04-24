package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class ItemMapper {
    public static ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static Item toModel(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public static List<ItemDto> toDto(Collection<Item> items) {
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<Item> toModel(Collection<ItemDto> dtos) {
        return dtos.stream()
                .map(ItemMapper::toModel)
                .collect(Collectors.toList());
    }
}
