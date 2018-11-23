//https://github.com/gridstack/gridstack.js/tree/develop/doc
var app;
var letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];

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
            vertical: letters,
            horizontal: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
            ships:[],
            playerA:"",
            playerB:"",
            msgA:"",
            msgB:"",
            salvo:[],
            playerGpId:"",
            opponentGpId:"",
            playerId:"",
            opponentId:"",
            playerHits:{},
            opponentHits:{},
            playerSunkBoats:{},
            opponentSunkBoats:{},
            turnNumbers:[],
            playerLefts:[],
            opponentLefts:[],
            tableGame:[
                {
                    turn:"",
                    opponentHitsOnPlayerTable:[],
                    sunksPlayerTable:[],
                    playerLeftsTable:"",
                    playerHitsOnOpponentTable:[],
                    sunksOpponentTable:[],
                    opponentLeftsTable:""
                }
            ]
        }
    })
    fetchJson("http://localhost:8080/api/game_view/" + numberVariable, {method: 'GET',})
        .then(function (json) {
            console.log("Good fetch!")
            getIds(json);
            app.ships = json.ship;
            app.playerA = json.gamePlayers[0].player.userName;
            if (json.gamePlayers[1]) { app.playerB = json.gamePlayers[1].player.userName;}
            app.salvo=json.salvo;
            placeSavedShips(app.ships);
            app.playerHits = json.playerHits;
            app.opponentHits = json.opponentHits;
            app.playerSunkBoats = json.playerSunkBoats;
            app.opponentSunkBoats = json.opponentSunkBoats;
            app.turnNumbers = getReverseTurnNumbers(json, app.salvo);
            app.playerLefts = getLeftBoats(json.playerSunkBoats);
            app.opponentLefts = getLeftBoats(json.opponentSunkBoats);
            app.tableGame = fillTableGame();
            getMsg(json);
            if (json.gamePlayers[1]) { salvoCross(json, app.salvo); }

            })
        .catch(function (error) {
            console.log("Wrong fetch...")
        });

    function getIds(json){
        if (json.gamePlayers[0].id==numberVariable){
            app.playerGpId=json.gamePlayers[0].id;
            app.playerId=json.gamePlayers[0].player.id;
            if (json.gamePlayers[1]) {
                app.opponentGpId=json.gamePlayers[1].id;
                app.opponentId=json.gamePlayers[1].player.id;
            }
        }
        else {
            app.playerGpId=json.gamePlayers[1].id;
            app.playerId=json.gamePlayers[1].player.id;
            app.opponentGpId=json.gamePlayers[0].id
            app.opponentId=json.gamePlayers[0].player.id;
        }
    }

    function getMsg(json){
        numberVariable == json.gamePlayers[0].id ? app.msgA = "(you)" : app.msgB="(you)";
    }

    function getReverseTurnNumbers(json, salvo) { //Take the quantity of turns per player in reverse order
        var lastTurn = salvo.length/2;
        if (salvo.length % 2 !== 0 ) {
            lastTurn += 0.5;
        }
        var arrayTurns = [];
        while (lastTurn > 0) { //Reverse
            arrayTurns.push(lastTurn);
            lastTurn--;
        }
        return arrayTurns;
    }

    function getLeftBoats(sunkBoats) { //Take number of left boats per player and per turn
        var liveBoats = [];
        var totalBoats = 5;
        var numberOfSunks = Object.keys(sunkBoats).length
        for (var i = numberOfSunks; i > 0 ; i--) {
            liveBoats.push(totalBoats - (Object.keys(sunkBoats[i]).length));
        }
        return liveBoats;
    }

    function takeSunkInTurn(playerSunks, numberOfTurns, turn) { //Take type of sunk boats per player and per turn
        var computedSunks = []; //Sunk boats so far
        var sinkingNow = [];
        var sinkInTurn = [];
        for (var i = 0; i < numberOfTurns; i++) {
            var position = i+1; //No index 0
            for (var j = 0; j < Object.keys(playerSunks[position]).length; j++) {
                if (!computedSunks.includes(playerSunks[position][j].type)) { //If it is sunk in this turn
                    computedSunks.push(playerSunks[position][j].type);
                    sinkingNow.push(playerSunks[position][j].type);
                }
            }
            sinkInTurn.push(sinkingNow); //The ones for this turn
            sinkingNow = []; //To not accumulate
        }
        return sinkInTurn[turn].join("\n").toLowerCase();
    }
    function fillTableGame() {
        var objectGameTable=[];
        var allTurns = app.turnNumbers;
        var allPlayerLefts = app.playerLefts;
        var allOpponentLefts = app.opponentLefts;
        var allPlayerHits = app.playerHits;
        var allOpponentHits = app.opponentHits;
        var playerHitsPerTurn = [];
        var opponentHitsPerTurn = [];
        var playerSunkBoats = app.playerSunkBoats;
        var opponentSunkBoats = app.opponentSunkBoats;

        for (var i = 0; i < allTurns.length; i++) { //Main 'for'
            var lastHits = allPlayerHits[Object.keys(allPlayerHits).length-i]; //Start with the last hit
            for (var ship in lastHits) {
                var numberHits = lastHits[ship].length;
                playerHitsPerTurn.push((ship).toLowerCase());
                playerHitsPerTurn.push(getStars(numberHits));
            }

            if (playerHitsPerTurn.length == 0) {
                playerHitsPerTurn = "-";
            }

            var lastOpponentHits = allOpponentHits[Object.keys(allPlayerHits).length-i];
            for (var ship in lastOpponentHits) {
                var numberHits = lastOpponentHits[ship].length;
                opponentHitsPerTurn.push((ship).toLowerCase());
                opponentHitsPerTurn.push(getStars(numberHits));
            }

            if(opponentHitsPerTurn.length == 0) {
                opponentHitsPerTurn = "-";
            }

            objectGameTable.push({
                           turn: allTurns[i],
                           opponentHitsOnPlayerTable: joinHits(opponentHitsPerTurn),
                           sunksPlayerTable:takeSunkInTurn(playerSunkBoats, allTurns.length, allTurns.length-i-1),
                           playerLeftsTable: allOpponentLefts[i],
                           playerHitsOnOpponentTable: joinHits(playerHitsPerTurn),
                           sunksOpponentTable:takeSunkInTurn(opponentSunkBoats, allTurns.length, allTurns.length-i-1),
                           opponentLeftsTable: allPlayerLefts[i]
            })

            var playerHitsPerTurn = [];
            var opponentHitsPerTurn = [];
        }
        return objectGameTable;
    }; //Finish fillTableGame()

    function getStars (numberHits) {
        return "*".repeat(numberHits);
    }

    function joinHits(hits) { // Join hits per ship per line with stars
        if (hits == "-") return hits;
        var result = [];
        for (var i = 0; i < hits.length; i += 2) {
            result.push(hits[i] + " " + hits[i+1]);
        }
        return result.join("\n");
    }
}); //END MAIN FUNCTION

