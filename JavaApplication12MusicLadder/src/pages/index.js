console.log("Welcome to index.js");

var hashedPassword;
var $password; 
var $username; 
var clientRN; 
var serverRN; 
var $loginStatus;

function evaluateLoginServerResponse( err, data ) {
    console.log("HI DUDE");
    console.log(" data is : ", err, data);
    if( data.userId && data.username && data.userLevel ) {
        window.location = "/musicLadder";
    } else {
        console.log("Hello");
        $loginStatus.html("Incorrect login information.");
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
        "fail": evaluateLoginServerResponse
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
        $loginStatus.html("The length of the username should be longer than 5 symbols.");
        return false;
    } 
    if (password.length <= 10 ) {
        $loginStatus.html("The length of the password should be longer than 10 symbols.");
        return false;
    }
    var matches = password.match(/\d+/g);
    
    if (matches === null) {
        $loginStatus.html("The password should contain at least 1 numeric value.");
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
}

$( window ).ready( load );