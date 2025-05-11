package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<Mpa> getAll() {
        if (mpaStorage.getAll().isEmpty()) {
            log.info("Ошибка валидации, список с рейтингами пуст");
            throw new NotFoundException("Список с рейтингами пуст");
        }
        log.info("Успешно обработан запрос на получение списка рейтингов фильмов");
        return mpaStorage.getAll().stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .toList();
    }

    public Mpa getById(Integer id) {
        return mpaStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден по id: " + id));
    }
}