//---------------------------------------------------------CROSSES-------

    function salvoCross(json, salvoes){
        for (var i = 0; i < salvoes.length; i++) {
            if (salvoes[i].player.id == app.playerGpId) {
                salvoes[i].locations.forEach(location => playerSalvoesLocation(location, json, i));
            }
            else {
                salvoes[i].locations.forEach(location => opponentSalvoesLocation(location, json, i));
            }
        }
    }


//ORANGE (Player sends)
    function playerSalvoesLocation(location, json, i) {
        var horizontal = location.substr(1) - 1;
        var vertical = letters.indexOf(location[0]);
        for (var i = 1; i <= Object.keys(json.playerHits).length; i++) {
            if (json.playerHits[i].AIRCRAFT) {
                putTickPlayerHitsOpponent('AIRCRAFT', i, json, location, horizontal, vertical);
            }
            if (json.playerHits[i].BATTLESHIP) {
                putTickPlayerHitsOpponent('BATTLESHIP', i, json, location, horizontal, vertical);
            }
            if (json.playerHits[i].SUBMARINE) {
                putTickPlayerHitsOpponent('SUBMARINE', i, json, location, horizontal, vertical);
            }
            if (json.playerHits[i].DESTROYER) {
                putTickPlayerHitsOpponent('DESTROYER', i, json, location, horizontal, vertical);
            }
            if (json.playerHits[i].PATROL) {
                putTickPlayerHitsOpponent('PATROL', i, json, location, horizontal, vertical);
            }
            else {
                document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/cruzNaranja.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
            }
        }
    }

    function putTickPlayerHitsOpponent(type, i, json, location, horizontal, vertical) {
        for(var j = 0; j < json.playerHits[i][type].length; j++) {
            if (location == json.playerHits[i][type][j]) {
                document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/greenTick.png' alt='green tick' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
            }
        }
    }

    function opponentSalvoesLocation(location, json, i) {
        var grid = $('#gridFix').data('gridstack');
        var horizontal = location.substr(1) - 1;
        var vertical = letters.indexOf(location[0]);
//RED (opponent hits player)
        if (!grid.isAreaEmpty(horizontal, vertical, 1, 1)){
            document.getElementById("my-complete-grid").innerHTML+= "<img src='photos/cruzRoja.png' alt='red cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
        }
//YELLOW (opponent send but doesnt hit player)
        else {
            document.getElementById("my-complete-grid").innerHTML+= "<img src='photos/cruzAmarilla.png' alt='yellow cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
        }
    }

