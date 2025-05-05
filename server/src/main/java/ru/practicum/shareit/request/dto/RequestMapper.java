package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public static RequestDto toDto(Request request) {

        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestorName(request.getRequestor().getName());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static List<RequestDto> toDto(Collection<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Request toRequestModel(RequestDto dto) {

        Request request = new Request();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        return request;
    }

    public static RequestItemDto toRequestItemDto(Item item) {

        RequestItemDto dto = new RequestItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwner(item.getOwner());
        return dto;
    }

    public static List<RequestItemDto> toRequestItemDto(Collection<Item> items) {
        return items.stream()
                .map(RequestMapper::toRequestItemDto)
                .collect(Collectors.toList());
    }
}
