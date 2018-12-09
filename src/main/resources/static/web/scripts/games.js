var app;
setInterval(function(){location.reload(); }, 180000);

$(function() {
    app = new Vue({
        el: '#app',
        data: {
            games: [],
            dataObjectsOfPlayers : [],
            currentUserName:"",
            currentUserId:""
        }
    })

    fetchJson("http://localhost:8080/api/games", {
            method: 'GET',
        })
        .then(function (json) {
            app.games = json.games;
            processPoints(json);
            app.currentUserName=json.user.userName ? json.user.userName : "guest";
            app.currentUserId=json.user.id;
        })
        .then(function () {
            checkIfGuest();
        })
        .catch(function (error) {
            console.log(error)
        });

        function processPoints(json) {
            for (var i =0; i<json.games.length; i++) {
                for (var j=0; j<2;j++) {
                    if(json.games[i].gamePlayers[j]) {
                        var currentIdInJson = json.games[i].gamePlayers[j].player.id;
                        var findInAppIdJson = app.dataObjectsOfPlayers.findIndex(player=>player.id === currentIdInJson);
                        var currentPlayerInApp= app.dataObjectsOfPlayers[findInAppIdJson];
                        if (findInAppIdJson == -1) {
                            var obj = new Object;
                                obj.id = currentIdInJson;
                                obj.userName = json.games[i].gamePlayers[j].player.userName;
                                obj.win=0;
                                obj.lost=0;
                                obj.tied=0;
                                obj.points=0;
                                if (json.games[i].gamePlayers[j].scores==3) {
                                    obj.win++;
                                }
                                else if (json.games[i].gamePlayers[j].scores==0) {
                                    obj.lost++;
                                }
                                else if (json.games[i].gamePlayers[j].scores==2) {
                                    obj.tied++;
                                }
                            obj.points=(obj.win*3) + (obj.tied*2);
                            app.dataObjectsOfPlayers.push(obj);
                        }
                        else {
                            if (json.games[i].gamePlayers[j].scores==3) {
                                currentPlayerInApp.win++;
                            }
                            else if (json.games[i].gamePlayers[j].scores==0) {
                                currentPlayerInApp.lost++;
                            }
                            else if (json.games[i].gamePlayers[j].scores==2) {
                                currentPlayerInApp.tied++;
                            }
                            currentPlayerInApp.points=(currentPlayerInApp.win*3) + (currentPlayerInApp.tied*2);
                        }
                    }
                }
            }
        };
});

function checkIfGuest() {
    if (app.currentUserName!=="guest") {
        $(".logoutButton").show();
        $("#myForm").hide();
        $("#greeting").show();
        $("#newGameButton").show();
        $(".joinButton").show();
        $("#main-title").hide();
        $("#firstSignupButton").hide();
        $("#firstLoginButton").hide();
        $("#signupButton").hide();
        $("#loginButton").hide();
        $("#formEnter").hide();
    }
    else {
        $(".logoutButton").hide();
        $("#myForm").show();
        $("#greeting").hide();
        $("#newGameButton").hide();
        $(".joinButton").hide();
        $("#main-title").show();
        $("#firstSignupButton").show();
        $("#firstLoginButton").show();
    }
};
