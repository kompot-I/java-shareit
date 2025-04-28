package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDataDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto create(Long userId, ItemDto entity) {
        getUser(userId);
        Item item = ItemMapper.toModel(entity);
        item.setOwner(userId);
        Item createdItem = itemRepository.save(item);
        return ItemMapper.toDto(createdItem);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto entity) {
        User user = getUser(userId);
        Item item = getItem(itemId);

        if (!item.getOwner().equals(user.getId())) {
            throw new NotFoundException("User with id " + user.getId() + " is not the owner of item with id " + itemId);
        }

        if (entity.getName() != null) {
            item.setName(entity.getName());
        }
        if (entity.getDescription() != null) {
            item.setDescription(entity.getDescription());
        }
        if (entity.getAvailable() != null) {
            item.setAvailable(entity.getAvailable());
        }

        item = itemRepository.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDataDto getItem(Long userId, Long itemId) {
        User user = getUser(userId);
        Item item = getItem(itemId);
        ItemDataDto itemData = ItemMapper.toItemDataDto(item);
        Collection<Booking> bookings = bookingRepository.findAllByItemOrderByStartAsc(item);

        boolean isOwner = user.getId().equals(item.getOwner());
        boolean isBooker = false;

        for (Booking booking : bookings) {
            if (isOwner && booking.getEnd().isBefore(LocalDateTime.now())) {
                itemData.setLastBooking(BookingMapper.toDto(booking));
            }
            if (isOwner && booking.getStart().isAfter(LocalDateTime.now())) {
                itemData.setNextBooking(BookingMapper.toDto(booking));
            }
            if (booking.getBooker().getId().equals(userId)) {
                isBooker = true;
            }
        }

        if (!isBooker && !isOwner) {
            throw new AccessDeniedException("Access denied");
        }

        List<CommentDto> comments = ItemMapper.toCommentDto(commentRepository.findAllByItem(item));
        itemData.setComments(comments);
        return itemData;
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        getUser(userId);
        return ItemMapper.toDto(itemRepository.findAllByOwner(userId));
    }

    @Override
    public Collection<ItemDto> searchItems(Long userId, String text) {
        getUser(userId);
        if (text.isEmpty() || text.isBlank()) {
            return List.of();
        }
        return ItemMapper.toDto(itemRepository.getSearch(text));
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto entity) {
        User user = getUser(userId);
        Item item = getItem(itemId);
        Booking booking = bookingRepository.findAllByItemAndBookerPast(item, user, LocalDateTime.now()).stream()
                .findFirst()
                .orElseThrow(() -> new ValidationException("User " + userId + " didn't take the item " + itemId + " for rent"));

        if (!booking.getStatus().equals(BookingStatus.APPROVED) || booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("User " + userId + " didn't take the item " + itemId + " for rent");
        }

        Comment comment = ItemMapper.toCommentModel(entity);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return ItemMapper.toCommentDto(comment);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
    }
}
