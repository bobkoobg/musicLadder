var $username;
var $password;
var $passwordRepeated; 
var $registrationStatus;
var hashedPassword; 
var clientRN; 
var serverRN;
var cookieName = "musicladder-user-sessionid";

function evaluateRegistrationServerResponse( object, status ) {
    if ( status === "success" && object != null ) {
         window.location = '/';
    } else {
        $registrationStatus.html(status + " - Incorrect login information.");
    }
}

function sendRegistrationInformation( data ) {
    var password = (serverRN+"").concat( hashedPassword.concat( (clientRN+"") ) );
    $.ajax({
        "url": "/api/register",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify( {'username': $username.val(), 'password': password} ),
        "success": evaluateRegistrationServerResponse,
        "error": evaluateRegistrationServerResponse
    });
}

function sendClientIdentifier( data ) {
    serverRN = data;
    clientRN = Math.floor((Math.random() * 10) + 1);
    
    $.ajax({
        "url": "/api/clientId",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify( clientRN ),
        "success": sendRegistrationInformation
    });
}

function requestServerIdentifier() {
    hashedPassword = sha256( $password.val() );
    
    $.ajax({
        "url": "/api/registerServerId",
        "type": "GET",
        "headers": {},
        "data": {},
        "success": sendClientIdentifier
    });
}

function basicCheck() {
    var username = $username.val();
    var password = $password.val();
    var passwordRepeated = $passwordRepeated.val();
    
    if( username.length <= 5 ) {
        alert("username.length <= 5 : " + username.length);
        return false;
    } 
    if ( password !== passwordRepeated ) {
        alert("password fields are not matching");
        return false;
    }
    if ( password.length <= 8 || passwordRepeated.length <= 8 ) {
        alert("password.length <= 8 : " + password.length);
        return false;
    }
    var matches = password.match(/\d+/g);
    var matchesRepeated = passwordRepeated.match(/\d+/g);
    
    if ( matches === null || matchesRepeated === null ) {
        alert('password does not contain number(s)');
        return false;
    }
    
    requestServerIdentifier();
}

function breakSubmitRedirect() {
    basicCheck();
    return false;
}

function loadComponents() {
    $username = $("#register-form").find("input[name='username']");
    $password = $("#register-form").find("input[name='password']");
    $passwordRepeated = $("#register-form").find("input[name='password-repeated']");
    $registrationStatus = $(".registration-status");
    
    $("#register-form-button").removeAttr("disabled");
    $('#register-form').submit( breakSubmitRedirect );
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

function evaluateServerCookieResponse(object, status) {
    if (status === "success" && object != true) {
        window.location = "/musicLadder";
    } else {
        $registrationStatus = $(".registration-status");
        $registrationStatus.html(status + " - Incorrect session id, please relog.");
        loadComponents();
    }
}

function evaluateUserCookie( cookie ) {
    $.ajax({
        "url": "/api/session",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify( cookie ),
        "success": evaluateServerCookieResponse,
        "error": evaluateServerCookieResponse
    });
}

function load() {
    console.log("register.js loaded...");
    
    var cookie = getCookie( cookieName );
    if ( cookie ) {
        evaluateUserCookie( cookie );
    } else {
        loadComponents();
    }
}

$( window ).ready( load );