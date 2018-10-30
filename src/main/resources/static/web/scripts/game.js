var app;

var currentURL=window.location.href; //http://localhost:8080/web/game.html?gp=1
var numberVariable = takeNumberURL(currentURL)

function takeNumberURL(url){
    var n = url.slice(url.indexOf("gp=")+3);
    console.log(n)
    return n;
}

$(function () {
    app = new Vue({
        el: '#app',
        data: {
            vertical: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
            horizontal: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
            ships:[],
            playerA:"",
            playerB:"",
            msgA:"",
            msgB:"",
            salvo:[],
            myGpId:"",
            opponentGpId:"",
            myId:"",
            opponentId:""
        }
    })
    fetchJson("http://localhost:8080/api/game_view/" + numberVariable, {method: 'GET',})
        .then(function (json) {
            console.log("esta bien el fetch!")
            getIds(json);
            app.ships = json.ship;
            app.playerA = json.gamePlayers[0].player.userName;
            app.playerB = json.gamePlayers[1].player.userName;
            app.salvo=json.salvo;
            getMsg(json);
            paintPositionOwnShips(app.ships);
            paintPositionSalvoes(json, app.salvo);
            })
        .catch(function (error) {
            console.log("Esta mal el fetch...")
        });

    function getIds(json){
        if (json.gamePlayers[0].id==numberVariable){
            app.myGpId=json.gamePlayers[0].id;
            app.myId=json.gamePlayers[0].player.id;
            app.opponentGpId=json.gamePlayers[1].id;
            app.opponentId=json.gamePlayers[1].player.id;
        }
        else {
            app.myGpId=json.gamePlayers[1].id;
            app.myId=json.gamePlayers[1].player.id;
            app.opponentGpId=json.gamePlayers[0].id
            app.opponentId=json.gamePlayers[0].player.id;
        }
    }

    function paintPositionOwnShips(ships){
        ships.forEach(function (ship) {
            ship.locations.forEach(function (location) {
                $('#' + location).addClass("my-ship");
            })
        });
    };

    function paintPositionSalvoes(json, salvoes){
        for (var i = 0; i < salvoes.length; i++) {
            if (salvoes[i].player.id == app.myGpId) {
                salvoes[i].locations.forEach(location => mySalvosStyle(location, json, i));
            }
            else {
                salvoes[i].locations.forEach(location => opponentSalvosStyle(location, json, i));
            }
        }
    }

    function mySalvosStyle(location, json, i) {
        $('#' + location + 'S').addClass("my-salvo").html(json.salvo[i].turn);
    }

    function opponentSalvosStyle(location, json, i){
    if ($('#' + location).hasClass("my-ship")){
            $('#' + location).addClass("hit-my-ship").html(json.salvo[i].turn);
    }
    else{
        $('#' + location).addClass("opponent-salvo").html(json.salvo[i].turn);
        }
    }

    function getMsg(json){
        if (numberVariable == json.gamePlayers[0].id){
            app.msgA = "(you)";
        }
        else{
            app.msgB="(you)"
        }
    }

});

$(".logoutButton2").click(function(){
    console.log("ok");
    postLoginPlayerOut2();
})
function postLoginPlayerOut2(userName, userPassword) {
     $.post( "/api/logout",{ username: userName, password: userPassword })
         .done(function( ) {
           console.log( "BIENNNN");
            location.reload();
            location.href="http://localhost:8080/web/games.html"
     })
         .fail(function( jqXHR, textStatus ) {
           console.log( "ERRORRRR" + textStatus );
     });
};