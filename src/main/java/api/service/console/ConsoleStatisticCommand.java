package api.service.console;

public class ConsoleStatisticCommand {
    ConsoleService consoleService;

    public ConsoleStatisticCommand(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    public void generateStatistics() {
        System.out.println("Сгенерировать статистику: ");
        System.out.println("Введите число дней, за которые вы хотите сгенерировать статистику: ");
        int days = Integer.parseInt(consoleService.getInput());
        System.out.println(consoleService.habitController.generateStatistics(consoleService.loginUser.getId(), days));
    }

    public void reportProgress() {
        System.out.println(consoleService.habitController.reportProgress(consoleService.loginUser.getId()));
    }
}
