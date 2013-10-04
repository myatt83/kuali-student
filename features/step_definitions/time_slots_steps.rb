When /^I add 2 different time slots to a single term type$/ do
  @time_slots = create TimeSlots
  @time_slots.add_new_time_slot( make TimeSlots::TimeSlot )
  @time_slots.add_new_time_slot( make TimeSlots::TimeSlot, :start_time => "11:00", :start_time_am_pm => "PM", :end_time => "11:50", :end_time_am_pm => "PM" )
end

When /^I show time slots for two different term types$/ do
  @time_slots = create TimeSlots, :term_types => [ "Fall - Full", "Spring - Full" ]
end


When /^I add a single time slot per 2 different term types$/ do
  @time_slots = create TimeSlots, :term_types => [ "Fall - Full", "Spring - Full" ]
end

Then /^the timeslots are saved$/ do
  @time_slots.show_time_slots
  @time_slots.new_time_slots.each do |time_slot|
    on TimeSlotMaintenance do |page|
      row = page.target_results_row time_slot.code
      row[TimeSlotMaintenance::TIME_SLOT_RESULTS_CODE].text == time_slot.code
      row[TimeSlotMaintenance::TIME_SLOT_RESULTS_TERM_TYPE].text == time_slot.term_type
      row[TimeSlotMaintenance::TIME_SLOT_RESULTS_DAYS].text == time_slot.days
      row[TimeSlotMaintenance::TIME_SLOT_RESULTS_START_TIME].text.split(' ')[0] == time_slot.start_time
      row[TimeSlotMaintenance::TIME_SLOT_RESULTS_START_TIME].text.split(' ')[1] == time_slot.start_time_am_pm
      row[TimeSlotMaintenance::TIME_SLOT_RESULTS_END_TIME].text.split(' ')[0] == time_slot.end_time
      row[TimeSlotMaintenance::TIME_SLOT_RESULTS_END_TIME].text.split(' ')[1] == time_slot.end_time_am_pm
    end
  end
end



###############################################################
###
### DRAFT REMNANTS -- DELETE ONCE DEV IS COMPLETE!
###
###############################################################

When /^I test some timeslot stuff$/ do


#  time = Time.now
#  puts time
#  puts time.hour
#  puts time.min
#
#  time = time + 60
#  puts time
#
#  time = Time.gm(2000,1,1,23,59,59)
#  puts time
#
#  time = time + 60
#  puts time


  @time_slots = create TimeSlots
  on TimeSlotMaintenance do |page|
    result = page.generate_unused_start_and_end_times
    puts "RESULT -> #{result}"
  end


end



When /^I target a row in the timeslots result table then I can get it's code$/ do

  on TimeSlotMaintenance do |page|
    row = page.target_results_row("A")
    code = row[1].text
    puts 'found code -> ' + code
  end

end

When /^I? ?specify Term Type (.*)$/  do  |termType|
  pending
end

When /^I? ?specify Term Types (.*) and (.*)$/ do |termType1, termType2|
  pending
end

Then /^only time slots of Term Type (.*) appear.?$/ do |termType|
  pending
end

When /^I? ?add Time Slot with Term Type (.*), Days (M|T|W|H|F|S|U), Start time (\d+:\d+ ?.*), End time (\d+:\d+ ?.*)$/ do |termType, days, startTime, endTime|
  pending
end

When /^I? ?(.*) ?edit the (.*) ?Time Slot added above to be (.*), Days (M|T|W|H|F|S|U|blank), Start time (\d+:\d+ ?.*|blank), End time (\d+:\d+ ?.*|blank)$/ do |attempt, tsOrdinality, termType, days, startTime, endTime|
  pending
end

Then /^the changes are saved.?$/ do
  pending
end

When /^I? ?attempt to (.*) the Time Slot added above$/ do |action|
  pending
end

Then /^the (.*) ?Time Slot (is|is not) deleted.?$/  do |tsOrdinality, isIsNot|
  pending
end

When /^I? ?add an RDL using the (.*) ?Time Slot added above$/ do |tsOrdinality|
  pending
end

When /^I? ?attempt to delete both the Time Slots added above in the same action$/ do
  pending
end

Then /^(a|an error) message is displayed on the time slot page.?$/ do |msgType|
  pending
end

Then /^the Time Slot edits (are|are not) saved.$/ do |areAreNot|
  pending
end