package dk.nykredit.pmp.core.remote.json.raw_types;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawCommit {
    private LocalDateTime pushDate;
    private String user;
    private String message;
    private List<String> affectedServices;

    private List<RawChange> changes;

    // public void setPushDate(String pushDate) {
    // this.pushDate = LocalDateTime.parse(pushDate);
    // }

    @Override
    public String toString() {
        return "RawCommit{" +
                "pushDate=" + pushDate +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", affectedServices=" + affectedServices +
                ", changes=" + changes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RawCommit))
            return false;

        RawCommit rawCommit = (RawCommit) o;

        if (pushDate != null ? !pushDate.equals(rawCommit.pushDate) : rawCommit.pushDate != null)
            return false;
        if (user != null ? !user.equals(rawCommit.user) : rawCommit.user != null)
            return false;
        if (message != null ? !message.equals(rawCommit.message) : rawCommit.message != null)
            return false;
        if (affectedServices != null ? !affectedServices.equals(rawCommit.affectedServices)
                : rawCommit.affectedServices != null)
            return false;
        return changes != null ? changes.equals(rawCommit.changes) : rawCommit.changes == null;
    }

    @Override
    public int hashCode() {
        int result = pushDate != null ? pushDate.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (affectedServices != null ? affectedServices.hashCode() : 0);
        result = 31 * result + (changes != null ? changes.hashCode() : 0);
        return result;
    }
}
