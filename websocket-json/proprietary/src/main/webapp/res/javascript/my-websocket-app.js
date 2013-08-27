var ws = undefined;
var scrollHandle = undefined;
var messagesDiv = undefined;

$(function() {
    if (window.WebSocket) {
        ws = new WebSocket(socketPath);
        $(window).unload(function () {
                ws.close();
                ws = null
            }
        );      
    
        // listen for messages from server using standard syntax
        ws.onmessage = function (event) {
           var messageFromServer = jQuery.parseJSON(event.data);
           addMessage(messageFromServer.timestamp, messageFromServer.content);
        };
        ws.onerror = function (event) {
                $('#error_msg').html('Error: WebSocket not supported by the server');
        };
    } else {
        $('#error_msg').html('Error: WebSocket not supported by the browser');
    }
    var tabs = $( "#tabs" ).tabs();

    $('#input input').keydown(inputKeyDownHandler);
    $('#input input').focus();
    
    messagesDiv = document.getElementById('messages');
    scrollHandle = window.setInterval(scrollToBottom, 150);
});

function scrollToBottom() {
    if (messagesDiv !== undefined) {
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    } else {
        clearInterval(scrollHandle);
    }
}

function inputKeyDownHandler(event) {
    if (event.which != 13) {
        return true;
    }
    event.preventDefault();
    var textToSend = $('#input input').val();
    ws.send(textToSend);
    setTimeout(clearInput, 150);
}

function clearInput() {
    $('#input input').val("");
}

function addMessage(timestamp, content) {
    var time = getTimeFromTimestamp(timestamp);
    $("#messages").append(time + ' ' + content + '<br />');
};

function getTimeFromTimestamp(timestampString) {

    var timestamp       = parseInt(timestampString, 10);
    var dateTime        = new Date(timestamp);
    var hours           = dateTime.getHours()   < 10 ? ("0" + dateTime.getHours()) : dateTime.getHours();
    var minutes         = dateTime.getMinutes() < 10 ? ("0" + dateTime.getMinutes()) : dateTime.getMinutes();
    var seconds         = dateTime.getSeconds() < 10 ? ("0" + dateTime.getSeconds()) : dateTime.getSeconds();

    return hours + ':' + minutes + ':' + seconds;
}