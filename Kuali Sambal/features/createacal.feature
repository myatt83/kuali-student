Feature: Create ACal
  Scenario: Create and save academic calendar from blank
    Given I am logged in as admin
    When I create a new Academic Calendar
    Then I should be able to save it, and the Make Official button should become active

  Scenario: Search for newly created academic calendar
    Given I create a new Academic Calendar
    And I save the new calendar
    When I search for the Academic Calendar
    Then it should appear in search results