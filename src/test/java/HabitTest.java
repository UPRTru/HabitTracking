import api.controller.HabitController;
import api.controller.UserController;
import api.model.Habit;
import api.repository.HabitRepositoryLocal;
import api.repository.UserRepositoryLocal;
import api.service.HabitServiceImpl;
import api.service.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HabitTest {
    private static final String PATH = "src/main/resources/";
    private static final String DB_FILE = PATH + "habits.xml";
    HabitController habitController = new HabitController(new HabitServiceImpl(new HabitRepositoryLocal()));
    UserController userController = new UserController(new UserServiceImpl(new UserRepositoryLocal()));

    @Test
    public void test() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(DB_FILE)))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<habits>");
            writer.println("</habits>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userController.addUser("test@test.com", "test", "test", "test");

        habitController.createHabit(0, "test", "test", 1, 1);

        List<Habit> habits = habitController.getHabitsByUserId(0);

        assertEquals(1, habits.size());
        assertEquals("test", habits.get(0).getName());

        habitController.updateHabit(0, 0, "test2", "test2", 1, 1);
        habits = habitController.getHabitsByUserId(0);
        assertEquals("test2", habits.get(0).getName());
        assertNull(habits.get(0).getNextExecutionDate());
        assertEquals(0, habits.get(0).getStreak());

        habitController.executeHabit(0, 0);
        habits = habitController.getHabitsByUserId(0);
        assertEquals(1, habits.get(0).getStreak());
        assertTrue((habits.get(0).getNextExecutionDate() != null));

        habits = habitController.getHabitsByUserIdAndFilter(0, "date");
        assertEquals(1, habits.size());
        habits = habitController.getHabitsByUserIdAndFilter(0, "active");
        assertEquals(1, habits.size());
        habits = habitController.getHabitsByUserIdAndFilter(0, "done");
        assertEquals(0, habits.size());
        habits = habitController.getHabitsByUserIdAndFilter(0, "missed");
        assertEquals(0, habits.size());

        String generateStatistics = habitController.generateStatistics(0, 1);
        assertTrue(generateStatistics.startsWith("Статистика за 1 дней:\n" +
                "\n" +
                "test2:"));

        String reportProgress = habitController.reportProgress(0);
        assertTrue(reportProgress.startsWith("Прогресс по привычкам:\n" +
                "\n" +
                "test2:"));

        habitController.removeHabit(0, 0);
        habits = habitController.getHabitsByUserId(0);
        assertEquals(0, habits.size());
    }
}
