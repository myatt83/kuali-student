@draft @red_team

Feature: REG.Registration Date Check

  CR 12.1 - As an admin I want to allow students to add courses to their cart before registration begins
            so they can prepare their transactions

  CR 12.2 - As an admin, I want students to have the ability to perform registration transactions
            on courses whose deadlines are different from the standard term

  CR 16.1 and CR 17.1 - As an admin I want to prevent a student from accessing the registration functions
                        for a term because registration isn’t open

  Background:
    Given I am using a mobile screen size

#KSENROLL-13255
  Scenario: CR 12.1 Verify pre-registration period allows access to cart
    Given I log in to student registration as studentx
    When I attempt to display my registration cart during pre-registration
    Then I can view my registration cart
    And I can add and remove courses from my cart

