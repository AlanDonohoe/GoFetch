//for allowing collapsible content in dashboard
//see:http://kahimyang.info/kauswagan/howto_blogs/1403-implementing_a_collapsible_ui_repeat_rows_in_jsf


var prevIcon=null;
var prevPanel=null;
             
function vistoggle (icon, panel){
    if (prevPanel != null && panel != prevPanel) {
        $(prevPanel).hide('slow');        
    }                
     
    if($(panel).is(":visible")){
        $(panel).hide ('slow');
    }
    else {
        $(panel).show ('slow');
    }
     
    prevPanel=panel;
                 
    if($(icon).hasClass("ui-icon-plus")){
        $(icon).removeClass("ui-icon-plus");
        $(icon).addClass("ui-icon-minus");
    }else{
        $(icon).removeClass("ui-icon-minus");
        $(icon).addClass("ui-icon-plus");
    } 
                 
    if(prevIcon != null && icon!=prevIcon){        
        $(prevIcon).removeClass("ui-icon-minus");
        $(prevIcon).addClass("ui-icon-plus");        
    } 
     
    prevIcon=icon;
     
}          