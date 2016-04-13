console.log("Welcome to index.js");

var bCrypt, hashedPassword, $password, $username, clientRN, serverRN;

function evaluateLogin( data ) {
    console.log("evaluateLogin data is : ", data);
}

function execLogin( data ) {
    console.log("execLogin says : ", clientRN, serverRN);
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
    console.log("logging...", username, hashedPassword);
    
    $.ajax({
        "url": "/api/serverId",
        "type": "GET",
        "headers": {},
        "data": {},
        "success": sendCid
    });
}

function result(hash){
    hashedPassword = hash;
    $("#hash").val(hashedPassword);
    $("#progressbar").progressbar({ value: 100 });
    getSid();
}

function crypt(){
	var salt;
	if( $("#salt").val().length != 0 ) {
            salt = $("#salt").val();
	} else {
            try{
                salt = bCrypt.gensalt($("#rounds").val());
            } catch( err ) {
                alert( err );
                return;
            }
            $("#salt").val(salt);
	}
        try{
            $("#progressbar").progressbar({ value: 0 });
            bCrypt.hashpw( 
                $password.val(), 
                $("#salt").val(), 
                result, 
                function() {
                    var value = $('#progressbar').progressbar( "option", "value" );
                    $('#progressbar').progressbar( { value: value + 1 } );
                }
            );
        }catch(err){
                alert(err);
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
    crypt();
}

function trickForm() {
    basicCheck();
    return false;
}

function load() {
    $username = $("#login-form").find("input[name='username']");
    $password = $("#login-form").find("input[name='password']");
    
    bCrypt = new bCrypt();
    if( bCrypt.ready() ){
            $("#login-form-button").removeAttr("disabled");
    }
    $("#progressbar").progressbar({ value: 0 });
    
    $('#login-form').submit( trickForm );
}

$(window).ready(load);