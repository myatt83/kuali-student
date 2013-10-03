Given /^I create a Course Offering from catalog using the default waitlists option \(enabled\)$/ do
  @course_offering = create CourseOffering, :course => "ENGL300", :waitlists => true


end

Given /^I create a Course Offering from catalog using the waitlists option disabled$/ do
  @course_offering = create CourseOffering, :course => "ENGL300", :waitlists => false
end

Given /^I manage an activity offering with waitlists enabled$/ do
  @term = make AcademicTerm, :term_code => Rollover::MAIN_TEST_TERM_TARGET if @term.nil?

  @course_offering = create CourseOffering, :term => @term.term_code, :course => "ENGL300", :waitlists => true
  @activity_offering = create ActivityOffering, :parent_course_offering => @course_offering
  @activity_offering.save
end

Given /^I manage an activity offering with the limit waitlist size set$/ do
  @course_offering = create CourseOffering, :course => "ENGL300", :waitlists => true
  waitlist_config = make Waitlist, :limit_size => 30
  @activity_offering = create ActivityOffering, :parent_course_offering => @course_offering, :waitlist_config => waitlist_config
  @activity_offering.save

  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_max_size.should == "Limit to 30"
    page.close
  end
end

Given /^I manage an activity offering with waitlists processing type set to (.*)$/ do |processing_type|
  @course_offering = create CourseOffering, :course => "ENGL300", :waitlists => true
  waitlist_config = make Waitlist, :type => processing_type
  @activity_offering = create ActivityOffering, :parent_course_offering => @course_offering, :waitlist_config => waitlist_config
  @activity_offering.save

  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_processing.should == processing_type
    page.close
  end
end

Given /^I can update the processing type to (.*)$/ do |processing_type|
  waitlist = @activity_offering.waitlist_config
  waitlist.type = processing_type
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save

  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_processing.should == processing_type
    page.close
  end
end

Then /^I make changes to the default waitlist configuration$/ do
  waitlist = @activity_offering.waitlist_config
  waitlist.type = "Manual"
  waitlist.limit_size = 10
  waitlist.allow_hold_list = true
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save
end

Then /^I set the limit waitlist size$/ do
  waitlist = @activity_offering.waitlist_config
  waitlist.limit_size = 25
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save
end

Then /^I remove the limit waitlist size$/ do
  waitlist = @activity_offering.waitlist_config
  waitlist.limit_size = 0
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save
end

Then /^I modify the limit waitlist size$/ do
  waitlist = @activity_offering.waitlist_config
  waitlist.limit_size = 50
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save
end

Then /^I enable the allow holds list option$/ do
  waitlist = @activity_offering.waitlist_config
  waitlist.allow_hold_list = true
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save
end

Then /^I disable the allow holds list option$/ do
  waitlist = @activity_offering.waitlist_config
  waitlist.allow_hold_list = false
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save
end

Given /^I add two activity offerings$/ do
  @activity_offering = create ActivityOffering, :parent_course_offering => @course_offering
  @activity_offering.save
  @activity_offering2 = create ActivityOffering, :parent_course_offering => @course_offering
  @activity_offering2.save
end

Given /^I add an activity offering$/ do
  @activity_offering = create ActivityOffering, :parent_course_offering => @course_offering
  @activity_offering.save
end

Given /^the activity offerings have active waitlists and waitlists have the default configuration$/ do
  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_active?.should be_true
    page.waitlists_allow_holds?.should be_false
    page.waitlists_processing.should == "Confirmation"
    page.waitlists_max_size.should == "Unlimited"
    page.close
  end

  on(ManageCourseOfferings).view_activity_offering(@activity_offering2.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_active?.should be_true
    page.waitlists_allow_holds?.should be_false
    page.waitlists_processing.should == "Confirmation"
    page.waitlists_max_size.should == "Unlimited"
    page.close
  end

  @activity_offering.edit
  on ActivityOfferingMaintenance do |page|
    page.waitlist_checkbox.set?.should be_true
    page.waitlist_confirmation_radio.set?.should be_true
    page.waitlist_limit_checkbox.set?.should be_false
    page.waitlist_allow_hold_checkbox.set?.should be_false
    page.cancel
  end

  @activity_offering2.edit
  on ActivityOfferingMaintenance do |page|
    page.waitlist_checkbox.set?.should be_true
    page.waitlist_confirmation_radio.set?.should be_true
    page.waitlist_limit_checkbox.set?.should be_false
    page.waitlist_allow_hold_checkbox.set?.should be_false
    page.cancel
  end

end

Given /^the waitlist option cannot be enabled for the activity offering$/ do
  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_active?.should be_false
    page.close
  end

  @activity_offering.edit
  on ActivityOfferingMaintenance do |page|
    page.no_waitlists_msg.should == "Waitlists are deactivated for the Course Offering"
    page.cancel
  end
end

Given /^the limit waitlist size is successfully updated$/ do
  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_active?.should be_true
    page.waitlists_max_size.should == "Limit to #{@activity_offering.waitlist_config.limit_size}"
    page.close
  end
end

Given /^the allow holds list option is successfully updated$/ do
  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_active?.should be_true
    page.waitlists_allow_holds?.should == @activity_offering.waitlist_config.allow_hold_list
    page.close
  end
end

Given /^I re-enable the waitlists option for the activity offering the modified waitlist configuration is restored$/ do
  @activity_offering.edit

  on ActivityOfferingMaintenance do |page|
    page.waitlist_checkbox.set
    page.waitlist_processing_type.should == @activity_offering.waitlist_config.type
    page.waitlist_limit_checkbox.set?.should == @activity_offering.waitlist_config.limit_size > 0
    page.waitlist_limit.value.to_i.should == @activity_offering.waitlist_config.limit_size
    page.waitlist_allow_hold_checkbox.set?.should == @activity_offering.waitlist_config.allow_hold_list
    page.cancel
  end
end

Given /^I (?:can )?disable the waitlists option for the activity offering$/ do
  waitlist = @activity_offering.waitlist_config
  waitlist.enabled = false
  @activity_offering.edit :waitlist_config => waitlist
  @activity_offering.save

  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_active?.should be_false
    page.close
  end
end

Given /^the limit waitlist size is successfully updated to unlimited$/ do
  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_max_size.should == "Unlimited"
    page.close
  end
end

Given /^I manage an activity offering with waitlists the allow holds list option is enabled$/ do
  @course_offering = create CourseOffering, :course => "ENGL300", :waitlists => true
  waitlist_config = make Waitlist, :allow_hold_list => true
  @activity_offering = create ActivityOffering, :parent_course_offering => @course_offering, :waitlist_config => waitlist_config
  @activity_offering.save

  on(ManageCourseOfferings).view_activity_offering(@activity_offering.code)

  on ActivityOfferingInquiry do |page|
    page.waitlists_allow_holds?.should == true
    page.close
  end
end
