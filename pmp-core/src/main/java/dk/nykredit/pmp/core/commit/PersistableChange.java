package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeEntityFactory;

public interface PersistableChange extends Change {
	ChangeEntity toChangeEntity(ChangeEntityFactory changeEntityFactory);
}