//-------------------------------------------LOGOUT ...-------------------------------------
$(".logoutButton2").click(function(){
    postLoginPlayerOut2();
})
function postLoginPlayerOut2(userName, userPassword) {
     $.post( "/api/logout",{ username: userName, password: userPassword })
         .done(function( ) {
           console.log( "Good fetch post login");
            location.reload();
            location.href="http://localhost:8080/web/games.html"
     })
         .fail(function( jqXHR, textStatus ) {
           console.log( "Wrong fetch post login" + textStatus );
     });
};
//---------------------------------------------------------OPTIONS GRID----------------------------

$(function () {
    var options= {
        width: 10,
        height: 10,
        padding:1,
        verticalMargin: 0,
        cellHeight: 45,
        disableResize: true,
		float: true,
        disableOneColumnMode: true,
        staticGrid: false,
        animate: true,
        acceptWidgets: true,
        resizable:false
    }
    $('#grid').gridstack(options);
    grid = $('#grid').data('gridstack');

    var optionsFix = {
        width: 10,
        height: 10,
        padding:1,
        verticalMargin: 0,
        cellHeight: 45,
        disableResize: true,
		float: true,
        disableOneColumnMode: true,
        staticGrid: true,
        animate: true,
        acceptWidgets: true,
        resizable:false
    }
    $('#gridFix').gridstack(optionsFix);
    gridFix = $('#gridFix').data('gridstack');

/*-------------------------------------------TURN BOATS---------------------------*/

    $("#aircraft").dblclick(function(){
        checkBoatPosition('aircraft', this, "aircraftHorizontal", 'aircraftVertical', 5);
    });

    $("#battleship").dblclick(function(){
        checkBoatPosition('battleship', this, "battleshipHorizontal", 'battleshipVertical', 4);
    });

    $("#submarine").dblclick(function(){
        checkBoatPosition('submarine', this, "submarineHorizontal", 'submarineVertical', 3);
    });

    $("#destroyer").dblclick(function(){
        checkBoatPosition('destroyer', this, "destroyerHorizontal", 'destroyerVertical', 3);
    });

    $("#patrol").dblclick(function(){
        checkBoatPosition('patrol', this, "patrolHorizontal", 'patrolVertical', 2);
    });

    function checkBoatPosition(type, that, horizontal, vertical, size) {
        var boatId = document.getElementById(type);
        var x = parseInt(boatId.getAttribute("data-gs-x"));
        var y = boatId.getAttribute("data-gs-y");
        if($(that).children().hasClass(horizontal) && fromHorizontalToVertical(size-1, x, y)) {
            turnBoat(horizontal, vertical, that, size, 1);
        }else if ($(that).children().hasClass(vertical) && fromVerticalToHorizontal(size-1, x, y)) {
            turnBoat(vertical, horizontal, that, 1, size);
        }
        else {
            alertify.error("Incorrect movement.");
        }
    }
    function turnBoat(currentPosition, nextPosition, that, currentSizeHorizontal, currentSizeVertical) {
            grid.resize($(that),currentSizeVertical,currentSizeHorizontal);
            $(that).children().removeClass(currentPosition);
            $(that).children().addClass(nextPosition);
    }

    function fromHorizontalToVertical(length, x, y){
        var i = length;
        while (i > 0){
            if(!((parseInt(y)+length < 10) && (grid.isAreaEmpty(x,(parseInt(y)+i),1,1)))) {
                return false;
            }
            i--;
        }
        return true;
    }
    function fromVerticalToHorizontal(length, x, y) {
        var i = length;
            while (i > 0){
                if(!((parseInt(x)+length < 10) && (grid.isAreaEmpty(x+i),y,1,1))){
                return false;
            }
            i--;
        }
        return true;
    }
});
//--------------------------------------SAVE SHIPS----------------------------------------------------------

