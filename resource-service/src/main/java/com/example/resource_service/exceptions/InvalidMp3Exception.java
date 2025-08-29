package com.example.resource_service.exceptions;

public class InvalidMp3Exception extends BadRequestException {
    public InvalidMp3Exception(String msg) {
        super(msg);
    }
}
