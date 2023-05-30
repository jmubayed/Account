package account.enumeration;

public enum Role {
    ACCOUNTANT("ACCOUNTANT"),
    USER("USER"),
    ADMINISTRATOR("ADMINISTRATOR"),
    AUDITOR("AUDITOR");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
