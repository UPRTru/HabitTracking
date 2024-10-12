package api.service;

import api.model.Habit;
import api.repository.HabitRepositoryLocal;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HabitServiceImpl implements HabitService {
    private final HabitRepositoryLocal habitRepository;

    public HabitServiceImpl(HabitRepositoryLocal habitRepository) {
        this.habitRepository = habitRepository;
    }
    
    public void addHabit(long user_id, String name, String description, int frequencyDay, int duration) {
        if (!name.isEmpty() && frequencyDay > 0 && duration > 0) {
            try {
                habitRepository.addHabit(new Habit(habitRepository.getNewHabitId(), user_id, name, description, frequencyDay, duration));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Недостаточно данных для создания привычки");
        }
    }

    public void updateHabit(long user_id, long id, String name, String description, int frequencyDay, int duration) {
        try {
            Habit habit = habitRepository.findHabitById(id);
            if (habit != null) {
                if (habit.getUser_id() == user_id) {
                    if (!name.isEmpty()) {
                        habit.setName(name);
                    }
                    if (!description.isEmpty()) {
                        habit.setDescription(description);
                    }
                    if (frequencyDay > 0) {
                        habit.setFrequencyDay(frequencyDay);
                    }
                    if (duration > 0) {
                        habit.setDuration(duration);
                    }
                    habitRepository.updateHabit(habit);
                } else {
                    System.out.println("У вас нет доступа к этой привычке");
                }
            } else {
                System.out.println("Привычка не найдена");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeHabit(long user_id, long id) {
        try {
            Habit habit = habitRepository.findHabitById(id);
            if (habit != null) {
                if (habit.getUser_id() == user_id) {
                    habitRepository.removeHabit(id);
                } else {
                    System.out.println("У вас нет доступа к этой привычке");
                }
            } else {
                System.out.println("Привычка не найдена");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Habit findHabitById(long id) {
        return null;
    }

    public List<Habit> getHabitsByUserIdAndFilter(long user_id, String filter) {
        try {
            List<Habit> habits = habitRepository.getHabitsByUserId(user_id);
            if (habits != null && habits.size() > 0) {
                return switch (filter) {
                    case "date" ->
                            habits.stream().sorted(Comparator.comparing(Habit::getStartDate)).collect(Collectors.toList());
                    case "active" ->
                            habits.stream().filter(Habit::completed).collect(Collectors.toList());
                    case "done" ->
                            habits.stream().filter(habit -> !habit.completed()).collect(Collectors.toList());
                    case "missed" ->
                            habits.stream().filter(habit -> habit.completed() && habit.checkMissedDays() > 0).collect(Collectors.toList());
                    default -> List.of();
                };
            } else {
                return List.of();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public List<Habit> getHabitsByUserId(long user_id) {
        try {
            return habitRepository.getHabitsByUserId(user_id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public void executeHabit(long user_id, long id) {
        try {
            Habit habit = habitRepository.findHabitById(id);
            if (habit != null) {
                if (habit.getUser_id() == user_id) {
                    habit.executed();
                    habitRepository.updateHabit(habit);
                    System.out.println(habit.getName() + " успешные выполнения подряд: " + habit.getStreak());
                } else {
                    System.out.println("У вас нет доступа к этой привычке");
                }
            } else {
                System.out.println("Привычка не найдена");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
