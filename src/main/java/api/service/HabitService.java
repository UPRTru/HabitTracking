package api.service;

import api.model.Habit;

import java.util.List;

public interface HabitService {

    void addHabit(long user_id, String name, String description, int frequencyDay, int duration);

    void updateHabit(long user_id, long id, String name, String description, int frequencyDay, int duration);

    void removeHabit(long user_id, long id);

    Habit findHabitById(long id);

    List<Habit> getHabitsByUserIdAndFilter(long user_id, String filter);

    List<Habit> getHabitsByUserId(long user_id);

    void executeHabit(long user_id, long id);
}
