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
            myGpId:"",
            opponentGpId:"",
            myId:"",
            opponentId:"",
            myHits:{},
            otherHits:{},
            currentSunkBoats:{},
            opponentSunkBoats:{},
            turnNumbers:[],
            myLefts:[],
            otherLefts:[],
            tableGame:[
                {
                    turn:"",
                    myHitsTable:[],
                    mySunks:[],
                    myLeftsTable:"",
                    otherHitsTable:[],
                    otherSunks:[],
                    otherLeftsTable:""
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
            app.myHits = json.myHits;
            app.otherHits = json.otherHits;
            app.currentSunkBoats = json.currentSunkBoats;
            app.opponentSunkBoats = json.opponentSunkBoats;
            app.turnNumbers = getOnlyTurnNumbers(json, app.salvo);
            app.myLefts = getLeftBoats(json.currentSunkBoats);
            app.otherLefts = getLeftBoats(json.opponentSunkBoats);
            app.tableGame = fillTableGame();
            getMsg(json);
            paintPositionOwnShips(app.ships);
            paintPositionSalvoes(json, app.salvo);
            placeSavedShips(app.ships);
            if (json.gamePlayers[1]) { salvoCross(json, app.salvo); }

            })
        .catch(function (error) {
            console.log("Wrong fetch...")
        });

    function getIds(json){
        if (json.gamePlayers[0].id==numberVariable){
            app.myGpId=json.gamePlayers[0].id;
            app.myId=json.gamePlayers[0].player.id;
            if (json.gamePlayers[1]) {
                app.opponentGpId=json.gamePlayers[1].id;
                app.opponentId=json.gamePlayers[1].player.id;
            }
        }
        else {
            app.myGpId=json.gamePlayers[1].id;
            app.myId=json.gamePlayers[1].player.id;
            app.opponentGpId=json.gamePlayers[0].id
            app.opponentId=json.gamePlayers[0].player.id;
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

    function getOnlyTurnNumbers(json, salvo) {
        var lastTurn = salvo.length/2;
        if (salvo.length % 2 !== 0 ) {
            lastTurn = (salvo.length/2) + 0.5;
        }
        var arrayTurns = [];
        while (lastTurn > 0) {
            arrayTurns.push(lastTurn);
            lastTurn--;
        }
        return arrayTurns;
    }

    function getLeftBoats(sunkBoats) {
        var liveBoats = [];
        var totalBoats = 5;
        //var totalBoats = app.myLefts[0];
        for (var i = Object.keys(sunkBoats).length; i > 0 ; i--) {
            liveBoats.push(totalBoats - (Object.keys(sunkBoats[i]).length));
        }
        return liveBoats;
    }

    function takeSunkInTurn(playerSunks, numberOfTurns, turn) {
        var computedSunks = []; // unique set of sunks already computed in turn i
        var sinkingNow = []; //
        var sinkInTurn = [];
        for (var i = 0; i < numberOfTurns; i++) {
            var position = i+1;
            for (var m = 0; m < Object.keys(playerSunks[position]).length; m++) {
                if (!computedSunks.includes(playerSunks[position][m].type)) {
                    computedSunks.push(playerSunks[position][m].type);
                    sinkingNow.push(playerSunks[position][m].type);
                }
            }
            sinkInTurn.push(sinkingNow);
            sinkingNow = [];
        }
        return sinkInTurn[turn].join("\n");
    }
    function fillTableGame() {
        var arrayTable=[];
        var allTurns = app.turnNumbers;
        var allMyLefts = app.myLefts;
        var allOtherLefts = app.otherLefts;
        var allMyHits = app.myHits;
        var allOtherHits = app.otherHits;
        var niceMyHits = [];
        var niceOtherHits = [];
        var sunksAccumulated = app.currentSunkBoats;
        var otherSunks = app.opponentSunkBoats;



        for (var i = 0; i < allTurns.length; i++) {

            var lastHits = allMyHits[Object.keys(allMyHits).length-i];
            for (var ship in lastHits) {
                niceMyHits.push(("" + ship).toLowerCase());
                var numberHits = lastHits[ship].length;
                niceMyHits.push(getStars(numberHits));
            }

            if(niceMyHits.length == 0) {
                niceMyHits = "-";
            }


            var lastOtherHits = allOtherHits[Object.keys(allMyHits).length-i];
            for (var ship in lastOtherHits) {
                niceOtherHits.push(("" + ship).toLowerCase()); // we make a copy otherwise the key goes to lowercase
                var numberHits = lastOtherHits[ship].length;
                niceOtherHits.push(getStars(numberHits));
            }

            if(niceOtherHits.length == 0) {
                niceOtherHits = "-";
            }

            arrayTable.push({
                           turn: allTurns[i],
                           myHitsTable: joinHits(niceMyHits),//allMyHits[i+1],//niceMyHits,
                           mySunks:takeSunkInTurn(sunksAccumulated, allTurns.length, allTurns.length-i-1),
                           myLeftsTable: allMyLefts[i],
                           otherHitsTable: joinHits(niceOtherHits), //allOtherHits[i+1],
                           otherSunks:takeSunkInTurn(otherSunks, allTurns.length, allTurns.length-i-1),
                           otherLeftsTable: allOtherLefts[i]
                       })
            var niceMyHits = [];
            var niceOtherHits = [];
        }
        return arrayTable;
    };

    function getStars (numberHits) {
        return "*".repeat(numberHits);
    }

    // Join hits per ship per line with stars
    function joinHits(hits) {
        var result = [];
        for (var i = 0; i < hits.length; i += 2) {
            result.push(hits[i] + " " + hits[i+1]);
        }
        return result.join("\n");
    }

    //NEED TO CHECK-------------------------------------------------------------------------------

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
                salvoes[i].locations.forEach(location => mySalvoesLocation(location, json, i));//mySalvosStyle
            }
            else {
                salvoes[i].locations.forEach(location => opponentSalvosStyle(location, json, i));
            }
        }
    }

    /*function mySalvosStyle(location, json, i) {
        $('#' + location + 'S').addClass("my-salvo").html(json.salvo[i].turn);
    }*/

    function opponentSalvosStyle(location, json, i){
    if ($('#' + location).hasClass("my-ship")){
            $('#' + location).addClass("hit-my-ship").html(json.salvo[i].turn);
    }
    else{
        $('#' + location).addClass("opponent-salvo").html(json.salvo[i].turn);
        $('#' + location + 'S').css(style="padding:0");
        }
    }

}); //END MAIN FUNCTION



