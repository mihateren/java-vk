package model;

public class Match {
    private final User user;
    private final Job job;
    private final int score;

    public Match(User user, Job job, int score) {
        this.user = user;
        this.job = job;
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public Job getJob() {
        return job;
    }

    public int getScore() {
        return score;
    }
}
