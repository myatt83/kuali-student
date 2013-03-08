/**
 * This method handles the colocated checkbox event.
 * @param control colocated checkbox control
 */
function setupColoCheckBoxChange(control){

    if(jQuery(control).is(":checked")) {

        jQuery("#max_enrollment").hide();
        jQuery("#ActivityOfferingEdit-MainPage-CoLocation").show();

        if(jQuery("#share_seats_control_0").length != 0 && jQuery("#share_seats_control_0").is(":checked")) {
            jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate").hide();
            jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentShared").show();
            jQuery("#shared_max_enr_control").removeClass("ignoreValid");
            jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").addClass("ignoreValid");
        } else {
            jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate").show();
            jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentShared").hide();
            jQuery("#shared_max_enr_control").addClass("ignoreValid");
            jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").removeClass("ignoreValid");
        }
    } else {
        jQuery("#max_enrollment").show();
        jQuery("#ActivityOfferingEdit-MainPage-CoLocation").hide();
        jQuery("#shared_max_enr_control").addClass("ignoreValid");
        jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").addClass("ignoreValid");
    }
}

/**
 * This method handles the on load event
 *
 */
function activityEditDocumentOnLoad(){

    jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").addClass("ignoreValid");
    jQuery("#shared_max_enr_control").addClass("ignoreValid");

    if(jQuery("#is_co_located").length != 0) {
        setupColoCheckBoxChange(jQuery("#is_co_located"));
    }

    if (jQuery("#share_seats_control_0").length != 0){
        jQuery('#share_seats_control_0').change(function() {
            if(jQuery(this).is(":checked")) {
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate").hide();
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentShared").show();
                jQuery("#shared_max_enr_control").removeClass("ignoreValid");
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").addClass("ignoreValid");
            } else {
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate").show();
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentShared").hide();
                jQuery("#shared_max_enr_control").addClass("ignoreValid");
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").removeClass("ignoreValid");
            }
        });
    }

    if (jQuery("#share_seats_control_1").length != 0){
        jQuery('#share_seats_control_1').change(function() {
            if(jQuery(this).is(":checked")) {
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate").show();
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentShared").hide();
                jQuery("#shared_max_enr_control").addClass("ignoreValid");
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").removeClass("ignoreValid");
            } else {
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate").hide();
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentShared").show();
                jQuery("#shared_max_enr_control").removeClass("ignoreValid");
                jQuery("#ActivityOfferingEdit-MainPage-CoLocatedEnrollmentSeperate :text").addClass("ignoreValid");
            }
        });
    }

}

function clearRoomResourcesSelections(sourceLink) {
    var divInputField = sourceLink.siblings('div.uif-inputField');
    divInputField.find('option').each(function() {
            jq(this).removeAttr('selected');
        });
}

function clearFeaturesSelected(){
    jQuery('#featuresList_control option').attr('selected', false);
    return false;
}

