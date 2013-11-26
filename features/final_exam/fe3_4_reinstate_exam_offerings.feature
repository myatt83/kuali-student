@nightly @blue_team
Feature: SA.FE3-4 Reinstate Activity Offering will reinstate the Exam Offerings as well
  FE 3.4 As a Central Adminstrator I want to reinstate exam offerings if I reinstate the governing course offering
  or activity offering bulk creation so that exam offerings are available

  Background:
    Given I am logged in as admin

  #FE3.4.EB1 (KSENROLL-10322)
  Scenario: Test that a suspended AO does have an EO after it has been reinstated
    Given I suspend an Activity Offering for a CO with a standard final exam driven by Activity Offering
    When I reinstate the activity offering
    And I view the Exam Offerings for the Course Offering
    Then the Exam Offerings for each Activity Offering in the EO for AO table should be in a Draft state

  #FE3.4.EB2 (KSENROLL-10322)
  Scenario: Test that a canceled AO does have an EO after it has been reinstated
    Given I cancel an Activity Offering for a CO with a standard final exam driven by Activity Offering
    When I reinstate the activity offering
    And I view the Exam Offerings for the Course Offering
    Then the Exam Offerings for each Activity Offering in the EO for AO table should be in a Draft state
