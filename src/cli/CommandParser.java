package cli;

import service.JobRecommenderService;

import java.util.Arrays;
import java.util.List;

public class CommandParser {
    private final JobRecommenderService service;

    public CommandParser(JobRecommenderService service) {
        this.service = service;
    }

    public String execute(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String[] parts = input.trim().split("\\s+");
        String command = parts[0];

        return switch (command) {
            case "user" -> handleUser(parts);
            case "user-list" -> handleUserList();
            case "job" -> handleJob(parts);
            case "job-list" -> handleJobList();
            case "suggest" -> handleSuggest(parts);
            case "stat" -> handleStat(parts);
            case "history" -> "HISTORY_COMMAND";
            case "exit" -> "EXIT_COMMAND";
            default -> "";
        };
    }

    private String handleUser(String[] parts) {
        if (parts.length < 4) {
            return "";
        }
        String name = parts[1];
        String skillsStr = findArg(parts, "--skills=");
        String expStr = findArg(parts, "--exp=");

        if (skillsStr == null || expStr == null) {
            return "";
        }

        List<String> skills = Arrays.stream(skillsStr.split(","))
                .filter(s -> !s.isEmpty())
                .toList();
        int exp = Integer.parseInt(expStr);

        service.addUser(name, skills, exp);
        return "";
    }

    private String handleUserList() {
        StringBuilder sb = new StringBuilder();
        for (var user : service.getUsers()) {
            sb.append(user).append("\n");
        }
        return sb.toString();
    }

    private String handleJob(String[] parts) {
        if (parts.length < 4) {
            return "";
        }
        String title = parts[1];
        String company = findArg(parts, "--company=");
        String tagsStr = findArg(parts, "--tags=");
        String expStr = findArg(parts, "--exp=");

        if (company == null || tagsStr == null || expStr == null) {
            return "";
        }

        List<String> tags = Arrays.stream(tagsStr.split(","))
                .filter(s -> !s.isEmpty())
                .toList();
        int exp = Integer.parseInt(expStr);

        service.addJob(title, company, tags, exp);
        return "";
    }

    private String handleJobList() {
        StringBuilder sb = new StringBuilder();
        for (var job : service.getJobs()) {
            sb.append(job).append("\n");
        }
        return sb.toString();
    }

    private String handleSuggest(String[] parts) {
        if (parts.length < 2) {
            return "";
        }
        String username = parts[1];
        StringBuilder sb = new StringBuilder();
        for (var job : service.suggestJobs(username, 2)) {
            sb.append(job).append("\n");
        }
        return sb.toString();
    }

    private String handleStat(String[] parts) {
        if (parts.length < 3) {
            return "";
        }

        String option = parts[1];
        int value = Integer.parseInt(parts[2]);

        StringBuilder sb = new StringBuilder();

        if ("--exp".equals(option)) {
            for (var job : service.getJobsWithMinExperience(value)) {
                sb.append(job).append("\n");
            }
        } else if ("--match".equals(option)) {
            for (var user : service.getUsersWithMinMatches(value)) {
                sb.append(user).append("\n");
            }
        } else if ("--top-skills".equals(option)) {
            for (var skill : service.getTopSkills(value)) {
                sb.append(skill).append("\n");
            }
        }

        return sb.toString();
    }

    private String findArg(String[] parts, String prefix) {
        for (String part : parts) {
            if (part.startsWith(prefix)) {
                return part.substring(prefix.length());
            }
        }
        return null;
    }
}
