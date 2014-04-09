var dirtyFieldsRefreshed = false;

function toggleAddAOCButton(buttonId, controlId) {
    if ((buttonId=="moveAOCButton" && jQuery("#clusterIDListForAOMove_control").val()== "createNewCluster")
        || buttonId=="addAOCButton" || buttonId=="renameAOCButton") {
        if (jQuery("#"+controlId).val() == "" ) {
            jQuery("#"+buttonId).attr("disabled", "disabled");
        } else {
            jQuery("#"+buttonId).removeAttr("disabled");
        }
    } else {
        jQuery("#"+buttonId).removeAttr("disabled");
    }
}

/* Disables the default-behavior for the ENTER-key in the
 * "Move Activity > Private/Published Names"-fields; replaces instead with
 * a click on the "Move"-button (but only if the button has previously been
 * enabled by toggleAddAOCBUtton() )
 */
function submitMoveAoOnEnterKeyIfValid() {
    var KEYCODE_ENTER = 13;
    var moveAocButton = jQuery( "#moveAOCButton" );

    // cross browser support, use jquery event.which property that normalizes event.keyCode and event.charCode
    var keyPressed = event.which;

    // redirect enter-key to submit via the "Move"-AOC button instead of the default page-submit
    if( keyPressed == KEYCODE_ENTER ) {

        // cross browser support
        event.preventDefault ? event.preventDefault() : event.returnValue = false;
        event.which = 0;

        if( moveAocButton.attr( "disabled" ) == undefined ) {
            moveAocButton.click();
        }

    }

}

function removeCheckboxColumns(column, componentId, functionToCall) {
    var components = jQuery('div[id^="' + componentId + '"]');
    jQuery.each(components, function (index) {
        var subComponentId = jQuery(this).attr('id');
        var table = jQuery(this).find('table');
        var tableId = jQuery(table).attr('id');

        var foundCheckBox = false;

        jQuery('#' + tableId + ' tbody > tr > td:nth-child(' + column + ')').find('[type=checkbox]').each(function () {
            var div = jQuery(this).parent('div');
            if (jQuery(div).is(":visible")) {
                foundCheckBox = true;
                return foundCheckBox;
            }
        });

        if (!foundCheckBox) {
            var th = jQuery('#' + tableId + ' thead tr').find('th:nth-child(' + column + ')');
            jQuery(th).remove();
            jQuery('#' + tableId + ' tbody tr').find('td:nth-child(' + column + ')').each(function () {
                jQuery(this).remove();
            });
            var tf = jQuery('#' + tableId + ' tfoot tr').find('th:nth-child(' + column + ')');
            jQuery(tf).remove();
        } else {
            var toggleCheckbox = jQuery("<input type='checkbox' id='" + subComponentId + "_toggle_control_checkbox'/>");
            var isChecked = toggleCheckbox.prop('checked');
            toggleCheckbox.click(function (e) {
                jQuery('#' + tableId + ' tbody > tr > td:nth-child(' + column + ')').find('[type=checkbox]').each(function () {
                    //jQuery(this).trigger( "click" );
                    jQuery(this).prop('checked', jQuery(toggleCheckbox).prop('checked'));
                    jQuery(this).closest('tr').toggleClass('selected-row', jQuery(this).prop('checked') );
                });
                if (functionToCall) {
                    var target = jQuery.makeArray(functionToCall);
                    var clickFn = new Function(functionToCall)
                    clickFn.call(target);
                }
            });
            var th = jQuery('#' + tableId + ' thead tr').find('th:nth-child(' + column + ')');
            jQuery(th).append(toggleCheckbox);

            var collectionCheckboxes = jQuery('div#'+ subComponentId +' tbody > tr').find('td:first input[type="checkbox"]');
            var clickName;
            collectionCheckboxes.each(function(ndx,ctl) {
                clickName = "click." + subComponentId + "_row_" + ndx;
                jQuery(this).on(clickName, function(){
                    controlCheckboxStatus(subComponentId,this);
                });
            });
        }

    });
}

/*
 *  Collection row checkboxes in the first cell will cause the control checkbox
 *  to be set or unset.  The control checkbox should only be checked when all
 *  collection checkboxes are set, otherwise it should be unchecked.
 */
function controlCheckboxStatus(collectionId,source) {
    var controlCheckbox = jQuery("#" + collectionId + "_toggle_control_checkbox");

    // if any row selection checkbox is false, the control checkbox is false also
    if ( ! jQuery(source).prop('checked')) {
        controlCheckbox.prop('checked',false);
        return;
    }

    // if all row selection checkboxes are true, make the control checkbox true also
    var areAllRowsChecked = true;
    jQuery('div#' + collectionId + ' tbody > tr').find('td:first input[type="checkbox"]')
        .each(function(ndx,ctl) {
            if (!jQuery(this).prop('checked')) {
                areAllRowsChecked = false;
                return false; // exit .each()
            }
        });
    controlCheckbox.prop('checked',areAllRowsChecked);
}

