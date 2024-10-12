package api.service.console;

import api.model.User;

import java.util.List;

public class ConsoleAdminCommand {
    ConsoleService consoleService;

    public ConsoleAdminCommand(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    public void listAllUsers() {
        List<User> users = consoleService.userController.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
            return;
        }
        for (User user : users) {
            System.out.printf("%s %s %s %s\n", user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
        }
    }

    public void blockUser() {
        System.out.print("Введите ID пользователя: ");
        long id = Long.parseLong(consoleService.getInput());
        consoleService.userController.blockUser(id);
    }

    public void unblockUser() {
        System.out.print("Введите ID пользователя: ");
        long id = Long.parseLong(consoleService.getInput());
        consoleService.userController.unblockUser(id);
    }

    public void deleteUser() {
        System.out.print("Введите ID пользователя: ");
        long id = Long.parseLong(consoleService.getInput());
        consoleService.userController.deleteUser(id);
    }

    public void roleAdmin() {
        System.out.print("Введите ID пользователя: ");
        long id = Long.parseLong(consoleService.getInput());
        consoleService.userController.roleAdmin(id);
    }

    public void roleUser() {
        System.out.print("Введите ID пользователя: ");
        long id = Long.parseLong(consoleService.getInput());
        consoleService.userController.roleUser(id);
    }
}
