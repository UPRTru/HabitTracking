package api.service.console;

import api.model.authenticators.UsersAuthenticator;
import api.model.dto.UserDto;

public class ConsoleUserCommand {
    ConsoleService consoleService;

    public ConsoleUserCommand(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    public void startAdminUser() {
        consoleService.userController.startAdminUser();
    }

    public void loginUser() {
        System.out.println("Введите email: ");
        String email = consoleService.getInput();
        System.out.println("Введите пароль: ");
        String password = consoleService.getInput();
        if (consoleService.userController.auth(email, password)) {
            consoleService.loginUser = consoleService.userController.getUserByEmail(email);
        }
    }

    public void registerUser() {
        System.out.println("Введите email: ");
        String email = consoleService.getInput();
        System.out.println("Введите имя: ");
        String firstName = consoleService.getInput();
        System.out.println("Введите фамилию: ");
        String lastName = consoleService.getInput();
        System.out.println("Введите пароль: ");
        String password = consoleService.getInput();
        UserDto user = consoleService.userController.addUser(email, firstName, lastName, password);
        if (user != null) {
            System.out.println("Вы успешно зарегистрировались!");
            consoleService.loginUser = user;
        }
    }

    public void myProfile() {
        System.out.println(consoleService.userController.getUserByEmail(consoleService.loginUser.getEmail()).toString());
    }

    public void editProfile() {
        System.out.println("Редактирование профиля");
        System.out.println("Оставляйте пустые поля, чтобы не менять профиль.");
        System.out.println("Введите email: ");
        String email = consoleService.getInput();
        if (consoleService.userController.getUserByEmail(email) != null) {
            consoleService.cleanConsole();
            System.out.println("Этот email уже используется. Пожалуйста, введите другой.");
            editProfile();
        } else {
            if (!email.isEmpty()) {
                if (UsersAuthenticator.isValidEmail(email)) {
                    consoleService.loginUser.setEmail(email);
                } else {
                    consoleService.cleanConsole();
                    System.out.println("Некорректный email. Пожалуйста, введите другой.");
                }
            }
            System.out.println("Введите новое имя: ");
            String newFirstName = consoleService.getInput();
            if (!newFirstName.isEmpty()) {
                consoleService.loginUser.setFirstName(newFirstName);
            }
            System.out.println("Введите новую фамилию: ");
            String newLastName = consoleService.getInput();
            if (!newLastName.isEmpty()) {
                consoleService.loginUser.setLastName(newLastName);
            }
            consoleService.userController.editProfile(consoleService.loginUser);
            consoleService.cleanConsole();
            System.out.println("Ваш профиль был обновлен.");
            System.out.println(consoleService.loginUser.toString());
        }
    }

    public void changePassword() {
        System.out.println("Введите старый пароль: ");
        String oldPassword = consoleService.getInput();
        if (consoleService.userController.auth(consoleService.loginUser.getEmail(), oldPassword)) {
            System.out.println("Введите новый пароль: ");
            String newPassword = consoleService.getInput();
            consoleService.userController.changePassword(consoleService.loginUser.getEmail(), newPassword);
            System.out.println("Пароль успешно изменен.");
        }
    }

    public void resetPassword() {
        System.out.println("После сброса, пароль станет стандартным:\npassword");
        System.out.println("Для подтверждения, введите команду reset:");
        String reset = consoleService.getInput();
        if (reset.equals("reset")) {
            consoleService.cleanConsole();
            consoleService.userController.resetPassword(consoleService.loginUser.getEmail());
        }
    }

    public void deleteAccount() {
        System.out.println("Подтвердите удаление аккаунта");
        System.out.println("Введите команду delete: ");
        String delete = consoleService.getInput();
        if (delete.equals("delete")) {
            consoleService.cleanConsole();
            consoleService.userController.deleteUser(consoleService.loginUser.getId());
            consoleService.loginUser = null;
        }
    }
}
