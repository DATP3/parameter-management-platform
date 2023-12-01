package dk.nykredit.pmp.core.commit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.service.ParameterService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commit {
    private LocalDateTime pushDate;
    private String user;
    private String message;

    private List<Change> changes;

    // Uses command pattern to apply changes
    public void apply(ParameterService parameterService) throws CommitException {
        List<Change> appliedChanges = new ArrayList<>(changes.size());

        for (Change change : changes) {
            try {
                change.apply(parameterService);
                appliedChanges.add(change);
            } catch (CommitException e) {
                undoChanges(appliedChanges, parameterService);
                throw e;
            }
        }
    }

    private void undoChanges(List<Change> changes, ParameterService parameterService) {
        for (Change change : changes) {
            change.undo(parameterService);
        }
    }

	public void undoChanges(ParameterService parameterService) {
		undoChanges(changes, parameterService);
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