function saveShips(){
    var gamePlayerId = numberVariable;
    console.log("gamePlayerId: "+ gamePlayerId);

    var playerShips= new Set;
    playerShips = [
        {
            "shipType":"AIRCRAFT",        //0. length 5
            "shipLocation":[]
        },
        {
            "shipType":"BATTLESHIP",      //1. length 4
            "shipLocation":[]
        },
        {
            "shipType":"SUBMARINE",      //2. length 3
            "shipLocation":[]
        },
        {
            "shipType":"DESTROYER",       //3. length 3
            "shipLocation":[]
        },
        {
            "shipType":"PATROL",            //4. length 2
            "shipLocation":[]
        }
    ];

    function boatData(type, vertical, sizeBoat, boatNumber){
        var typeId = document.getElementById(type);
        var x = parseInt(typeId.getAttribute("data-gs-x"));
        var y = typeId.getAttribute("data-gs-y");
        var number = x+1;
        var letter = letters[y];
        var first = letter+number;

        if(typeId.firstChild.classList.contains(vertical)){
            if(sizeBoat == 2) {
                var second = (letters[letters.indexOf(letter)+1])+number;
                playerShips[boatNumber].shipLocation = [first, second];
            }

            if(sizeBoat == 3) {
                var second = (letters[letters.indexOf(letter)+1])+number;
                var third = (letters[letters.indexOf(letter)+2])+number;
                playerShips[boatNumber].shipLocation = [first, second, third];
            }
            if(sizeBoat == 4) {
                var second = (letters[letters.indexOf(letter)+1])+number;
                var third = (letters[letters.indexOf(letter)+2])+number;
                var fourth = (letters[letters.indexOf(letter)+3])+number;
                playerShips[boatNumber].shipLocation = [first, second, third, fourth];
            }
            if(sizeBoat == 5) {
                var second = (letters[letters.indexOf(letter)+1])+number;
                var third = (letters[letters.indexOf(letter)+2])+number;
                var fourth = (letters[letters.indexOf(letter)+3])+number;
                var fifth = (letters[letters.indexOf(letter)+4])+number;
                playerShips[boatNumber].shipLocation = [first, second, third, fourth, fifth];
            }
        }
        else{  //horizontal
            if(sizeBoat == 2) {
                var second = letter + (number+1);
                playerShips[boatNumber].shipLocation = [first, second];
            }

            if(sizeBoat == 3) {
                var second = letter + (number+1);
                var third = letter + (number+2);
                playerShips[boatNumber].shipLocation = [first, second, third];
            }
            if(sizeBoat == 4) {
                var second = letter + (number+1);
                var third = letter + (number+2);
                var fourth = letter + (number+3);
                playerShips[boatNumber].shipLocation = [first, second, third, fourth];
            }
            if(sizeBoat == 5) {
                var second = letter + (number+1);
                var third = letter + (number+2);
                var fourth = letter + (number+3);
                var fifth = letter + (number+4);
                playerShips[boatNumber].shipLocation = [first, second, third, fourth, fifth];
            }
        }
    }
    function aircraftData(){
        boatData("aircraft", "aircraftVertical", 5, 0);
    }
    function battleshipData(){
        boatData("battleship", "battleshipVertical", 4, 1);
    }
    function submarineData(){
        boatData("submarine", "submarineVertical", 3, 2);
    }
    function destroyerData(){
        boatData("destroyer", "destroyerVertical", 3, 3);
    }
    function patrolData(){
        boatData("patrol", "patrolVertical", 2, 4);
    }
    aircraftData();
    battleshipData();
    submarineData();
    destroyerData();
    patrolData();
    addShips(playerShips, gamePlayerId);
    location.reload();
};
function addShips(playerShips, gamePlayerId) {
        $.post({
             url: "/api/games/players/" + gamePlayerId + "/ships",
             data: JSON.stringify(playerShips),
             dataType: "text",
             contentType: "application/json"
           })
            .done(function(response) {
                console.log("la response: ")
                console.log(response)
                console.log(playerShips)

                $.get("/api/game_view/" + gamePlayerId)
                    .done(function(json){
                        app.ships = playerShips;
                        placeSavedShips(app.ships);
                        console.log("app.ships: ")
                        console.log(app.ships)
                    });
            })
            .fail(function( response ) {
                console.log("mal no entra bien  en save ships. response: ")
                console.log(response.responseText);
            })
}

