$(function () {
    $('#myModal table input').click(function () {
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
    
    $('.alvo td input:radio').click(function () {
        console.log('close');
        $('#myModal').modal('hide');
    });
    
    $('#filtro').keyup(function () {
        $('.alvo td span').each(function (i, e) {
            if ($(e).html().indexOf($('#filtro').val()) !== -1) {
                $(e).parent().show();
            } else {
                $(e).parent().hide();
            }
        });
    });
    
});