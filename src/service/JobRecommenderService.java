package service;

import model.Job;
import model.Match;
import model.User;

import java.util.*;
import java.util.stream.Collectors;

public class JobRecommenderService {
    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Job> jobs = new LinkedHashMap<>();

    public boolean addUser(String name, List<String> skills, int experience) {
        if (users.containsKey(name)) {
            return false;
        }
        users.put(name, new User(name, skills, experience));
        return true;
    }

    public boolean addJob(String title, String company, List<String> tags, int requiredExperience) {
        if (jobs.containsKey(title)) {
            return false;
        }
        jobs.put(title, new Job(title, company, tags, requiredExperience));
        return true;
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public Collection<Job> getJobs() {
        return jobs.values();
    }

    public User getUser(String name) {
        return users.get(name);
    }

    public List<Job> suggestJobs(String username, int limit) {
        User user = users.get(username);
        if (user == null) {
            return Collections.emptyList();
        }

        return jobs.values().stream()
                .map(job -> new Match(user, job, job.calculateMatchScore(user)))
                .filter(match -> match.getScore() > 0)
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .map(Match::getJob)
                .collect(Collectors.toList());
    }

    public List<Job> getJobsWithMinExperience(int minExp) {
        return jobs.values().stream()
                .filter(job -> job.getRequiredExperience() >= minExp)
                .sorted(Comparator.comparing(Job::getTitle))
                .collect(Collectors.toList());
    }

    public List<User> getUsersWithMinMatches(int minMatches) {
        return users.values().stream()
                .filter(user -> {
                    long totalMatches = jobs.values().stream()
                            .mapToInt(job -> job.calculateMatchScore(user))
                            .sum();
                    return totalMatches >= minMatches;
                })
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    public List<String> getTopSkills(int limit) {
        return users.values().stream()
                .flatMap(user -> user.getSkills().stream())
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> {
                    int cmp = Long.compare(b.getValue(), a.getValue());
                    if (cmp != 0) return cmp;
                    return a.getKey().compareTo(b.getKey());
                })
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
