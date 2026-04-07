package model;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Job {
    private final String title;
    private final String company;
    private final Set<String> tags;
    private final int requiredExperience;

    public Job(String title, String company, List<String> tags, int requiredExperience) {
        this.title = title;
        this.company = company;
        this.tags = new TreeSet<>(tags);
        this.requiredExperience = requiredExperience;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public int getRequiredExperience() {
        return requiredExperience;
    }

    public int calculateMatchScore(User user) {
        int matchingSkills = user.countMatchingSkills(tags);
        if (user.getExperience() < requiredExperience) {
            return matchingSkills / 2;
        }
        return matchingSkills;
    }

    @Override
    public String toString() {
        return title + " at " + company;
    }
}
