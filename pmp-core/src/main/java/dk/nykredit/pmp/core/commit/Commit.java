package dk.nykredit.pmp.core.commit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dk.nykredit.pmp.core.commit.exception.CommitException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commit {
    private LocalDateTime pushDate;
    private String user;
    private String message;
    private List<String> affectedServices;
    private long commitHash;

    private List<Change> changes;

    private List<PersistableChange> appliedChanges;

    // Uses command pattern to apply changes
    public void apply(CommitDirector commitDirector) throws CommitException {
        appliedChanges = new ArrayList<>(changes.size());

        for (Change change : changes) {
            try {
                appliedChanges.addAll(change.apply(commitDirector));
            } catch (CommitException e) {
                undoChanges(appliedChanges, commitDirector);
                appliedChanges.clear();
                throw e;
            }
        }
    }

    private void undoChanges(List<PersistableChange> changes, CommitDirector commitDirector) {
        for (Change change : changes) {
            change.undo(commitDirector);
        }
    }

    public void undoChanges(CommitDirector commitDirector) {
        undoChanges(appliedChanges, commitDirector);
    }

    @Override
    public String toString() {
        return "Commit{" +
                "pushDate=" + pushDate +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", changes=" + changes.stream().map(Objects::toString).collect(Collectors.joining(",")) +
                '}';
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.getCommitHash());
    }

    public long getCommitHash() {
        return pushDate.hashCode()
                + user.hashCode()
                + message.hashCode()
                + changes.stream().mapToInt(Change::hashCode).sum();

    }

    public List<Change> getChanges() {
        return changes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Commit)) {
            return false;
        }

        Commit other = (Commit) obj;

        return pushDate.equals(other.pushDate)
                && user.equals(other.user)
                && message.equals(other.message)
                && changes.equals(other.changes);
    }

}
