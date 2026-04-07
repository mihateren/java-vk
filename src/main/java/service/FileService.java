package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class FileService {
    private static final String HISTORY_FILE = "commands.log";
    private final Path historyPath;

    public FileService() {
        this.historyPath = Path.of(HISTORY_FILE);
    }

    public void saveCommand(String command) {
        try {
            Files.writeString(historyPath, command + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            // Ignore file write errors
        }
    }

    public List<String> getHistory() {
        if (!Files.exists(historyPath)) {
            return Collections.emptyList();
        }
        try {
            return Files.readAllLines(historyPath);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
