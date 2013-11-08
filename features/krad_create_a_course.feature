Feature: Create a Course Propsoal in Krad

  Background:
    Given I am logged in as admin
    Given I create a course proposal in krad

  Scenario: Create a course proposal in krad
    Then I should see data in the proposal title on course information
    And I should see data in the course title on course information

  Scenario: Create a course proposal in krad with only required fields
    Given I complete require fields on the course proposal
    Then I should see data in required fields for the course proposal

  Scenario: Create a course proposal in krad with all fields completed
    Given I complete require fields on the course proposal
    And I complete non-required fields on the course proposal
    Then I should see data in required fields for the course proposal
    And I should see data in all non required fields for the course proposal
