var hashedPassword;
var $password;
var $username;
var clientRN;
var serverRN;
var $loginStatus;
var cookieName = "musicladder-user-sessionid";

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ')
            c = c.substring(1);
        if (c.indexOf(name) == 0)
            return c.substring(name.length, c.length);
    }
    return "";
}

function setCookie(cname, cvalue, minutes) {
    var d = new Date();
    d.setTime(d.getTime() + (minutes * 60 * 1000)); //days*24*60*60*1000
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
    window.location = "/musicLadder";
}

function evaluateLoginServerResponse(object, status) {
    if (status === "success" && object != null) {
        console.log("status is : ", status);
        console.log("object is : ", object);
        setCookie(cookieName, object.sessionId, 30);

    } else {
        $loginStatus.html(status + " - Incorrect login information.");
    }
}

function sendLoginInformation(ignore) {
    var password = (serverRN + "").concat(hashedPassword.concat((clientRN + "")));
    console.log("LAST CALL");
    $.ajax({
        "url": "/api/login",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify({'username': $username.val(), 'password': password}),
        "success": evaluateLoginServerResponse,
        "error": evaluateLoginServerResponse
    });
}

function sendClientIdentifier(dataServerID) {
    serverRN = dataServerID;
    clientRN = Math.floor((Math.random() * 10) + 1);

    $.ajax({
        "url": "/api/clientId",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify(clientRN),
        "success": sendLoginInformation
    });
}

function requestServerIdentifier() {
    hashedPassword = sha256($password.val());

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

    if (username.length <= 5) {
        alert("username.length <= 5 : " + username.length);
        return false;
    }
    if (password.length <= 8) {
        alert("password.length <= 8 : " + password.length);
        return false;
    }
    var matches = password.match(/\d+/g);

    if (matches === null) {
        alert('password does not contain number(s)');
        return false;
    }

    requestServerIdentifier();
}

function breakSubmitRedirect() {
    basicCheck();
    return false;
}

function evaluateServerCookieResponse(object, status) {
    if (status === "success" && object != true) {
        window.location = "/musicLadder";
    } else {
        $loginStatus.html(status + " - Incorrect session id, please relog.");
    }
}

function evaluateUserCookie(cookie) {
    $.ajax({
        "url": "/api/session",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify(cookie),
        "success": evaluateServerCookieResponse,
        "error": evaluateServerCookieResponse
    });
}

function load() {
    console.log("index.js loaded...");

    var cookie = getCookie(cookieName);
    if (cookie) {
        evaluateUserCookie(cookie);
    } else {
        $username = $("#login-form").find("input[name='username']");
        $password = $("#login-form").find("input[name='password']");
        $loginStatus = $(".login-status");

        $("#login-form-button").removeAttr("disabled");

        $('#login-form').submit(breakSubmitRedirect);
    }
}

$(window).ready(load);