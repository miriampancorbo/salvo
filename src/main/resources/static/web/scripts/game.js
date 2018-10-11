/*var myTableRows = [
    [{"th":""},{"th":"1"},{"th":"2"},{"th":"3"}],
    [{"th":"A"},{"td":""},{"td":""},{"td":""}],
    [{"th":"B"},{"td":""},{"td":""},{"td":""}],
    [{"th":"C"},{"td":""},{"td":""},{"td":""}],
];*/



/*var table = document.createElement("table");
for(var rowIndex in myTableRows) {
    var row = document.createElement("tr");
    for(var colIndex in myTableRows[rowIndex]) {
        for(var tag in myTableRows[rowIndex][colIndex]) {
            var cell = document.createElement(tag);
            var cellContents = document.createTextNode(myTableRows[rowIndex][colIndex][tag]);
            cell.appendChild(cellContents);
            row.appendChild(cell);
        }
    }
    table.appendChild(row);
}
document.body.appendChild(table);*/
 var app;

    app = new Vue({
        el: '#app',
        data: {
            vertical: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
            horizontal: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]
        }
    })