function addActionColumn(isReadOnly, componentId) {
    if (isReadOnly) {
        // Find the table
        var div = jQuery('#' + componentId);
        var table = jQuery(div).find('table');
        var tableId = jQuery(table).attr('id');

        // Create the header
        var thHeader = jQuery('<th scope="col" colspan="1" rowspan="1" class="infoline sorting_disabled" role="columnheader" aria-label="ACTIONS"/>');
        var spanHeader = jQuery('<span id="action_manage_header_span" class="infoline"/>');
        var labelHeader = jQuery('<label id="action_manage_header_label" for="">ACTIONS</label>');
        spanHeader.append(labelHeader);
        thHeader.append(spanHeader);

        // Add the Header
        jQuery('#' + tableId + ' thead tr').append(thHeader);

        jQuery('#' + tableId + ' tbody tr').each(function () {

            var firstColumnDiv = jQuery(this).find('td').find('div');
            var fristColumnId = jQuery(firstColumnDiv).attr('id');
            var index = fristColumnId.substring(fristColumnId.lastIndexOf('_line') + '_line'.length, fristColumnId.length);

            // Create the body
            var tdBody = jQuery('<td role="presentation" colspan="1" rowspan="1" class="uif-field uif-fieldGroup uif-horizontalFieldGroup" style="text-align: left;"/>');
            var bodyDiv1 = jQuery('<div id="bodyDiv1_line' + index + '" class="uif-field uif-fieldGroup uif-horizontalFieldGroup" style="text-align: left;" data-parent="' + componentId + '" data-label="ACTIONS" data-group="bodyDiv2_line' + index + '"/>');
            tdBody.append(bodyDiv1);
            var bodyFieldset = jQuery('<fieldset aria-labelledby="bodyDiv1_line' + index + '_label" id="bodyDiv1_line' + index + '_fieldset"/>');
            bodyDiv1.append(bodyFieldset);
            var bodyLegend = jQuery('<legend style="display: none">ACTIONS</legend>');
            bodyFieldset.append(bodyLegend);
            var bodyDiv2 = jQuery('<div id="bodyDiv2_line' + index + '" class="uif-group uif-boxGroup uif-horizontalBoxGroup" style="text-align: left;" data-parent="bodyDiv1_line' + index + '"/>');
            bodyFieldset.append(bodyDiv2);
            var bodyDiv3 = jQuery('<div id="bodyDiv3_line' + index + '" class="uif-validationMessages uif-groupValidationMessages"style="display: none;" data-messagesfor="bodyDiv2_line' + index + '"/>');
            bodyDiv2.append(bodyDiv3);
            var bodyDiv4 = jQuery('<div id="bodyDiv4_line' + index + '_boxLayout" class="uif-boxLayout uif-horizontalBoxLayout clearfix"/>');
            bodyDiv2.append(bodyDiv4);
            var bodyAnchor = jQuery('<a id="bodyAnchor_line' + index + '" onclick="return false;" class="uif-action uif-actionLink uif-navigationActionLink" tabindex="0" data-ajaxreturntype="update-component" data-loadingmessage="Loading..." data-disableblocking="false" data-ajaxsubmit="false" data-refreshid="' + componentId + '" data-validate="false">Manage</a>');
            bodyDiv4.append(bodyAnchor);

            jQuery(this).append(tdBody);

            jQuery('#bodyAnchor_line' + index).data('submitData', {
                "methodToCall":"loadAOs_RGs_AOCs",
                "actionParameters[selectedCollectionPath]":"courseOfferingResultList",
                "actionParameters[selectedLineIndex]":index,
                "jumpToId":componentId
            });

            jQuery('#' + 'bodyAnchor_line' + index).click(function (e) {
                e.preventDefault();
                if (jQuery(this).hasClass('disabled')) {
                    return false;
                }
                if (checkDirty(e) == false) {
                    actionInvokeHandler(this);
                }
            });

            jQuery('#bodyDiv2_line' + index).data('validationMessages', {
                summarize:true,
                displayMessages:true,
                collapseFieldMessages:true,
                displayLabel:true,
                hasOwnMessages:false,
                pageLevel:false,
                forceShow:true,
                sections:[],
                order:[],
                serverErrors:[],
                serverWarnings:[],
                serverInfo:[]
            });

        });
        // Create the footer
        var thFooter = jQuery('<th rowspan="1" colspan="1"/>');

        // Add the Footer
        jQuery('#' + tableId + ' tfoot tr').append(thFooter);
    }
}

function addNewClusterOptionSuccessCallBack(){
    retrieveComponent('KS-CourseOfferingManagement-MoveAOCPopupForm',undefined, addNewClusterOption, undefined);
}

/* This function contains some temporary fixes for jira 6319 */
function addNewClusterOption(responseContents) {
    jQuery('#foNameForAOMoveId').hide();
    jQuery('#privateNameForMoveId').hide();
    jQuery('#publishedNameForMoveId').hide();

    var dropDown = jQuery('#clusterIDListForAOMove_control');
    var container = jQuery('#KS-CourseOfferingManagement-AOClustersCollection');
    var checkedCBs = jQuery(container).find(":checkbox:checked[name$=isCheckedByCluster]");
    if (checkedCBs.length == 0) {
        var createNewClusterOption = new Option("No Activity has been selected", "");
        jQuery('#clusterIDListForAOMove_control option').remove();
        jQuery(dropDown).append(createNewClusterOption);
        jQuery('#KS-CourseOfferingManagement-MoveAOCPopupForm').find(':contains("Move")').closest('button').attr("disabled", "disabled");
    } else {
        if (jQuery(dropDown).find('option[value=""]').length == 0) {
            if (jQuery(dropDown).find('option[value=createNewCluster]').length == 0) {
                var createNewClusterOption = new Option("Create new Cluster...", "createNewCluster");
                jQuery(dropDown).append(createNewClusterOption).change(function () {
                    if (jQuery(dropDown).val() == "createNewCluster") {
                        jQuery('#foNameForAOMoveId').show();
                        jQuery('#privateNameForMoveId').show();
                        jQuery('#publishedNameForMoveId').show();
                    } else {
                        jQuery('#foNameForAOMoveId').hide();
                        jQuery('#privateNameForMoveId').hide();
                        jQuery('#publishedNameForMoveId').hide();
                    }
                });
            }
        }
    }
}

function removeZebraColoring(id){
    var aoDivs = jQuery('div[id^="' + id + '"]');

    jQuery.each(aoDivs, function(index){
        var rows = jQuery(this).find('table tbody tr');
        jQuery.each(rows, function(index){
            jQuery(this).removeClass('odd');
            jQuery(this).removeClass('even');
        });
    });

}

