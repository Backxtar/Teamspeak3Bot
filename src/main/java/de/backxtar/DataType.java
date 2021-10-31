package de.backxtar;

public enum DataType {
    INT("Integer"),
    STRING("String"),
    HASHMAP("Hashmap");
    final String type;

    DataType(String type) {
        this.type = type;
    }
}
