package de.backxtar;

public enum DataType {
    INT("Integer"),
    STRING("String");
    final String type;

    DataType(String type) {
        this.type = type;
    }
}