function createErrorDiv(message, url, controlId) {
    var div = jQuery('<div id="' + controlId + '_messageDiv" class="uif-clientMessageItems uif-clientErrorDiv" style="display: none;"/>');
    var ul = jQuery("<ul/>");
    var li = jQuery("<li class='uif-errorMessageItem-field'/>");
    var image = jQuery("<img class='uif-validationImage' src='" + url + "/themes/ksboot/images/validation/error.png' alt='Error'>" + message + "</img>");
    //jQuery(image).text(message);
    jQuery(li).append(image);
    jQuery(ul).append(li);
    jQuery(div).append(ul);

    return div;
}

function createErrorTable(url) {
    var table = jQuery("#errorTable");
    if (jQuery(table).length) {
        return table;
    }
    table = jQuery('<table id="errorTable" style="display: none; position: absolute;"/>');
    var tbody = jQuery('<tbody/>');
    jQuery(table).append(tbody);
    var tr1 = jQuery('<tr/>');
    jQuery(tr1).append('<td class="jquerybubblepopup-top-left" style="background-image:url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/top-left.png); padding : 0px;"/>');
    jQuery(tr1).append('<td class="jquerybubblepopup-top-middle" style="background-image:url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/top-middle.png); padding : 0px;"/>');
    jQuery(tr1).append('<td class="jquerybubblepopup-top-right" style="background-image:url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/top-right.png); padding : 0px;"/>');
    var tr2 = jQuery('<tr/>');
    jQuery(tr2).append('<td class="jquerybubblepopup-middle-left" style="background-image:url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/middle-left.png); padding : 0px;"/>');
    var td2 = jQuery('<td class="jquerybubblepopup-innerHtml" style="padding : 0px;" />');
    jQuery(tr2).append(td2);
    var div2 = jQuery('<div id="_messageDiv" class="uif-clientMessageItems uif-clientErrorDiv"/>');
    jQuery(td2).append(div2);
    jQuery(tr2).append('<td class="jquerybubblepopup-middle-right" style="background-image:url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/middle-right.png); padding : 0px;"/>');
    var tr3 = jQuery('<tr/>');
    jQuery(tr3).append('<td class="jquerybubblepopup-bottom-left" style="background-image:url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/bottom-left.png); padding : 0px;"/>');
    var td3 = jQuery('<td class="jquerybubblepopup-bottom-middle" style="background-image: url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/bottom-middle.png); text-align: left; padding : 0px; "/>');
    var image3 = jQuery('<img src="../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/tail-bottom.png" alt="" class="jquerybubblepopup-tail"/>');
    jQuery(td3).append(image3);
    jQuery(tr3).append(td3);
    jQuery(tr3).append('<td class="jquerybubblepopup-bottom-right" style="background-image:url(../plugins/tooltip/jquerybubblepopup-theme/kr-error-cs/bottom-right.png); padding : 0px;"/>');
    jQuery(tbody).append(tr1);
    jQuery(tbody).append(tr2);
    jQuery(tbody).append(tr3);
    jQuery(document.body).append(table);
    return table;
}

function highlightElements(validationJSONString, isValid, url) {
    if (!isValid) {
        var table = createErrorTable(url);
        jQuery.each(validationJSONString, function (id, message) {
            var errorContainer = jQuery('[id^="' + id + '"][id$="_errors"]:first');
            var controlDiv = jQuery('[id^="' + id + '"][id$="_control"]:first');
            var parentDiv = jQuery(controlDiv).parent().closest('div');
            var id = jQuery(parentDiv).attr("id");
            var errorDiv = createErrorDiv(message, url, id);

            jQuery(parentDiv).append(errorDiv);
            jQuery(controlDiv).addClass("ks-uif-hasError");
            jQuery(controlDiv).hover(
                function (e) {
                    var moveLeft = -20;
                    var moveDown = -60;
                    var id = jQuery(this).parent().closest('div').attr("id");
                    var thisErrorDiv = jQuery('#' + id + '_messageDiv');
                    var div = jQuery(table).find('[id$="_messageDiv"]');
                    jQuery(table).find('[id$="_messageDiv"]').html(jQuery(thisErrorDiv).html());
                    jQuery(table).find('[id$="_messageDiv"]').text(jQuery(thisErrorDiv).text());
                    jQuery('table#errorTable').show().css('top', jQuery(this).offset().top + moveDown).css('left', jQuery(this).offset().left + moveLeft);
                },
                function (e) {
                    jQuery('table#errorTable').hide();
                }
            );
        });
    }
}

/*
 Because the context bar resides in the topGroup, it only gets loaded once when the view is loaded. Because the context
 bar needs to update every time the page loads, we have to create some custom JS to update items in the context bar.

 Note: passing null for the 1st-argument (or if an element for 1st-argument is not found) will result in element targeted
 by 2nd-argument being emptied without getting a replacement.

 Since Upgrade  2.3 the context bar is replaced on page_load, however on get the context bar is remained the same
 So on page load we are loading the component that krad is replacing the context bar with instead of the place holder.
 */
// KSENROLL-9951 - Rice Trackback - topGroup should only be displayed when there are items to show
function updateContextBar(contextBarId) {
    if (!initialViewLoad) {
        var topGroupUpdateContextbar = jQuery("#" + kradVariables.TOP_GROUP_UPDATE + " > #" + contextBarId);
        var bc = jQuery("#Uif-BreadcrumbWrapper");
        var vh = jQuery(".uif-viewHeader-contentWrapper.uif-sticky");
        var applicationHeaderWrapper = jQuery("#Uif-ApplicationHeader-Wrapper");

        if (topGroupUpdateContextbar.length) {
            var contextBarHeight = topGroupUpdateContextbar.outerHeight(true);
            if (contextBarHeight > 0) {

                var applicationHeaderWrapperHeight = 0;
                if (applicationHeaderWrapper.length) {
                    applicationHeaderWrapperHeight = applicationHeaderWrapper.outerHeight(true);
                }
                var bcHeight = 0;
                if (jQuery(bc).length) {
                    bcHeight = bc.outerHeight(true);
                }
                var vhOffset = applicationHeaderWrapperHeight + bcHeight + contextBarHeight;
                if (jQuery(vh).length) {
                    vh.offset({top: vhOffset});
                    vh.data("offset", vh.offset());
                }
            }
        }
    }
}

