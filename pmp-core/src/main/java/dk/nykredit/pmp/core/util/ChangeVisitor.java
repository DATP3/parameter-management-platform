package dk.nykredit.pmp.core.util;

import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;

public interface ChangeVisitor {
    public void visit(ParameterChange change);
    public void visit(CommitRevert change);
    public void visit(ParameterRevert change);
}
