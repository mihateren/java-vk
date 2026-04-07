import cli.CommandParser;
import service.FileService;
import service.JobRecommenderService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JobRecommenderService service = new JobRecommenderService();
        FileService fileService = new FileService();
        CommandParser parser = new CommandParser(service);

        // Load and execute history commands (only entity-creating ones)
        for (String cmd : fileService.getHistory()) {
            if (cmd.startsWith("user ") || cmd.startsWith("job ")) {
                parser.execute(cmd);
            }
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            // Save command to history (except exit)
            if (!input.trim().equals("exit")) {
                fileService.saveCommand(input);
            }

            String result = parser.execute(input);

            if ("EXIT_COMMAND".equals(result)) {
                System.exit(0);
            }

            if ("HISTORY_COMMAND".equals(result)) {
                for (String cmd : fileService.getHistory()) {
                    System.out.println(cmd);
                }
            } else if (!result.isEmpty()) {
                System.out.print(result);
            }
        }
    }
}
