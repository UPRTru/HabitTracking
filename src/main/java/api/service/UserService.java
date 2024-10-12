package api.service;

import api.model.User;
import api.model.dto.UserDto;

import java.util.List;

public interface UserService {

    void startAdminUser();

    boolean auth(String email, String password);

    UserDto addUser(String email, String firstName, String lastName, String password);

    List<User> getAllUsers();

    void editProfile(UserDto userDto);

    void deleteUser(long id);

    void changePassword(String email, String newPassword);

    void resetPassword(String email);

    User findUserByEmail(String email);

    void blockUser(long id);

    void unblockUser(long id);

    void roleAdmin(long id);

    void roleUser(long id);
}
