$(function() {
    $('#step2').hide();
    $('#step1 .next').click(function() {
        $('#header1').removeClass('active');
        $('#header2').addClass('active');
        $('#step1').hide(650);
        $('#step2').show(650);
    });
    $('#step2 .previous').click(function() {
        $('#header1').addClass('active');
        $('#header2').removeClass('active');
        $('#step1').show(650);
        $('#step2').hide(650);
    });
});