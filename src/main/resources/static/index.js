/**
 *
 */


$('document').ready(function () {

    $('.table .eBtn').on('click',function(event){

        event.preventDefault();
        var href = $(this).attr('href');

        //  я не понимаю почему не работает prevent default


        $.get(href, function (user ,status){
            // $('#idEdit').val(user.id)
            $('.myForm #username').val(user.username)
            $('.myForm #email').val(user.email)
            $('.myForm #age').val(user.age)
            $('.myForm #password').val(user.password)
            // $('idEdit').val(user.roles)
        });




        $('.myForm #editModal').modal();

    });

});
