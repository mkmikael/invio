function pass(status, direction) {
    $('#header' + status).removeClass('active');
    $('#header' + (status + direction)).addClass('active');
    $('#step' + status).hide(650);
    $('#step' + (status + direction)).show(650);
}

$(function () {
    $('#step2').hide();
    $('#step3').hide();
    $('#step1 .next').click(function () {
        pass(1, 1);
    });
    $('#step2 .previous').click(function () {
        pass(2, -1);
    });
    $('#step2 .next').click(function () {
        pass(2, 1);
    });
    $('#step3 .previous').click(function () {
        pass(3, -1);
    });
});