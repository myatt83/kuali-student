And /^I create a number of COs with an AO in each$/ do

  # BSCI181
  @bsci_co = create CourseOffering, :term => "201201", :course => "BSCI181", :delivery_format => "Lecture", :grade_format => "Lecture"
  @bsci_co.create_ao :ao_code => "A"

  # CHEM181
  @chem_co = create CourseOffering, :term => "201201", :course => "CHEM181", :delivery_format => "Lecture", :grade_format => "Lecture"
  @chem_co.create_ao :ao_code => "A"

  # PHYS181
  @phys_co = create CourseOffering, :term => "201201", :course => "PHYS181", :delivery_format => "Lecture", :grade_format => "Lecture"
  @phys_co.create_ao :ao_code => "A"

end

When /^I indicate multiple activities for colocation, selecting to "(share|separately manage)" enrollments$/ do |max_enrollment_flag|

  $should_enrollment_be_shared_flag = true
  if max_enrollment_flag == 'separately manage'
    $should_enrollment_be_shared_flag = false
  end

  @bsci_co.colocate :cos_to_colo => [@chem_co, @phys_co],
                    :should_enrollment_be_shared_flag => $should_enrollment_be_shared_flag,
                    :enrollment_size => 10

end

Then /^the AO indicates it is colocated$/ do

  @bsci_co.manage
  on ManageCourseOfferings do |page|
    $colocated_tooltip_text = page.target_row('A')[1].image.alt.upcase

    $chem_expected = @chem_co.course.upcase + ' ' + @chem_co.ao_list[0].upcase
    $phys_expected = @phys_co.course.upcase + ' ' + @phys_co.ao_list[0].upcase

    $colocated_tooltip_text.should include $chem_expected
    $colocated_tooltip_text.should include $phys_expected

  end

end

When(/^I designate a valid term and Course Offering Code with a fully colocated AO$/) do
  @course_offering = make CourseOffering, :course=>"CHEM441", :term=>"201208"
  @course_offering.manage
end

And(/^I delete the fully colcated AO$/) do
  on ManageCourseOfferings do |page|
    @course_offering.delete_ao_list :code_list =>  page.codes_list
  end
end

Then(/^The AO is successfully deleted$/) do
  @course_offering.manage
  begin
    on(ManageCourseOfferings).codes_list
  rescue RuntimeError => e
    e.message.should include "error in activity_offering_results_table - no AOs"
  end
end