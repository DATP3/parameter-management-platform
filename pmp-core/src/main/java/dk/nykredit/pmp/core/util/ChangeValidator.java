package dk.nykredit.pmp.core.util;

import java.util.List;

import dk.nykredit.pmp.core.commit.Change;

public interface ChangeValidator extends ChangeVisitor {
    public List<Change> getValidatedChanges();
}
