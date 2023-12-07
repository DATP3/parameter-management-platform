package dk.nykredit.pmp.core.audit_log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.Commit;
import dk.nykredit.pmp.core.remote.json.AuditLogEntrySerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "PMP_AUDIT_LOG")
@Getter
@Setter
@JsonSerialize(using = AuditLogEntrySerializer.class)
public class AuditLogEntry {
    @Id
    @Column(name = "COMMIT_ID")
    private long commitId;

    @OneToMany(mappedBy = "commit")
    private List<ChangeEntity> changes;

    @Column(name = "PUSH_DATE")
    private LocalDateTime pushDate;

    @Column(name = "AUTHOR")
    private String user;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "AFFECTED_SERVICES")
    private String affectedServices;

    public List<ChangeEntity> getChangeEntities() {
        return changes;
    }

    @Override
    public String toString() {
        return "AuditLogEntry{" +
                "commitId=" + commitId +
                ", changes=" + changes +
                ", pushDate=" + pushDate +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