/*
 This method is for removing the empty context bar from a page.
 The position of the page header is repositioned accordingly
 */
function removeEmptyContextBar(contextBarId){
    if ((contextBarId==null) || (contextBarId.length==0)) return;

    var contextBar = jQuery("#" + contextBarId);

    if (contextBar && contextBar.css('display') != "none") {
        //hide the empty context bar
        contextBar.hide();

        //re-position header.
        var headerDiv = jQuery(".uif-viewHeader-contentWrapper");
        if (headerDiv) {
            var headerOffsetTop = headerDiv.offset().top - contextBar.height();
            headerDiv.offset({top:headerOffsetTop});
            headerDiv.data("offset", headerDiv.offset());
        }
    }
}

/*
 This method is for putting the page header back to the original position
 */
function resetHeaderPosition(contextBarId){
    if ((contextBarId==null) || (contextBarId.length==0)) return;

    var contextBar = jQuery("#" + contextBarId);
    if (contextBar && contextBar.css('display') != "none") {

        var headerDiv = jQuery(".uif-viewHeader-contentWrapper");
        var headerOffsetTop = parseInt(headerDiv.offset().top);
        var stickyContentOffsetTop = parseInt(stickyContentOffset.top);

        //if headerOffsetTop equals to stickyContentOffsetTop, it means the header position has
        //been adjusted back and no need to adjust it again
        if (headerDiv && (headerOffsetTop != stickyContentOffsetTop)) {
            var headerOffsetTop = headerDiv.offset().top + contextBar.height();

            //adjust header back to the original position
            headerDiv.offset({top:headerOffsetTop});
            headerDiv.data("offset", headerDiv.offset());
        }
    }
}

function updateHeaderRightGroup(srcId, rightGroupId){

    if( rightGroupId == null ) return;

    var rightGroup = jQuery("#" + rightGroupId);    // grab the placeholder
    if( rightGroup ) {
        var src = jQuery("#" + srcId);                  // grab the new header right group
        jQuery(rightGroup).html(jQuery(src).html());         // copy the content of the right group to the place holder
        addBootstrapImageToLink(rightGroupId);          // add web-font (icons) to the links if there are any
    }
}

// note: this seems to only apply to views that use the unified-header; maybe this should be named more explicitly?
function updateViewHeader(newHeaderTextSource){
    // Get Header node
    var header = jQuery("div.ks-unified-header h1.uif-headerText span.uif-headerText-span");
    var source = jQuery("#"+newHeaderTextSource+"_span");
    var newHeaderText = source.html();
    // Update header text
    header.html(newHeaderText);
}

/*
 The users wanted to have a small strip of color that coincides with the term. If the user hasn't configured
 an explicit color then use the default coloring from a gradient.

 In this implementation we pass in the day of the year that you want to show a color for.
 Ie. Jan 1 = 1st day of year. The code does 1/365 = some %. that % is used to find how far
 down the gradient we pick a color.
 The default gradient goes : ice blue(winter), green(spring),yellow(summer), brown(fall), ice blue(winter).
 two winters because winter starts at the end of the year and goes through February.
 */
function setSeasonalColor(elementToColor, dayOfYear, baseUrl) {
    if(console){
        console.log("Called setSeasonalColor()");
    }
    if (dayOfYear > 0 && dayOfYear <= 365) {
        var image = jQuery('<img src="' + baseUrl + '/themes/ksboot/images/season_gradient.png"/>');
        var elemToColor;
        if (!initialViewLoad) {
            elemToColor = jQuery("#" + kradVariables.TOP_GROUP_UPDATE + " > #" + elementToColor);
        }else{
            elemToColor = jQuery('#' + elementToColor);
        }
        var percentage = dayOfYear / 365;

        image.load(function () {
            var canvas = document.createElement('canvas');
            var tw = this.width;
            var th = this.height;
            canvas.width = this.width;
            canvas.height = this.height;
            canvas.getContext('2d').drawImage(this, 0, 0, tw, th);
            var x = 10;
            var y = th * percentage;
            var p = canvas.getContext('2d').getImageData(x, y, 1, 1).data;
            var hex = "#" + ("000000" + rgbToHex(p[0], p[1], p[2])).slice(-6);

            elemToColor.css("border-left", '8px solid ' + hex);
        });
    }
}

function rgbToHex(r, g, b) {
    if (r > 255 || g > 255 || b > 255)
        throw "Invalid color component";
    return ((r << 16) | (g << 8) | b).toString(16);
}

/*
 This function changes the UifImage components to bootsrtap links
 It only converts images that have their styles starting with the word icon

 I am now using class since the style is not rendered in IE.
 */
function addBootstrapImageToLink() {
    jQuery("img[class*=ks-fontello-icon-]").each(function () {
        /*Style is not rendered in IE in krad. Use class*/
        var src = jQuery.grep(this.className.split(" "), function(v, i){
            return v.indexOf('ks-fontello-icon-') === 0;
        }).join();
        var parent = jQuery(this).parent();
        if (jQuery(parent).is("span")) {
            parent.addClass(src);
            jQuery(this).remove();
        } else {
            var imagePosition = jQuery(parent).data("imageposition");
            var aText = parent.text();
            parent.text("");
        var imageFont = '<i class="' + src + '"></i>';
        if (imagePosition != undefined && imagePosition == 'right') {
            imageFont = jQuery.trim(aText) + imageFont;
        } else {
            imageFont = imageFont + jQuery.trim(aText);
        }
            jQuery(parent).append(imageFont);
        }
    });
}


