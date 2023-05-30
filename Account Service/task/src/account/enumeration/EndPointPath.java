package account.enumeration;

public enum EndPointPath {

    CREATE_USER("/api/auth/signup"),
    CHANGE_PASSWORD("/api/auth/changepass"),
    GRANT_ROLE("/api/admin/user/role"),
    REMOVE_ROLE("/api/admin/user/role"),
    DELETE_USER("/api/admin/user"),
    LOCK_USER("/api/admin/user/access"),
    UNLOCK_USER("/api/admin/user/access");

    private final String endPoint;

    EndPointPath(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

}
