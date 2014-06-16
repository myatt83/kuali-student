Given(/^I have a basic course proposal created as Faculty$/) do
  steps %{Given I am logged in as Faculty}
  @course_proposal = create CmCourseProposalObject, :create_new_proposal => false, :create_basic_propsal => true,
                            :proposal_title => random_alphanums(10,'test basic proposal title '),
                            :course_title => random_alphanums(10,'test basic course title ')
  @course_proposal.create_course_continue
  @course_proposal.create_basic_proposal
end

When(/^I add comments to the course proposal$/) do
  @comment_add = make CmCommentsObject
  @comment_add.commentText = random_alphanums(10,'test proposal comment')
  @course_proposal.load_comments_action
  sleep 1
  @comment_add.add_comment (@comment_add.commentText)
  @comment_add.close_comment_dialog
end

Then(/^I should see my comments on the course proposal$/) do
  steps %{And edit the course proposal after finding it}
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (1)"
    page.comment_content_text(0).should == @comment_add.commentText
  end
end

Given(/^I have a basic course admin proposal created as Curriculum Specialist$/) do
  steps %{Given I am logged in as Curriculum Specialist}
  @course_proposal = create CmCourseProposalObject, :create_new_proposal => false, :create_basic_propsal => true,
                            :proposal_title => random_alphanums(10,'test basic proposal title '),
                            :course_title => random_alphanums(10,'test basic course title ')
  @course_proposal.create_course_continue
  @course_proposal.create_basic_proposal
end

Given(/^I have a basic course proposal with comments created as Faculty$/) do
  steps %{Given I have a basic course proposal created as Faculty}
  steps %{When I add comments to the course proposal}
end

When(/^I edit a comment$/) do
  @comment_edit = make CmCommentsObject
  @comment_edit.commentText = random_alphanums(10,'edit proposal comment')
  @course_proposal.load_comments_action
  sleep 1
  @comment_edit.edit_comment(0, @comment_edit.commentText)
  @comment_edit.close_comment_dialog
  on CmCourseInformation do |page|
    if(page.alert.exists?)
      page.alert.ok
    end
  end
end

Then(/^I should see my edited comments on the course proposal$/) do
  steps %{And edit the course proposal after finding it}
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (1)"
    page.comment_content_text(0).should == @comment_edit.commentText
  end
end

And(/^I delete my comments$/) do
  @course_proposal.load_comments_action
  sleep 1
  @comment_add.delete(0)
  @comment_add.close_comment_dialog
end

Then(/^I should not see any comments on the course proposal$/) do
  steps %{And edit the course proposal after finding it}
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (0)"
  end
end

Given(/^I have a basic course admin proposal with comments created as CS$/) do
  steps %{Given I have a basic course admin proposal created as Curriculum Specialist}
  steps %{When I add comments to the course proposal}
end

And(/^I undo delete of one comment$/) do
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (0)"
    page.undo_delete_comment_link.exists?.should == true
    page.undo_delete
    page.close_dialog
  end
end

Then(/^I should see the undeleted comment on the course admin proposal$/) do
  steps %{And review the course proposal after finding it}
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (1)"
    page.comment_content_text(0).should == @comment_add.commentText
  end
end

Given(/^I have a basic course proposal with comments created as Faculty with comments by CS$/) do
  steps %{Given I have a basic course proposal with comments created as Faculty}
  steps %{Given I am logged in as Curriculum Specialist}
  steps %{When I perform a search for the course proposal}
  steps %{And edit the course proposal after finding it}
  steps %{When I add comments to the course proposal}
  steps %{Given I am logged in as Faculty}
end

Then(/^I should see my comments and CS comments on the course proposal$/) do
  steps %{And review the course proposal after finding it}
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (2)"
    page.comment_content_text(0).should == @comment_add.commentText
    (page.comment_header_id_text(0).include? "alice").should == true
    (page.comment_header_id_text(1).include? "fred").should == true
  end
end

And(/^I should not have edit or delete options for CS comments$/) do
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (2)"
    page.comment_edit_link(0).exists?.should == false
    page.comment_delete_link(0).exists?.should == false
    page.comment_edit_link(1).exists?.should == true
    page.comment_delete_link(1).exists?.should == true
  end
end

Then(/^I should see CS comments on the course proposal$/) do
  steps %{And review the course proposal after finding it}
  on CmProposalComments do |page|
    page.comment_list_header_text.should == "Comments (1)"
    page.comment_content_text(0).should == @comment_add.commentText
  end
end

And(/^I should not have ability to add comments$/) do
  on CmProposalComments do |page|
    page.comment_text_input.exists?.should == false
    page.add_comment_button.exists?.should == false
  end
end

And (/^edit the course proposal after finding it$/) do
  @course_proposal.edit_proposal_action
  @course_proposal.load_comments_action
  sleep 1
end

When (/^I delete my comments without closing comment dialog$/) do
  @course_proposal.load_comments_action
  sleep 1
  @comment_add.delete(0)
end

And (/^review the course proposal after finding it$/) do
  @course_proposal.review_proposal_action
  @course_proposal.load_comments_on_review_page
end
