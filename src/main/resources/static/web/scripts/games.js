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
            games: []
        }
    })
    fetchJson("http://localhost:8080/api/games", {
            method: 'GET',
        })
        .then(function (json) {
            app.games = json;
        }).catch(function (error) {
            console.log("error")
        });
})