/**
 * Processes the form before attempting to save.
 */
function saveAcalPreProcess(returnFieldId){
    //set the default tab to the current active tab
    var activeTabIndexId = jQuery("#acal_tabs_tabs").tabs("option", "active");
    var defaultTabToShow = jQuery("#default_tab_to_show_control");
    if (activeTabIndexId == "0") {
        defaultTabToShow.val("info");
    } else if (activeTabIndexId == "1") {
        defaultTabToShow.val("term");
    }

    //find the dirty fields
    findDirtyFields(returnFieldId);
}

/**
 * Gathers property names of all fields that have been changed on the page.
 * Properties are stored in a csv string and returned by the passed in object.
 */
function findDirtyFields(returnFieldId){
    var dirtyFields = jQuery('.dirty');
    var returnObject = jQuery('#'+returnFieldId+'_control');
    var returnString="";
    for (i=0;i<dirtyFields.length;i++){
        returnString=returnString+dirtyFields[i].name+",";

    }
    returnObject[0].value=returnString;
    dirtyFieldsRefreshed=false;
}

/*
 The users want the page titling to be dynamically-updated depending on which particular page is showing at the time.
 For example, when the ManageCO landing-page is first shown, by default the view-title is 'Course Offerings' but
 if you conduct a subject-search (say, WMST) they want the view-title to be "WMST: Women's Studies".
 */
function updateViewHeaderText( value ) {
    //  If value is empty then don't update the view header (e.g. the session times out and the user clicks a breadcrumb).
    if (value) {
        jQuery( 'div.uif-formView h1.uif-headerText span.uif-headerText-span' ).html( value );
    }
}

/**
 * Function for a delayed removal of the highlighting of added collection items.
 *
 * @return {Function} - Function to be ran after delay.
 */
function removeNewItemHighlights(){
    return function(){
        var newItems = jQuery(".uif-newCollectionItem");
        newItems.removeClass('uif-newCollectionItem');
        newItems.addClass('highlight_fadeout');
    }

}


/*
 Replaces the checkbox inputs with radio in a table.
 It does not change the name attribute and therefore each radio button
 has a click event which deselects the previous selected radio button.
 */
function replaceCheckBoxWithRadio(containerId) {
    var selectedRadio;
    jQuery("#" + containerId).find("input:checkbox").each(function () {
        jQuery(this).replaceWith(
            jQuery("<input>", {
                    type:'radio',
                    id:jQuery(this).attr('id'),
                    name:jQuery(this).attr('name'),
                    value:jQuery(this).attr('value')
                }
            ).click(function(){
                    if(selectedRadio !== null){
                        jQuery(selectedRadio).prop("checked", false);
                    }
                    selectedRadio = this;
                })
        );
    });
}

/**
 * Resets all dialog responses to unchecked
 */
function resetDialogResponses(){
    var dialogResponses = jQuery('input.uif-dialogButtons');
    for(i =0; i < dialogResponses.length; i++){
        dialogResponses[i].checked=false;
    }
}

function resetDirtyFields(returnFieldId){
    if(!dirtyFieldsRefreshed){
        resetDialogResponses();
        var dirtyFields = jQuery('#'+returnFieldId+'_control');
        if(dirtyFields.length==0) return;
        if(typeof dirtyFields[0].value === 'undefined') return;
        var fields = dirtyFields[0].value.split(',');
        for(i =0; i<fields.length;i++){
            var field = fields[i];
            if(field.length==0) continue;
            var marker = jQuery('[name="'+field+'"]');
            marker.addClass('dirty');
        }
        dirtyFieldsRefreshed=true;
    }
}

/**
 * hide the add exam period button based on if there is already an existing exam
 * period attached to the given term after the add exam period button is clicked
 * and then the exam period component is reloaded
 *
 * @param collectionGroupId: id of each collection group item
 * @param lineIdPrefix: id prefix of exam period line
 * @param addExamPeriodButtonIdPrefix: id prefix of add exam period button
 */
function hideAddExamPeriodButton(collectionGroupId, lineIdPrefix, addExamPeriodButtonIdPrefix) {
    var termLineIndex = collectionGroupId.substring(collectionGroupId.lastIndexOf('line'));
    var lineId = lineIdPrefix + '_' + termLineIndex + '_line0';
    var addExamPeriodButtonId = addExamPeriodButtonIdPrefix + '_' + termLineIndex;

    if (jQuery("#"+lineId).length > 0) {
        jQuery("#"+addExamPeriodButtonId).hide();
    }

}

/**
 * display or hide the add exam period button based on if there is already an existing exam
 * period attached to the given term when the term section is loaded
 *
 * @param examPeriodSectionId: id of exam period component
 * @param lineIdPrefix: id prefix of exam period line
 * @param addExamPeriodButtonIdPrefix: id prefix of add exam period button
 */
function initAddExamPeriodButtons(examPeriodSectionId, lineIdPrefix, addExamPeriodButtonIdPrefix, baseUrl) {
    var examPeriodIdPrefix = examPeriodSectionId + '_line';
    var examPeriodComponents = jQuery('div[id^="' + examPeriodIdPrefix + '"]');

    jQuery.each(examPeriodComponents, function (index) {
        var lineId = lineIdPrefix + '_line' + index + '_line0';
        var addExamPeriodButtonId = addExamPeriodButtonIdPrefix + '_line' + index;
        var exam_date_start_date = 'exam_date_start_date_line' + index + '_line0_control';
        var exam_date_end_date = 'exam_date_end_date_line' + index + '_line0_control';

        if (jQuery("#"+lineId).length > 0) {
            jQuery("#"+addExamPeriodButtonId).hide();
        }
    });

}

function toggleTextBoxes(textBox){
    jQuery(textBox).bind('input', function(event) {
        jQuery(textBox).keyup();
    });
}