//---------------------------------------------------------FUNCIONES PARA PONER CRUCES-------

    function salvoCross(json, salvoes){
    console.log("primer salvo: " + salvoes[0].locations)
        for (var i = 0; i < salvoes.length; i++) {
            if (salvoes[i].player.id == app.myGpId) {
                salvoes[i].locations.forEach(location => mySalvoesLocation(location, json, i));
            }
            else {
                salvoes[i].locations.forEach(location => opponentSalvoesLocation(location, json, i));
            }
        }
    }

//ORANGE (yo lanzo)
    function mySalvoesLocation(location, json, i) {
        //var grid = $('#grid').data('gridstack');
        var horizontal = location.substr(1) - 1;
        var vertical = letters.indexOf(location[0]);
        for (var i = 1; i <= Object.keys(json.myHits).length; i++) {
            if (json.myHits[i].AIRCRAFT) {
                for(var j = 0; j < json.myHits[i].AIRCRAFT.length; j++) {
                    if (location == json.myHits[i].AIRCRAFT[j]) {
                        document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/greenTick.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
                    }
                }
            }
            if (json.myHits[i].BATTLESHIP) {
                for(var j = 0; j < json.myHits[i].BATTLESHIP.length; j++) {
                    if (location == json.myHits[i].BATTLESHIP[j]) {
                        document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/greenTick.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
                    }
                }
            }
            if (json.myHits[i].SUBMARINE) {
                for(var j = 0; j < json.myHits[i].SUBMARINE.length; j++) {
                    if (location == json.myHits[i].SUBMARINE[j]) {
                        document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/greenTick.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
                    }
                }
            }
            if (json.myHits[i].DESTROYER) {
                for(var j = 0; j < json.myHits[i].DESTROYER.length; j++) {
                    if (location == json.myHits[i].DESTROYER[j]) {
                        document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/greenTick.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
                    }
                }
            }
            if (json.myHits[i].PATROL) {
                for(var j = 0; j < json.myHits[i].PATROL.length; j++) {
                    if (location == json.myHits[i].PATROL[j]) {
                        document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/greenTick.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
                    }
                }
            }
            else {
                document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/cruzNaranja.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
            }
        }


    }

    function opponentSalvoesLocation(location, json, i) {
        var grid = $('#gridFix').data('gridstack');
        console.log("Mi oponente me dispara: " + location);
        console.log("Mi oponente me dispara primer elemento:" + location[0])  //G
        console.log("Mi oponente me dispara segundo elemento:" + location[1]) //5
        var horizontal = location.substr(1) - 1;
        var vertical = letters.indexOf(location[0]);
//RED (me dan)
        if (!grid.isAreaEmpty(horizontal, vertical, 1, 1)){
            console.log("me disparan y me dan:" + location);
            document.getElementById("my-complete-grid").innerHTML+= "<img src='photos/cruzRoja.png' alt='red cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
        }
//YELLOW (no me dan)
        else {
            console.log("me disparan pero no me dan:" + location);
            document.getElementById("my-complete-grid").innerHTML+= "<img src='photos/cruzAmarilla.png' alt='yellow cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
        }
    }


