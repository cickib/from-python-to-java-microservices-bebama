// noLocation() called twice:
// when the browser doesn't support geolocating
// when it does, but an error/permission issue occured
function getCoords() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(getCity, noLocation);
    } else {
        noLocation();
    }
}

function noLocation() {
    $.ajax({
            type: "POST",
            url: "http://localhost:60000/get_location?apikey=" + apikey,
            data: JSON.stringify({"city": "N/A", "country": "N/A", "countryCode": "N/A"})
        }
    );
}

function getCity(position) {
    return new Promise(function (resolve, reject) {
        var request = new XMLHttpRequest();
        var method = 'GET';
        var url = 'http://maps.googleapis.com/maps/api/geocode/json?latlng=' +
            position.coords.latitude + ',' + position.coords.longitude + '&sensor=true';
        var async = true;
        request.open(method, url, async);
        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                if (request.status == 200) {
                    var data = JSON.parse(request.responseText);
                    var address = data.results[0];
                    resolve(address);
                    var city = address.address_components[3].short_name;
                    var countryCode = address.address_components[4].short_name;
                    var country = address.address_components[4].long_name;
                    sendLocationData(city, country, countryCode);
                }
                else {
                    reject(request.status);
                }
            }
        };
        request.send();
    });
}


function sendLocationData(city, country, countryCode) {
    $.ajax({
            type: "POST",
            url: "http://localhost:60000/get_location",
            data: JSON.stringify({"city": city, "country": country, "countryCode": countryCode})
        }
    );
}

getCoords();
