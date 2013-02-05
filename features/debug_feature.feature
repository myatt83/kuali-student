@draft
Feature: debug feature

debug feature


  Scenario: Debug
	I am logged in as a Schedule Coordinator
    When I manage registration groups for a course offering


 Scenario: clean up aos
   I am logged in as a Schedule Coordinator
   Then I cleanup AOs


	#When I setup the debug object
  Scenario: Successfully display schedule of classes by subject code and display individual course details
    I am logged in as a Schedule Coordinator
    And I am using the schedule of classes page
    When I search for course offerings by course by entering a subject code
    Then a list of course offerings with that subject code is displayed
    And the course offering details for all offerings can be shown


  Scenario Outline: Schedule of classes - expands details for all course offerings in specified subject areas
    I am logged in as a Schedule Coordinator
    And I am using the schedule of classes page
    When I search for course offerings by course by entering a subject code: <Subject>
    Then a list of course offerings with that subject code is displayed
    And the course offering details for all offerings can be shown
  Examples:
  | Subject |
  | BSCI |
  | CHEM |
  | ENGL |
  | HIST |
  | PHYS |
  | WMST |
