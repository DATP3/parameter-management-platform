package dk.nykredit.pmp.core.commit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dk.nykredit.pmp.core.audit_log.ChangeEntity;
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
    private Long commitHash;

    private List<Change> changes;

    @JsonIgnore
    private List<ChangeEntity> appliedChanges;

    public Commit() {
        changes = new ArrayList<>();
        affectedServices = new ArrayList<>();
    }

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

    private void undoChanges(List<ChangeEntity> changes, CommitDirector commitDirector) {
        for (ChangeEntity change : changes) {
            commitDirector.getParameterService().updateParameter(change.getParameterName(), change.getOldValue());
        }
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

    public void setCommitHash(long commitHash) {
        this.commitHash = commitHash;
    }

    public void setCommitHash(Long commitHash) {
        if (commitHash != null) {
            this.commitHash = commitHash;
        }
    }

    public long getCommitHash() {
        // Commit hash should preferably be calculated from the raw commit before
        // creating this, but as commits may come from other sources in the future, we
        // calculate it here otherwise. Calculating here cannot take changes to other
        // services into account
        if (commitHash == null) {
            commitHash = pushDate.hashCode()
                    + user.hashCode()
                    + message.hashCode()
                    + changes.stream().mapToLong(Change::hashCode).sum();
        }

        return commitHash;

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
