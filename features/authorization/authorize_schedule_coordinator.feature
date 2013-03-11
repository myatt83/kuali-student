@nightly
Feature: Schedule Coordinator Authorization

  Background:
    Given I am logged in as a Schedule Coordinator

  Scenario: AZ 5.1C As a Schedule Coordinator I have access to delete Activity Offerings in a Final Edits state
    Given I am working on a term in "Final Edits" SOC state
    Then I have access to delete an activity offering in a "Draft" state
    And  I have access to delete an activity offering in a "Approved" state

  Scenario: AZ 5.1C As a Schedule Coordinator I have access to delete Activity Offerings in a Published state
    Given I am working on a term in "Published" SOC state before the first day of class
    And there is a "Draft" course present
    Then I have access to delete an activity offering in a "Draft" state

  Scenario: AZ 5.1C As a Schedule Coordinator I have access to delete Activity Offerings in a Open state
    Given I am working on a term in "Open" SOC state
    Then I have access to delete an activity offering in a "Draft" state
    And  I have access to delete an activity offering in a "Approved" state

  Scenario: AZ 5.1D As a Schedule Coordinator I have access to delete Course Offerings in a Open state
    Given I am working on a term in "Open" SOC state
    And there is a "Planned" course present
    Then I have access to delete a course offering in a "Draft" state
    And I have access to delete a course offering in a "Planned" state

  Scenario: AZ 6.1 As a Schedule Coordinator I have access to Activity Offerings for a term in Draft SOC state
    Given I am in working on a term in "Draft" SOC state
    When I manage a course offering
    Then I have access to view the activity offering details
    And the next, previous and list all course offering links are enabled
    And I have access to add a new activity offering
    And I have access to delete an activity offering
    And I have access to edit an activity offering
    And I have access to copy activity offering

  Scenario: AZ 6.1 As a Schedule Coordinator I have access to Course Offerings for a term in Draft SOC state (CO list view)
    Given I am in working on a term in "Draft" SOC state
    When I manage course offerings for a subject code
    Then I have access to view course offering details
    And I have access to add new course offerings
    And I have access to approve course offerings for scheduling
    And I have access to delete course offerings
    And I have access to edit course offerings
    And I have access to copy course offerings

  Scenario: AZ 6.1 As a Schedule Coordinator I have access to Course Offerings for a term in Draft SOC state (manage CO view)
    Given I am in working on a term in "Draft" SOC state
    When I manage a course offering
    Then I have access to view the course offering details
    And I have access to delete the course offering
    And I have access to edit the course offering


  Scenario: AZ 6.2 Schedule Coordinator has access to edit course offering grading option for a term with SOC State Open
    Given I am working on a term in "Open" SOC state
    When I edit a course offering in my department
    Then I have access to edit the grading options

  Scenario: AZ 6.2 Schedule Coordinator has access to edit course offering grading option for a term with SOC State Final Edits
    Given I am working on a term in "Final Edits" SOC state
    When I edit a course offering in my department
    Then I have access to edit the grading options

  Scenario: AZ 6.2 Schedule Coordinator has access to edit course offering grading option for a term with SOC State Published
    Given I am working on a term in "Published" SOC state before the first day of class
    When I edit a course offering in my department
    Then I have access to edit the grading options