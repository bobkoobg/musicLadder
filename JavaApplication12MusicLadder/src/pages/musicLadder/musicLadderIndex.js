'use strict';

console.log('hi musicLadderIndex.js');

var $slider;
var $saveDuelButton;
var amountOfDuelsToPlay = 5;
var amountOfPlayedDuels = 15;

function loadSlider() {
    $('.sliderr').slider({
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
    $("#playedDuelsList tbody").empty();
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
    var source, template, context, html;

    var style = "border: 1px solid black;";
    var style2 = "border: 1px solid black;font-weight: 700;";

    $("#duelsToPlayList tbody").empty();
    $.each(data, function (curDuelID, curDuel) {
        loadSong(curDuel.song1ID, function (song1) {
            loadSong(curDuel.song2ID, function (song2) {
                if (curDuelID === 0) {
                    loadPredictions(curDuel.song1BeforeMatchRating, curDuel.song2BeforeMatchRating, function (predictions) {
                        $(".duelTemplateWrapper").remove();
                        source = $("#current-duel-template").html();
                        template = Handlebars.compile( source );

                        context = {
                            duelId: curDuel.duelID,
                            song1Name: song1.name,
                            song1Rating: curDuel.song1BeforeMatchRating,
                            song2Name: song2.name,
                            song2Rating: curDuel.song2BeforeMatchRating,
                            predSong1Win: parseFloat(Math.round(predictions[0] * 100) / 100).toFixed(2),
                            predSong1Draw: parseFloat(Math.round(predictions[2] * 100) / 100).toFixed(2),
                            predSong1Loss: parseFloat(Math.round(predictions[4] * 100) / 100).toFixed(2),
                            predSong2Loss: parseFloat(Math.round(predictions[1] * 100) / 100).toFixed(2),
                            predSong2Draw: parseFloat(Math.round(predictions[3] * 100) / 100).toFixed(2),
                            predSong2Win: parseFloat(Math.round(predictions[5] * 100) / 100).toFixed(2)
                        };
                        html = template(context);
                        //$("#duelsToPlayList tbody").appendTo( html );
                        $("#saveDuel").before( html );
                        loadSlider();
                    });
                }
                source = $("#duels-to-play-row-template").html();
                template = Handlebars.compile(source);
                
                context = {
                    duelID: curDuel.duelID,
                    style: style,
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
    loadPlayedDuels( amountOfPlayedDuels );
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

function evaluateDuelGeneration( data ) {
    console.log('data is : ' + JSON.stringify(data) );
    loadDuelsToPlay( amountOfDuelsToPlay )
    
    
}

function generateDuels(amount) {
    
    $.ajax({
        "url": "/musicLadderAPI/duel/1/" + amount,
        "type": "PUT",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify({}),
        "success": evaluateDuelGeneration
    });
}

function evaluateDuelSave( data ) {
    console.log('data is : ' + JSON.stringify(data) );
    
    $('#duelsToPlayListBody').filter(function(){
        return $(this).data('duelid') === $('.row-duelTemplateWrapper').data("duelid")
    }).remove();
    
    var curAmountOfDuels = 0;
    $('.duelToPlay').each(function(i, obj) {
        if ( $(obj).data('duelid') === $('.duelTemplateWrapper').data("duelid") ) {
            $(obj).remove();
        } else {
            curAmountOfDuels++;
        }
    });
    
    if ( curAmountOfDuels < amountOfDuelsToPlay ) {
        generateDuels( ( amountOfDuelsToPlay - curAmountOfDuels ) );
    }
}

function saveDuel() {
    var duelId = $(".duelTemplateWrapper").data("duelid");
    var sliderScore = $(".sliderr").slider("value");
    
    $.ajax({
        "url": "/musicLadderAPI/duel/" + duelId + "/" + Math.abs(0 - sliderScore) + "/" + Math.abs(sliderScore - 10 ),
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify({}),
        "success": evaluateDuelSave
    });
}

function load() {

    loadSongs();
    loadDuelsToPlay( amountOfDuelsToPlay );

    $saveDuelButton = $("#saveDuel");
    $slider = $(".sliderr");

    $saveDuelButton.on("click", saveDuel);
}

$(window).ready(load);