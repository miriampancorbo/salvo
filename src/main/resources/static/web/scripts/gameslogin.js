jQuery(document).ready(function($) {
    $("#newUserButton").change(function(){
         $("#loginButton").toggle();
         $("#signupButton").toggle();
    });

    //SIGN UP------------------------------------------------------------------
    $("#signupButton").click(function(){
        var newUserName = $("#writeUserName").val();
        var newUserPassword = $("#writePassword").val();
        if (newUserName && newUserPassword) {
          postPlayer(newUserName, newUserPassword);
        }
    });

    function postPlayer(newUserName, newUserPassword) {
        $.post( "/api/players",{ userName: newUserName, password: newUserPassword })
            .done(function( ) {
                loginData();
                console.log( "Saved -- reloading");
        })
            .fail(function( jqXHR, textStatus ) {
                console.log( "Failed: " + textStatus );
        });
    }

    //LOGIN-----------------------------------------------------
    $("#loginButton").click(function(){
        loginData();
    })

        function loginData(){
        var userName = $("#writeUserName").val();
            var userPassword = $("#writePassword").val();
            if (userName && userPassword) {
              postLoginPlayer(userName, userPassword);
            }
         }

        function postLoginPlayer(userName, userPassword) {
             $.post( "/api/login",{ username: userName, password: userPassword })
                 .done(function( ) {
                   console.log( "You are logged");
                   hideForm();
             })
                 .fail(function( jqXHR, textStatus ) {
                   console.log( "error" + textStatus );
             });
        }
         //Hide form and show logoutButton
        function hideForm(){
        $("#logoutButton").show();
        $("#myForm").hide();
        $("#greeting").show();
        }

    //LOGOUT----------------------------------------------------------
    $("#logoutButton").click(function(){
        postLoginPlayerOut();
    })

    function postLoginPlayerOut(userName, userPassword) {
         $.post( "/api/logout",{ username: userName, password: userPassword })
             .done(function( ) {
               console.log( "You are logged out.");
               //$("#logoutButton").hide();
               //$("#myForm").show();
               fetchJson("http://localhost:8080/api/games", {
                       method: 'GET',
                   })

         })
             .fail(function( jqXHR, textStatus ) {
               console.log( "error" + textStatus );
         });
    };
});