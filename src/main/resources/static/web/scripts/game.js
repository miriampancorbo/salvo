var app;
var currentURL=window.location.href;
var numberVariable = takeNumberURL(currentURL)

function takeNumberURL(url){
    var n = url.slice(url.indexOf("gp=")+3);
    console.log(n)
    return n;
}

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
            vertical: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
            horizontal: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
            shipsLocations:[],
            playerA:"",
            playerB:"",
            msgA:"",
            msgB:""
        }
    })
      fetchJson("http://localhost:8080/api/game_view/" + numberVariable, {
                method: 'GET',
            })
            .then(function (json) {
                console.log("esta bien el fetch!")
                app.shipsLocations = addAllLocations(json);
                app.playerA = json.gamePlayers[0].player.userName;
                app.playerB = json.gamePlayers[1].player.userName;
                getMsg(json);
                paintPosition(app.shipsLocations);
            }).catch(function (error) {
                console.log("Esta mal el fetch...")
            });

       function addAllLocations(json){
        var shipCells=[];
           for(i=0; i<json.ship.length; i++){
               shipCells.push(json.ship[i].locations)
           }
           return shipCells;
       }

      function paintPosition(allShipsLocations){
        for (var j=0; j<allShipsLocations.length; j++){
               for (var i=0; i<allShipsLocations[j].length; i++){
                    $("#"+ allShipsLocations[j][i]).addClass("my-ship");
                }
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
})