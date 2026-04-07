package model;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class User {
    private final String name;
    private final Set<String> skills;
    private final int experience;

    public User(String name, List<String> skills, int experience) {
        this.name = name;
        this.skills = new TreeSet<>(skills);
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public Set<String> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public int getExperience() {
        return experience;
    }

    public int countMatchingSkills(Set<String> jobTags) {
        return (int) skills.stream()
                .filter(jobTags::contains)
                .count();
    }

    @Override
    public String toString() {
        return name + " " + String.join(",", skills) + " " + experience;
    }
}
