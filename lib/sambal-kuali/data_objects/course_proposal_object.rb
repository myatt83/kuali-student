class CmCourseProposalObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Utilities

  # Course Information, Governance, Course Logistics, Active Dates completed
  attr_accessor :proposal_title, :course_title, :transcript_course_title, :subject_code, :course_number,
        :cross_listed_course_list,
        :jointly_offered_course_list,
        :version_code_list,
        :instructor, :instructor_first_name, :instructor_last_name, :instructor_username, :instructor_display_name,
        :description_rationale, :proposal_rationale,
        :instructor_adding_method, :joint_offering_adding_data, :joint_offering_name,
        :joint_offering_description, :joint_offering_course_code,
        # Governance Page
        :camp_local, :campus_location, :curriculum_oversight, :administering_organization,
        :location_north, :location_south, :location_extended, :location_all,
        :administering_organization, :adv_admin_org_identifier,
        :adv_admin_org_name, :adv_admin_org_abbreviation, :adv_admin_org_name, :admin_org_adding_method,
        # Course Logistics
        :term_any, :term_fall, :term_spring, :term_summer,
        :assessment_scale,:final_exam_type, :final_exam_rationale,
        :exam_standard, :exam_alternate, :exam_none,
        #
        :outcome_list,
        :format_list,
        :audit, :pass_fail_transcript_grade,
        :assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail, :assessment_percentage, :assessment_satisfactory,
        #Course Requisites


        :rule_adv_course_title,
        :rule_adv_course_code,
        :rule_adv_course_description_snip,
        :rule_credit,

        :eligibility_add_method,
        :student_eligibility_add_method,
        :student_eligibility_course,
        :student_eligibility_title,
        :student_eligibility_phrase,
        :student_eligibility_rule,
        :student_eligibility_rule_with_value,

        :corequisite_add_method,
        :corequisite_title,
        :corequisite_course,
        :corequisite_phrase,
        :course_requisite_added_rule,
        :corequisite_rule,
        :corequisite_rule,
        :corequisite_rule_with_value,

        :recommended_preparation_add_method,
        :recommended_preparation_course,
        :recommended_preparation_title,
        :recommended_preparation_phrase,
        :recommended_preparation_rule,
        :recommended_preparation_rule_with_value,

        :antirequisite_add_method,
        :antirequisite_course,
        :antirequisite_title,
        :antirequisite_phrase,
        :antirequisite_rule,
        :antirequisite_rule_with_value,

        :repeatable_for_credit_credit,
        :repeatable_for_credit_credit,
        :repeatable_for_credit_rule,
        :repeatable_for_credit_rule_with_value,


        :course_that_restricts_credits_add_method,
        :course_that_restricts_credits_course,
        :course_that_restricts_credits_title,
        :course_that_restricts_credits_course,
        :course_that_restricts_credits_rule,
        :course_that_restricts_credits_rule_with_value,


        # Active Dates
        :start_term, :pilot_course, :end_term,

        # Financials
        :course_fees,

        # Authors & Collaborators
        :author_name_search, :author_username_search, :author_permission, :action_request,
        :author_notation, :author_name_method, :author_display_name, :curriculum_review_process,

        #Save
        :create_new_proposal,
        :save_proposal



  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        #COURSE INFORMATION
        proposal_title:             random_alphanums(10,'test proposal title '),
        course_title:               random_alphanums(10, 'test course title '),
        subject_code:               "MATH",
        course_number:              "123",
        transcript_course_title:    random_alphanums(5,'test transcript'),
        description_rationale:      random_alphanums(20, 'test description rationale '),
        proposal_rationale:         random_alphanums(20, 'test proposal rationale '),
        #GOVERNANCE
        campus_location: [:location_all, :location_extended, :location_north, :location_south],
        curriculum_oversight:       '::random::',
        #COURSE LOGISTICS
        #ASSESSMENT SCALE
        assessment_scale:           [:assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail, :assessment_percentage, :assessment_satisfactory],
        #FINAL EXAM
        final_exam_type:            [:exam_standard, :exam_alternate, :exam_none],
        final_exam_rationale:       random_alphanums(10,'test final exam rationale '),
        #OUTCOMES
        outcome_list: [
            (make CmOutcomeObject, :outcome_type => "Fixed", :outcome_level => 0, :credit_value=>(1..5).to_a.sample),
            (make CmOutcomeObject, :outcome_type => "Range", :outcome_level => 1, :credit_value => "#{(1..4).to_a.sample}-#{(5..9).to_a.sample}"),
            (make CmOutcomeObject, :outcome_type => "Multiple",:outcome_level => 2, :credit_value => "#{(1..4).to_a.sample},#{(5..9).to_a.sample}")
        ],
        format_list: [(make CmFormatsObject)],
        start_term: '::random::',
        #FINANCIALS
        course_fees:                random_alphanums(10, 'test course fees '),
        curriculum_review_process:  nil,
        create_new_proposal:        true,
        create_basic_propsal:       false,
        save_proposal:              true
    }
    set_options(defaults.merge(opts))



    # random_checkbox and random_radio is used to select a random checkbox/radio on a page.
    # That will then be set the instance variable to :set, so that it can be used in the fill_out method for later tests
    random_checkbox(@scheduling_term,"Scheduling Term")
    random_radio(@final_exam_status)
    random_checkbox(@campus_location,"Campus Location")
    random_checkbox(@assessment_scale,"Assessment Scale")
    random_radio(@final_exam_type)
  end


  def create
    navigate_rice_to_cm_home
    navigate_to_create_course_proposal
    set_curriculum_review

    if(@create_basic_propsal)
      create_course_continue
      create_course_proposal_required_for_save unless @proposal_title.nil?
      determine_save_action
    elsif @create_new_proposal
      create_course_continue
      create_course_proposal_required unless @proposal_title.nil?
      determine_save_action
    end
  end


  def edit (opts={})
    determine_edit_action

    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?
      page.proposal_title.fit opts[:proposal_title]
      page.course_title.fit opts[:course_title]
      page.transcript_course_title.fit opts[:transcript_course_title]
      page.subject_code.fit opts[:subject_code]
      page.course_number.fit opts[:course_number]
      page.description_rationale.fit opts[:description_rationale]
      page.proposal_rationale.fit opts[:proposal_rationale]
    end

    determine_save_action

    on CmGovernance do |page|
      page.governance unless page.current_page('Governance').exists?
      if opts[:campus_location] != nil
        reset_checkbox(@campus_location, "Campus Location") unless opts[:campus_location].nil?
        fill_out page, :location_all, :location_extended, :location_north, :location_south unless opts[:campus_location].nil?
      end
      page.curriculum_oversight.pick! opts[:curriculum_oversight] unless opts[:curriculum_oversight].nil?
    end

    determine_save_action

    on CmCourseLogistics do |page|
      page.course_logistics unless page.current_page('Course Logistics').exists?
      #Edit Assessment Scale\
      if opts[:assessment_scale] != nil
        reset_checkbox(@assessment_scale, "Assessment Scale")
        fill_out page,  :assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail, :assessment_percentage, :assessment_satisfactory
      end
      #Edit Final Exam Status
      if opts[:final_exam_type] != nil
        random_radio(opts[@final_exam_type])
        fill_out page, :exam_standard, :exam_alternate, :exam_none
        #This 'UNLESS' is required for 'Standard Exam' which, does not have rationale and should skip filling in final_exam_rationale
        #if that radio is selected
        page.final_exam_rationale.wait_until_present unless page.exam_standard.set?
        page.final_exam_rationale.fit opts[:final_exam_rationale] unless page.exam_standard.set?
      end

    end
    determine_save_action
    
    on CmActiveDates do |page|
      #Active Dates
      page.active_dates unless page.current_page('Active Dates').exists?
      page.start_term.pick! opts[:start_term] unless opts[:start_term].nil?
    end

    determine_save_action

    set_options(opts)
  end

  def create_course_proposal_required_for_save
    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?

      fill_out page, :proposal_title, :course_title
      determine_save_action
    end
   end


  def create_course_proposal_required
    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?

      fill_out page, :proposal_title, :course_title, :transcript_course_title
      #page.subject_code.fit @subject_code
      page.auto_lookup @subject_code unless @subject_code.nil?
      fill_out page, :course_number

      if @cross_listed_course_list != nil
        @cross_listed_course_list.each do |cross_listed_course|
          cross_listed_course.create
        end
      end

      if @jointly_offered_course_list != nil
        @jointly_offered_course_list.each do |jointly_offered_course|
          jointly_offered_course.create
        end
      end

      if @version_code_list != nil
        @version_code_list.each do |version_code|
          version_code.create
        end
      end

      fill_out page, :description_rationale, :proposal_rationale
      determine_save_action
    end

    on CmGovernance do |page|
      page.governance unless page.current_page('Governance').exists?

      fill_out page, :location_all, :location_extended, :location_north, :location_south
      page.curriculum_oversight.pick! @curriculum_oversight
      determine_save_action
    end

    on CmCourseLogistics do |page|
      page.course_logistics unless page.current_page('Course Logistics').exists?

      page.loading_wait
      #page.add_outcome unless @outcome_type.nil?
      # outcome_type needs to be done first because of how page loading is working
