package api.model;

import api.model.dto.UserDto;

import java.util.Objects;

public enum Role {
    USER,
    ADMIN,
    BLOCKED;

    public static boolean checkAdmin (UserDto user) {
        return Objects.equals(user.getRole(), Role.ADMIN.name());
    }

    public static boolean checkBlocked (UserDto user) {
        return Objects.equals(user.getRole(), Role.BLOCKED.name());
    }

    public static User addUserRole (User user) {
        user.setRole(Role.USER.name());
        return user;
    }

    public static User addAdminRole (User user) {
        user.setRole(Role.ADMIN.name());
        return user;
    }

    public static User addBlockedRole (User user) {
        user.setRole(Role.BLOCKED.name());
        return user;
    }
}
