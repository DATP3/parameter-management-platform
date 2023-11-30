package dk.nykredit.pmp.core.commit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.commit.exception.StoredValueNullException;
import dk.nykredit.pmp.core.commit.exception.TypeInconsistentException;
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
    public void apply(ParameterService parameterService) {
        List<Change> appliedChanges = new ArrayList<>(changes.size());

        for (Change change : changes) {
            try {
                change.apply(parameterService);
                appliedChanges.add(change);
            } catch (TypeInconsistentException e) {
                undoChanges(appliedChanges, parameterService);
                throw e;
            } catch (OldValueInconsistentException e) {
                undoChanges(appliedChanges, parameterService);
                throw e;
            } catch (StoredValueNullException e) {
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

    @Override
    public String toString() {
        return "Commit{" +
                "pushDate=" + pushDate +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", changes=" + changes.stream().map(Objects::toString).collect(Collectors.joining(",")) +
                '}';
    }
}