#      page.outcome_type.pick! '::random::'
#      fill_out page, :outcome_type
      fill_out page,
               :assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail,
               :assessment_percentage, :assessment_satisfactory

      sleep 1
      #set_outcome_type
      if @outcome_list != nil
        @outcome_list.each do |outcome|
          outcome.create
        end
      end

      if @format_list != nil
        @format_list.each do |format|
          format.create
        end
      end




      if @final_exam_type != nil
        fill_out page, :exam_standard, :exam_alternate, :exam_none
        #This 'UNLESS' is required for 'Standard Exam' which, does not have rationale and should skip filling in final_exam_rationale
        #if that radio is selected
        page.final_exam_rationale.wait_until_present unless page.exam_standard.set?
        page.final_exam_rationale.fit @final_exam_rationale unless page.exam_standard.set?
      end
      determine_save_action
    end

    on CmActiveDates do |page|
      page.active_dates unless page.current_page('Active Dates').exists?
      page.start_term.pick! @start_term unless start_term.nil?

      #page.pilot_course.fit @pilot_course
      #page.loading_wait
      # SPECIAL:: Need this second start_term because end_term not immediately selectable after checkbox is selected
      #page.start_term.fit @start_term
      #sleep 1
      #page.end_term.fit @end_term

      determine_save_action
    end

  end # required proposal
  def course_proposal_nonrequired
    on CmCourseInformation do |page|

      page.course_information unless page.current_page('Course Information').exists?
      page.loading_wait
      page.expand_course_listing_section unless page.collapse_course_listing_section.visible?


      page.add_another_course_listing unless @course_listing_subject.nil? and @course_listing_number.nil?
      fill_out page, :course_listing_number

      page.course_listing_subject.fit @course_listing_subject
      page.auto_lookup @course_listing_subject unless @course_listing_subject.nil?

      #Joint offering adding and instructor adding in private methods do to complexity of advanced search adding
      add_joint_offering
      add_instructor

      page.save_and_continue
    end

    on CmGovernance do |page|
      page.governance unless page.current_page('Governance').exists?



      # Admin organization in private method do to complexity of advanced search adding
      adding_admin_organization

      page.save_and_continue
    end

    on CmCourseLogistics do |page|
      page.course_logistics unless page.current_page('Course Logistics').exists?
      fill_out page, :term_any, :term_fall, :term_spring, :term_summer,
               :audit, :pass_fail_transcript_grade,
               :duration_type, :duration_count,
               :activity_contacted_hours, :activity_class_size,
               :activity_duration_count, :activity_frequency, :activity_duration_type
      page.save_and_continue
    end

    on CmLearningObjectives do |page|
      page.learning_objectives unless page.current_page('Learning Objectives').exists?
      # TODO:: NEED TO MAKE TESTS FOR THIS PAGE
      page.save_and_continue
    end

    on CmCourseRequisites do |page|
      page.course_requisites unless page.current_page('Course Requisites').exists?

  #Private methods do to complexity of adding rules
  #STUDENT ELIGIBILITY
      adding_rule_student_eligibility
  #COREQUISITE
      adding_rule_corequisite
  #RECOMMENDED PREPARATION
      adding_rule_recommended_preparation_rule
  #ANTIREQUISITE
      adding_rule_antirequisite
  #REPEATABLE FOR CREDIT
      adding_rule_repeatable_for_credit
  #COURSE THAT RESTRICTS CREDIT
      adding_course_that_restricts_credits

      page.save_and_continue
    end

    on CmAuthorsCollaborators do |page|
      page.authors_collaborators unless page.current_page('Authors Collaborators').exists?
      # Adding author name in private method do to complexity of advanced search adding
      adding_author_name

      fill_out page, :author_permission, :action_request, :author_notation
      page.add_author
      page.save_and_continue
     end

  end # non-required


  #Used for Advanced Search to "Return Value" of the result that matches
  #Defaults to 4th Column to match instructor display name but can be altered for different advacned search results by passing in a number of the column
  def return_search_result(search_result_value_to_match, row_number=3)
    on CmCourseRequisites do |page|
    page.search_results_table.rows.each do |row|
        if row.cells[row_number].text == search_result_value_to_match
          row.cells[0].link(text: 'return value').click
          break
        end
      end
    end
  end

  def cancel_create_course
    on CmCreateCourseStart do |page|
      page.cancel
    end
  end

  def cancel_course_proposal
     on CmCourseInformation do |page|
       page.cancel
     end
  end

  def set_curriculum_review
  on CmCreateCourseStart do |create|
    create.curriculum_review_process.fit checkbox_trans[@curriculum_review_process] unless @curriculum_review_process.nil?
  end
  end

  def create_course_continue
    on CmCreateCourseStart do |create|
      create.continue
    end
  end

  def create_course_proposal_required_fields
  on CmCourseInformation do |create|
    fill_out create, :proposal_title, :course_title
  end
  end

  def determine_save_action
  on CmCourseInformation do |create|
    unless !@save_proposal
      if create.logged_in_user == "Alice"
        create.save_progress
      elsif create.logged_in_user == "Fred"
        create.save_and_continue
      end
    end


  end

  end

  def search(search_text)
    navigate_to_find_course_proposal
       on FindProposalPage do |page|
          page.name.wait_until_present
          page.name.set search_text
          page.find_a_proposal
       end

  end

  def load_comments_action
    on CmCourseInformation do |page|
      page.load_comments
    end
  end


  def load_decisions_action
    on CmCourseInformation do |page|
      page.load_decisions
    end
  end


  def review_proposal_action
    on FindProposalPage do |page|
      page.review_proposal_action_link(@proposal_title)
    end
  end

  def edit_proposal_action
    on FindProposalPage do |page|
      page.edit_proposal_action(@proposal_title)
    end
  end

  def edit_course_information
    on CmReviewProposal do |page|
      page.edit_course_information
    end
  end

  def determine_edit_action
    #if current page is find a proposal click the pencil icon


    on CmReviewProposal do |page|
      if page.proposal_title_element.exists?
        edit_course_information
      end
    end

    #if current page is review proposal click the course information edit icon

    on FindProposalPage do |page|
      if page.name.exist?
        edit_proposal_action
      end

    end

  end

  def add_outcome (opts)
    defaults = {
        :outcome_type => "Fixed"
    }
    options = defaults.merge(opts)
    options[:outcome].create
    @outcome_list << options[:outcome]
    determine_save_action
  end



  #-----
  private
  #-----

  def checkbox_trans
     { "Yes" => :set, "No" => :clear }
  end

  #Used to select a random checkbox/radio button and then set to :set for fill_out method
  def random_checkbox(pass_in_an_array, type)
    if type == "Campus Location"
      @sample_checkbox_location = pass_in_an_array.sample unless pass_in_an_array.nil?
      set(@sample_checkbox_location, :set)  unless pass_in_an_array.nil?
    end
    if type == "Assessment Scale"
      @sample_checkbox_assessment = pass_in_an_array.sample unless pass_in_an_array.nil?
      set(@sample_checkbox_assessment, :set)  unless pass_in_an_array.nil?
    end
  end

  
  def reset_checkbox(pass_in_an_array, type)
    if type == "Campus Location"
    set(@sample_checkbox_location, :clear) unless pass_in_an_array.nil?
    pass_in_an_array.delete(@sample_checkbox_location)
    @sample_checkbox_new_location = pass_in_an_array.sample
    set(@sample_checkbox_new_location, :set) unless pass_in_an_array.nil?
    end
    if type == "Assessment Scale"
      set(@sample_checkbox_assessment, :clear) unless pass_in_an_array.nil?
      pass_in_an_array.delete(@sample_checkbox_assessment)
      @sample_checkbox_new_assessment = pass_in_an_array.sample
      set(@sample_checkbox_new_assessment, :set) unless pass_in_an_array.nil?
    end
  end

  def random_radio(pass_in_an_array)
    @sample_radio = pass_in_an_array.sample unless pass_in_an_array.nil?
    set(@sample_radio, :set)  unless pass_in_an_array.nil?
  end


