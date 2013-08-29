class ActivityOfferingInquiry < BasePage

  wrapper_elements
  expected_element :term_element

  def frm
    self.frame(class: "fancybox-iframe")
  end

  action(:close) { |b| b.frm.button(text: "Close").click; b.loading.wait_while_present }

  value(:subterm) { |b| b.frm.span(id: "subterm_name_control").text }
  value(:subterm_start_date) { |b| b.frm.span(id: "start_end_date_control").text[/.*(?=-)/].strip }
  value(:subterm_end_date) { |b| b.frm.span(id: "start_end_date_control").text[/(?<=-).*/].strip }
  value(:course_offering_code) { |b| b.frm.span(id: "u14").text } # Persistent ID needed!
  value(:activity_code) { |b| b.frm.span(id: "activityCode_control").text }
  value(:course_offering_title) { |b| b.frm.span(id: "u32").text } # Persistent ID needed!
  element(:term_element) { |b| b.frm.span(id: "term_control") }
  value(:term) { |b| b.frm.span(id: "term_control").text }
  value(:type) { |b| b.frm.span(id: "type_name_control").text }
  value(:format_offering) { |b| b.frm.span(id: "u59").text } # Persistent ID needed!
  value(:total_maximum_enrollment) { |b| b.frm.span(id: "maximumEnrollment_label").text }
  element(:actual_delivery_logistics) { |b| b.frm.table(id: "u130") } # Persistent ID needed!
  element(:requested_delivery_logistics) { |b| b.frm.table(id: "u235") } # Persistent ID needed!
  value(:state) { |b| b.frm.span(id: "u77").text } # Persistent ID needed!
  value(:requires_evaluation) { |b| b.frm.span(id: "u86").text } # Persistent ID needed!
  value(:honors_offering) { |b| b.frm.span(id: "u95").text } # Persistent ID needed!
  value(:activity_offering_url) { |b| b.frm.span(id: "u104").text } # Persistent ID needed!
  value(:affiliated_personnel) { |b| b.frm.span(id: "u114").text } # Persistent ID needed!
  value(:maximum_enrollment) { |b| b.frm.span(id: "u123").text } # Persistent ID needed!

end