console.log("Welcome to index.js");

var bCrypt;
var hashedPassword;
var $password; 
var $username; 
var clientRN; 
var serverRN; 
var $loginStatus;

function evaluateLogin( data ) {
    if( data.userId && data.username && data.userLevel ) {
        window.location = '/musicLadder';
    } else {
        $loginStatus.text("Incorrect login info")
    }
}

function execLogin( data ) {
    var password = (serverRN+"").concat( hashedPassword.concat( (clientRN+"") ) );
    $.ajax({
        "url": "/api/login",
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
    var username = $username.val();
    hashedPassword = $password.val();
    
    $.ajax({
        "url": "/api/loginServerId",
        "type": "GET",
        "headers": {},
        "data": {},
        "success": sendCid
    });
}

function result( hash ){
    hashedPassword = hash;
    getSid();
}

function crypt(){
    var salt;
    try {
        salt = bCrypt.gensalt( 5 );
    } catch( err ) {
        alert( err );
        return;
    }
    try{
        bCrypt.hashpw( 
            $password.val(), 
            salt, 
            result
        );
    } catch( err ){
            alert( err );
            return;
    }
}

function basicCheck() {
    var username = $username.val();
    var password = $password.val();
    if( username.length <= 5 ) {
        alert("username.length <= 5 : " + username.length);
        return false;
    } 
    if (password.length <= 8 ) {
        alert("password.length <= 8 : " + password.length);
        return false;
    }
    var matches = password.match(/\d+/g);
    if (matches === null) {
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
    $username = $("#login-form").find("input[name='username']");
    $password = $("#login-form").find("input[name='password']");
    $loginStatus = $(".login-status");
    
    bCrypt = new bCrypt();
    if( bCrypt.ready() ){
            $("#login-form-button").removeAttr("disabled");
    }
    
    $('#login-form').submit( trickForm );
}

$(window).ready(load);