function placeSavedShips(appShips){
    for (var i = 0; i < appShips.length; i++){
        switch (appShips[i].type){
            case "AIRCRAFT":
                placeEachShip(appShips, i, 'aircraftHorizontal', 'aircraftVertical', 5);
                break;

            case "BATTLESHIP":
                placeEachShip(appShips, i, 'battleshipHorizontal', 'battleshipVertical', 4);
                break;

            case "SUBMARINE":
                placeEachShip(appShips, i, 'submarineHorizontal', 'submarineVertical', 3);
                break;

            case "DESTROYER":
                placeEachShip(appShips, i, 'destroyerHorizontal', 'destroyerVertical', 3);
                break;

            case "PATROL":
                placeEachShip(appShips, i, 'patrolHorizontal', 'patrolVertical', 2);
                break;
        };
    }
}
function placeEachShip(appShips, i, shipHorizontal, shipVertical, shipSize) {
    if(appShips[i].locations[0][1] !== appShips[i].locations[1][1]) {
        gridFix.addWidget($('<div class="grid-stack-item-content ' + shipHorizontal + '"></div>'),
        appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),shipSize,1);
    }
    else {
        gridFix.addWidget($('<div class="grid-stack-item-content ' +  shipVertical + '"></div>'),
        appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),1,shipSize);
    };
}

//----------------------------------------SALVOES--------------------------------------
//Paint salvoes when click
jQuery(document).ready(function($) {
    printEachSalvo();
    countTurn();
})
function printEachSalvo() {
    var json = getJson ();
    //var count = json.ship.length;

    var count;
    if (json.playerSunkBoats[2]) {
        count = json.ship.length - [Object.keys(json.playerSunkBoats[2]).length];
    }
    else {
        count = json.ship.length;
    }


    document.getElementById("count-salvoes").innerHTML= "salvoes availables: " + count;
    $(".cells").click(function() {
        if ($(this).hasClass("send-new-salvo") && count >= 0){
            $(this).removeClass("send-new-salvo");
            count++;
            document.getElementById("count-salvoes").innerHTML= "salvoes availables: " + count;
        }
        else if (count > 0){
            $(this).addClass("send-new-salvo");
            count--;
            document.getElementById("count-salvoes").innerHTML= "salvoes availables: " + count;
        }
        else {
            alertify.error("No more salvoes availables.");
        }
        getJson ()
    })
}

function takeSalvoCells() {
    var salvoesLocations=[];
    for(var i = 0; i < 10; i++) {
        for (var j = 1; j < 11; j++) {
            if($('#' + letters[i] + j + "S").hasClass("send-new-salvo")) {
                salvoesLocations.push(letters[i] + j);
            }
        }
    }
    return salvoesLocations;
}

function getJson (){
    var gamePlayerId = numberVariable;
    var myJson;
    $.ajax({
       type: "GET",
       url: "/api/game_view/" + gamePlayerId,
       async: false,
       success: function(json) { myJson = json }
    });
    return myJson;
}

function countTurn() {
    var gamePlayerId = numberVariable;
    var json = getJson ();
    var turn = 1;
    for (var i = 0; i < json.salvo.length; i++) {
        if (json.salvo[i].player.id == gamePlayerId) {
            turn++;
        }
    }
    document.getElementById("count-turn").innerHTML= "Your turn number: " + turn;
    return turn;
}

function postSalvo(){
    getJson ()
    var gamePlayerId = numberVariable;
    var playerSalvo = {
        "salvoLocation": takeSalvoCells()
    };
    console.log("gamePlayerId: "+ gamePlayerId);
    $.post({
         url: "/api/games/players/" + gamePlayerId + "/salvoes",
         data: JSON.stringify(playerSalvo),
         dataType: "text",
         contentType: "application/json"
       })
        .done(function(response) {
            console.log("la response: ")
            console.log(response)
            console.log(playerSalvo)
            location.reload();
        })
        .fail(function( response) {
            console.log("mal no entra bien  en save salvoes. response: ")
            var empty={
                        "error" : "Need to send at least one salvo."
                      }
            if(response.responseText == JSON.stringify(empty)){alertify.error("Need to send at least one salvo")}//NO FUNCTIONA
            console.log(response.responseText);
        })
}