//-------------------------------------------LOGOUT ...-------------------------------------
$(".logoutButton2").click(function(){
    console.log("ok");
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
        verticalMargin: 0,//separacion entre elementos (les llaman widgets)
        cellHeight: 45,
        disableResize: true,
		float: true,
        disableOneColumnMode: true,//permite que el widget ocupe mas de una columna
        staticGrid: false,//false permite mover, true impide
        animate: true,//activa animaciones (cuando se suelta el elemento se ve más suave la caida)
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


/*-------------------------------------------GIRAR BARQUITOS---------------------------*/

    $("#aircraft").dblclick(function(){
        var aircraftId = document.getElementById("aircraft");
        var x = parseInt(aircraftId.getAttribute("data-gs-x"));
        var y = aircraftId.getAttribute("data-gs-y");
        if($(this).children().hasClass("aircraftHorizontal") && fromHorizontalToVertical(4, x, y)) {
            grid.resize($(this),1,5);
            $(this).children().removeClass("aircraftHorizontal");
            $(this).children().addClass("aircraftVertical");
        }else if ($(this).children().hasClass("aircraftVertical") && fromVerticalToHorizontal(4, x, y)) {
            grid.resize($(this),5,1);
            $(this).children().addClass("aircraftHorizontal");
            $(this).children().removeClass("aircraftVertical");
        }
        else {
            alertify.error("Incorrect movement.");
        }
    });

    $("#battleship").dblclick(function(){
        var battleshipId = document.getElementById("battleship");
        var x = parseInt(battleshipId.getAttribute("data-gs-x"));
        var y = battleshipId.getAttribute("data-gs-y");
        if($(this).children().hasClass("battleshipHorizontal") && fromHorizontalToVertical(3, x, y)) {
            grid.resize($(this),1,4);
            $(this).children().removeClass("battleshipHorizontal");
            $(this).children().addClass("battleshipVertical");
        }else  if ($(this).children().hasClass("battleshipVertical") &&  fromVerticalToHorizontal(3, x, y)) {
            grid.resize($(this),4,1);
            $(this).children().addClass("battleshipHorizontal");
            $(this).children().removeClass("battleshipVertical");
        }
        else {
            alertify.error("Incorrect movement.");
        }
    });

    $("#submarine").dblclick(function(){
        var submarineId = document.getElementById("submarine");
        var x = parseInt(submarineId.getAttribute("data-gs-x"));
        var y = submarineId.getAttribute("data-gs-y");
        if($(this).children().hasClass("submarineHorizontal") && fromHorizontalToVertical(2, x, y)) {
            grid.resize($(this),1,3);
            $(this).children().removeClass("submarineHorizontal");
            $(this).children().addClass("submarineVertical");
        }else if ($(this).children().hasClass("submarineVertical") && fromVerticalToHorizontal(2, x, y)) {
            grid.resize($(this),3,1);
            $(this).children().addClass("submarineHorizontal");
            $(this).children().removeClass("submarineVertical");
        }
        else {
            alertify.error("Incorrect movement.");
        }
    });

    $("#destroyer").dblclick(function(){
        var destroyerId = document.getElementById("destroyer");
        var x = parseInt(destroyerId.getAttribute("data-gs-x"));
        var y = destroyerId.getAttribute("data-gs-y");
        if($(this).children().hasClass("destroyerHorizontal") && fromHorizontalToVertical(2, x, y)) {
            grid.resize($(this),1,3);
            $(this).children().removeClass("destroyerHorizontal");
            $(this).children().addClass("destroyerVertical");
        }else if ($(this).children().hasClass("destroyerVertical") && fromVerticalToHorizontal(2, x, y)) {
            grid.resize($(this),3,1);
            $(this).children().addClass("destroyerHorizontal");
            $(this).children().removeClass("destroyerVertical");
        }
        else {
            alertify.error("Incorrect movement.");
        }
    });

    $("#patrol").dblclick(function(){
        var patrolId = document.getElementById("patrol");
        var x = parseInt(patrolId.getAttribute("data-gs-x"));
        var y = patrolId.getAttribute("data-gs-y");
        if($(this).children().hasClass("patrolHorizontal") && fromHorizontalToVertical(1, x, y)) {
            grid.resize($(this),1,2);
            $(this).children().removeClass("patrolHorizontal");
            $(this).children().addClass("patrolVertical");
        }else if ($(this).children().hasClass("patrolVertical") && fromVerticalToHorizontal(1, x, y)) {
            grid.resize($(this),2,1);
            $(this).children().addClass("patrolHorizontal");
            $(this).children().removeClass("patrolVertical");
        }
        else {
            alertify.error("Incorrect movement.");
        }
    });


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
                if(!((parseInt(x)+length < 10) && (grid.isAreaEmpty((parseInt(x)+i),y,1,1)))){
                return false;
            }
            i--;
        }
        return true;
    }

    //https://github.com/gridstack/gridstack.js/tree/develop/doc
});



