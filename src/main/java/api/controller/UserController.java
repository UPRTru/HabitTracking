package api.controller;

import api.model.User;
import api.model.dto.UserDto;
import api.model.mapper.UserMapper;
import api.service.UserService;

import java.util.List;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void startAdminUser() {
        userService.startAdminUser();
    }

    public UserDto addUser(String email, String firstName, String lastName, String password) {
        return userService.addUser(email, firstName, lastName, password);
    }

    public boolean auth(String email, String password) {
        return userService.auth(email, password);
    }

    public UserDto getUserByEmail(String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return null;
        } else {
            return UserMapper.mapToUserDto(user);
        }
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public void editProfile(UserDto userDto) {
        userService.editProfile(userDto);
    }

    public void deleteUser(long id) {
        userService.deleteUser(id);
    }

    public void changePassword(String email, String newPassword) {
        userService.changePassword(email, newPassword);
    }

    public void resetPassword(String email) {
        userService.resetPassword(email);
    }

    public void blockUser(long id) {
        userService.blockUser(id);
    }

    public void unblockUser(long id) {
        userService.unblockUser(id);
    }

    public void roleAdmin(long id) {
        userService.roleAdmin(id);
    }

    public void roleUser(long id) {
        userService.roleUser(id);
    }
}
