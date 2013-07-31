When /^I initiate a rollover by specifying source and target terms$/ do
  @rollover = make Rollover, :target_term => Rollover::ROLLOVER_TEST_TERM_TARGET , :source_term => Rollover::OPEN_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in open state$/ do
  @rollover = make Rollover, :source_term => Rollover::MAIN_TEST_TERM_SOURCE, :target_term => Rollover::MAIN_TEST_TERM_TARGET
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in open state$/ do
  @rollover = make Rollover, :source_term => Rollover::MAIN_TEST_TERM_SOURCE, :target_term => Rollover::MAIN_TEST_TERM_TARGET
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in default state EC/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::DRAFT_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in open state EC$/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::OPEN_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in final edits state EC$/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::FINAL_EDITS_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in published state for milestones testing$/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::PUBLISHED_MILESTONES_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in published state EC$/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::PUBLISHED_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in locked state EC$/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::LOCKED_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in closed state EC$/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::CLOSED_SOC_TERM
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in default state WC/ do
  @rollover = make Rollover, :source_term => "201205", :target_term => "201805"
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in open state WC$/ do
  @rollover = make Rollover, :source_term => "201205", :target_term => "201705"
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in final edits state WC$/ do
  @rollover = make Rollover, :source_term => "201205", :target_term => "201605"
  @rollover.perform_rollover
end

When /^I initiate a rollover to create a term in published state WC$/ do
  @rollover = make Rollover, :source_term => "201205", :target_term => "201505"
  @rollover.perform_rollover
end

When /^I approve the "(.*)" subject code for scheduling in the target term$/ do |subject_code|
  @course_offering = make CourseOffering, :term=>@rollover.target_term, :course=>subject_code
  @course_offering.approve_subject_code
  on ManageCourseOfferingList do |page|
    sleep 1
    page.course_offering_results_table.rows[2].cells[ManageCourseOfferingList::CO_STATUS_COLUMN].text.should == CourseOffering::PLANNED_STATUS
  end
end

And /^I manage SOC for the target term$/ do
  @manageSoc = make ManageSoc, :term_code =>@rollover.target_term
end

Then /^the results of the rollover are available$/ do
  @rollover.wait_for_rollover_to_complete
  #TODO validation
  #page.source_term.should ==
  #page.date_initiated.should ==
  #page.date_completed.should ==
  #page.rollover_duration.should ==
  #page.course_offerings_transitioned.should ==
  #page.course_offerings_exceptions.should ==
  #page.activity_offerings_transitioned.should ==
  #page.activity_offerings_exceptions.should ==
  #page.non_transitioned_courses_table.rows[1].cells[0].text #first exception
end

Then /^course offerings are copied to the target term$/ do
  #TODO validation
end

Then /^the rollover can be released to departments$/ do
  @rollover.release_to_depts
  #TODO validation
end

When /^I initiate a rollover to create a term for manage soc testing$/ do
  @rollover = make Rollover, :source_term => Rollover::SOC_STATES_SOURCE_TERM, :target_term => Rollover::MANAGE_SOC_TERM_TARGET
  @rollover.perform_rollover
end

When /^I am working on a term in "Open" SOC state$/ do
  @term_for_test = Rollover::OPEN_SOC_TERM
end

When /^I am working on a term in "Final Edits" SOC state$/ do
  @term_for_test = Rollover::FINAL_EDITS_SOC_TERM
end

When /^I am working on a term in "Published" SOC state$/ do
  @term_for_test = Rollover::PUBLISHED_SOC_TERM
end

When /^I am working on a term in "Published" SOC state for milestones testing$/ do
  @term_for_test = Rollover::PUBLISHED_MILESTONES_SOC_TERM
end

When /^I am working on a term in "Draft" SOC state$/ do
  @term_for_test = Rollover::DRAFT_SOC_TERM
end

When /^I am working on a term in "Locked" SOC state$/ do
  @term_for_test = Rollover::LOCKED_SOC_TERM
end

When /^I am working on a term in "Closed" SOC state$/ do
  @term_for_test = Rollover::CLOSED_SOC_TERM
end

And /^I setup a target term with those subterms setup$/ do
  @calendar_target = create AcademicCalendar, :year => @calendar.year.to_i + 1 #, :name => "TWj64w1q3e"
  @term_target = make AcademicTerm, :term_year => @calendar_target.year
  @calendar_target.add_term(@term_target)

  @subterm_list_target = Array.new(2)
  @subterm_list_target[0] = make AcademicTerm, :term_year => @calendar_target.year, :term_type=> "Half Fall 1", :parent_term=> "Fall Term", :subterm => true
  @calendar_target.add_term(@subterm_list_target[0])

  @subterm_list_target[1] = make AcademicTerm, :term_year => @calendar_target.year, :term_type=> "Half Fall 2", :parent_term=> "Fall Term", :subterm => true
  @calendar_target.add_term(@subterm_list_target[1])

  @subterm_list_target.each do |subterm|
    subterm.make_official
  end

  @term_target.set_up_soc
end

