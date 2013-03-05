Then /^I have access delete an activity offering in a "([^"]*)" state$/ do |aostate|
  if @newCO
    @course_offering = make CourseOffering, :term=> @term_for_test, :course => @newCO
  else
    @course_offering = make CourseOffering, :term=> @term_for_test, :course => "CHEM611"
  end
  @course_offering.manage
  @course_offering.attempt_ao_delete_by_status(aostate).should == true
end

Then /^I have access to delete a course offering in a "([^"]*)" state$/ do |costate|
  @course_offering.search_by_subjectcode
  @course_offering.attempt_co_delete_by_status(costate).should == true
end