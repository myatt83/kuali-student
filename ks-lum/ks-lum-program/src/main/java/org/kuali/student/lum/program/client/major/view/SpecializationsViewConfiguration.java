package org.kuali.student.lum.program.client.major.view;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import org.kuali.student.common.assembly.data.Metadata;
import org.kuali.student.common.assembly.data.QueryPath;
import org.kuali.student.common.ui.client.configurable.mvc.FieldDescriptorReadOnly;
import org.kuali.student.common.ui.client.configurable.mvc.binding.ListToTextBinding;
import org.kuali.student.common.ui.client.configurable.mvc.binding.ModelWidgetBinding;
import org.kuali.student.common.ui.client.configurable.mvc.sections.VerticalSection;
import org.kuali.student.common.ui.client.configurable.mvc.views.SectionView;
import org.kuali.student.common.ui.client.configurable.mvc.views.VerticalSectionView;
import org.kuali.student.common.ui.client.mvc.Controller;
import org.kuali.student.common.ui.client.widgets.KSCheckBox;
import org.kuali.student.common.ui.client.widgets.field.layout.element.MessageKeyInfo;
import org.kuali.student.common.ui.client.widgets.table.summary.SummaryTableFieldBlock;
import org.kuali.student.common.ui.client.widgets.table.summary.SummaryTableFieldRow;
import org.kuali.student.common.ui.client.widgets.table.summary.SummaryTableSection;
import org.kuali.student.lum.common.client.configuration.AbstractSectionConfiguration;
import org.kuali.student.lum.common.client.widgets.AppLocations;
import org.kuali.student.lum.program.client.ProgramConstants;
import org.kuali.student.lum.program.client.ProgramSections;
import org.kuali.student.lum.program.client.major.MajorEditableHeader;
import org.kuali.student.lum.program.client.major.edit.MajorEditController;
import org.kuali.student.lum.program.client.major.proposal.MajorProposalController;
import org.kuali.student.lum.program.client.properties.ProgramProperties;
import org.kuali.student.lum.program.client.variation.VariationsBinding;

/**
 * @author Igor
 */
public class SpecializationsViewConfiguration extends AbstractSectionConfiguration {

	private Controller controller = null;

    public static SpecializationsViewConfiguration create() {
        return new SpecializationsViewConfiguration(new VerticalSectionView(ProgramSections.SPECIALIZATIONS_VIEW, ProgramProperties.get().program_menu_sections_specializations(), ProgramConstants.PROGRAM_MODEL_ID));
    }

    public static SpecializationsViewConfiguration createSpecial(Controller controller) {
        String title = ProgramProperties.get().program_menu_sections_specializations();
        return new SpecializationsViewConfiguration(new VerticalSectionView(ProgramSections.SPECIALIZATIONS_VIEW, title, ProgramConstants.PROGRAM_MODEL_ID, new MajorEditableHeader(title, ProgramSections.SPECIALIZATIONS_EDIT)), controller);
    }

    private SpecializationsViewConfiguration(SectionView sectionView) {
        rootSection = sectionView;
    }

    private SpecializationsViewConfiguration(SectionView sectionView, Controller controller) {
        rootSection = sectionView;
    	this.controller = controller;
    }

    @Override
    protected void buildLayout() {
		VerticalSection section = new VerticalSection();
    	if (controller instanceof MajorProposalController || controller instanceof MajorEditController) 
       		section.addSection(createSpecializationsSectionEdit());
    	else
    	{
    		KSCheckBox isVariationRequiredCheckBox = new KSCheckBox(ProgramProperties.get().programSpecialization_instructions());
    		isVariationRequiredCheckBox.setEnabled(false);
    		configurer.addReadOnlyField(section, ProgramConstants.IS_VARIATION_REQUIRED, null, isVariationRequiredCheckBox);
    		configurer.addReadOnlyField(section, ProgramConstants.VARIATIONS, new MessageKeyInfo(""), new FlexTable()).setWidgetBinding(new VariationsBinding(AppLocations.Locations.VIEW_VARIATION.getLocation(), false));
    	}
		rootSection.addSection(section);
    }
    
    // Side-by-side comparison (when controller is not null)  
    private SummaryTableSection createSpecializationsSectionEdit() { 
      	SummaryTableSection section = new SummaryTableSection((Controller) controller);     		
      	section.setEditable(false);
      	section.addSummaryTableFieldBlock(createSpecializationsSectionEditBlock());

        return section;
    }

  	@SuppressWarnings("unchecked")
  	public SummaryTableFieldBlock createSpecializationsSectionEditBlock() {
  		SummaryTableFieldBlock block = new SummaryTableFieldBlock();
 
		KSCheckBox isVariationRequiredCheckBox = new KSCheckBox(ProgramProperties.get().programSpecialization_instructions());
		isVariationRequiredCheckBox.setEnabled(false);
		KSCheckBox isVariationRequiredCheckBox2 = new KSCheckBox(ProgramProperties.get().programSpecialization_instructions());
		isVariationRequiredCheckBox2.setEnabled(false);

        block.addSummaryTableFieldRow(getFieldRow(ProgramConstants.IS_VARIATION_REQUIRED, null, isVariationRequiredCheckBox, isVariationRequiredCheckBox2, null, null, false));        
        block.addSummaryTableFieldRow(getFieldRow(ProgramConstants.VARIATIONS, new MessageKeyInfo(""), new FlexTable(),
        		new FlexTable(), null, new VariationsBinding(AppLocations.Locations.VIEW_VARIATION.getLocation(), false), false));        

  		return block;
  	}

  	protected SummaryTableFieldRow getFieldRow(String fieldKey, MessageKeyInfo messageKey) {
  		return getFieldRow(fieldKey, messageKey, null, null, null, null, false);
  	}
     
  	protected SummaryTableFieldRow getFieldRow(String fieldKey,
  			MessageKeyInfo messageKey, Widget widget, Widget widget2,
  			String parentPath, ModelWidgetBinding<?> binding, boolean optional) 
  	{
  		QueryPath path = QueryPath.concat(parentPath, fieldKey);
  		Metadata meta = configurer.getModelDefinition().getMetadata(path);

  		FieldDescriptorReadOnly fd = new FieldDescriptorReadOnly(path.toString(), messageKey, meta);
  		if (widget != null) {
  			fd.setFieldWidget(widget);
  		}
  		if (binding != null) {
  			fd.setWidgetBinding(binding);
  		}
  		fd.setOptional(optional);

  		FieldDescriptorReadOnly fd2 = new FieldDescriptorReadOnly(path.toString(), messageKey, meta);
  		if (widget2 != null) {
  			fd2.setFieldWidget(widget2);
  		}
  		if (binding != null) {
  		    // Pass in a variable to distinguish that this is the right hand column of side-by-side
  		    // so we can disable links in that column.  We created a new constructor to pass this
  		    // variable
  		    if (binding instanceof VariationsBinding){
  		      binding = new VariationsBinding(AppLocations.Locations.VIEW_VARIATION.getLocation(), false, true); 
  		    }
  			fd2.setWidgetBinding(binding);
  		}
  		fd2.setOptional(optional);

  		SummaryTableFieldRow fieldRow = new SummaryTableFieldRow(fd, fd2);

  		return fieldRow;
  	} 
}
