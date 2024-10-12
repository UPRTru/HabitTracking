import api.controller.UserController;
import api.model.User;
import api.model.dto.UserDto;
import api.repository.UserRepositoryLocal;
import api.service.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static final String PATH = "src/main/resources/";
    private static final String DB_FILE = PATH + "users.xml";
    UserController userController = new UserController(new UserServiceImpl(new UserRepositoryLocal()));

    @Test
    public void test() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(DB_FILE)))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<users>");
            writer.println("</users>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(0, userController.getAllUsers().size());
        userController.startAdminUser();
        assertEquals(1, userController.getAllUsers().size());
        assertEquals("admin", userController.getAllUsers().get(0).getFirstName());
        assertEquals("ADMIN", userController.getAllUsers().get(0).getRole());

        userController.addUser("test@test.com", "test", "test", "test");
        assertEquals(2, userController.getAllUsers().size());
        assertEquals("test", userController.getAllUsers().get(1).getFirstName());

        assertTrue(userController.auth("test@test.com", "test"));

        UserDto userDto = userController.getUserByEmail("test@test.com");

        assertEquals("User{id=1, email='test@test.com', firstName='test', lastName='test', role='USER'}", userDto.toString());

        userDto.setFirstName("Update");
        userController.editProfile(userDto);
        userDto = userController.getUserByEmail("test@test.com");

        assertEquals("Update", userDto.getFirstName());

        userController.changePassword("test@test.com", "newPassword");
        assertTrue(userController.auth("test@test.com", "newPassword"));

        userController.resetPassword("test@test.com");
        assertTrue(userController.auth("test@test.com", "password"));

        userController.blockUser(1);
        assertEquals("BLOCKED", userController.getAllUsers().get(1).getRole());
        userController.unblockUser(1);
        assertEquals("USER", userController.getAllUsers().get(1).getRole());
        userController.roleAdmin(1);
        assertEquals("ADMIN", userController.getAllUsers().get(1).getRole());
        userController.roleUser(1);
        assertEquals("USER", userController.getAllUsers().get(1).getRole());

        userController.deleteUser(1);
        assertEquals(1, userController.getAllUsers().size());
        assertFalse(userController.auth("test@test.com", "password"));
    }
}
