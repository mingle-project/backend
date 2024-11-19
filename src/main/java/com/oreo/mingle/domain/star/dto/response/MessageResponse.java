package com.oreo.mingle.domain.star.dto.response;

public record MessageResponse<T>(String message, T data) {}
