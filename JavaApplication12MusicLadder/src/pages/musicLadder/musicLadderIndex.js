'use strict';

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

function displayPlayedDuels(data) {
    var style = "border: 1px solid black;";
    var style2 = "border: 1px solid black;font-weight: 700;";
    var song1Points, song1Update, song2Points, song2Update, style3, style4;

    $.each(data, function (curDuelID, curDuel) {
        loadSong(curDuel.song1ID, function (song1) {
            loadSong(curDuel.song2ID, function (song2) {
                song1Points = curDuel.song1AfterMatchRating - curDuel.song1BeforeMatchRating;
                song1Update = parseFloat(Math.round(song1Points * 100) / 100).toFixed(2);
                song2Points = curDuel.song2AfterMatchRating - curDuel.song2BeforeMatchRating;
                song2Update = parseFloat(Math.round(song2Points * 100) / 100).toFixed(2);

                if (song1Update > 0) {
                    song1Update = "+" + song1Update;
                }

                if (song2Update > 0) {
                    song2Update = "+" + song2Update;
                }

                style3 = "border: 1px solid black;background-color: " + evaluateRating(curDuel.song1AfterMatchRating, curDuel.song1BeforeMatchRating) + ";";
                style4 = "border: 1px solid black;background-color: " + evaluateRating(curDuel.song2AfterMatchRating, curDuel.song2BeforeMatchRating) + ";";


                var source = $("#played-duels-row-template").html();
                var template = Handlebars.compile(source);
                var context = {
                    style: style,
                    song1Name: song1.name,
                    style3: style3,
                    song1Update: song1Update,
                    song1Rating: parseFloat(Math.round(curDuel.song1AfterMatchRating * 100) / 100).toFixed(2),
                    song1Score: curDuel.song1Score,
                    style2: style2,
                    song2Score: curDuel.song2Score,
                    style4: style4,
                    song2Rating: parseFloat(Math.round(curDuel.song2AfterMatchRating * 100) / 100).toFixed(2),
                    song2Update: song2Update,
                    song2Name: song2.name

                };
                var html = template(context);

                $("#playedDuelsList tbody").append(html);
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
                JSON.stringify({'song1BeforeMatchRating': song1Rating, 'song2BeforeMatchRating': song2Rating})
        ,
        "success": callback
    });
}

function displayDuelsToPlay(data) {
    var source = $("#duels-to-play-row-template").html();
    var template = Handlebars.compile(source);
    var context, html;

    var style = "border: 1px solid black;";
    var style2 = "border: 1px solid black;font-weight: 700;";


    $.each(data, function (curDuelID, curDuel) {
        loadSong(curDuel.song1ID, function (song1) {
            loadSong(curDuel.song2ID, function (song2) {
                if (curDuelID === 0) {
                    loadPredictions(curDuel.song1BeforeMatchRating, curDuel.song2BeforeMatchRating, function (predictions, status) {
                        $("#competitor1Name").text(song1.name);
                        $("#competitor2Name").text(song2.name);
                        $("#competitor1Rating").text(parseFloat(Math.round(curDuel.song1BeforeMatchRating * 100) / 100).toFixed(2));
                        $("#competitor2Rating").text(parseFloat(Math.round(curDuel.song2BeforeMatchRating * 100) / 100).toFixed(2));
                        $("#prediction1Win").text(parseFloat(Math.round(predictions[0] * 100) / 100).toFixed(2));
                        $("#prediction1Draw").text(parseFloat(Math.round(predictions[2] * 100) / 100).toFixed(2));
                        $("#prediction1Loss").text(parseFloat(Math.round(predictions[4] * 100) / 100).toFixed(2));
                        $("#prediction2Win").text(parseFloat(Math.round(predictions[5] * 100) / 100).toFixed(2));
                        $("#prediction2Draw").text(parseFloat(Math.round(predictions[3] * 100) / 100).toFixed(2));
                        $("#prediction2Loss").text(parseFloat(Math.round(predictions[1] * 100) / 100).toFixed(2));
                    });
                }

                context = {
                    style: style,
                    duelID: curDuel.duelID,
                    song1Name: song1.name,
                    style2: style2,
                    song2Name: song2.name
                };
                html = template(context);
                //$("#duelsToPlayList tbody").appendTo( html );
                $(html).appendTo("#duelsToPlayList tbody");
            });
        });
    });
}

function displaySongs(data) {
    $.each(data, function (k, v) {
        var style = "border: 1px solid black;";
        var style2 = "border: 1px solid black;width: 20px;background-color:" + evaluateRating(v.currentRating, v.formerRating) + ";";

        var source = $("#song-row-template").html();
        var template = Handlebars.compile(source);
        var context = {
            style: style,
            rank: (k + 1),
            name: v.name,
            matches: (v.wins + v.draws + v.loses),
            wins: v.wins,
            draws: v.draws,
            losses: v.loses,
            style2: style2,
            rating: parseFloat(Math.round(v.currentRating * 100) / 100).toFixed(2)
        };
        var html = template(context);
        $("#songsList tbody").append(html);
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
    console.log('saving...', $slider.slider("value"));
//    $.ajax({
//        "url": "/musicLadderAPI/duel",
//        "type": "POST",
//        "headers": {"Content-Type": "application/json"},
//        "data": 
//            JSON.stringify({ 'song1BeforeMatchRating': song1Rating,'song2BeforeMatchRating': song2Rating })
//        ,
//        "success": callback
//    });
}

function load() {

    loadSongs();
    loadDuelsToPlay(5);
    loadPlayedDuels(15);

    $saveDuelButton = $("#saveDuel");
    $slider = $(".sliderr");

    $saveDuelButton.on("click", saveDuel);
    $slider.slider({
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

$(window).ready(load);