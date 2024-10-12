package api.service;

import api.model.Habit;

import java.util.Date;
import java.util.List;

public class StaticticServiceImpl implements StaticticService {
    HabitService habitService;

    public StaticticServiceImpl(HabitService habitService) {
        this.habitService = habitService;
    }

    public String generateStatistics(long user_id, int days) {
        List<Habit> habits = habitService.getHabitsByUserId(user_id);
        if (habits == null || habits.size() == 0) {
            return "У вас нет привычек";
        }
        StringBuilder result = new StringBuilder("Статистика за " + days + " дней:\n\n");
        long startLongStatistics = new Date().getTime() - (days * 86400000L);
        for(Habit habit : habits) {
            List<Date> dates = habit.getHistory().stream().filter(date -> date.getTime() >= startLongStatistics).toList();
            result.append(habit.getName()).append(": ").append(dates.size()).append(" успешных выполнений. \nДата следующего выполнения: ");
            if (habit.getNextExecutionDate() != null) {
                result.append(habit.getNextExecutionDate().toString());
            } else {
                result.append(habit.getNotNextExecutionDate().toString());
            }
            result.append(". \nСтрайк: ").append(habit.getStreak())
                    .append(". \nПропущено дней для следующего выполнения: ")
                    .append(habit.checkMissedDays())
                    .append(". \nСтатус: ")
                    .append(status(habit));
            if (dates != null && dates.size() > 0) {
                StringBuilder history = new StringBuilder();
                for(Date dateHistory : dates) {
                    history.append("\n").append(dateHistory.toString());
                }
                result.append("\nВыполнения : ").append(history);
            }
            result.append("\n\n");
        }
        return result.toString();
    }

    public String reportProgress(long user_id) {
        List<Habit> habits = habitService.getHabitsByUserId(user_id);
        if (habits == null || habits.size() == 0) {
            return "У вас нет привычек";
        }
        StringBuilder result = new StringBuilder("Прогресс по привычкам:\n\n");
        for(Habit habit : habits) {
            int procentDay = procent((new Date().getTime() - habit.getStartDate().getTime()),
                    (habit.getEndDate().getTime() - habit.getStartDate().getTime()));
            long successfully = (((new Date().getTime() - habit.getStartDate().getTime()) / 86400000) / habit.getFrequencyDay());
            int procentSuccessfully = 100;
            if (successfully > habit.getHistory().size()) {
                procentSuccessfully = procent(habit.getHistory().size(), successfully);
            }
            result.append(habit.getName())
                    .append(": \n")
                    .append("Процент выполнения: ")
                    .append(procentDay)
                    .append("\nПроцент успешных выполнений: ")
                    .append(procentSuccessfully).append("\n\n");
        }
        return result.toString();
    }

    private String status(Habit habit) {
        if(habit.completed()) {
            return "Выполняется";
        } else {
            return "Завершено";
        }
    }

    private int procent(long a, long b) {
        return (int) ((a * 100) / b);
    }
}
