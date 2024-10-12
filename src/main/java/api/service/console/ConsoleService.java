package api.service.console;

import api.controller.HabitController;
import api.controller.UserController;
import api.model.Role;
import api.model.dto.UserDto;
import api.repository.HabitRepositoryLocal;
import api.repository.UserRepositoryLocal;
import api.service.HabitServiceImpl;
import api.service.NotificationService;
import api.service.UserServiceImpl;

import java.util.Scanner;

public class ConsoleService {
    public final UserController userController = new UserController(new UserServiceImpl(new UserRepositoryLocal()));
    public final HabitController habitController = new HabitController(new HabitServiceImpl(new HabitRepositoryLocal()));
    public UserDto loginUser = null;
    boolean newMenu = true;
    Scanner scanner = new Scanner(System.in);
    ConsoleUserCommand consoleUserCommand = new ConsoleUserCommand(this);
    ConsoleAdminCommand consoleAdminCommand = new ConsoleAdminCommand(this);
    ConsoleHabitCommand consoleHabitCommand = new ConsoleHabitCommand(this);
    ConsoleStatisticCommand consoleStatisticCommand = new ConsoleStatisticCommand(this);
    NotificationService notificationService = new NotificationService();

    public void startAdminUser() {
        consoleUserCommand.startAdminUser();
    }
    public void start() {
        try {
            while (true) {
                System.out.println(" =====> ");
                if (newMenu) {
                    if (loginUser != null) {
                        notificationService.sendNotificationConsole(habitController.getHabitsByUserId(loginUser.getId()));
                        System.out.println(" =====> ");
                    }
                    startMenu();
                } else {
                    menu();
                }
                System.out.println("Введите команду:");
                String command = getInput();

                switch (command) {
                    case "register":
                        if (loginUser == null) {
                            cleanConsole();
                            consoleUserCommand.registerUser();
                            break;
                        }
                    case "login":
                        if (loginUser == null) {
                            cleanConsole();
                            consoleUserCommand.loginUser();
                            break;
                        }
                    case "my_profile":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleUserCommand.myProfile();
                            break;
                        }
                    case "edit_profile":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleUserCommand.editProfile();
                            break;
                        }
                    case "delete_account":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleUserCommand.deleteAccount();
                            break;
                        }
                    case "change_password":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleUserCommand.changePassword();
                            break;
                        }
                    case "reset_password":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleUserCommand.resetPassword();
                            break;
                        }
                    case "create_habit":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleHabitCommand.createHabit();
                            break;
                        }
                    case "edit_habit":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleHabitCommand.editHabit();
                            break;
                        }
                    case "delete_habit":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleHabitCommand.deleteHabit();
                            break;
                        }
                    case "view_habits":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleHabitCommand.viewHabits();
                            break;
                        }
                    case "execute_habit":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleHabitCommand.executeHabit();
                            break;
                        }
                    case "generate_statistics":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleStatisticCommand.generateStatistics();
                            break;
                        }
                    case "report_progress":
                        if (loginUser != null) {
                            cleanConsole();
                            consoleStatisticCommand.reportProgress();
                            break;
                        }
                    case "list_users":
                        if (loginUser != null && Role.checkAdmin(loginUser)) {
                            cleanConsole();
                            consoleAdminCommand.listAllUsers();
                            break;
                        }
                    case "block_user":
                        if (loginUser != null && Role.checkAdmin(loginUser)) {
                            cleanConsole();
                            consoleAdminCommand.blockUser();
                            break;
                        }
                    case "unblock_user":
                        if (loginUser != null && Role.checkAdmin(loginUser)) {
                            cleanConsole();
                            consoleAdminCommand.unblockUser();
                            break;
                        }
                    case "delete_user":
                        if (loginUser != null && Role.checkAdmin(loginUser)) {
                            cleanConsole();
                            consoleAdminCommand.deleteUser();
                            break;
                        }
                    case "admin_role":
                        if (loginUser != null && Role.checkAdmin(loginUser)) {
                            cleanConsole();
                            consoleAdminCommand.roleAdmin();
                            break;
                        }
                    case "user_role":
                        if (loginUser != null && Role.checkAdmin(loginUser)) {
                            cleanConsole();
                            consoleAdminCommand.roleUser();
                            break;
                        }
                    case "exit":
                        if (loginUser != null) {
                            cleanConsole();
                            loginUser = null;
                        }
                        break;
                    default:
                        cleanConsole();
                        System.out.println("Неизвестная команда.");
                        break;
                }
                newMenu = loginUser == null;
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            start();
        }
    }

    private void startMenu() {
        System.out.println("login - Вход в систему");
        System.out.println("register - Регистрация");
    }

    private void menu() {
        if (loginUser != null) {
            System.out.println("my_profile - Вывести данные пользователя");
            System.out.println("edit_profile - Редактировать профиль");
            System.out.println("delete_account - Удалить аккаунт");
            System.out.println("change_password - Изменить пароль");
            System.out.println("reset_password - Сбросить пароль");
            System.out.println("create_habit - Создать привычку");
            System.out.println("edit_habit - Изменить привычку");
            System.out.println("delete_habit - Удалить привычку");
            System.out.println("view_habits - Просмотреть привычки");
            System.out.println("execute_habit - Отметить выполнение привычки");
            System.out.println("generate_statistics - Сгенерировать статистику");
            System.out.println("report_progress - Сообщить о прогрессе");
            if (Role.checkAdmin(loginUser)) {
                System.out.println("list_users - Показать список пользователей");
                System.out.println("block_user - Заблокировать пользовате");
                System.out.println("unblock_user - Разблокировать пользовате");
                System.out.println("delete_user - Удалить пользовате");
                System.out.println("admin_role - сделать пользователя админом");
                System.out.println("user_role - сделать пользователя обычным");
            }
        }
        System.out.println("exit - Выход");
    }

    public void cleanConsole() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    public String getInput() {
        return scanner.nextLine().trim();
    }
}
