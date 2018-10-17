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
            jackTotalPoints:[],
            chloeTotalPoints:[],
            kimTotalPoints:[],
            almeidaTotalPoints:[],
            jackSummary:[],
            chloeSummary:[],
            kimSummary:[],
            almeidaSummary:[]
        }
    })
    fetchJson("http://localhost:8080/api/games", {
            method: 'GET',
        })
        .then(function (json) {
            app.games = json;
           // app.jackTotalPoints = joinAllPunctuations(app.jackTotalPoints, 1, json);
           // app.chloeTotalPoints = joinAllPunctuations(app.chloeTotalPoints, 2, json);
            //app.KimTotalPoints = joinAllPunctuations(app.kimTotalPoints, 3, json);
            //app.AlmeidaTotalPoints = joinAllPunctuations(app.almeidaTotalPoints, 4, json);
            app.jackSummary = operationsGetTablePoints(app.jackSummary, app.jackTotalPoints, 1, json);
            app.chloeSummary = operationsGetTablePoints(app.chloeSummary, app.chloeTotalPoints, 2, json);
            app.kimSummary = operationsGetTablePoints(app.kimSummary, app.kimTotalPoints, 3, json);
            app.almeidaSummary = operationsGetTablePoints(app.almeidaSummary, app.almeidaTotalPoints, 4, json);
        }).catch(function (error) {
            console.log("error")
        });
        function joinAllPunctuations(playerPoints, number, json){
            var playerPoints;
            for (var i =0; i<json.length; i++){
                if (json[i].gamePlayers[0] && json[i].gamePlayers[0].player.id == number){
                    playerPoints.push(json[i].gamePlayers[0].scores);
                }
                else if (json[i].gamePlayers[1] && json[i].gamePlayers[1].player.id == number){
                    playerPoints.push(json[i].gamePlayers[1].scores);
                }
            }
            return playerPoints;
        }

        function operationsGetTablePoints(summary, playerPoints, number, json){
            var everyResult = joinAllPunctuations(playerPoints, number, json);
            var won=0;
            var lost=0;
            var tied=0;
            var sum=0;
            for (var i = 0; i<everyResult.length; i++){
                if (everyResult[i]==3){
                    won++;
                }
                else if (everyResult[i]==0){
                    lost++;
                }
                else{
                    tied++;
                }
            }
            sum = (won*3) + (tied*2);
            summary.push(sum);
            summary.push(won);
            summary.push(lost);
            summary.push(tied);
            return summary;
        }

})