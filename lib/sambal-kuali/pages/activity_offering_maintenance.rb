class ActivityOfferingMaintenance < ActivityOfferingMaintenanceBase

  expected_title /Kuali :: Edit Activity Offering/

  expected_element :mainpage_section

  action(:submit_button) { |b| b.frm.button(text: "submit") }
  action(:submit) { |b| b.submit_button.click; b.loading.wait_while_present(120) }
  
  element(:activity_code) { |b| b.frm.text_field(name: "document.newMaintainableObject.dataObject.aoInfo.activityCode") }
  element(:total_maximum_enrollment) { |b| b.frm.text_field(id: "maximumEnrollment_control") }

  element(:mainpage_section){|b| b.frm.div(id:"ActivityOfferingEdit-MainPage")}

  element(:add_logistics_div) { |b| b.frm.div(id: "ActivityOffering-DeliveryLogistic-New") }
  element(:add_tba){ |b|b.add_logistics_div.div(data_label: "TBA").checkbox()}
  element(:add_days) { |b| b.add_logistics_div.div(data_label: "Days").text_field() }
  element(:add_start_time) { |b| b.add_logistics_div.div(data_label: "Start Time").text_field() }
  element(:add_start_time_ampm) { |b| b.add_logistics_div.select(name: "document.newMaintainableObject.dataObject.newScheduleRequest.startTimeAMPM") }
  element(:add_end_time) { |b| b.add_logistics_div.div(data_label: "End Time").text_field() }
  element(:add_end_time_ampm) { |b| b.add_logistics_div.select(name: "document.newMaintainableObject.dataObject.newScheduleRequest.endTimeAMPM") }
  element(:add_facility) { |b| b.add_logistics_div.div(data_label: "Facility").text_field() }
  action(:lookup_facility) { |b| b.add_logistics_div.div(data_label: "Facility").button().click; b.loading.wait_while_present }
  element(:add_room) { |b| b.add_logistics_div.div(data_label: "Room").text_field() }
  action(:lookup_room) { |b| b.add_logistics_div.div(data_label: "Room").button().click; b.loading.wait_while_present }

  action(:facility_features) { |b| b.frm.link(id: "ActivityOffering-DeliveryLogistic-New-Features-Section_toggle").click; b.loading.wait_while_present }
  element(:feature_list){ |b|b.frm.select(id: "featuresList_control")}

  element(:add_new_delivery_logistics_button) { |b| b.add_logistics_div.button(text: "Add") }
  action(:add_new_delivery_logistics) { |b| b.add_new_delivery_logistics_button.click; b.loading.wait_while_present }

  element(:view_requested_delivery_logistics_link) { |b| b.frm.link(id: "ActivityOffering-DeliveryLogistic-Requested_toggle") }
  element(:delete_requested_delivery_logistics_button) { |b| b.actual_logistics_table.button(text: "delete") } #TODO: identify button by row (days + start_time)

  PERS_ACTION_COLUMN = 4

  element(:add_person_id) { |b| b.personnel_table.rows[1].cells[ID_COLUMN].text_field() }

  action(:lookup_person) { |b| b.personnel_table.rows[1].cells[ID_COLUMN].button().click; b.loading.wait_while_present }
  element(:add_affiliation) { |b| b.personnel_table.rows[1].cells[AFFILIATION_COLUMN].select() }
  element(:add_inst_effort) { |b| b.personnel_table.rows[1].cells[INST_EFFORT_COLUMN].text_field() }
  action(:add_personnel_element) { |b| b.personnel_table.rows[1].cells[PERS_ACTION_COLUMN].button() }
  action(:add_personnel) { |b| b.add_personnel_element.click; b.loading.wait_while_present }

  # requested delivery logistics table
  element(:requested_delivery_logistics_table) { |b| b.frm.table(class: "uif-tableCollectionLayout") }

  TBA = 0
  DAYS = 1
  START_TIME = 2
  END_TIME = 3
  FACILITY = 4
  ROOM = 5
  FEATURES = 6
  ACTIONS = 7

  def edit_requested_delivery_logistics_row(row_num)
    requested_delivery_logistics_table[row_num][7].link(text: "Edit").click
  end

  def days_for_requested_delivery_logistics_row(row_num)
    requested_delivery_logistics_table[row_num][1].text_field.value
  end


  def days_for_actual_delivery_logistics_row(row_num)
    actual_delivery_logistics.table[row_num][1].span.text
  end

  def get_inst_effort(id)
    target_person_row(id).cells[INST_EFFORT_COLUMN].text_field.value
  end

  def update_affiliation(id, affiliation)
    target_person_row(id).select affiliation
  end
  
  def update_inst_effort(id, effort)
    target_person_row(id).text_field.set effort
  end

  def delete_id(id)
    target_person_row(id).button.click
    loading.wait_while_present
  end


  SEATS_ACTION_COLUMN = 5

  #seat pool validation elements
  element(:seatpool_error_list) { |b| b.seat_pools_div.ul(class: "uif-validationMessagesList") }
  element(:seatpool_info_list) { |b| b.seat_pools_div.ul(class: "uif-validationMessagesList") }
  value(:seatpool_first_msg) { |b| b.seatpool_info_list.li.text }

  element(:add_pool_priority) { |b| b.seat_pools_table.rows[1].cells[PRIORITY_COLUMN].text_field() }
  element(:add_pool_seats) { |b| b.seat_pools_table.rows[1].cells[SEATS_COLUMN].text_field() }
  value(:add_pool_name)  { |b| b.seat_pools_table.rows[1].cells[POP_NAME_COLUMN].text_field().text }
  
  action(:lookup_population_name) { |b| b.seat_pools_table.button(title: "Search Field").click; b.loading.wait_while_present }
  
  element(:add_pool_expiration_milestone) { |b| b.seat_pools_table.rows[1].cells[EXP_MILESTONE_COLUMN].select() }
  element(:add_pool_element) { |b| b.seat_pools_table.rows[1].cells[SEATS_ACTION_COLUMN].button()}
  action(:add_seat_pool) { |b| b.seat_pools_table.rows[1].cells[SEATS_ACTION_COLUMN].button().click; b.loading.wait_while_present }

  def remove_seatpool(pop_name)
    target_pool_row(pop_name).button(text: "delete").click
    loading.wait_while_present
  end

  def update_priority(pop_name, priority)
    target_pool_row(pop_name).text_field(name: /processingPriority/).set priority
  end

  def update_seats(pop_name, seats)
    target_pool_row(pop_name).text_field(name: /seatLimit/).set seats
  end

  def update_expiration_milestone(pop_name, milestone)
    target_pool_row(pop_name).cells[EXP_MILESTONE_COLUMN].select.select(milestone)
  end

  def get_priority(pop_name)
    target_pool_row(pop_name).cells[PRIORITY_COLUMN].text_field.value #cell is hard-coded, getting this value was very problematic
  end

  def get_seats(pop_name)
    target_pool_row(pop_name).cells[SEATS_COLUMN].text_field.value #cell is hard-coded, getting this value was very problematic
  end

  def get_expiration_milestone(pop_name)
    target_pool_row(pop_name).cells[EXP_MILESTONE_COLUMN].select.selected_options[0].text #cell is hard-coded, getting this value was very problematic
  end

  element(:course_url) { |b| b.frm.text_field(name: "document.newMaintainableObject.dataObject.aoInfo.activityOfferingURL") }
  element(:requires_evaluation) { |b| b.frm.checkbox(name: "document.newMaintainableObject.dataObject.aoInfo.isEvaluated") }
  element(:honors_flag) { |b| b.frm.checkbox(name: "document.newMaintainableObject.dataObject.aoInfo.isHonorsOffering") }

  #validation error dialog
  element(:validation_error_dialog_div)  { |b| b.frm.div(class: "fancybox-wrap fancybox-desktop fancybox-type-html fancybox-opened") }
  value(:validation_error_dialog_text) { |b| b.validation_error_dialog_div.div(index: 2).text }
  action(:close_validation_error_dialog) { |b| b.validation_error_dialog_div.div(title: "Close").click}
  #validation error dialog

end