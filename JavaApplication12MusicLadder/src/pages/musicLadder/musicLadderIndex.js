console.log('hi musicLadderIndex.js');

//var $songList;
//var $duelsToPlayList;
var $slider;

function evaluateRating(currentRating, formerRating) {
    if (currentRating > formerRating) {
        return "#00ff00";
    } else if (currentRating < formerRating) {
        return "#ff0000";
    } else {
        return "#ffff00";
    }
}

function loadSong(songId, callback) {
    $.ajax({
        "url": "/musicLadderAPI/song/" + songId,
        "type": "GET",
        "data": {},
        "success": callback
    });
}

function loadPredictions(song1Rating, song2Rating, callback) {
    $.ajax({
        "url": "/musicLadderAPI/probability",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": 
            JSON.stringify({ 'song1BeforeMatchRating': song1Rating,'song2BeforeMatchRating': song2Rating })
        ,
        "success": callback
    });
}

function eachDuelFunc(curDuelID, curDuel) {

    loadSong(curDuel.song1ID, function (song1) {
        loadSong(curDuel.song2ID, function (song2) {
            loadPredictions(curDuel.song1BeforeMatchRating, curDuel.song2BeforeMatchRating, function (predictions, status) {
                console.log('Status ? : ', status);
                if (curDuelID === 0) {
                    $("#competitor1Name").text(song1.name);
                    $("#competitor2Name").text(song2.name);
                    $("#competitor1Rating").text(curDuel.song1BeforeMatchRating);
                    $("#competitor2Rating").text(curDuel.song2BeforeMatchRating);
                    $("#prediction1Win").text(predictions[0]);
                    $("#prediction1Draw").text(predictions[2]);
                    $("#prediction1Loss").text(predictions[4]);
                    $("#prediction2Win").text(predictions[5]);
                    $("#prediction2Draw").text(predictions[3]);
                    $("#prediction2Loss").text(predictions[1]);
                }

                var style = "style=\"border: 1px solid black;\"";
                var content = "<tr>"
                        + "<td " + style + ">" + curDuel.duelID + "</td>"
                        + "<td " + style + ">" + song1.name + "</td>"
                        + "<td " + style + ">" + curDuel.song1BeforeMatchRating + "</td>"
                        + "<td " + style + ">vs</td>"
                        + "<td " + style + ">" + curDuel.song2BeforeMatchRating + "</td>"
                        + "<td " + style + ">" + song2.name + "</td>"
                        + "</tr>";
                $("#duelsToPlayList tbody").append(content);
            });
        });
    });
}

function displayPlayedDuels(data) {

    $.each(data, eachDuelFunc);
}

function displaySongs(data) {
    $.each(data, function (k, v) {
        var style = "style=\"border: 1px solid black;\"";
        var style2 = "style=\"border: 1px solid black;width: 20px;background-color:" + evaluateRating(v.currentRating, v.formerRating) + "\"";
        var content = "<tr>"
                + "<td " + style + ">" + (k + 1) + "</td>"
                + "<td " + style + ">" + v.name + "</td>"
                + "<td " + style + ">" + (v.wins + v.draws + v.loses) + "</td>"
                + "<td " + style + ">" + v.wins + "</td>"
                + "<td " + style + ">" + v.draws + "</td>"
                + "<td " + style + ">" + v.loses + "</td>"
                + "<td " + style2 + "></td>"
                + "<td " + style + ">" + v.currentRating + "</td>"
                + "</tr>";
        $("#songsList tbody").append(content);
    });
}

function loadPlayedDuels(amount) {
    $.ajax({
        "url": "/musicLadderAPI/all/duelsToPlay/" + amount,
        "type": "GET",
        "data": {},
        "success": displayPlayedDuels
    });
}

function loadSongs() {
    //Send the AJAX call to the server
    $.ajax({
        //The URL to process the request
        'url': '/musicLadderAPI/all/songs/1',
        //The type of request, also known as the "method" in HTML forms
        //Can be 'GET' or 'POST'
        'type': 'GET',
        //Any post-data/get-data parameters
        //This is optional
        'data': {
//          'paramater1' : 'value',
//          'parameter2' : 'another value'
        },
        //The response from the server
        'success': displaySongs
    });
}

function load() {
    loadSongs();
    loadPlayedDuels(5);

//    $songList = $("#songsList");
//    $duelsToPlayList = $("#duelsToPlayList");

    $(".sliderr").slider({
        value: 5,
        min: 1,
        max: 10,
        step: 1
    })
            .each(function () {

                // Add labels to slider whose values 
                // are specified by min, max

                // Get the options for this slider (specified above)
                var opt = $(this).data().uiSlider.options;

                // Get the number of possible values
                var vals = opt.max - opt.min;

                // Position the labels
                for (var i = 0; i <= vals; i++) {

                    // Create a new element and position it with percentages
                    var el = $("<label style=\"position: absolute;width: 20px;margin-top: 20px;margin-left: -10px;text-align: center;\">" + (i + opt.min) + "</label>").css("left", (i / vals * 100) + "%");

                    // Add the element inside #slider
                    $(".sliderr").append(el);

                }

            });
}

$(document).ready(load);