//--------------------------------------SAVE SHIPS----------------------------------------------------------

function saveShips(){
    var currentURL=window.location.href; //http://localhost:8080/web/game.html?gp=1
    var gamePlayerId = takeNumberURL(currentURL);
    function takeNumberURL(url){
        var n = url.slice(url.indexOf("gp=")+3);
        console.log(n)
        return n;
    }
    console.log("gamePlayerId: "+ gamePlayerId);

    var myShips= new Set;
    myShips = [
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

    function aircraftData(){
        var aircraftId = document.getElementById("aircraft");
        var x = parseInt(aircraftId.getAttribute("data-gs-x"));
        var y = aircraftId.getAttribute("data-gs-y");
        var number = x+1;
        var letter = letters[y];
        if(aircraftId.firstChild.classList.contains("aircraftVertical")){
            var first = letter+number;
            var second = (letters[letters.indexOf(letter)+1])+number;
            var third = (letters[letters.indexOf(letter)+2])+number;
            var fourth = (letters[letters.indexOf(letter)+3])+number;
            var fifth = (letters[letters.indexOf(letter)+4])+number;
            myShips[0].shipLocation = [first, second, third, fourth, fifth];
        }
        else{
            var first = letter+number;
            var second = letter + (number+1);
            var third = letter + (number+2);
            var fourth = letter + (number+3);
            var fifth = letter + (number+4);
            myShips[0].shipLocation = [first, second, third, fourth, fifth];
        }
    }
    function battleshipData(){
        var battleshipId = document.getElementById("battleship");
        var x = parseInt(battleshipId.getAttribute("data-gs-x"));
        var y = battleshipId.getAttribute("data-gs-y");
        var number = x+1;
        var letter = letters[y];
        if(battleshipId.firstChild.classList.contains("battleshipVertical")){
            var first = letter+number;
            var second = (letters[letters.indexOf(letter)+1])+number;
            var third = (letters[letters.indexOf(letter)+2])+number;
            var fourth = (letters[letters.indexOf(letter)+3])+number;
            myShips[1].shipLocation = [first, second, third, fourth];
        }
        else{
            var first = letter+number;
            var second = letter + (number+1);
            var third = letter + (number+2);
            var fourth = letter + (number+3);
            myShips[1].shipLocation = [first, second, third, fourth];
        }
    }
    function submarineData(){
        var submarineId = document.getElementById("submarine");
        var x = parseInt(submarineId.getAttribute("data-gs-x"));
        var y = submarineId.getAttribute("data-gs-y");
        var number = x+1;
        var letter = letters[y];
        if(submarineId.firstChild.classList.contains("submarineVertical")){
            var first = letter+number;
            var second = (letters[letters.indexOf(letter)+1])+number;
            var third = (letters[letters.indexOf(letter)+2])+number;
            myShips[2].shipLocation = [first, second, third];
        }
        else{
            var first = letter+number;
            var second = letter + (number+1);
            var third = letter + (number+2);
            myShips[2].shipLocation = [first, second, third];
        }
    }
    function destroyerData(){
        var destroyerId = document.getElementById("destroyer");
        var x = parseInt(destroyerId.getAttribute("data-gs-x"));
        var y = destroyerId.getAttribute("data-gs-y");
        var number = x+1;
        var letter = letters[y];
        if(destroyerId.firstChild.classList.contains("destroyerVertical")){
            var first = letter+number;
            var second = (letters[letters.indexOf(letter)+1])+number;
            var third = (letters[letters.indexOf(letter)+2])+number;
            myShips[3].shipLocation = [first, second, third];
        }
        else{
            var first = letter+number;
            var second = letter + (number+1);
            var third = letter + (number+2);
            myShips[3].shipLocation = [first, second, third];
        }
    }
    function patrolData(){
        var patrolId = document.getElementById("patrol");
        var x = parseInt(patrolId.getAttribute("data-gs-x"));
        var y = patrolId.getAttribute("data-gs-y");
        var number = x+1;
        var letter = letters[y];
        if(patrolId.firstChild.classList.contains("patrolVertical")){
            var first = letter+number;
            var second = (letters[letters.indexOf(letter)+1])+number;
            myShips[4].shipLocation = [first, second];
        }
        else{
            var first = letter+number;
            var second = letter + (number+1);
            myShips[4].shipLocation = [first, second];
        }
    }
    aircraftData();
    battleshipData();
    submarineData();
    destroyerData();
    patrolData();
    console.log(myShips);
    addShips(myShips, gamePlayerId);
    location.reload();
};
function addShips(myShips, gamePlayerId) {
        $.post({
             url: "/api/games/players/" + gamePlayerId + "/ships",
             data: JSON.stringify(myShips),
             dataType: "text",
             contentType: "application/json"
           })
            .done(function(response) {
                console.log("la response: ")
                console.log(response)
                console.log(myShips)

                $.get("/api/game_view/" + gamePlayerId)
                    .done(function(json){
                        app.ships = myShips;
                        placeSavedShips(app.ships);
                        console.log("app.ships: ")
                        console.log(app.ships)
                    });
            })
            .fail(function( response ) {
                console.log("mal no entra bien  en save ships. response: ")
                console.log(response.responseText);
            })
        console.log("Save ships ha entrado en la función")
}

function placeSavedShips(appShips){
    for (var i = 0; i < appShips.length; i++){
        switch (appShips[i].type){
            case "AIRCRAFT":
                if(appShips[i].locations[0][1] !== appShips[i].locations[1][1]) {
                    gridFix.addWidget($('<div class="grid-stack-item-content aircraftHorizontal"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),5,1);
                }
                else {
                    gridFix.addWidget($('<div class="grid-stack-item-content aircraftVertical"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),1,5);
                };
                break;

            case "BATTLESHIP":
                if(appShips[i].locations[0][1] !== appShips[i].locations[1][1]) {
                    gridFix.addWidget($('<div class="grid-stack-item-content battleshipHorizontal"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),4,1);
                }
                else {
                    gridFix.addWidget($('<div class="grid-stack-item-content battleshipVertical"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),1,4);
                };
                break;

            case "SUBMARINE":
                if(appShips[i].locations[0][1] !== appShips[i].locations[1][1]) {
                    gridFix.addWidget($('<div class="grid-stack-item-content submarineHorizontal"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),3,1);
                }
                else {
                    gridFix.addWidget($('<div class="grid-stack-item-content submarineVertical"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),1,3);
                };
                break;

            case "DESTROYER":
                if(appShips[i].locations[0][1] !== appShips[i].locations[1][1]) {
                    gridFix.addWidget($('<div class="grid-stack-item-content destroyerHorizontal"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),3,1);
                }
                else {
                    gridFix.addWidget($('<div class="grid-stack-item-content destroyerVertical"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),1,3);
                };
                break;

            case "PATROL":
                if(appShips[i].locations[0][1] !== appShips[i].locations[1][1]) {
                    gridFix.addWidget($('<div class="grid-stack-item-content patrolHorizontal"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),2,1);
                }
                else {
                    gridFix.addWidget($('<div class="grid-stack-item-content patrolVertical"></div>'),
                    appShips[i].locations[0].slice(1,appShips[i].locations[0].length)-1, letters.indexOf(appShips[i].locations[0][0]),1,2);
                };
                break;
        };
    }
}

//----------------------------------------SALVOES--------------------------------------
//paint salvoes when click
jQuery(document).ready(function($) {
    printEachSalvo();
    countTurn();
})
function printEachSalvo() {
    var json = getJson ();
    var count = json.ship.length;
    document.getElementById("count-salvoes").innerHTML= "salvoes availables: " + count;
    $(".cells").click(function() {

        if ($(this).hasClass("send-new-salvo") && count>=0){
            $(this).removeClass("send-new-salvo");
            count++;
            document.getElementById("count-salvoes").innerHTML= "salvoes availables: " + count;
        }
        else if (count>0){
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
    for(var i = 0; i<10; i++) {
        for (var j = 1; j < 11; j++) {
            if($('#' + letters[i] + j + "S").hasClass("send-new-salvo")) {
                salvoesLocations.push(letters[i] + j);
            }
        }
    }
    console.log(salvoesLocations);
    return salvoesLocations;
}


function getJson (){
    var currentURL=window.location.href; //http://localhost:8080/web/game.html?gp=1
    var gamePlayerId = takeNumberURL(currentURL);
    function takeNumberURL(url){
        var n = url.slice(url.indexOf("gp=")+3);
        console.log(n)
        return n;
    }
    var myJson;
    $.ajax({
       type: "GET",
       url: "/api/game_view/" + gamePlayerId,
       async: false,
       success: function(json) { myJson = json }
    });
    //console.log(myJson)
    return myJson;
}

function countTurn() {
    var currentURL=window.location.href; //http://localhost:8080/web/game.html?gp=1
    var gamePlayerId = takeNumberURL(currentURL);
    function takeNumberURL(url){
        var n = url.slice(url.indexOf("gp=")+3);
        console.log(n)
        return n;
    }
    var json = getJson ();
    var turn = 1;
    for (var i = 0; i < json.salvo.length; i++) {
        if (json.salvo[i].player.id == gamePlayerId) {
            turn++;
        }
    }
    console.log("turno: " + turn);
    document.getElementById("count-turn").innerHTML= "Your turn number: " + turn;
    return turn;
}

function postSalvo(){
    getJson ()
    var currentURL=window.location.href; //http://localhost:8080/web/game.html?gp=1
    var gamePlayerId = takeNumberURL(currentURL);
    function takeNumberURL(url){
        var n = url.slice(url.indexOf("gp=")+3);
        //console.log(n)
        return n;
    }
    var mySalvo = {
        //"turnNumber": countTurn(),
        "salvoLocation": takeSalvoCells()
    };
    console.log("gamePlayerId: "+ gamePlayerId);
    $.post({
         url: "/api/games/players/" + gamePlayerId + "/salvoes",
         data: JSON.stringify(mySalvo),
         dataType: "text",
         contentType: "application/json"
       })
        .done(function(response) {
            console.log("la response: ")
            console.log(response)
            console.log(mySalvo)
            location.reload();
        })
        .fail(function( response ) {
            console.log("mal no entra bien  en save salvoes. response: ")
            var empty={
                        "error" : "Need to send at least one salvo."
                      }
            if(response.responseText == JSON.stringify(empty)){alertify.error("Need to send at least one salvo")}//NO FUNCTIONA
            console.log(response.responseText);
        })
}
