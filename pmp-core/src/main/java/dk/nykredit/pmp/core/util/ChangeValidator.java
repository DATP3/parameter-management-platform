package dk.nykredit.pmp.core.util;

public interface ChangeValidator extends ChangeVisitor{
    List<Change> getValidatedChanges();
}
