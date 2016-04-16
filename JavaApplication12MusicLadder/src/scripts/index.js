var hashedPassword;
var $password; 
var $username; 
var clientRN; 
var serverRN; 
var $loginStatus;

function evaluateLoginServerResponse( object, status ) {
    if ( status === "success" && object != null ) {
        window.location = "/musicLadder";
    } else {
        $loginStatus.html(status + " - Incorrect login information.");
    }
}

function sendLoginInformation( ignore ) {
    var password = (serverRN+"").concat( hashedPassword.concat( (clientRN+"") ) );
    console.log("LAST CALL");
    $.ajax({
        "url": "/api/login",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify( {'username': $username.val(), 'password': password} ),
        "success": evaluateLoginServerResponse,
        "error": evaluateLoginServerResponse
    });
}

function sendClientIdentifier( dataServerID ) {
    serverRN = dataServerID;
    clientRN = Math.floor((Math.random() * 10) + 1);
    
    $.ajax({
        "url": "/api/clientId",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify( clientRN ),
        "success": sendLoginInformation
    });
}

function requestServerIdentifier() {
    hashedPassword = sha256 ( $password.val() );
    
    $.ajax({
        "url": "/api/loginServerId",
        "type": "GET",
        "headers": {},
        "data": {},
        "success": sendClientIdentifier
    });
}

function basicCheck() {
    var username = $username.val();
    var password = $password.val();
    
    if( username.length <= 5 ) {
        alert("username.length <= 5 : " + username.length);
        return false;
    }
    if ( password.length <= 8 ) {
        alert("password.length <= 8 : " + password.length);
        return false;
    }
    var matches = password.match(/\d+/g);
    
    if ( matches === null ) {
        alert('password does not contain number(s)');
        return false;
    }
    
    requestServerIdentifier();
}

function breakSubmitRedirect() {
    basicCheck();
    return false;
}

function load() {
    $username = $("#login-form").find("input[name='username']");
    $password = $("#login-form").find("input[name='password']");
    $loginStatus = $(".login-status");
    
    $("#login-form-button").removeAttr("disabled");
    
    $('#login-form').submit( breakSubmitRedirect );
    
    console.log("Index.js loaded...");
}

$( window ).ready( load );