/* new function to display calendar information (title and dates) on fly (when someone type it in)
 called from AcademicCalendarEditPage.xml
 KSENROLL-8189
 */
function displayCalendarInformation(jqObject, fieldId){
    var currentId = jqObject.attr('id');
    if (currentId.indexOf("_control") == -1) {
        currentId = currentId + "_control";
    }
    var element = jQuery('#' + currentId);
    var value = element.val();

    var field = jQuery('#' + fieldId + ' span[class=uif-readOnlyContent]');
    field.text(value);
}

function stepBrowserBackTwoPages() {
    window.history.back(-2);
}

/*
Added below method for fixing issue KSENROLL-9127-Create Academic Calendar Stacktrace.
This method is called from AcademicCalendarEditPage.xml
*/
function disableIfSelectedValue(matchText,dropDownId,buttonId) {
    var selectedText = jQuery('#'+ dropDownId).find(":selected").val();
    var field = jQuery('#'+buttonId);
    if(selectedText==matchText) {
        field.addClass("disabled");
        field.attr("disabled","disabled");
    } else {
        field.removeClass("disabled");
        field.removeAttr("disabled");
    }
}

/* Renaming Twitter Boostrap button() function to avoid conflict with jQueryUI's button() function
  This problem manifested with dialog radio buttons that did not get the treatment from jQueryUI
  to render them as regular button inputs.

  To use the Twitter Boostrap button() function, use bootstrapBtn() instead.

  KSENROLL-9338
 */
jQuery.fn.bootstrapBtn = jQuery.fn.button.noConflict();

/*
Below method helps to un-check all checkboxes exist in the given Container Id.
 */
function resetCheckboxes(containerId) {
    var components = jQuery("div[id^='"+containerId+"']");
    jQuery.each(components, function (index) {
        jQuery(this).find("input:checkbox").each(function () {
            jQuery(this).prop("checked", false);
        });
    });
}

/*
 Capture checkbox input check/uncheck to add a class for styling selected
 rows in datatables
 */
jQuery(document).on("click", ".dataTable input[type=checkbox]", function() {
    if(jQuery(this).parent('div.non-row-selector').length > 0){
        return;
    }
    jQuery(this).closest('tr').toggleClass('selected-row');
    handleEventforDisabledElements();
});

/*
 Due to conflicts with jQueryUI, we need to cleverly manipulate the icons used
 for the datepicker trigger (the little calendar icon)
 */

/* Set custom font icons for calendar datepickers */
jQuery(document).on('DOMNodeInserted', 'button.ui-datepicker-trigger', function() {
    jQuery(this).empty().addClass('btn-link ks-fontello-icon-calendar').attr('alt', 'Date picker').attr('value', 'Date picker');
});

/*
Skip dirty check
*/
 function skipDirtyChecks() {
 dirtyFormState.skipDirtyChecks=true;
 }

function retrieveFinalExamMatrix (id, methodToCall, dropdownId) {
    var dropDownElement = jQuery('#' + dropdownId + '_control');
    if (dropDownElement.val() != 'na') {
        retrieveComponent(id, methodToCall);
    }
}

function toggleButtonOnInput(fieldId, buttonId) {
    var inputVal = jQuery('#' + fieldId + '_control').val();
    if (inputVal != '') {
        jQuery('#' + buttonId).removeAttr("disabled");
    } else {
        jQuery('#' + buttonId).attr("disabled", "disabled");
    }
}

function toggleShowButton() {
    var termCodeVal = jQuery("#termCodeField_control").val();
    var inputCodeVal = jQuery("#inputCode_control").val();
    if (termCodeVal != '' && inputCodeVal != '') {
        jQuery("#show_button").removeAttr("disabled");
    } else {
        jQuery("#show_button").attr("disabled", "disabled");
    }
}

/*
 * Setup a blur on the given element once the window loads. This idea here is to make the client-side validation happen
 * after the page is completely loaded, so that any server-side messages will be merged with the client side messages.
 */
function triggerFieldValidationAfterPageLoads(id) {
    var element = jQuery(id);
    if (element.val()) {
        jQuery(window).load(function() { element.trigger('blur'); });
    }
}

/* Disabling the enter key pressed in text fields, checkboxes, radio buttons, and option selection widgets
 * so that the only the links, buttons will respond to the enter key.
*/
function dismissEnterKeyAction(event) {
    // cross browser support, use jquery event.which property that normalizes event.keyCode and event.charCode
    var keyPressed = event.which;

    if (keyPressed == 13) {
        if (event.target.type == "text" || event.target.type == "checkbox" ||
            event.target.type == "radio" || event.target.type == "select-one") {

            // cross browser support
            event.preventDefault ? event.preventDefault() : event.returnValue = false;
            event.which = 0;
        }
    }
}

/*
 * TODO: KSENROLL-11002 - This is a hack to work around a Rice bug.
 */
function fixActionLinkJumpToIds(actionLinksId, jumpToElementId) {
    var idSelector =  "[id^=" + actionLinksId + "]";
    jQuery(idSelector).each(
        function() {
            var actionLink = jQuery( this );
            var submitData = jQuery.parseJSON(actionLink.attr("data-submit_data"))
            submitData.jumpToId = jumpToElementId;
            actionLink.attr("data-submit_data", JSON.stringify(submitData));
        });
}

/*
 Capture click events on rows in datatables
 */
jQuery(document).on("click", ".dataTable tbody tr", function (e) {
    if (jQuery(e.target).is(":checkbox") || jQuery(e.target).is("a") || jQuery(e.target).is("i")) {

        // stop the bubbling to prevent firing the row's click event
        e.stopPropagation();
    } else {
        jQuery(this).find(':checkbox').each(function(){
            var $checkbox = jQuery(this);
            if($checkbox.parent('div.non-row-selector').length > 0){
                return;
            }
            $checkbox.attr('checked', !$checkbox.attr('checked'));
            jQuery(this).closest('tr').toggleClass('selected-row', $checkbox.prop('checked'));
            $checkbox.trigger("change");
            handleEventforDisabledElements();

            var $table = jQuery(this).closest('table');
            var $toggleCB = jQuery('input:checkbox[id$="_toggle_control_checkbox"]');
            if ($toggleCB.length > 0) {
                var toggleCBId = $toggleCB.attr('id');
                var subComponentId = toggleCBId.split('_toggle_control_checkbox')[0];
                controlCheckboxStatus(subComponentId, $checkbox);
            }
        });
    }
});

