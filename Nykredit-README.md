# Parameter management platform

PMP is a library for creating, interacting and retrieving database persisted variables.

# Current implementation status

PMP is currently a WIP meaning an MVP is being created

# Documentation

[Jira board](https://features.tools.nykredit.it/secure/RapidBoard.jspa?rapidView=18638&projectKey=PMP&view=detail&selectedIssue=PMP-5)

[Confluence](https://wiki.tools.nykredit.it/pages/viewpage.action?pageId=316081961)


Table needed
---------------
```yaml
databaseChangeLog:
  - changeSet:
      id: 1
      author: nykredit
      changes:
        - createTable:
            tableName: PARAMETER_MANAGEMENT
            columns:
              - column:
                  name: ID
                  type: VARCHAR2(36)
                  constraints:
                    primaryKey: true
              - column:
                  name: NAME
                  type: VARCHAR2(255)
                  constraints:
                    unique: true
              - column:
                  name: TYPE
                  type: VARCHAR2(255)
              - column:
                  name: PVALUE
                  type: VARCHAR2(4000)