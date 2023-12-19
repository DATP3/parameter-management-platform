package dk.nykredit.pmp.core.util;

import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;

public interface ChangeVisitor {
    /**
     * Validates that the service of the {@link ParameterChange} is the same as the
     * service the library is running on as indicated by the {@link ServiceInfo}. If
     * the {@link ParameterChange} is valid, it will be added to the
     * {@link validatedChanges} list.
     * 
     * @param change The change to be validated.
     */
    public void visit(ParameterChange change);

    /**
     * Validates that the service of the {@link ParameterRevert} is the same as the
     * service the library is running on as indicated by the {@link ServiceInfo}. If
     * the {@link ParameterRevert} is valid, it will be added to the
     * {@link validatedChanges} list.
     * 
     * @param change The change to be validated.
     */
    public void visit(CommitRevert change);

    /**
     * Checks if the {@link CommitRevert} is valid by checking if its
     * commitHash refrences a commit in the {@link auditlog}. Adds change to
     * {@link validatedChanges} if it is valid.
     * 
     * @param change The change to be validated.
     */
    public void visit(ParameterRevert change);
}