/*
    Perform operations as Document is loaded
 */
jQuery(function () {
    handleEventforDisabledElements();
    addBootstrapImageToLink();
});

/*
    Apply changes to the DOM as elements are inserted.
 */
jQuery(document).on('DOMNodeInserted', function (e) {
    var element = e.target;
    if (jQuery(element).is('div.uif-page')) {
        handleEventforDisabledElements();
        addBootstrapImageToLink();
    } else if (jQuery(element).is('div.uif-tableCollectionSection')) {
        addBootstrapImageToLink();
    }
});

/*
    This function displays the tooltip on disabled buttons.
    Furthermore it adds a dotted underline to buttons's label.
    It has not been tested for any other element except for the toolbar buttons.
    To add this functionality to a button add the class 'ks-enableMouseOver' to the button.
    Please note that this will not work if the page is not reloaded.
 */
function handleEventforDisabledElements() {
    jQuery(".ks-enableMouseOver").each(function () {
        var id = jQuery(this).attr('id');
        jQuery(this).contents().filter(function() {
            return this.nodeType === 3;
        }).wrap( '<span class="ks-dotted-underline"></span>' );
        var divId = "disabled-div-for-" + id;
        var div = jQuery("#" + divId);
        if (jQuery(div).length == 0) {
            div = jQuery('<div id="' + divId + '" class="tree-bar-button-container uif-boxLayoutHorizontalItem" />');
            jQuery(this).after(div);
            jQuery('[data-for=' + id + ']').each(function () {
                var dataValue = jQuery(this).val();
                dataValue = dataValue.replace(id, divId);
                if (jQuery.browser.msie) {
                    eval("new " + dataValue + ";");
                } else {
                eval(dataValue);
                }
            });
        }
        jQuery(div).css({"position": "absolute", "top": jQuery(this).offset(top) + "px", "left": jQuery(this).offset().left + "px", "background": "transparent"});
        jQuery(div).height(jQuery(this).height());
        jQuery(div).width(jQuery(this).width());
        if (jQuery(this).is(':disabled')) {
            jQuery(div).css('z-index', parseInt(jQuery(this).css('z-index')) + 1);
            if (!div.HasBubblePopup()) {
                div.addClass("uif-tooltip");
                div.CreateBubblePopup(".uif-tooltip");
            }
        } else {
            jQuery(div).css('z-index', parseInt(jQuery(this).css('z-index')) - 1);
        }
    });
}

var inlineTableInitialFields = {};
var prefix = 'inline_field_index_';

function toggleInlineRow(event, saveInitialValues){
    var row = jQuery(event.target).closest('tr');
    var index = jQuery(row).index();
    var selectedIndex = prefix + index;
    var initialValues = [];
    jQuery(row).find('.toggleable-element').each(function(){
        if(jQuery(this).hasClass("on")) {
            jQuery(this).switchClass("on", "off");
        } else {
            jQuery(this).switchClass("off", "on");
        };
        if(saveInitialValues){
            var id = jQuery(this).attr("id");
            var controlId = id + '_control';
            var control = jQuery("#" + controlId);
            if(control.length){
                var initialValue = {};
                initialValue['id'] = controlId;
                var value;
                if(jQuery('#' + controlId).is(':checkbox')){
                    value = control.is(':checked');
                }else{
                    value = control.val();
                }
                initialValue['value'] = value;
                initialValues.push(initialValue);
            }
        }
    });
    inlineTableInitialFields[selectedIndex] = initialValues;
}

function editInlineRow(event){
    //resetInlineEditCheckboxReadonlyValue(event);
    toggleInlineRow(event, true);
}

function cancelInlineRow(event){
    // Reset the fields in the selected row
    var row = jQuery(event.target).closest('tr');
    var index = jQuery(row).index();
    var selectedIndex = prefix + index;
    var initialValues = inlineTableInitialFields[selectedIndex];
    jQuery.each(initialValues, function(i, initialValue){
        //if(jQuery('#' + initialValue.id).is(':not(:checkbox)') && jQuery('#' + initialValue.id).parent('div.toggleable-element').parent('div').find(':checkbox').length > 0){
        if(jQuery('#' + initialValue.id).is(':checkbox')){
            //setInlineEditCheckboxReadonlyValue(jQuery('#' + initialValue.id).parent('div.toggleable-element').attr('id'), initialValue.value);
            jQuery('#' + initialValue.id).prop("checked", initialValue.value);
            //setInlineEditCheckboxReadonlyValue(jQuery('#' + initialValue.id).parent('div.toggleable-element').attr('id'), initialValue.value);
        }else{
            jQuery("#" + initialValue.id).val(initialValue.value);
        }
    });

    // Remove all error messages
    jQuery(row).find('.toggleable-element').each(function(){
        jQuery(this).removeClass('uif-hasError');
        jQuery(this).removeAttr('data-has_messages');
        jQuery(this).removeAttr('data-validation_messages');
        var id = jQuery(this).attr('id');
        var messageDivId = id + "_errors";
        var messageDiv = jQuery("#" + messageDivId);
        jQuery(messageDiv).html("");
        var controlId = id + '_control';
        var control = jQuery('#' + controlId);
        if(control.length){
            control.switchClass('error', 'valid');
        }
    });

    // Make the row readonly
    toggleInlineRow(event, false);
}

