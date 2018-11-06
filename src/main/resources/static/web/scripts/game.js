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
            placeAlreadyShips(app.ships);
            salvoCross(json, app.salvo);
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

    function getMsg(json){
        if (numberVariable == json.gamePlayers[0].id){
            app.msgA = "(you)";
        }
        else{
            app.msgB="(you)"
        }
    }

    function placeAlreadyShips(ships){
        console.log("número de barcos: " + ships.length)
        var letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
        for (var i = 0; i < ships.length; i++){
            console.log("LAS CELDAS DE MI BARCO " + i + ": " + ships[i].locations)
            console.log("Longitud del barco :" + i + ": " + ships[i].locations.length)
            console.log("La primera celda es: " + ships[i].locations[0])
            console.log("La letra de la primera celda es: " + ships[i].locations[0][0])
            console.log("El número de la primera celda es: " + ships[i].locations[0][1])
            console.log("El número de la segunda celda es: " + ships[i].locations[1][1])


            if ((ships[i].locations[0][1] == ships[i].locations[1][1]) && ships[i].locations.length==3){
                grid.addWidget($('<div class="grid-stack-item-content carrierHorizontalRed"></div>'),
                ships[i].locations[0][1]-1, letters.indexOf(ships[i].locations[0][0]),1,3);
            }
            else if ((ships[i].locations[0][1] == ships[i].locations[1][1]) && ships[i].locations.length==2){
                grid.addWidget($('<div class="grid-stack-item-content patroalHorizontalRed"></div>'),
                ships[i].locations[0][1]-1, letters.indexOf(ships[i].locations[0][0]),1,2);
            }
            else if (ships[i].locations.length==3){
                grid.addWidget($('<div class="grid-stack-item-content carrierHorizontal"></div>'),
                ships[i].locations[0][1]-1, letters.indexOf(ships[i].locations[0][0]), 3, 1);
            }
            else{
                grid.addWidget($('<div class="grid-stack-item-content patroalHorizontal"></div>'),
                ships[i].locations[0][1]-1, letters.indexOf(ships[i].locations[0][0]), 2, 1);
            }
        }
    }






        //NO LO NECESITO-------------------------------
        //.............................................

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
            /*grid.addWidget($('<div id="patroal2"><div class="grid-stack-item-content patroalHorizontal"></div><div/>'),
            1, location[1], 1, 1);*/
    }
    else{
        $('#' + location).addClass("opponent-salvo").html(json.salvo[i].turn);
        $('#' + location + 'S').css(style="padding:0");
        }
    }

    //-------------------------------------------
    //------------------------------------------
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




//---------------------------------------------------------FUNCIONES PARA CRUCES
//---------------------------------------------------------





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

    function mySalvoesLocation(location, json, i) {
         console.log("yo le disparo: " + location);
        var letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
        var grid = $('#grid').data('gridstack');
        var horizontal = location[1]-1;
        var vertical = letters.indexOf(location[0]);
        document.getElementById("opponent-complete-grid").innerHTML+= "<img src='photos/cruzNaranja.png' alt='orange cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"

    }

    function opponentSalvoesLocation(location, json, i) {
        var grid = $('#grid').data('gridstack');
        console.log("Mi oponente me dispara: " + location);
        console.log("Mi oponente me dispara primer elemento:" + location[0])  //G
        console.log("Mi oponente me dispara segundo elemento:" + location[1]) //5
        var letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
        var horizontal = location[1]-1;
        var vertical = letters.indexOf(location[0]);
        if (!grid.isAreaEmpty(horizontal, vertical, 1, 1)){
            //($('#' + location).hasClass("grid-stack-item-content")) {
            console.log("me disparan y me dan:" + location);
            document.getElementById("my-complete-grid").innerHTML+= "<img src='photos/cruzRoja.png' alt='red cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"
        }
        else {
            console.log("me disparan pero no me dan:" + location);
            document.getElementById("my-complete-grid").innerHTML+= "<img src='photos/cruzAmarilla.png' alt='yellow cross' height='45' width='45' style='position:absolute; margin-left:" + horizontal*45 + "px; margin-top: " + vertical*45 + "px; z-index:1;'>"

            // grid.addWidget($('<img src="photos/cruzAmarilla.png" alt="red cross" height="40" width="40">'),location[1]-1, letters.indexOf(location[0]), 1, 1)
             //.style.position = 'absolute';
        }
    }





//---------------------------------------------------------FUNCIONES PARA BARCOS
//---------------------------------------------------------



$(function () {

    var options = {
        //grilla de 10 x 10
        width: 10,
        height: 10,
        padding:1,
        verticalMargin: 0,//separacion entre elementos (les llaman widgets)
        cellHeight: 45,
        disableResize: true,
		float: true,
        //removeTimeout: 100,
        disableOneColumnMode: true,//permite que el widget ocupe mas de una columna
        staticGrid: false,//false permite mover, true impide
        animate: true,//activa animaciones (cuando se suelta el elemento se ve más suave la caida)
        acceptWidgets: true
    }
    var optionsTwo = {
        //grilla de 10 x 10
        width: 5,
        height: 5,
        padding:1,
        verticalMargin: 0,//separacion entre elementos (les llaman widgets)
        cellHeight: 45,
        disableResize: true,
		float: true,
        //removeTimeout: 100,
        disableOneColumnMode: true,//permite que el widget ocupe mas de una columna
        staticGrid: false,//false permite mover, true impide
        animate: true,//activa animaciones (cuando se suelta el elemento se ve más suave la caida)
        acceptWidgets: true
    }
    //se inicializa el grid con las opciones
    $('#grid').gridstack(options);
    $('#grid-opponent').gridstack(options);
    $('#grid-two').gridstack(optionsTwo);

    gridTwo = $('#grid-two').data('gridstack');
    grid = $('#grid').data('gridstack');
    //grid-opponent = $('#grid-opponent').data('gridstack');

    //agregando un elmento(widget) desde el javascript a mi grilla directamente
    /*grid.addWidget($('<div id="carrier2"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'),
        0, 0, 3, 1);

    grid.addWidget($('<div id="patroal2"><div class="grid-stack-item-content patroalHorizontal"></div><div/>'),
        0, 1, 2, 1);*/

    //verificando si un area se encuentra libre (false si no está libre)
    console.log(grid.isAreaEmpty(1, 8, 3, 1));
    console.log(grid.isAreaEmpty(1, 7, 3, 1));

    $("#carrier,#carrier2").click(function(){
        if($(this).children().hasClass("carrierHorizontal")){
            grid.resize($(this),1,3);
            $(this).children().removeClass("carrierHorizontal");
            $(this).children().addClass("carrierHorizontalRed");
        }else{
            grid.resize($(this),3,1);
            $(this).children().addClass("carrierHorizontal");
            $(this).children().removeClass("carrierHorizontalRed");
        }
    });

    $("#patroal,#patroal2").click(function(){
        if($(this).children().hasClass("patroalHorizontal")){
            grid.resize($(this),1,2);
            $(this).children().removeClass("patroalHorizontal");
            $(this).children().addClass("patroalHorizontalRed");
        }else{
            grid.resize($(this),2,1);
            $(this).children().addClass("patroalHorizontal");
            $(this).children().removeClass("patroalHorizontalRed");
        }
    });

    //https://github.com/gridstack/gridstack.js/tree/develop/doc



});

