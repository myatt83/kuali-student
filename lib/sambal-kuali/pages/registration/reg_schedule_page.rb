class StudentSchedule < RegisterForCourseBase

  element(:reg_credit_count) { |b| b.span(id: "reg_credit_count").text }

  element(:waitlist_user_message_div) { |b| b.div(id: "waitlist_course_status_message") }
  element(:waitlist_user_message) { |b| b.waitlist_user_message_div.div(index: 0).text }

  element(:remove_course_button) { |course_code,reg_group_code,b| b.button(id: "remove_#{course_code}_#{reg_group_code}") }
  element(:course_code) { |course_code,reg_group_code,b| b.span(id: "course_code_#{course_code}_#{reg_group_code}") }
  element(:waitlisted_course_code) { |course_code,reg_group_code,b| b.span(id: "waitlist_course_code_#{course_code}_#{reg_group_code}") }
  element(:course_title_div) { |course_code,reg_group_code,b| b.div(id: "title_#{course_code}_#{reg_group_code}") }
  element(:course_title) { |course_code,reg_group_code,b| b.div(id: "title_#{course_code}_#{reg_group_code}").text }
  element(:course_info_div) { |course_code,reg_group_code,b| b.div(id: "course_info_#{course_code}_#{reg_group_code}") }
  element(:course_info) { |course_code,reg_group_code,b| b.div(id: "course_info_#{course_code}_#{reg_group_code}").text }
  element(:grading_option_badge) { |course_code,reg_group_code,b| b.span(id: "grading_badge_#{course_code}_#{reg_group_code}") }
  element(:grading_option) { |course_code,reg_group_code,b| b.grading_option_badge(course_code,reg_group_code).text }
  element(:edit_course_options_button) { |course_code,reg_group_code,b| b.button(id: "edit_#{course_code}_#{reg_group_code}") }
  action(:edit_course_options) { |course_code,reg_group_code,b| b.edit_course_options_button(course_code,reg_group_code).click }
  element(:ao_type) { |course_code,reg_group_code,index,b| b.div(id: "ao_type_#{course_code}_#{reg_group_code}_#{index}").text }
  element(:course_schedule) { |course_code,reg_group_code,ao_index,index,b| b.div(id: "schedule_#{course_code}_#{reg_group_code}_#{ao_index}_#{index}").text }
  element(:confirm_drop) { |course_code,reg_group_code,b| b.button(id: "dropRegGroup_#{course_code}_#{reg_group_code}") }
  element(:cancel_drop) { |b| b.button(id: "dropRegGroupCancel") }
  element(:credits_selection) { |course_code,reg_group_code,b| b.select(id: "credits_#{course_code}_#{reg_group_code}") }
  element(:grading_audit) { |course_code,reg_group_code,b| b.radio(id: "grading_#{course_code}_#{reg_group_code}", value: "kuali.resultComponent.grade.audit") }
  element(:grading_letter) { |course_code,reg_group_code,b| b.radio(id: "grading_#{course_code}_#{reg_group_code}", value: "kuali.resultComponent.grade.letter") }
  element(:grading_pass_fail) { |course_code,reg_group_code,b| b.radio(id: "grading_#{course_code}_#{reg_group_code}", value: "kuali.resultComponent.grade.passFail") }
  element(:edit_save_button) { |course_code,reg_group_code,b| b.button(id: "saveScheduleItem_#{course_code}_#{reg_group_code}") }
  action(:save_edits) { |course_code,reg_group_code,b| b.edit_save_button(course_code,reg_group_code).click }
  element(:edit_cancel_link) { |course_code,reg_group_code,b| b.link(id: "cancelEditScheduleItem_#{course_code}_#{reg_group_code}") }
  action(:cancel_edits) { |course_code,reg_group_code,b| b.edit_cancel_link(course_code,reg_group_code).click }

  element(:waitlist_course_info_div) { |course_code,reg_group_code,b| b.div(id: "waitlist_course_info_#{course_code}_#{reg_group_code}") }
  element(:waitlist_course_info) { |course_code,reg_group_code,b| b.waitlist_course_info_div(course_code,reg_group_code).text }
  element(:edit_waitlist_item_button)  { |course_code,reg_group_code,b| b.button(id: "waitlist_edit_#{course_code}_#{reg_group_code}") }
  action(:edit_waitlisted_course_options) { |course_code,reg_group_code,b| b.edit_waitlist_item_button(course_code,reg_group_code).click }
  element(:waitlist_credits_selection) { |course_code,reg_group_code,b| b.select(id: "waitlist_credits_#{course_code}_#{reg_group_code}") }
  element(:waitlist_grading_audit) { |course_code,reg_group_code,b| b.radio(id: "waitlist_grading_#{course_code}_#{reg_group_code}", value: "kuali.resultComponent.grade.audit") }
  element(:waitlist_grading_letter) { |course_code,reg_group_code,b| b.radio(id: "waitlist_grading_#{course_code}_#{reg_group_code}", value: "kuali.resultComponent.grade.letter") }
  element(:waitlist_grading_pass_fail) { |course_code,reg_group_code,b| b.radio(id: "waitlist_grading_#{course_code}_#{reg_group_code}", value: "kuali.resultComponent.grade.passFail") }
  element(:waitlist_grading_option_badge) { |course_code,reg_group_code,b| b.span(id: "waitlist_grading_badge_#{course_code}_#{reg_group_code}") }
  element(:waitlist_grading_option) { |course_code,reg_group_code,b| b.waitlist_grading_option_badge(course_code,reg_group_code).text }
  element(:waitlist_edit_save_button) { |course_code,reg_group_code,b| b.button(id: "waitlist_saveScheduleItem_#{course_code}_#{reg_group_code}") }
  action(:save_waitlist_edits) { |course_code,reg_group_code,b| b.waitlist_edit_save_button(course_code,reg_group_code).click }
  element(:waitlist_edit_cancel_link) { |course_code,reg_group_code,b| b.link(id: "waitlist_cancelEditScheduleItem_#{course_code}_#{reg_group_code}") }
  action(:cancel_waitlist_edits) { |course_code,reg_group_code,b| b.waitlist_edit_cancel_link(course_code,reg_group_code).click }
  element(:remove_from_waitlist_button) { |course_code,reg_group_code,b| b.button(id: "waitlist_remove_#{course_code}_#{reg_group_code}") }
  element(:confirm_remove_waitlist_button) { |b| b.button(id: "removeWaitlist") }
  element(:cancel_remove_waitlist_button) { |b| b.button(id: "removeWaitlistCancel") }

  def toggle_course_details(course_code, reg_group_code, course_status="registered")
    case course_status
      when "registered"
        course_code(course_code,reg_group_code).wait_until_present
        course_code(course_code,reg_group_code).click
      when "waitlisted"
        waitlisted_course_code(course_code,reg_group_code).wait_until_present
        waitlisted_course_code(course_code,reg_group_code).click
    end
  end

  def remove_course_from_schedule(course_code,reg_group_code)
    remove_course_button(course_code,reg_group_code).wait_until_present
    remove_course_button(course_code,reg_group_code).click
    confirm_drop(course_code,reg_group_code).wait_until_present
    confirm_drop(course_code,reg_group_code).click
  end

  def cancel_drop_course(course_code,reg_group_code)
    remove_course_button(course_code,reg_group_code).click
    cancel_drop.wait_until_present
    cancel_drop.click
  end

  def select_credits(course_code, reg_group_code, credits)
    credits_selection(course_code, reg_group_code).select(credits)
  end

  def select_grading(course_code, reg_group_code, grading_option)
    case grading_option
      when "Audit" then grading_audit(course_code,reg_group_code).set
      when "Letter" then grading_letter(course_code,reg_group_code).set
      when "Pass/Fail" then grading_pass_fail(course_code,reg_group_code).set
    end
  end

  def show_course_details(course_code, reg_group_code, course_status="registered")
    sleep 1
    case course_status
      when "registered"
        toggle_course_details(course_code, reg_group_code, course_status) unless edit_course_options_button(course_code, reg_group_code).visible?
      when "waitlisted"
        toggle_course_details(course_code, reg_group_code, course_status) unless edit_waitlist_item_button(course_code, reg_group_code).visible?
    end
  end

  def hide_course_details(course_code, reg_group_code, course_status="registered")
    sleep 1
    case course_status
      when "registered"
        toggle_course_details(course_code, reg_group_code, course_status) if edit_course_options_button(course_code, reg_group_code).visible?
      when "waitlisted"
        toggle_course_details(course_code, reg_group_code, course_status) if edit_waitlist_item_button(course_code, reg_group_code).visible?
    end
  end

  def select_waitlist_credits(course_code, reg_group_code, credits)
    waitlist_credits_selection(course_code, reg_group_code).select(credits)
  end

  def select_waitlist_grading(course_code, reg_group_code, grading_option)
    case grading_option
      when "Audit" then waitlist_grading_audit(course_code,reg_group_code).set
      when "Letter" then waitlist_grading_letter(course_code,reg_group_code).set
      when "Pass/Fail" then waitlist_grading_pass_fail(course_code,reg_group_code).set
    end
  end

  def remove_course_from_waitlist(course_code,reg_group_code)
    remove_from_waitlist_button(course_code,reg_group_code).wait_until_present
    remove_from_waitlist_button(course_code,reg_group_code).click
    confirm_remove_waitlist_button.wait_until_present
    confirm_remove_waitlist_button.click
  end

end