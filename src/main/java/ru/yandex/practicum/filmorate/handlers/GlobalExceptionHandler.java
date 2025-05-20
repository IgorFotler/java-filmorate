package ru.yandex.practicum.filmorate.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(UserNotFoundException unfe) {
        return ApiError.builder().errorCode(HttpStatus.NOT_FOUND.value()).description(unfe.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFilmNotFoundException(FilmNotFoundException fnfe) {
        return ApiError.builder().errorCode(HttpStatus.NOT_FOUND.value()).description(fnfe.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException nfe) {
        return ApiError.builder().errorCode(HttpStatus.NOT_FOUND.value()).description(nfe.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUncaught(Exception exception) {
        return ApiError.builder().errorCode(HttpStatus.BAD_REQUEST.value()).description(exception.getMessage()).build();
    }
}
