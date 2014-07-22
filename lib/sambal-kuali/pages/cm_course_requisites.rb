class CmCourseRequisitesPage < BasePage

  wrapper_elements
  cm_elements

  action(:expand_all_rule_sections) { |b| b.div(id: "CourseRequisites-DisclosureLinks-AgendaMaintenance").link(text: '[+] expand all').click; b.loading_wait }
  # element(:add_rule_link) { |b| b.link(text: 'Add Rule') }
  action(:collapse_all_rule_sections) { |b| b.link(text: '[-] collapse all').click }

  action(:add_rule) { |node, b| b.div(id: "Course-AgendaManage-AddRule_rule#{node}").link(text: 'Add Rule').click; b.loading_wait }
  action(:edit_rule) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Edit Rule').click; b.loading_wait }
  action(:delete_rule) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Delete Rule').click; b.loading_wait }

=begin
  #student_eligibility: A,G,M,S,Y
  action(:add_rule_student_eligibility) { |node, b| b.div(id: "Course-AgendaManage-AddRule_rule#{node}").link(text: 'Add Rule').click; b.loading_wait }
  action(:edit_rule_student_eligibility) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Edit Rule').click; b.loading_wait }
  action(:delete_rule_student_eligibility) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Delete Rule').click; b.loading_wait }

  #corequisite: B,H,N,T,Z
  action(:add_rule_corequisite) { |node, b| b.div(id: "Course-AgendaManage-AddRule_rule#{node}").link(text: 'Add Rule').click; b.loading_wait }
  action(:edit_rule_corequisite) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Edit Rule').click; b.loading_wait }
  action(:delete_rule_corequisite) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Delete Rule').click; b.loading_wait }

  #recommended_prep: C,I,O,U,AA
  action(:add_rule_recommended_prep) { |node, b| b.div(id: "Course-AgendaManage-AddRule_rule#{node}").link(text: 'Add Rule').click; b.loading_wait }
  action(:edit_rule_recommended_prep) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Edit Rule').click; b.loading_wait }
  action(:delete_rule_recommended_prep) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Delete Rule').click; b.loading_wait }

  #antirequisite: D,J,P,V,AB
  action(:add_rule_antirequisite) { |node, b| b.div(id: "Course-AgendaManage-AddRule_rule#{node}").link(text: 'Add Rule').click; b.loading_wait }
  action(:edit_rule_antirequisite) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Edit Rule').click; b.loading_wait }
  action(:delete_rule_antirequisite) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Delete Rule').click; b.loading_wait }

  #repeatable_for_credit: E,K,Q,W,AC
  action(:add_rule_repeatable_for_credit) { |node, b| b.div(id: "Course-AgendaManage-AddRule_rule#{node}").link(text: 'Add Rule').click; b.loading_wait }
  action(:edit_rule_repeatable_for_credit) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Edit Rule').click; b.loading_wait }
  action(:delete_rule_repeatable_for_credit) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Delete Rule').click; b.loading_wait }

  #restricts_credits: F,L,R,X, AD
  action(:add_rule_restricts_credits) { |node, b| b.div(id: "Course-AgendaManage-AddRule_rule#{node}").link(text: 'Add Rule').click; b.loading_wait }
  action(:edit_rule_restricts_credits) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Edit Rule').click; b.loading_wait }
  action(:delete_rule_restricts_credits) { |node, b| b.div(id: "Course-RuleEdit-ActionLinks_rule#{node}").link(text: 'Delete Rule').click; b.loading_wait }
=end

end #class

