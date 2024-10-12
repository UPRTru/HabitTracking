package api.controller;

import api.model.Habit;
import api.service.HabitService;
import api.service.StaticticService;
import api.service.StaticticServiceImpl;

import java.util.List;

public class HabitController {
    HabitService habitService;
    StaticticService staticticService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
        this.staticticService = new StaticticServiceImpl(habitService);
    }

    public void createHabit(long user_id, String name, String description, int frequencyDay, int duration) {
        habitService.addHabit(user_id, name, description, frequencyDay, duration);
    }

    public void updateHabit(long user_id, long id, String name, String description, int frequencyDay, int duration) {
        habitService.updateHabit(user_id, id, name, description, frequencyDay, duration);
    }

    public void removeHabit(long user_id, long id) {
        habitService.removeHabit(user_id, id);
    }

    public List<Habit> getHabitsByUserId(long user_id) {
        return habitService.getHabitsByUserId(user_id);
    }

    public List<Habit> getHabitsByUserIdAndFilter(long user_id, String filter) {
        return habitService.getHabitsByUserIdAndFilter(user_id, filter);
    }

    public void executeHabit(long user_id, long id) {
        habitService.executeHabit(user_id, id);
    }

    public String generateStatistics(long user_id, int days) {
        return staticticService.generateStatistics(user_id, days);
    }

    public String reportProgress(long user_id) {
        return staticticService.reportProgress(user_id);
    }
}
