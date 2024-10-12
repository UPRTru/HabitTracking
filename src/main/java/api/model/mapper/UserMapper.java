package api.model.mapper;

import api.model.User;
import api.model.dto.UserDto;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static User updateUser(User user, UserDto userDto) {
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(userDto.getRole());
        return user;
    }
}
