console.log('hi musicLadderIndex.js');

var $slider;
var $saveDuelButton;

function evaluateRating(currentRating, formerRating) {
    if (currentRating > formerRating) {
        return "#00ff00";
    } else if (currentRating < formerRating) {
        return "#ff0000";
    } else {
        return "#ffff00";
    }
}

function displayPlayedDuels( data ) {
     $.each(data, function( curDuelID, curDuel) {
        loadSong(curDuel.song1ID, function ( song1 ) {
            loadSong(curDuel.song2ID, function ( song2 ) {
                var song1Points = curDuel.song1AfterMatchRating - curDuel.song1BeforeMatchRating;
                var song1Update = parseFloat(Math.round(song1Points * 100) / 100).toFixed(2);
                var song2Points = curDuel.song2AfterMatchRating - curDuel.song2BeforeMatchRating;
                var song2Update = parseFloat(Math.round(song2Points * 100) / 100).toFixed(2);
                var style = "style=\"border: 1px solid black;\"";
                var style2 = "style=\"border: 1px solid black;font-weight: 700;\"";
                var content = "<tr>"
                        + "<td " + style + ">" + curDuel.duelID + "</td>"
                        + "<td " + style + ">" + song1.name + "</td>"
                        + "<td " + style + ">" + parseFloat(Math.round( curDuel.song1BeforeMatchRating * 100) / 100).toFixed(2) + "</td>"
                        + "<td " + style + ">" + song1Update +"</td>"
                        + "<td " + style + ">" + curDuel.song1Score + "</td>"
                        + "<td " + style2 + ">vs</td>"
                        + "<td " + style + ">" + curDuel.song2Score + "</td>"
                        + "<td " + style + ">" + song2Update + "</td>"
                        + "<td " + style + ">" + parseFloat(Math.round( curDuel.song2BeforeMatchRating * 100) / 100).toFixed(2) + "</td>"
                        + "<td " + style + ">" + song2.name + "</td>"
                        + "</tr>";
                $("#playedDuelsList tbody").append(content);
            });
        });
     });
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

function displayDuelsToPlay(data) {
    $.each(data, function( curDuelID, curDuel) {
        loadSong(curDuel.song1ID, function (song1) {
            loadSong(curDuel.song2ID, function (song2) {
                loadPredictions(curDuel.song1BeforeMatchRating, curDuel.song2BeforeMatchRating, function (predictions, status) {
                    if (curDuelID === 0) {
                        $("#competitor1Name").text(song1.name);
                        $("#competitor2Name").text(song2.name);
                        $("#competitor1Rating").text( parseFloat(Math.round(curDuel.song1BeforeMatchRating * 100) / 100).toFixed(2) );
                        $("#competitor2Rating").text( parseFloat(Math.round(curDuel.song2BeforeMatchRating * 100) / 100).toFixed(2) );
                        $("#prediction1Win").text( parseFloat(Math.round( predictions[0] * 100) / 100).toFixed(2) );
                        $("#prediction1Draw").text( parseFloat(Math.round( predictions[2] * 100) / 100).toFixed(2) );
                        $("#prediction1Loss").text( parseFloat(Math.round( predictions[4] * 100) / 100).toFixed(2) );
                        $("#prediction2Win").text( parseFloat(Math.round( predictions[5] * 100) / 100).toFixed(2) );
                        $("#prediction2Draw").text( parseFloat(Math.round( predictions[3] * 100) / 100).toFixed(2) );
                        $("#prediction2Loss").text( parseFloat(Math.round( predictions[1] * 100) / 100).toFixed(2) );
                    }

                    var style = "style=\"border: 1px solid black;\"";
                    var content = "<tr>"
                            + "<td " + style + ">" + curDuel.duelID + "</td>"
                            + "<td " + style + ">" + song1.name + "</td>"
                            + "<td " + style + ">" + parseFloat(Math.round(curDuel.song1BeforeMatchRating * 100) / 100).toFixed(2) + "</td>"
                            + "<td " + style + ">vs</td>"
                            + "<td " + style + ">" + parseFloat(Math.round(curDuel.song2BeforeMatchRating * 100) / 100).toFixed(2) + "</td>"
                            + "<td " + style + ">" + song2.name + "</td>"
                            + "</tr>";
                    $("#duelsToPlayList tbody").append(content);
                });
            });
        });
    });
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
                + "<td " + style + ">" + parseFloat(Math.round( v.currentRating * 100) / 100).toFixed(2) + "</td>"
                + "</tr>";
        $("#songsList tbody").append(content);
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

function loadDuelsToPlay(amount) {
    $.ajax({
        "url": "/musicLadderAPI/all/duelsToPlay/" + amount,
        "type": "GET",
        "data": {},
        "success": displayDuelsToPlay
    });
}

function loadPlayedDuels(amount) {
    $.ajax({
        "url": "/musicLadderAPI/all/duelsPlayed/" + amount,
        "type": "GET",
        "data": {},
        "success": displayPlayedDuels
    });
}

function saveDuel() {
    console.log('saving...');
    $.ajax({
        "url": "/musicLadderAPI/duel",
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": 
            JSON.stringify({ 'song1BeforeMatchRating': song1Rating,'song2BeforeMatchRating': song2Rating })
        ,
        "success": callback
    });
}

function load() {
    loadSongs();
    loadDuelsToPlay(5);
    loadPlayedDuels(15);

    $saveDuelButton = $("#saveDuel");
    
    $saveDuelButton.on("click", saveDuel );
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

$( window ).ready(load);