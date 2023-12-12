package dk.nykredit.pmp.core.audit_log;

public class RevertPartChangeEntityFactory {
    public ChangeEntity createChangeEntity(ChangeEntity originalChangeEntity, ChangeType revertType, long commitRef) {
        ChangeEntity resultingChangeEntity = new ChangeEntity();
        resultingChangeEntity.setCommitRevertRef(commitRef);
        // The client currently always recieves a logged revert as a COMMIT_REVERT. It should be changed to the correct type.
        // Reading this from from the input arguments causes the client to recieve an empty list of reverts.
        // This seems like an error in the "objectMapper.writeValue(res.getWriter(), response);" in LogServlet.java
        // as the response object does include the correct revert type, yet the client recieves an empty list of reverts.
        // Example of the response object at the bottom of this file.
        resultingChangeEntity.setChangeType(ChangeType.COMMIT_REVERT);
        resultingChangeEntity.setParameterName(originalChangeEntity.getParameterName());
        resultingChangeEntity.setParameterType(originalChangeEntity.getParameterType());

        // Swap the old and new values
        resultingChangeEntity.setOldValue(originalChangeEntity.getNewValue());
        resultingChangeEntity.setNewValue(originalChangeEntity.getOldValue());

        return resultingChangeEntity;
    }

}

/*
 * {
    "commits": [
        {
        "user": "DO52ZM@student.aau.dk",
        "pushDate": "2023-12-10T17:49:26.204",
        "hash": "ffffffff9e176cc4",
        "message": "change",
        "affectedServices": [
            "Example service"
        ],
        "changes": {
            "parameterChanges": [
            {
                "name": "max_retries",
                "newValue": "55",
                "oldValue": "5"
            },
            {
                "name": "person",
                "newValue": "Test Person a",
                "oldValue": "Test Person"
            }
            ],
            "reverts": []
        }
        },
        {
        "user": "DO52ZM@student.aau.dk",
        "pushDate": "2023-12-10T17:50:06.86",
        "hash": "ffffffff9ec4d690",
        "message": "revert person alteration in change",
        "affectedServices": [
            "Example service"
        ],
        "changes": {
            "parameterChanges": [],
            "reverts": [
            {}
            ]
        }
        }
    ],
    "name": "Example service"
    }

    Error on line 63. The reverts object is empty.
 */