package account.enumeration;

public enum Operation {
    GRANT("GRANT"),
    REMOVE("REMOVE"),
    LOCK("locked"),
    UNLOCK("unlocked");

    private final String name;

    Operation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
