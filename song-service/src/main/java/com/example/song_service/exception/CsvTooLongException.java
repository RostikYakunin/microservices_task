package com.example.song_service.exception;

public class CsvTooLongException extends RuntimeException {
    public CsvTooLongException(String message) {
        super(message);
    }
}
