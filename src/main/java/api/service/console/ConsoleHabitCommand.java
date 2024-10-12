package api.service.console;

import api.model.Habit;

import java.util.List;
import java.util.Objects;

public class ConsoleHabitCommand {
    ConsoleService consoleService;

    public ConsoleHabitCommand(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    public void createHabit() {
        System.out.println("Введите название: ");
        String name = consoleService.getInput();
        System.out.println("Введите описание: ");
        String description = consoleService.getInput();
        System.out.println("Введите частоту выполнения в днях: ");
        int frequencyDay = Integer.parseInt(consoleService.getInput());
        System.out.println("Введите длительность: ");
        int duration = Integer.parseInt(consoleService.getInput());
        consoleService.habitController.createHabit(consoleService.loginUser.getId(), name, description, frequencyDay, duration);
    }

    public void editHabit() {
        System.out.println("Введите ID: ");
        long id = Long.parseLong(consoleService.getInput());
        System.out.println("Оставляйте пустыми поля, если не хотите изменить");
        System.out.println("Введите название: ");
        String name = consoleService.getInput();
        System.out.println("Введите описание: ");
        String description = consoleService.getInput();
        System.out.println("Введите частоту выполнения в днях: ");
        int frequencyDay;
        String input = consoleService.getInput();
        if (!input.isEmpty()) {
            frequencyDay = Integer.parseInt(input);
        } else {
            frequencyDay = 0;
        }
        System.out.println("Введите длительность: ");
        int duration;
        String durationInput = consoleService.getInput();
        if (!durationInput.isEmpty()) {
            duration = Integer.parseInt(durationInput);
        } else {
            duration = 0;
        }
        consoleService.habitController.updateHabit(consoleService.loginUser.getId(), id, name, description, frequencyDay, duration);
    }

    public void deleteHabit() {
        System.out.println("Введите ID: ");
        long id = Long.parseLong(consoleService.getInput());
        consoleService.habitController.removeHabit(consoleService.loginUser.getId(), id);
    }

    public void viewHabits() {
        List<Habit> habits;
        System.out.println("Фильрация списка привычек: ");
        System.out.println("Введите команду: ");
        System.out.println("normal - по умолчанию");
        System.out.println("date - по дате");
        System.out.println("active - еще в работе");
        System.out.println("done - выполненные");
        System.out.println("missed - пропущенные");
        String filter = consoleService.getInput();
        if (Objects.equals(filter, "normal")) {
            System.out.println("Список привычек: ");
            habits = consoleService.habitController.getHabitsByUserId(consoleService.loginUser.getId());
        } else {
            System.out.println("Список привычек: ");
            habits = consoleService.habitController.getHabitsByUserIdAndFilter(consoleService.loginUser.getId(), filter);
        }
        if (habits.size() > 0) {
            for (Habit habit : habits) {
                System.out.println(habit.toString());
            }
        } else {
            System.out.println("У вас нет привычек");
        }
    }

    public void executeHabit() {
        System.out.println("Введите ID привычки: ");
        long id = Long.parseLong(consoleService.getInput());
        consoleService.habitController.executeHabit(consoleService.loginUser.getId(), id);
    }
}
