package dk.nykredit.pmp.core.commit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.inject.Inject;

import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.util.ChangeVisitor;
import dk.nykredit.pmp.core.util.ServiceInfoProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commit {

    private LocalDateTime pushDate;
    private String user;
    private String message;
    private List<String> affectedServices;

    private List<Change> changes;

    @JsonIgnore
    private List<Change> validatedChanges;
    @JsonIgnore
    private List<PersistableChange> appliedChanges;

    // Uses command pattern to apply changes
    public void apply(CommitDirector commitDirector) throws CommitException {
        if (!(validatedChanges.size() > 0)) {
            throw new OldValueInconsistentException("No changes to apply");
        }

        appliedChanges = new ArrayList<>(changes.size());

        for (Change change : validatedChanges) {

            try {
                appliedChanges.addAll(change.apply(commitDirector));
            } catch (CommitException e) {
                undoChanges(appliedChanges, commitDirector);
                appliedChanges.clear();
                throw e;
            }
        }
    }

    public void validateChanges(ChangeVisitor changeValidator) {

        validatedChanges = new ArrayList<>(changes.size());

        for (Change change : changes) {
            change.acceptVisitor(changeValidator);
        }

        validatedChanges.addAll(changeValidator.getValidatedChanges());
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