And /^I create the target Academic Term with subterms$/ do
  @calendar_target = create AcademicCalendar, :year => @calendar.year.to_i + 1 #, :name => "TWj64w1q3e"
  @term_target = make AcademicTerm, :term_year => @calendar_target.year
  @calendar_target.add_term(@term_target)

  #@subterm_list_target = Array.new(2)
  #@subterm_list_target[0] = make AcademicTerm, :term_year => @calendar_target.year, :term_type=> "Half Fall 1", :parent_term=> "Fall Term", :subterm => true
  #@calendar_target.add_term(@subterm_list_target[0])
  #
  #@subterm_list_target[1] = make AcademicTerm, :term_year => @calendar_target.year, :term_type=> "Half Fall 2", :parent_term=> "Fall Term", :subterm => true
  #@calendar_target.add_term(@subterm_list_target[1])
  #
  #@subterm_list_target.each do |subterm|
  #  subterm.make_official
  #end

  @term_target.set_up_soc
end

And /^I rollover the subterms' parent term to a target term with those subterms setup$/ do
  @calendar_target = create AcademicCalendar, :year => @calendar.year.to_i + 1 #,:name => "6aXt9C4nbM"
  @term_target = make AcademicTerm, :term_year => @calendar_target.year
  @calendar_target.add_term(@term_target)

  @subterm_list_target = Array.new(2)
  @subterm_list_target[0] = make AcademicTerm, :term_year => @calendar_target.year, :term_type=> "Half Fall 1", :parent_term=> "Fall Term", :subterm => true
  @calendar_target.add_term(@subterm_list_target[0])

  @subterm_list_target[1] = make AcademicTerm, :term_year => @calendar_target.year, :term_type=> "Half Fall 2", :parent_term=> "Fall Term", :subterm => true
  @calendar_target.add_term(@subterm_list_target[1])

  @subterm_list_target.each do |subterm|
    subterm.make_official
  end

  @term_target.set_up_soc

  @rollover = make Rollover, :target_term => @term_target.term_code , :source_term => @term.term_code
  @rollover.perform_rollover
  @rollover.wait_for_rollover_to_complete
  @rollover.release_to_depts
end


And /^I rollover the subterms' parent term to a target term with those subterms are NOT setup$/ do
  @calendar_target = create AcademicCalendar, :year => @calendar.year.to_i + 1 #, :name => "TWj64w1q3e"
  @term_target = make AcademicTerm, :term_year => @calendar_target.year
  @calendar_target.add_term(@term_target)
  @calendar_target.make_official
  @term_target.set_up_soc

  @rollover = make Rollover, :target_term => @term_target.term_code ,
                   :source_term => @term.term_code,
                   :exp_success => false
  @rollover.perform_rollover
end

Then /^there is a target term error message on the rollover page stating: (.*)$/ do |expected_msg|
  on PerformRollover do |page|
    page.target_term_first_validation_msg.should match /.*#{Regexp.escape(expected_msg)}.*/
  end
end


Then /^I approve the Course Offering for scheduling in the target term$/ do
  @course_offering_target = make CourseOffering, :term=> @term_target.term_code,
                                 :course => @course_offering.course
  @course_offering_target.search_by_subjectcode
  #on ManageCourseOfferings do |page|
  #    #if page.list_all_course_link.exists? then
  #      page.list_all_course_link.click
  #      page.loading.wait_until_present
  #    #end
  #end
  @course_offering_target.approve_co
end

Then /^I advance the SOC state from open to published state$/ do
  @manageSoc = make ManageSoc, :term_code =>@rollover.target_term, :co_code=> @course_offering_target.course
  @manageSoc.search
  @manageSoc.change_action "Lock"
  @manageSoc.check_state_change_button_exists "Schedule"
  @manageSoc.change_action "Schedule"
  @manageSoc.check_state_change_button_exists "FinalEdit"
  @manageSoc.change_action "FinalEdit"
  @manageSoc.check_state_change_button_exists "Publish"
  @manageSoc.change_action "Publish"
  @manageSoc.check_state_change_button_exists "Close"
  #@manageSoc.verify_publish_state_changes
end

Then /^the Course Offering is in offered state$/ do
  #@course_offering_target = make CourseOffering, :course => "CHEM132TUSNA", :term => "213108"
  @course_offering_target.manage

  on ManageCourseOfferings do |page|
    page.list_all_course_link.click
    page.loading.wait_while_present
  end

  on ManageCourseOfferingList do |page|
    page.co_status(@course_offering_target.course).should == "Offered"
  end

end

Then /^the Activity Offerings are assigned to the target subterms$/ do
  @course_offering_target = make CourseOffering, :course => @course_offering.course, :term => @term_target.term_code
  @course_offering_target.manage

  @activity_offering_target = make ActivityOffering, :code => @activity_offering.code, :parent_course_offering => @course_offering_target
  on ManageCourseOfferings do |page|
    page.has_subterm_icon(@activity_offering_target.code).should == true
    page.view_activity_offering(@activity_offering_target.code)
  end

  on ActivityOfferingInquiry do |page|
    page.subterm.should == @activity_offering.subterm
    page.close
  end

  @activity_offering_target.edit
  on ActivityOfferingMaintenance do |page|
    page.subterm.should == @activity_offering.subterm
    page.cancel
  end

  @activity_offering_target2 = make ActivityOffering, :code => @activity_offering2.code, :parent_course_offering => @course_offering_target
  on ManageCourseOfferings do |page|
    page.has_subterm_icon(@activity_offering_target2.code).should == true
    page.view_activity_offering(@activity_offering_target2.code)
  end

  on ActivityOfferingInquiry do |page|
    page.subterm.should == @activity_offering2.subterm
    page.close
  end

  @activity_offering_target2.edit
  on ActivityOfferingMaintenance do |page|
    page.subterm.should == @activity_offering2.subterm
    page.cancel
  end

end
