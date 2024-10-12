package api;

import api.service.console.ConsoleService;

public class Api {
    public static void main(String[] args) {
        // Инициализация сервиса работы с консолью
        ConsoleService consoleService = new ConsoleService();

        // Запуск работы с консолью
        consoleService.startAdminUser();
        consoleService.start();
    }
}