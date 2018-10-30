jQuery(document).ready(function($) {
    $("#firstLoginButton").click(function(){
        if($("#loginButton").is(":hidden")){
            $("#signupButton").hide();
            $("#loginButton").show();
            $("#formEnter").show();
        }
        else{
            $("#signupButton").hide();
            $("#loginButton").hide();
            $("#formEnter").hide();
        }
    });
    $("#firstSignupButton").click(function(){
        if($("#signupButton").is(":hidden")){
            $("#loginButton").hide();
            $("#signupButton").show();
            $("#formEnter").show();
        }
        else{
            $("#loginButton").hide();
            $("#signupButton").hide();
            $("#formEnter").hide();
        }
    });

//SIGN UP------------------------------------------------------------------
    $("#signupButton").click(function(){
        var newUserName = $("#writeUserName").val();
        var newUserPassword = $("#writePassword").val();
        if (newUserName && newUserPassword) {
            postPlayer(newUserName, newUserPassword);
        }
        else {
            console.log("Empty user or password.");
            alertify.error("Empty user or password.")
        }
    });

    function postPlayer(newUserName, newUserPassword) {
        $.post( "/api/players",{ userName: newUserName, password: newUserPassword })
            .done(function( ) {
                loginData();
                console.log( "Saved -- reloading");
            })
            .fail(function( response ) {
                console.log( "mal la creación");
                 if (response.status==409){
                     console.log("The nickname is already is use. Please, try with another one.");
                     alertify.error("The nickname is already is use. Please, try with another one.");
                 }
            });
    }

//LOGIN-----------------------------------------------------
    $("#loginButton").click(function(){
        loginData();
    });

    function loginData(){
        var userName = $("#writeUserName").val();
        var userPassword = $("#writePassword").val();
        if (userName && userPassword) {
            postLoginPlayer(userName, userPassword);
        }
        else {
             console.log("Empty user or password.");
             alertify.error("Empty user or password.");
        }
     }

    function postLoginPlayer(userName, userPassword) {
        $.post( "/api/login",{ username: userName, password: userPassword })
            .done(function( ) {
                console.log( "You are logged.");
                $.get("/api/games")
                    .done(function(json){
                        app.games = json.games;
                        app.currentUserName=json.user.userName;
                        app.currentUserId=json.user.id;
                        checkIfGuest(json);
                    })
                    .fail (function (error){
                        console.log(error);
                    })
            })
            .fail(function( response ) {
                console.log( response.status );


                if (response.status==401){
                    console.log("Incorrect user or password.")
                    alertify.error("Incorrect user or password.")
                }
            });
    }

//LOGOUT----------------------------------------------------------
    $(".logoutButton").click(function(){
        postLoginPlayerOut();
    })

    function postLoginPlayerOut(userName, userPassword) {
        $.post( "/api/logout",{ username: userName, password: userPassword })
            .done(function( ) {
                console.log( "You are logged out.");
                $.get("/api/games")
                    .done(function(json){
                        app.games = json.games;
                        app.currentUserName=json.user.userName;
                        app.currentUserId=json.user.id;
                        checkIfGuest(json);
                    })
                    .fail (function (error){
                        console.log(error);
                    })
            })
            .fail(function( jqXHR, textStatus ) {
                console.log( "error" + textStatus );
            });
    };

//CREATE NEW GAME--------------------------------------------------------------
    $("#newGameButton").click(function(){
        postNewGame();
    });

    function postNewGame(){
        $.post("/api/games")
            .done(function(response) {
                console.log("You have created a game.");
                console.log(response);
                window.location.href = "game.html?gp=" + response.gpid;

            })
            .fail(function( response ) {
                console.log( "Error in game creation:" + response);
            });
    }

});