function getSelectedCollectionPathAndIndex(row){
    var found = false;
    var selectedCollectionPathAndIndex = {
        selectedCollectionPath : "",
        selectedLineIndex : -1
    };

    jQuery(row).find('.toggleable-element.on').each(function(){
        jQuery(this).find(':input:not(:hidden)').each(function(){
            var name = jQuery(this).attr("name");
            if(name != undefined){
                var splitedName = name.split(".");
                var clusterName = splitedName[0];
                var wrapperName = splitedName[1].split('[')[0];
                selectedCollectionPathAndIndex['selectedCollectionPath'] = clusterName + "." + wrapperName;
                var index = splitedName[1].match(/\[(\d+)\]/)[1];
                selectedCollectionPathAndIndex['selectedLineIndex'] = index;
                found = true;
                return false;
            }
        });
        if(found) return false;
    });
    return selectedCollectionPathAndIndex;
}

function saveInlineRSI(event, baseUrl){
    var row = jQuery(event.target).closest('tr');

    var selectedCollectionPathAndIndex = getSelectedCollectionPathAndIndex(row);

    var formData = jQuery('#kualiForm').serialize() + '&' + jQuery.param(selectedCollectionPathAndIndex);

    jQuery.ajax({
        dataType:"json",
        url:baseUrl + "/kr-krad/courseOfferingManagement?methodToCall=saveExamOfferingRSIJSON",
        type:"POST",
        data:formData,
        success:function (data, textStatus, jqXHR) {
            updateInlineTableRow(event,baseUrl, data);
        },
        error: function (jqXHR, status, error) {
            showInlineUnhandledExcption(jqXHR);
        }
    });
}

function showInlineUnhandledExcption(request){
    // show exceptions
    jQuery("html").html(request.responseText);
    jQuery("#Uif-Application").show();
}


function processInlineRowError(row, data, baseUrl){
    jQuery.each(data.translatedErrorMessages, function(key, value){
        var input = jQuery(row).find('[name="' + key + '"]');
        jQuery(input).switchClass('valid', 'error');
        var parent = jQuery(input).closest('.toggleable-element.on');
        jQuery(parent).addClass('uif-hasError');
        jQuery(parent).attr('data-has_messages', 'true');
        jQuery(parent).attr('data-validation_messages', "{&quot;hasOwnMessages&quot;:true,&quot;serverErrors&quot;:[&quot;" + value[0] + "&quot;]}");
        var parentId = jQuery(parent).attr('id');
        var messageDivId = parentId + "_errors";
        var messageDiv = jQuery("#" + messageDivId);
        var errorDiv = createErrorDiv(value[0], baseUrl, parentId);
        jQuery(messageDiv).html(jQuery(errorDiv).html());
    });
}

function updateInlineTableRow(event, baseUrl, data) {
    var row = jQuery(event.target).closest('tr');
    if (data.hasErrors) {
        if(data.messageMap.errorCount > 0){
            processInlineRowError(row, data, baseUrl);
        }
    } else {
        jQuery(row).find('.toggleable-element.off').each(function () {
            var id = jQuery(this).attr("id");
            if (id != undefined) {
                var modelKey = getModelAttributeValue(id);
                var readonlyId = id.split("_id")[0];
                var span = jQuery(this).find('span.uif-readOnlyContent');
                var value = eval("data." + modelKey + "['" + readonlyId + "']");
                jQuery(span).text(value);
                if(jQuery('#' + id).parent().find('[name]').is(':checkbox')){
//                    setInlineEditCheckboxReadonlyValue(jQuery('#' + id).parent().attr('id'));
                    setInlineEditCheckboxReadonlyValue(id, JSON.parse(value));
                }
            }
        });
        toggleInlineRow(event, false);
    }
}

/*
 Assumptions: The read-only and it's corresponding form element have their correspond definition in the same java object.
 TODO: Find away to get rid of hard-coded examOfferingWrapper
 */
function getModelAttributeValue(id) {
    var modelKey = ["examOfferingWrapper"];
    jQuery('#' + id).parent('div').find('[name]').each(function () {
        var nameAttr = jQuery(this).attr('name').split('.');
        jQuery.each(nameAttr, function (i) {
            if(this.indexOf("[") < 0 && i < nameAttr.length -1 ){
                modelKey.push(this);
            }
        });
    });
    return modelKey.join('.');
}

function resetInlineEditCheckboxReadonlyValue(event){
    var row = jQuery(event.target).closest('tr');
    jQuery(row).find('input[type="checkbox"]').each(function(){
        var isChecked = jQuery(this).is(':checked');
        jQuery(this).parent('div.toggleable-element').parent('div').find('span.uif-readOnlyContent').val(isChecked);
    });
}

function setInlineEditCheckboxReadonlyValue(id, checked){
    var className = jQuery.grep(jQuery('#' + id).attr('class').split(" "), function(v, i){
        return v.indexOf('ks-fontello-icon-') === 0;
    }).join();
    jQuery('#' + id).find('span.uif-readOnlyContent').each(function () {
        if(checked){
            jQuery(this).addClass(className);
        }else{
            jQuery(this).removeClass(className);
        }
        jQuery(this).text("");
    });
}

function setMatrixOverrideReadonlyValue(id, checked){
    jQuery('#' + id).find('span.uif-readOnlyContent').each(function () {
        if(checked){
            jQuery(this).addClass("ks-fontello-icon-ok");
        }
        jQuery(this).text("");
    });
}

/**
 * auto finish the time field with the format hh:mm am
 */
function timeFieldOnBlur(event){
    var timeField = jQuery(event.target);
    if (timeField.val() == ''){
        return;
    }

    // Parse hours and minutes and am/pm
    var timeMap = parseTimeString(timeField.val());
    if (!timeMap) {
        return;
    }
    var formattedTimeFieldVal = formatTime(timeMap);
    timeField.val(formattedTimeFieldVal);
    validateFieldValue(timeField);
}