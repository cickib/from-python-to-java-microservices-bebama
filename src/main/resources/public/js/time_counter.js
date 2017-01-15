var startRequest;
if (window.XMLHttpRequest) {
    startRequest = new XMLHttpRequest();
} else {
    startRequest = new ActiveXObject("Microsoft.XMLHTTP");
}
var url = "http://localhost:60000/startTime?time="+new Date().getTime()+"&apikey=" + apiKey;
startRequest.open("GET",url,false);
startRequest.send(null);

window.onbeforeunload = function(){
    var stopRequest;
    if (window.XMLHttpRequest){
        stopRequest = new XMLHttpRequest();
    } else {
        stopRequest = new ActiveXObject("Microsoft.XMLHTTP");
    }
    var url = "http://localhost:60000/stopTime?time="+new Date().getTime()+"&apikey=" + apiKey;
    stopRequest.open("GET",url,false);
    stopRequest.send(null);
};