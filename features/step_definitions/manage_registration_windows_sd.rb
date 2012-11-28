When /^I manage Registration Windows for a term and a period$/ do
  go_to_manage_reg_windows
  on RegistrationWindowsTermLookup do |page|
    page.term_type.select 'Spring Term'
    page.year.set '2012'
    page.search
    #page.loading.wait_while_present
  end
  on RegistrationWindowsPeriodLookup do |page|
    page.period_id.select 'All Registration Periods for this Term'
    page.show
  end
end

Then /^I verify that all Registration Window fields are present$/ do
  on RegistrationWindowsCreate do |page|
    page.validate_fields
  end
end

Then /^I select One Slot per Window from Slot Allocation Method dropdown$/ do
  on RegistrationWindowsCreate do |page|
    page.window_type_key.select RegistrationWindowsConstants::METHOD_ONE_SLOT_PER_WINDOW
    sleep(5)
  end
end

Then /^I verify that no One Slot per Window field is present$/ do
  on RegistrationWindowsCreate do |page|
    page.validate_dynamic_fields(RegistrationWindowsConstants::METHOD_ONE_SLOT_PER_WINDOW).should be_true
  end
end

Then /^I select Max Slotted Window from Slot Allocation Method dropdown$/ do
  on RegistrationWindowsCreate do |page|
    page.window_type_key.select RegistrationWindowsConstants::METHOD_MAX_SLOTTED_WINDOW
    sleep(5)
  end
end

Then /^I verify that Max Slotted Window fields are visible$/ do
  on RegistrationWindowsCreate do |page|
    page.validate_dynamic_fields(RegistrationWindowsConstants::METHOD_MAX_SLOTTED_WINDOW).should be_true
  end
end

Then /^I select Uniform Slotted Window from Slot Allocation Method dropdown$/ do
  on RegistrationWindowsCreate do |page|
    page.window_type_key.select RegistrationWindowsConstants::METHOD_UNIFORM_SLOTTED_WINDOW
    sleep(5)
  end
end

Then /^I verify that Uniform Slotted Window fields are visible$/ do
  on RegistrationWindowsCreate do |page|
    page.validate_dynamic_fields(RegistrationWindowsConstants::METHOD_UNIFORM_SLOTTED_WINDOW).should be_true
  end
end

