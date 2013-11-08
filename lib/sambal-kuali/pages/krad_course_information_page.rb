class KradCourseInformation < BasePage

  wrapper_elements
  krad_elements

  element(:proposal_title) { |b| b.text_field(name: /proposal\.name$/) }
  element(:course_title) { |b| b.text_field(name: /course\.courseTitle$/) }
  element(:transcript_title) {|b| b.text_field(name: /transcriptTitle$/) }
  element(:subject_code) { |b| b.text_field(name: /subjectArea$/) }
  element(:course_number) { |b| b.text_field(name: /courseNumberSuffix$/) }

#CROSS LIST SECTION
  element(:course_listing_section_collapsed) { |b| b.img(id: /^KS-CrossListingEtcDisclosure-Section/, alt: 'collapse') }
  action(:expand_course_listing_section) { |b| b.img(id: 'KS-CrossListingEtcDisclosure-Section_toggle_col').click unless b.img(id: 'KS-CrossListingEtcDisclosure-Section_toggle_exp').visible?; b.add_a_version_code_button.wait_until_present }

  element(:add_another_course_listing_button) { |b| b.button(id: 'KS-CrossListed-Section_add') }
  element(:add_another_course_button) { |b| b.button(id: 'KS-JointlyOffered-Section_add') }
  element(:add_a_version_code_button) { |b| b.button(id: 'KS-VersionCodes-Section_add') }

  action(:add_another_course_listing) { |b| b.add_another_course_listing_button.click; b.loading_wait }
  action(:add_another_course) { |b| b.add_another_course_button.click; b.loading_wait }
  action(:add_a_version_code) { |b| b.add_a_version_code_button.click; b.loading_wait }

  # Needed this way because if user has multiple cross_list to access those multiple fields
  action(:course_listing_subject) { |cross_list_subject_code='0', b| b.text_field(id: /^KS-CrossList-SubjectArea-Field_line#{cross_list_subject_code}/) }
  action(:course_listing_number) { |cross_list_course_number='0', b| b.text_field(id: /^KS-CrossList-CourseNumberSuffix-Field_line#{cross_list_course_number}/) }

  action(:joint_offering_number) { |joint_offering_course_number='0', b| b.text_field(name: /#{joint_offering_course_number}\].courseCode$/) }

  action(:version_code_code) { |version_version_code='0', b| b.text_field(name: /#{version_version_code}\]\.variationCode$/) }
  action(:version_code_title) { |version_course_title='0', b| b.text_field(name: /#{version_course_title}\]\.variationTitle$/) }

#INSTRUCTORS
  element(:instructor_name) { |b| b.text_field(name: /instructorWrappers\'\]\.displayName$/) }
  action(:instructor_add) {|b| b.button(text: 'Add').click; b.adding_line.wait_while_present }
  action(:added_instructor_name) { |instructor_level='0', b| b.text_field(name: /instructorWrappers\[#{instructor_level}\]\.displayName$/)}
  action(:advanced_search) { |b| b.link(text: 'Advanced Search' ).click }

#DESCRIPTION AND RATIONALE
  element(:description_rationale) { |b| b.text_field(name: /course.descr.plain$/) }
  element(:proposal_rationale) { |b| b.text_field(name: /proposal.rationale.plain$/) }

  #action(:save_and_continue) { |b| b.button(id: 'usave').click; b.saving_wait }
  element(:error_popup) { |b| b.div(text: 'The form contains errors. Please correct these errors and try again.') }
  action(:error_message) { |error_number='2', b| b.h3(text: "This page has #{error_number} errors") }

end