function calculatePercent(jqObject){

    if(jqObject) {
        var currentId = jqObject.attr('id');
        if (currentId.indexOf("_control") == -1) {
            currentId = currentId + "_control";
        }
        var element = jQuery('#' + currentId);
        var numValue = element.val();
        var numericExpression = /^[0-9]+$/;
        if(!numValue.match(numericExpression) && numValue != '') {
            if (currentId == 'maximumEnrollment') {
                alert("Maximum enrollment must be a number!");
            } else {
                alert("Number of seats must be a number!");
            }
            element.val('');
        }
    }

//    var seatLimitAdd =  jQuery('#seatLimit_add_control');
//    var seatLimitPercentAdd =  jQuery('#seatLimitPercent_add #_span');
    var seatpoolCount = jQuery('#seatpoolCount span[class=uif-message]');
    var seatsRemaining = jQuery('#seatsRemaining span[class=uif-message]');
    var count = 0;
    var seatsTotal = 0;
    var maxEnrollValue = 0;

    // 3 different calculations: when on Edit page and when on View page, unfortunately IDs set up differently by KRAD
    var maxEnrollView = jQuery('span[id=maximumEnrollment]');
    var maxEnroll =  jQuery('#maximumEnrollment_control');

    if (maxEnrollView.length > 0) { // View page
        maxEnrollValue = maxEnrollView.text().trim();
        var rows = jQuery('span[id^=seatLimit_line]');
        rows.each(function () {
            var id = jQuery(this).attr('id');
            if(id.match(/_/g).length == 1) {
                var num = id.substring(14);
                var elemPct = jQuery('#seatLimitPercent_line' + num + ' span[class=uif-message]');
                var seatsNum = jQuery(this).text().trim();
                count += 1;
                if (maxEnrollValue != "" && maxEnrollValue != 0 && seatsNum != "") {
                    seatsTotal = parseInt(seatsTotal) + parseInt(seatsNum);
                    var result = (seatsNum / maxEnrollValue) * 100;
                    elemPct.text(Math.round(result) + "%");
                } else {
                    elemPct.text("");
                }
            }
        });
        seatpoolCount.text(count);
    } else if ( maxEnrollView.length == 0 && maxEnroll.hasClass("uif-readOnlyContent") ) { // View page
        maxEnrollValue = maxEnrollView.text().trim();
        if (maxEnrollValue == "") {
            maxEnrollValue = maxEnroll.text().trim();
        }
        var rows = jQuery('span[id^=seatLimit_line]');
        rows.each(function () {
            var id = jQuery(this).attr('id');
            if(id.indexOf("_control") != -1) {
                var num = id.substring(14,15);
                var elemPct = jQuery('#seatLimitPercent_line' + num + ' span[class=uif-message]');
                var seatsNum = jQuery(this).text().trim();
                count += 1;
                if (maxEnrollValue != "" && maxEnrollValue != 0 && seatsNum != "") {
                    seatsTotal = parseInt(seatsTotal) + parseInt(seatsNum);
                    var result = (seatsNum / maxEnrollValue) * 100;
                    elemPct.text(Math.round(result) + "%");
                } else {
                    elemPct.text("");
                }
            }
        });
        seatpoolCount.text(count);
    } else { // Edit page (different IDs)
        maxEnrollValue = maxEnroll.val();
        var rows = jQuery("[id^='seatLimit_line']");
        rows.each(function () {
            var id = jQuery(this).attr('id');
            if(id.indexOf("_control") != -1) {
                var num = id.substring(14, id.length - 8);
                var elemPct = jQuery('#seatLimitPercent_line' + num);
                var seatsNum = jQuery(this).val();
                count += 1;
                if (maxEnrollValue != "" && maxEnrollValue != 0 && seatsNum != "") {
                    seatsTotal = parseInt(seatsTotal) + parseInt(seatsNum);
                    var result = (seatsNum / maxEnrollValue) * 100;
                    elemPct.text(Math.round(result) + "%");
                } else {
                    elemPct.text("");
                }
            }
        });
        seatpoolCount.text(count);
    }

    if (maxEnrollValue != "") {
        var seatsRemain = (maxEnrollValue > seatsTotal) ? (maxEnrollValue - seatsTotal) : 0;
        var percTotal = Math.round((seatsTotal / maxEnrollValue) * 100);
        var percRemain = (seatsRemain > 0) ? (100 - percTotal) : 0;
        if (maxEnrollValue >= seatsTotal){
            seatsRemaining.text(percRemain + "% | " + seatsRemain + " Seats (Max Enrollment = " + maxEnrollValue + ")");
            jq(seatsRemaining).css('color', 'black');
        } else {
            seatsRemaining.text(percRemain + "% | " + seatsRemain + " Seats (Max Enrollment = " + maxEnrollValue + ")" +
                " - WARNING: Total seats exceeding the total max enrollment quantity by " + (seatsTotal - maxEnrollValue) + " seats!");
            jq(seatsRemaining).css('color', 'red');
        }
    } else {
        seatsRemaining.text("");
    }
}

function setRequestedDeliveryLogisticsFieldRequired(jqObject,required){

}