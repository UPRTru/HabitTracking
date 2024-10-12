package api.service;

import api.model.Role;
import api.model.User;
import api.model.authenticators.UsersAuthenticator;
import api.model.dto.UserDto;
import api.model.mapper.UserMapper;
import api.repository.UserRepositoryLocal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class UserServiceImpl implements UserService {
    private final String STRING_SALT = "gbcbeaahitklnmerqrsxutw888";
    private final String RESET_PASSWORD = "password";
    private final UserRepositoryLocal userRepository;

    public UserServiceImpl(UserRepositoryLocal userRepository) {
        this.userRepository = userRepository;
    }

    public void startAdminUser() {
        try {
            if (findUserByEmail("admin") == null) {
                User user = new User(
                        userRepository.getNewUserId(),
                        "admin",
                        encryptPassword("admin"),
                        "admin",
                        "admin");
                Role.addAdminRole(user);
                userRepository.addUser(Role.addAdminRole(user));
            }
        } catch (Exception ignored) {
        }
    }

    public boolean auth(String email, String password) {
        try {
            User user = findUserByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("Пользователь с таким email не найден");
            } else if (encryptPassword(password).equals(user.getPassword())) {
                if (Role.checkBlocked(UserMapper.mapToUserDto(user))) {
                    System.out.println("Пользователь заблокирован");
                    return false;
                } else {
                    System.out.println("Операция прошла успешно");
                    return true;
                }
            } else {
                throw new IllegalArgumentException("Неверный пароль");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public UserDto addUser(String email, String firstName, String lastName, String password) {
        if (!UsersAuthenticator.isValidEmail(email)) {
            System.out.println("Некорректный email");
            return null;
        }
        if (UsersAuthenticator.isValidNotNull(password) &&
                UsersAuthenticator.isValidNotNull(firstName) &&
                UsersAuthenticator.isValidNotNull(lastName)) {
            try {
                User userGetEmail = findUserByEmail(email);
                if (userGetEmail == null) {
                    User user = new User(userRepository.getNewUserId(), email, encryptPassword(password), firstName, lastName);
                    userRepository.addUser(Role.addUserRole(user));
                    return UserMapper.mapToUserDto(user);
                } else {
                    System.out.println("Пользователь с таким email уже существует");
                    return null;
                }
            } catch (Exception e) {
                System.out.println("Ошибка добавления пользователя");
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            System.out.println("Ошибка добавления пользователя");
            System.out.println("Заполните все обязательные поля");
            return null;
        }
    }

    public User findUserByEmail(String email) {
        try {
            return userRepository.findUserByEmail(email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.getAllUsers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public void editProfile(UserDto userDto) {
        boolean updated = false;
        User user = findUserByEmail(userDto.getEmail());
        if (user != null) {
            if (!user.getEmail().equals(userDto.getEmail())) {
                user.setEmail(userDto.getEmail());
                updated = true;
            }
            if (!user.getFirstName().equals(userDto.getFirstName())) {
                user.setFirstName(userDto.getFirstName());
                updated = true;
            }
            if (!user.getLastName().equals(userDto.getLastName())) {
                user.setLastName(userDto.getLastName());
                updated = true;
            }
            if (updated) {
                try {
                    userRepository.EditProfile(user);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void deleteUser(long id) {
        try {
            userRepository.removeUser(id);
            System.out.println("Пользователь удален");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changePassword(String email, String newPassword) {
        User user = findUserByEmail(email);
        if (user != null && UsersAuthenticator.isValidNotNull(newPassword)) {
            user.setPassword(encryptPassword(newPassword));
            try {
                userRepository.EditProfile(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void resetPassword(String email) {
        User user = findUserByEmail(email);
        if (user != null) {
            user.setPassword(encryptPassword(RESET_PASSWORD));
            try {
                userRepository.EditProfile(user);
                System.out.println("Пароль сброшен");
                System.out.println("Новый пароль: " + RESET_PASSWORD);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void blockUser(long id) {
        try {
            userRepository.blockUser(id);
            System.out.println("Пользователь заблокирован");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void unblockUser(long id) {
        try {
            userRepository.unblockUser(id);
            System.out.println("Пользователь разблокирован");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void roleAdmin(long id) {
        try {
            userRepository.roleAdmin(id);
            System.out.println("Пользователь сделан администратором");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void roleUser(long id) {
        try {
            userRepository.roleUser(id);
            System.out.println("Пользователь сделан обычным пользователем");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String encryptPassword(String input) {
        byte[] bytes;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(STRING_SALT.getBytes());
            bytes = md.digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка шифрования.", e);
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