#COURSE INFORMATION
  def add_joint_offering
    on CmCourseInformation do |page|
      if @joint_offering_adding_data == 'auto_lookup'
        page.add_another_course
        page.joint_offering_number.fit @joint_offering_number
        page.auto_lookup @joint_offering_number unless @joint_offering_number.nil?
      end

      if @joint_offering_adding_data == 'adv_given_name' or @joint_offering_adding_data == 'adv_course_code' or @joint_offering_adding_data == 'adv_plain_text'
        page.add_another_course
        page.joint_offering_advanced_search
        page.adv_given_name.wait_until_present
        page.adv_course_code.fit @joint_offering_course_code if @joint_offering_adding_data == 'adv_course_code'
        page.adv_given_name.fit @joint_offering_name if @joint_offering_adding_data == 'adv_given_name'
        page.adv_plain_text_description.fit @joint_offering_description if @joint_offering_adding_data == 'adv_plain_text'
        page.adv_search
        page.adv_return_value @joint_offering_course_code
      end
    end
  end

  def add_instructor
    on CmCourseInformation do |page|
      if instructor_adding_method == 'advanced' or instructor_adding_method == 'adv_name' or instructor_adding_method == 'adv_username'
        page.instructor_advanced_search
        page.adv_name.fit @instructor_last_name if instructor_adding_method == 'adv_name' or instructor_adding_method == 'advanced'
        page.adv_username.fit @instructor_username if instructor_adding_method == 'adv_username' or instructor_adding_method == 'advanced'
        page.adv_search
        page.adv_return_value_instructor @instructor_display_name
      end

      if instructor_adding_method == 'auto_lookup'
        page.instructor_name.fit @instructor_last_name
        page.auto_lookup @instructor_display_name unless @instructor_display_name.nil?

      end

      # DUE TO RICE ISSUE CODE NEEDS TO WAIT FOR FIELD TO DISPLAY THE RETURN RESULTS FOR ADV SEARCH
      # So we wait until the name field = returned value
      page.instructor_name.text == @instructor_display_name

      page.instructor_add unless @instructor_last_name.nil?
    end
  end

