# Created data used for testing
#
# CMRequisitesData contained in Course Proposal
#
# class attributes are initialized with default data unless values are explicitly provided
#
# Typical usage: (with optional setting of explicit data value in  )
#  @editAgenda = make ManageCORequisitesData
#  @editAgenda.create_data_advanced_search(section, course)
# Methods:
#  @initialize(browser, opts={})
#
# Note the use of the ruby options hash pattern re: setting attribute values

class CmRequisiteRuleObject < DataFactory
  include Foundry

  attr_accessor :type,
                :rule,
                :course,
                :completed_course_number,
                :course_combination_type,
                :add_method,
                :search_title,
                :search_phrase,
                :search_course_code,
                :complete_rule_text

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :type => "Student Eligibility & Prerequisite",
        :rule => "Permission of instructor required",
        :course => "ENGL201",
        :add_method => "text",
        :search_title => "English",
        :search_phrase => "ENGL",
        :complete_rule_text => "Permission of instructor required"
    }

    options = defaults.merge(opts)

    set_options(options)
  end



  def edit (opts ={})

  end

  def open_agenda_section
    sections = {"Student Eligibility & Prerequisite"=>:eligibility_prereq_section,
                "Antirequisite"=>:antirequisite_section, "Corequisite"=>:corequisite_section,
                "Recommended Preparation"=>:recommended_prep_section,
                "Repeatable for Credit"=>:repeatable_credit_section,
                "Course that Restricts Credits"=>:restricted_credit_section}
    on CmCourseRequistitesPage do |page|
      page.loading.wait_while_present(60)
      if !page.send(sections[@section]).span(id: /Course-AgendaManage-RulePrototype_rule[A-Z]_toggle_exp/).visible?
        page.send(sections[@section]).when_present.click
      end
    end
  end

  def navigate_to_requisite
    on CmCourseRequistitesPage do |page|
      page.course_requisites unless page.current_page('Course Requisites').exists?
    end
  end

  def preview_rule_changes
    on CmRequisiteRules do |page|
      begin
        page.preview_change('')
      rescue Exception => e
        page.preview_change_btn('node_2_parent_').click
        page.loading.wait_while_present
      end
    end
  end


  def advanced_search(field, code)
    on CmRequisiteRules do |page|
      page.edit_loading.wait_while_present
      if field == "course title"
        page.loading.wait_while_present
        click_search_link( Regexp.new(".*editTree.+proposition\.cluSet\.clus"))
        page.lookup_course_title.when_present.set code
      elsif field == "course set"
        page.loading.wait_while_present
        page.search_link
        page.lookup_set_name.when_present.set code
      elsif field == "program code"
        page.loading.wait_while_present
        click_search_link( Regexp.new(".*editTree.+proposition\.progCluSet\.clus"))
        page.lookup_course_code.when_present.set code
      elsif field == "class standing"
        page.loading.wait_while_present
        click_search_link( Regexp.new(".*editTree.+proposition\.classStanding"))
        page.lookup_class_standing.when_present.set code
      elsif field == "org"
        page.loading.wait_while_present
        click_search_link( Regexp.new(".*editTree.+proposition\.orgInfo\.id"))
        page.lookup_abrev_org.when_present.set code
      elsif field == "population"
        page.loading.wait_while_present
        click_search_link( Regexp.new(".*editTree.+proposition\.orgInfo\.population\.id"))
        page.lookup_population.when_present.set code
      end
      page.lookup_search_button
      page.loading.wait_while_present
      page.return_course_code(/.*#{Regexp.escape(code)}.*/i).a( text: /Select/).click
      page.loading.wait_while_present
    end
  end

  def click_search_link( regex)
    on CmRequisiteRules do |page|
      elements = page.edit_tree_section.elements( :tag_name, 'a')
      elements.each do |elem|
        if elem.text == "Advanced Search" && page.edit_tree_section.a( id: elem.id).attribute_value('data-submit_data') =~ regex
          page.edit_tree_section.a(id: elem.id).click
          break
        end
      end
      raise "co requisites click_search_link: link not found for: #{regex}"
    end
  end

end
