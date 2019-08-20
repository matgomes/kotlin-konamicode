$(function () {
    var result = $('#result');
    var ws = new WebSocket("ws://localhost:8080/ws");

    $(document).keyup(function(ev) {
        ws.send(ev.keyCode);
    });

    ws.onmessage = function(msg) {
        result.html(msg.data).show().fadeOut(2000);   // print the result
    };
});