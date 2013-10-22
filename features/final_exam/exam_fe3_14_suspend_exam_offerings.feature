#@nightly
Feature: SA.FE3-14 Suspending Course Offering will suspend the Exam Offerings as well
  FE 3.14: As a Central Administrator I want exam offerings to be Suspended if the governing course offering or
  activity offering is suspended after the bulk creation so that unnecessary exam offerings are removed

  Background:
    Given I am logged in as admin

  #FE3.14.EB1 (KSENROLL-10324)
  @pending
  Scenario: Test that suspending an AO does suspend the EOs when the FE Driver is set to CO
    When I suspend an Activity Offering for a CO with a standard final exam driven by Course Offering
    And I view the Exam Offerings for the Course Offering
    Then the Exam Offerings for Course Offering should be in a Suspended state

  #FE3.14.EB2 (KSENROLL-10324)
  @pending
  Scenario: Test that suspending an AO does suspend the EO for that AO when the FE Driver is set to AO
    When I suspend an Activity Offering for a CO with a standard final exam driven by Activity Offering
    And I view the Exam Offerings for the Course Offering
    Then the 1 Exam Offering for Activity Offering should be in a Suspended state