And /^I verify that the Registration Window is deleted/ do
  on RegistrationWindowsCreate do |page|
    page.is_window_deleted(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_true
  end
end

And /^I verify that the Registration Window is not deleted/ do
  on RegistrationWindowsCreate do |page|
    page.is_window_deleted(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_false
  end
end

And /^I verify that the registration window is not created$/ do
  on RegistrationWindowsCreate do |page|
    page.is_window_created(@registrationWindow.appointment_window_info_name, period_key = @registrationWindow.period_key).should be_false
  end
end

And /^I verify that the Registration Window is created$/ do
  on RegistrationWindowsCreate do |page|
    page.is_window_created(@registrationWindow.appointment_window_info_name, period_key = @registrationWindow.period_key).should be_true
  end
end

Then /^I verify that no field is editable in Registration Window and the Window Name is a link to a popup$/ do
  on RegistrationWindowsCreate do |page|
    page.are_window_fields_editable(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_false
    page.is_anchor(window_name, period_key).should be_true
  end
end

Then /^I verify that all editable fields in Registration Window are editable and Window Name is not a link$/ do
  on RegistrationWindowsCreate do |page|
    page.are_editable_window_fields_editable(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_true
    page.is_anchor(window_name, period_key).should be_false
  end
end

Then /^I verify the new Registration Window's read-only and editable fields$/ do
  on RegistrationWindowsCreate do |page|
    page.are_editable_window_fields_editable(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_true
    page.are_non_editable_window_fields_editable(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_false
  end
end

Then /^I verify the Registration Window is unique within the same period$/ do
  on RegistrationWindowsCreate do |page|
    page.is_window_name_unique(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_true
  end
end

Then /^I verify each Registration Window is unique within each period/ do
  on RegistrationWindowsCreate do |page|
    page.is_window_name_unique(@registrationWindow.appointment_window_info_name, 'Spring Registration Period 1').should be_true
    page.is_window_name_unique(@registrationWindow.appointment_window_info_name, 'Spring Registration Period 2').should be_true
  end
end

Then /^verify error exists for the registration page/ do
  on RegistrationWindowsCreate do |page|
    page.exists_error_message.should be_true
  end
end

Then /^I verify that the Registration Window is not modified$/ do
  on RegistrationWindowsCreate do |page|
    row_object = page.get_row_object(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key)
    row_object[:start_date].should == @registrationWindow.start_date
    row_object[:start_time].should == @registrationWindow.start_time
    row_object[:start_time_am_pm].should == @registrationWindow.start_time_am_pm
    row_object[:end_date].should == @registrationWindow.end_date
    row_object[:end_time].should == @registrationWindow.end_time
    row_object[:end_time_am_pm].should == @registrationWindow.end_time_am_pm
  end
end

Then /^I verify the new Registration Window's buttons are created$/ do
  puts "checking if the buttons are created for the Registration Window #{@registrationWindow.appointment_window_info_name}"
  on RegistrationWindowsCreate do |page|
    page.does_window_contain_elements(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key).should be_true
  end
end

When /^Successfully add a Registration Window for the period$/ do
  @registrationWindow = make RegistrationWindow
  @registrationWindow.add
end

When /^I add a Registration Window with Start Date falling out of the period dates$/ do
  @registrationWindow = make RegistrationWindow, :start_date => RegistrationWindowsConstants::DATE_BEFORE
  @registrationWindow.add
end

When /^I add a Registration Window with End Date falling out of the period dates$/ do
  @registrationWindow = make RegistrationWindow, :end_date => RegistrationWindowsConstants::DATE_BEFORE
  @registrationWindow.add
end

When /^I add a Registration Window with Start Date after the End Date$/ do
  @registrationWindow = make RegistrationWindow, :start_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE, :end_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE
  @registrationWindow.add
end

When /^I add a Registration Window with the same Start Date and End Date whose End Time is before the Start Time$/ do
  @registrationWindow = make RegistrationWindow, :start_time => '10:00', :end_time => '09:00', :end_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE
  @registrationWindow.add
end

When /^I add a Registration Window with the same Start Date and End Date whose End Time is in AM and its Start Time is in PM$/ do
  @registrationWindow = make RegistrationWindow, :start_time_am_pm => 'pm', :end_time_ap_pm => 'am', :end_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE
  @registrationWindow.add
end

When /^I add two Registration Windows with the same name for the same Period$/ do
  @registrationWindow = make RegistrationWindow
  @registrationWindow.add
  @registrationWindow = make RegistrationWindow, :appointment_window_info_name => @registrationWindow.appointment_window_info_name
  @registrationWindow.add
end

When /^I add two Registration Windows with the same name in two different Periods$/ do
  @registrationWindow = make RegistrationWindow, :period_key => 'Spring Registration Period 1'
  @registrationWindow.add
  @registrationWindow = make RegistrationWindow, :appointment_window_info_name => @registrationWindow.appointment_window_info_name, :period_key => 'Spring Registration Period 2'
  @registrationWindow.add
end

When /^I edit a Registration Window setting its Start Date outside the period dates$/ do
  @registrationWindow.edit_registration_window :start_date => RegistrationWindowsConstants::DATE_BEFORE
end

When /^I edit a Registration Window setting its End Date outside the period dates$/ do
  @registrationWindow.edit_registration_window :end_date => RegistrationWindowsConstants::DATE_AFTER
end

When /^I edit a Registration Window setting its Start Date after its End Date/ do
  @registrationWindow.edit_registration_window :start_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE, :end_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE
end

When /^I edit a Registration Window with the same Start Date and End Date setting its Start Time after its End Time$/ do
  @registrationWindow.edit_registration_window :end_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE, :start_time => '10:00', :end_time => '09:00'
end

When /^I edit a Registration Window with the same Start Date and End Date setting its End Time in AM and its Start Time in PM$/ do
  @registrationWindow.edit_registration_window :end_date => RegistrationWindowsConstants::DATE_WITHIN_REVERSE, :start_time_am_pm => 'pm', :end_time_ap_pm => 'am'
end

When /^I delete the Registration Window$/ do
  puts "Deleting Registration Window #{@registrationWindow.appointment_window_info_name}"
  on RegistrationWindowsCreate do |page|
    page.remove(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key)
    page.loading.wait_while_present
    sleep(5)
    page.confirm_delete
  end
end

When /^I try deleting of the Registration Window but I cancel the delete$/ do
  puts "Deleting Registration Window #{@registrationWindow.appointment_window_info_name}, #{@registrationWindow.period_key}"
  on RegistrationWindowsCreate do |page|
    page.remove(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key)
    page.loading.wait_while_present
    sleep(5)
    page.cancel_delete
  end
end

When /^I assign Student Appointments in Registration Window$/ do
  on RegistrationWindowsCreate do |page|
    page.assign_students(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key)
  end
end

When /^I break Student Appointments in Registration Window$/ do
  on RegistrationWindowsCreate do |page|
    page.break_appointments(@registrationWindow.appointment_window_info_name, @registrationWindow.period_key)
  end
end