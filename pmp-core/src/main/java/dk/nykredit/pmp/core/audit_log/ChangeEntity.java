package dk.nykredit.pmp.core.audit_log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dk.nykredit.pmp.core.remote.json.ChangeEntitySerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PMP_PARAM_CHANGES")
@Getter
@Setter
@JsonSerialize(using = ChangeEntitySerializer.class)
public class ChangeEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "COMMIT_ID")
    private AuditLogEntry commit;

    @Column(name = "PARAMETER_NAME")
    private String parameterName;

    @Column(name = "PARAMETER_TYPE")
    private String parameterType;

    @Column(name = "OLD_VALUE")
    private String oldValue;

    @Column(name = "NEW_VALUE")
    private String newValue;

    @Column(name = "CHANGE_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private ChangeType changeType;

    @Column(name = "COMMIT_REVERT_REF")
    private long commitRevertRef;

    @Override
    public String toString() {
        return "ChangeEntity{" +
                "id=" + id +
                ", commit=" + commit.getCommitId() +
                ", parameterName='" + parameterName + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", changeType=" + changeType +
                ", commitRevertRef=" + commitRevertRef +
                '}';
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) + Long.hashCode(commit.getCommitId()) + Long.hashCode(commitRevertRef)
                + parameterName.hashCode() + parameterType.hashCode() + oldValue.hashCode() + newValue.hashCode()
                + changeType.hashCode() + Long.hashCode(commitRevertRef);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChangeEntity)) {
            return false;
        }

        ChangeEntity other = (ChangeEntity) obj;
        return (Long.hashCode(id) == Long.hashCode(other.id) && Long.hashCode(commit.getCommitId()) == Long
                .hashCode(other.commit.getCommitId()) && Long.hashCode(commitRevertRef) == Long
                        .hashCode(other.commitRevertRef)
                && parameterName.equals(other.parameterName)
                && parameterType.equals(other.parameterType) && oldValue.equals(other.oldValue)
                && newValue.equals(other.newValue) && changeType.equals(other.changeType)
                && Long.hashCode(commitRevertRef) == Long.hashCode(other.commitRevertRef));
    }
}