#GOVERNANCE
  def adding_admin_organization
    on CmGovernance do |page|
      if admin_org_adding_method == 'auto_lookup'
        page.administering_organization.fit @administering_organization
        #TODO: uncomment this when bug KSCM-1204 is fixed for auto lookup on administering org text field
        #page.auto_lookup @administering_organization unless @administering_organization.nil?
        page.organization_add unless @administering_organization.nil?
      end

      if admin_org_adding_method == 'advanced'
        page.adv_search_admin_org
        page.adv_identifier.fit @adv_admin_org_identifier
        page.adv_org_name.fit @adv_admin_org_name
        page.adv_abbreviation.fit @adv_admin_org_abbreviation
        page.adv_search
        page.adv_admin_org_return_value(@adv_admin_org_name, @adv_admin_org_abbreviation)
        page.organization_add unless @administering_organization.nil?
      end
    end
  end

#COURSE REQUISITES
  def adding_rule_student_eligibility
    on CmCourseRequisites do |page| unless @student_eligibility_rule.nil?
          page.expand_all_rule_sections
      #STUDENT ELIGIBILITY
      page.add_rule_student_eligibility
      page.add_statement
      page.loading_wait
      page.rule_statement_option.fit @student_eligibility_rule
      page.loading_wait

      if  @student_eligibility_rule == 'Must have successfully completed <course>'
        @student_eligibility_rule_with_value = @student_eligibility_rule.sub('<course>', @student_eligibility_course)

        # Enter text
        if @student_eligibility_add_method == 'text'
          puts 'student text'
          page.rule_course_field.fit @student_eligibility_course
        end

        if @student_eligibility_add_method == 'advanced'
          puts 'student advanced'
          page.advanced_search
          #pick one field
          page.adv_course_title.fit @student_eligibility_title
          page.adv_course_code_rule.fit @student_eligibility_course
          page.adv_plain_text_description_rule.fit @student_eligibility_phrase
          page.adv_search
          #number is the column number 1 = course title, 2 = Course Code, 4 = Description
          return_search_result(@student_eligibility_course, 2)
        end
      end

      page.preview_change
      page.update_rule
                                        page.loading_wait
    end
    end
  end

  def adding_rule_corequisite
    on CmCourseRequisites do |page| unless @corequisite_rule.nil?
      page.expand_all_rule_section
      page.add_rule_corequisite
      page.add_statement
      page.rule_statement_option.fit @corequisite_rule
      page.loading_wait
      #page.rule_course_field.fit @rule_adv_course_code

      if  @corequisiste_rule == 'Must be concurrently enrolled in <course>'
        @corequisite_rule_with_value = @corequisite_rule.sub('<course>', @corequisite_course)

        if @corequisite_add_method == 'text'
          page.rule_course_field.fit @corequisite_course
        end

        if @corequisite_add_method == 'advanced'
          page.advanced_search
          page.adv_course_title.fit @corequisite_title
          page.adv_course_code_rule.fit @corequisite_course
          #bug where description is only displayed as '.'
          #page.adv_plain_text_description_rule.fit @corequisite_phrase

          page.adv_search
          page.loading_wait
          return_search_result(@corequisite_course, 2)
        end
      end
      page.preview_change
      page.update_rule
    end
    end
  end

  def adding_rule_recommended_preparation_rule
    on CmCourseRequisites do |page| unless @recommended_preparation_rule.nil?

    page.expand_all_rule_sections
    page.add_rule_recommended_prep
    page.add_statement
    page.rule_statement_option.fit @recommended_preparation_rule
    page.loading_wait
    @recommended_preparation_rule_with_value = @recommended_preparation_rule.sub('<course>', @recommended_preparation_course)

    if @recommended_preparation_rule == 'Must have successfully completed <course>'

      if @recommended_preparation_add_method == 'text'
        page.rule_course_field.fit @recommended_preparation_course
      end

      if @recommended_preparation_add_method == 'advanced'
        page.advanced_search
        page.adv_course_title.fit @recommended_preparation_title
        page.adv_course_code_rule.fit @recommended_preparation_course
        page.adv_plain_text_description_rule.fit @recommended_preparation_phrase
        page.adv_search
        #number is the column number 1 = course title, 2 = Course Code, 4 = Description
        return_search_result(@recommended_preparation_course, 2)
      end
    end
    page.preview_change
    page.update_rule
    end
    end
  end

  def adding_rule_antirequisite
    on CmCourseRequisites do |page| unless @antirequisite_rule.nil?
      page.expand_all_rule_sections
      page.add_rule_antirequisite
      page.add_statement
      page.rule_statement_option.fit @antirequisite_rule
      page.loading_wait

      if @antirequisite_rule == 'Must not have successfully completed <course>'
        @antirequisite_rule_with_value = @antirequisite_rule.sub('<course>', @antirequisite_course)

        if @antirequisite_add_method == 'text'
          page.rule_course_field.fit @antirequisite_course
        end

        if @antirequisite_add_method == 'advanced'
          page.advanced_search
          #pick one field
          page.adv_course_title.fit @antirequisite_title
          page.adv_course_code_rule.fit @antirequisite_course
          page.adv_plain_text_description_rule.fit @antirequisite_phrase
          page.adv_search
          #number is the column number 1 = course title, 2 = Course Code, 4 = Description
          return_search_result(@antirequisite_course, 2)
        end
      end

      page.preview_change
      page.update_rule
    end
    end
  end

  def adding_rule_repeatable_for_credit
    on CmCourseRequisites do |page| unless @repeatable_for_credit_rule.nil?
      page.expand_all_rule_sections
      page.add_rule_repeatable_for_credit
      page.add_statement
      page.rule_statement_option.fit @repeatable_for_credit_rule
      page.loading_wait

      if @repeatable_for_credit_rule == 'May be repeated for a maximum of <n> credits'
        @repeatable_for_credit_rule_with_value = @repeatable_for_credit_rule.sub('<n>', @repeatable_for_credit_credit)

        page.rule_credit.fit @repeatable_for_credit_credit
      end

      page.preview_change
      page.update_rule
    end
    end
  end

  def adding_course_that_restricts_credits
    on CmCourseRequisites do |page| unless @course_that_restricts_credits_rule.nil?
      page.expand_all_rule_sections
      page.add_rule_restricts_credits
      page.add_statement
      page.rule_statement_option.fit @course_that_restricts_credits_rule
      page.loading_wait

      if @course_that_restricts_credits_rule == 'Must not have successfully completed <course>'
        @course_that_restricts_credits_rule_with_value = @course_that_restricts_credits_rule.sub('<course>', @course_that_restricts_credits_course)

        if @course_that_restricts_credits_add_method == 'text'
          page.rule_course_field.fit @course_that_restricts_credits_course
        end
        if @course_that_restricts_credits_add_method == 'advanced'
          page.advanced_search
          #pick one field
          page.adv_course_title.fit @course_that_restricts_credits_title
          page.adv_course_code_rule.fit @course_that_restricts_credits_course
          page.adv_plain_text_description_rule.fit @course_that_restricts_credits_phrase
          page.adv_search
          #number is the column number 1 = course title, 2 = Course Code, 4 = Description
          return_search_result(@course_that_restricts_credits_course, 2)
        end
      end

      page.preview_change
      page.update_rule
    end
    end
  end

  #AUTHORS AND COLLABORATORS
    def adding_author_name
      on CmAuthorsCollaborators do |page|
        # Need to use auto lookup because when watir types in the parentheses are removed from text field
        if author_name_method == 'auto_lookup'
          page.author_name.fit @author_name_search
          page.auto_lookup @author_display_name
        end

        if author_name_method == 'advanced_name' or author_name_method == 'advanced_username'
          page.advanced_search
          page.adv_name.fit @author_name_search if author_name_method == 'advanced_name'
          page.adv_username_capD.fit @author_username_search if author_name_method == 'advanced_username'
          page.adv_search
          page.adv_return_value_name @author_display_name
        end
      end
    end
  
    def cs_course_proposal_required
      on CmCourseInformation do |page|
        page.course unless page.current_page('Course').exists?

        page.expand_course_listing_section unless page.collapse_course_listing_section.visible?

        fill_out page, :description_rationale, :proposal_rationale
        page.save_progress
      end

      on CmCourseLogistics do |page|
        page.logistics unless page.current_page('Logistics').exists?

        page.loading_wait

        fill_out page,
                 :assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail,
                 :assessment_percentage, :assessment_satisfactory

        fill_out page, :exam_standard, :exam_alternate, :exam_none

        #This 'UNLESS' is required for 'Standard Exam' which, does not have rationale and should skip filling in final_exam_rationale
        #if that radio is selected
        page.final_exam_rationale.fit @final_exam_rationale unless page.exam_standard.set?
        page.save_progress
      end

      on CmCourseFinancials do |page|
        page.financials unless page.current_page('Financials').exists?
        page.loading_wait
        fill_out page, :course_fees

        page.save_progress

      end

    end
  
  

end #class




