$(function () {
    $('#myModal table input').click(function() {
        $('.openDialogFocus').val($(this).val());
    });
    
    $('.openDialogFocus').focusin(function () {
        $('#myModal').modal('show');
        if ($(this).val() !== "") {
            var revista = $(this).val();
            $('#myModal table input').each(function (i, e) {
                $(e).prop('checked', revista === $(e).val());
            });
        }
    });
});