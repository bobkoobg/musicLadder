console.log("Namaste from register JS");

var $username;
var $password
var $passwordRepeated; 
var $registrationStatus;
var hashedPassword; 
var clientRN; 
var serverRN;

function evaluateLogin( data ) {
    if ( data.response === "Successfull registration" ) {
         window.location = '/';
    } else {
        $registrationStatus.text("Internal error");
    }
}

function execLogin( data ) {
    var password = (serverRN+"").concat( hashedPassword.concat( (clientRN+"") ) );
    $.ajax({
        "url": "/api/register",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify( {'username': $username.val(), 'password': password} ),
        "success": evaluateLogin
    });
}

function sendCid( data ) {
    serverRN = data;
    clientRN = Math.floor((Math.random() * 10) + 1);
    
    $.ajax({
        "url": "/api/clientId",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify( clientRN ),
        "success": execLogin
    });
}

function getSid() {
    hashedPassword = sha256( $password.val() );
    
    $.ajax({
        "url": "/api/registerServerId",
        "type": "GET",
        "headers": {},
        "data": {},
        "success": sendCid
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
    
    getSid();
}

function trickForm() {
    basicCheck();
    return false;
}

function load() {
    $username = $("#register-form").find("input[name='username']");
    $password = $("#register-form").find("input[name='password']");
    $passwordRepeated = $("#register-form").find("input[name='password-repeated']");
    $registrationStatus = $(".registration-status");

    $("#register-form-button").removeAttr("disabled");

    $('#register-form').submit( trickForm );
}

$( window ).ready( load );