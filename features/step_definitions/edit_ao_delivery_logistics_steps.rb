When /^I revise an AO's requested delivery logistics$/ do
  course_offering = create CourseOffering, :create_by_copy => (make CourseOffering, :term => "201208", :course=>"ENGL222")
  #course_offering = make CourseOffering, :term => "201208", :course=>"ENGL222E"
  course_offering.manage_and_init
  @activity_offering = course_offering.activity_offering_cluster_list[0].get_ao_obj_by_code("A")
  @rdl = @activity_offering.requested_delivery_logistics_list.values[0]
  @activity_offering.edit
  @rdl.edit :days => "FU"
  @activity_offering.save
  #@orig_days = @activity_offering.change_rdl_days(1)
end

Then /^the AO's delivery logistics shows the new schedule$/ do
  @activity_offering.parent_course_offering.manage
  @activity_offering.edit
  on ActivityOfferingMaintenance do |page|
    page.view_requested_delivery_logistics
    #new_days = page.requested_logistics_table[1][1].text.gsub!(/\s+/, "")
    #new_days.should_not == @orig_days
  end
end
