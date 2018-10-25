var app;

function fetchJson(url, init) {
    return fetch(url, init).then(function (response) {
        if (response.ok) {
            return response.json();
        }
        throw new Error(response.statusText);
    });
}
$(function () {
    app = new Vue({
        el: '#app',
        data: {
            games: [],
            dataObjectsOfPlayers : [],
            currentUserName:""
        }
    })

    fetchJson("http://localhost:8080/api/games", {
            method: 'GET',
        })
        .then(function (json) {
            app.games = json.games;
            processPoints(json);
            app.currentUserName=json.user.userName;
            checkIfGuest(json);
            console.log(json.user==="guest")
        })
        .catch(function (error) {
            console.log(error)
        });

        function processPoints(json){
            for (var i =0; i<json.games.length; i++){
                for (var j=0; j<2;j++){
                    if(json.games[i].gamePlayers[j]){
                        var currentIdInJson = json.games[i].gamePlayers[j].player.id;
                        var findInAppIdJson = app.dataObjectsOfPlayers.findIndex(player=>player.id === currentIdInJson);
                        var currentPlayerInApp= app.dataObjectsOfPlayers[findInAppIdJson];
                            if (findInAppIdJson == -1){
                                var obj = new Object;
                                    obj.id = currentIdInJson;
                                    obj.userName = json.games[i].gamePlayers[j].player.userName;
                                    obj.win=0;
                                    obj.lost=0;
                                    obj.tied=0;
                                    obj.points=0;
                                    if (json.games[i].gamePlayers[j].scores==3){
                                        obj.win++;
                                    }
                                    else if (json.games[i].gamePlayers[j].scores==0){
                                        obj.lost++;
                                    }
                                    else if (json.games[i].gamePlayers[j].scores==2){
                                        obj.tied++;
                                    }
                                obj.points=(obj.win*3) + (obj.tied*2);
                                app.dataObjectsOfPlayers.push(obj);
                            }
                            else {
                                if (json.games[i].gamePlayers[j].scores==3){
                                    currentPlayerInApp.win++;
                                }
                                else if (json.games[i].gamePlayers[j].scores==0){
                                    currentPlayerInApp.lost++;
                                }
                                else if (json.games[i].gamePlayers[j].scores==2){
                                    currentPlayerInApp.tied++;
                                }
                                currentPlayerInApp.points=(currentPlayerInApp.win*3) + (currentPlayerInApp.tied*2);
                            }
                    }
                }
            }
        }
});

function checkIfGuest(json){
    if (json.user!=="guest"){       //there is a user
        $(".logoutButton").show();
        $("#myForm").hide();
        $("#greeting").show();
        $("#newGameButton").show();
        $("#main-title").hide();
        $("#firstSignupButton").hide();
        $("#firstLoginButton").hide();
        $("#signupButton").hide();
        $("#loginButton").hide();
        $("#formEnter").hide();
    }
    else{                           //there is no user
        $(".logoutButton").hide();
        $("#myForm").show();
        $("#greeting").hide();
        $("#newGameButton").hide();
        $("#main-title").show();
        $("#firstSignupButton").show();
        $("#firstLoginButton").show();
    }
};
