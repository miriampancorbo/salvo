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
            //shipsLocations:[],
            ships:[],
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
                //app.shipsLocations = addAllLocations(json);
                app.ships = json.ship;
                app.playerA = json.gamePlayers[0].player.userName;
                app.playerB = json.gamePlayers[1].player.userName;
                getMsg(json);
                //paintPosition(app.shipsLocations);
                paintPosition(app.ships);
            }).catch(function (error) {
                console.log("Esta mal el fetch...")
            });

       /*function addAllLocations(json){
        var shipCells=[];
           for(i=0; i<json.ship.length; i++){
               shipCells.push(json.ship[i].locations)
           }
           return shipCells;
       }

        for (var j=0; j<ships.length; j++){
               for (var i=0; i<ships[j].locations.length; i++){
                    $("#"+ ships[j].locations[i]).addClass("my-ship");
                }
        }*/

      function paintPosition(ships){
        ships.forEach(function (ship) {
            ship.locations.forEach(function (location) {
                $('#' + location).addClass("my-ship");
            })
        });
      };


     function getMsg(json){
        if (numberVariable == json.gamePlayers[0].id){
        app.msgA = "(you)";
        }
        else{
        app.msgB="(you)"
        }
     }
})