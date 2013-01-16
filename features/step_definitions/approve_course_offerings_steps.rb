When /^I create two new Course Offerings$/ do
  go_to_create_course_offerings

  on(CreateCourseOffering).create_co_from_existing "20122", "CHEM142"
end

And /^I add Activity Offerings to the new Course Offerings$/ do
  @course_offering = make CourseOffering, :course=>"CHEM142A"
  @course_offering.manage

  on ManageCourseOfferings do |page|
    format = page.format.options[1].text
    page.add_ao format, 2
  end
end

And /^I approve the subject code for scheduling$/ do
  @course_offering = make CourseOffering, :course=>"CHEM142", :search_by_subj => true
  @course_offering.manage

  on(ManageCourseOfferingList).approve_subject_code_for_scheduling
end

When /^I manage a Course Offering$/ do
  @course_offering = make CourseOffering, :course=>"CHEM317"
  @course_offering.manage
end

And /^I add Activity Offerings to the Course Offering$/ do
  on ManageCourseOfferings do |page|
    format = page.format.options[1].text
    page.add_ao format, 2
  end
end

And /^I approve the Course Offering for scheduling$/ do
  pending # express the regexp above with the code you wish you had
end

And /^I approve selected Activity Offerings for scheduling$/ do
  on ManageCourseOfferings do |page|
    @new_ao_list = @course_offering.ao_list.to_set ^ page.codes_list.to_set
    page.select_aos(@new_ao_list)
    page.selected_offering_actions.select("Approve for Scheduling")
    page.go
  end
end

Then /^the Activity Offerings should be in Approved state$/ do
  on ManageCourseOfferings do |page|
    for code in @new_ao_list
      page.ao_status(code, "Approved").should == true
    end
  end
end

Then /^the selected Activity Offerings should be in Approved state$/ do
  on ManageCourseOfferings do |page|
    for code in @new_ao_list
      page.ao_status(code, "Approved").should == true
    end
  end
end