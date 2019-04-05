package com.example.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Categories {

    POP("pop"),
    ROCK("rock"),
    PARTY("party"),
    MEXICO("mexican"),
    CLASSICAL("classical");

    private final String categoryId;

}
