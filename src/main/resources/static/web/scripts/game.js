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
            console.log( location)
            /*grid.addWidget($('<div id="patroal2"><div class="grid-stack-item-content patroalHorizontal"></div><div/>'),
            1, location[1], 1, 1);*/
    }
    else{
        $('#' + location).addClass("opponent-salvo").html(json.salvo[i].turn);
        $('#' + location + 'S').css(style="padding:0");
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


$(function () {

    var options = {
        //grilla de 10 x 10
        width: 10,
        height: 10,
        padding:1,
        //separacion entre elementos (les llaman widgets)
        verticalMargin: 0,
        //altura de las celdas
        cellHeight: 45,
        //desabilitando el resize de los widgets
        disableResize: true,
        //widgets flotantes
		float: true,
        //removeTimeout: 100,
        //permite que el widget ocupe mas de una columna
        disableOneColumnMode: true,
        //false permite mover, true impide
        staticGrid: false,
        //activa animaciones (cuando se suelta el elemento se ve más suave la caida)
        animate: true,
        acceptWidgets: true
    }
    //se inicializa el grid con las opciones
    $('.grid-stack').gridstack(options);

    grid = $('#grid').data('gridstack');

    //agregando un elmento(widget) desde el javascript

    /*for (var i = 1; i <= 10; i++){
        (for j in [A, B, C, D, E, F, G, H, I, J]){
            if((#ji).hasClass
        }
    }*/



    grid.addWidget($('<div id="carrier2"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'),
        1, 5, 3, 1);

    grid.addWidget($('<div id="patroal2"><div class="grid-stack-item-content patroalHorizontal"></div><div/>'),
        1, 8, 2, 1);

    //agregando un elmento(widget) desde el javascript
    /*grid.addWidget($('<div id="carrier"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'),
        1, 6, 3, 1);

    grid.addWidget($('<div id="patroal"><div class="grid-stack-item-content patroalHorizontal"></div><div/>'),
        1, 7, 2, 1);*/

    //verificando si un area se encuentra libre
    //no está libre, false
    console.log(grid.isAreaEmpty(1, 8, 3, 1));
    //está libre, true
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

    //todas las funciones se encuentran en la documentación
    //https://github.com/gridstack/gridstack.js/tree/develop/doc
});

