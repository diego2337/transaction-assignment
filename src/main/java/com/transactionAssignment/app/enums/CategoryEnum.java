package com.transactionAssignment.app.enums;

import lombok.Getter;

@Getter
public enum CategoryEnum {
    MEAL("5811", "5812"),
    FOOD("5411", "5412"),
    CASH("5011");

    private final String[] codes;

    CategoryEnum(String... codes) {
        this.codes = codes;
    }

    public static CategoryEnum fromCode(String code) {
        for (CategoryEnum category : values()) {
            for (String categoryCode : category.codes) {
                if (categoryCode.equals(code)) {
                    return category;
                }
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static CategoryEnum fromName(String name) {
        try {
            return CategoryEnum.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown category name: " + name);
        }
    }


}
