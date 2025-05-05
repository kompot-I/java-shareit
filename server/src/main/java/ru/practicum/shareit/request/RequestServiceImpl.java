package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestDto create(Long userId, RequestDto entity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Request request = RequestMapper.toRequestModel(entity);
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);
        return RequestMapper.toDto(request);
    }

    @Override
    public RequestDto getRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос с идентификатором " + requestId + " не найден"));
        RequestDto requestDto = RequestMapper.toDto(request);
        List<RequestItemDto> items = RequestMapper.toRequestItemDto(itemRepository.findAllByRequestId(requestId));

        requestDto.setItems(items);
        return requestDto;
    }

    @Override
    public Collection<RequestDto> getMyRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Collection<RequestDto> requests = RequestMapper.toDto(requestRepository.getMyRequests(user));

        for (RequestDto r : requests) {
            List<RequestItemDto> items = RequestMapper.toRequestItemDto(itemRepository.findAllByRequestId(r.getId()));
            r.setItems(items);
        }
        return requests;
    }

    @Override
    public Collection<RequestDto> getAllRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
        Collection<RequestDto> requests = RequestMapper.toDto(requestRepository.getAllRequests(user));

        for (RequestDto r : requests) {
            List<RequestItemDto> items = RequestMapper.toRequestItemDto(itemRepository.findAllByRequestId(r.getId()));
            r.setItems(items);
        }
        return requests;
    }
}
