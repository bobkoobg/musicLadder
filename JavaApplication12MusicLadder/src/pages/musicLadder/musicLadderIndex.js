'use strict';

console.log('hi musicLadderIndex.js');

var $slider;
var $saveDuelButton;
var initialUpdate = true;

function evaluateRating(currentRating, formerRating) {
    if (currentRating > formerRating) {
        return "#00ff00";
    } else if (currentRating < formerRating) {
        return "#ff0000";
    } else {
        return "#ffff00";
    }
}

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

//function loadSong(songId, callback) {
//    $.ajax({
//        "url": "/musicLadderAPI/song/" + songId,
//        "type": "GET",
//        "data": {},
//        "success": callback
//    });
//}

//Generate duels ***START***
function loadDuel( duelId, callback ) {
    $.ajax({
        "url": "/musicLadderAPI/duel/" + duelId,
        "type": "GET",
        "data": {},
        "success": callback
    });
}

function evaluateDuelGeneration( data ) {
    
    var source, template, context, html;
    var style = "border: 1px solid black;";
    var style2 = "border: 1px solid black;font-weight: 700;";
    
    for (var i = 0; i < data.length; i++) {
        
        loadDuel( data[i], function( duelData ) {
            
            console.log('duelData : ', duelData);
            source = $("#duels-to-play-row-template").html();
            template = Handlebars.compile(source);

            context = {
                duelID: duelData.duelID,
                style: style,
                song1Name: duelData.song1Name,
                style2: style2,
                song2Name: duelData.song2Name
            };
            html = template(context);
            
            $(html).appendTo("#duelsToPlayList tbody");
        
        });
    }
}

function generateDuels( amount ) {
    $.ajax({
        "url": "/musicLadderAPI/duel/1/" + amount,
        "type": "PUT",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify({}),
        "success": evaluateDuelGeneration
    });
}
//Generate duels ***END***
//Save duel functionality ***START***
function evaluateDuelSave( data ) {
    var amountOfDuelsToPlay = 5;
    console.log('data is : ' + JSON.stringify(data) );
    var curAmountOfDuels = 0;
    $('.duelToPlay').each(function(i, obj) {
        if ( $(obj).data('duelid') === $('.duelTemplateWrapper').data("currduelid") ) {
            $(obj).remove();
        } else {
            curAmountOfDuels++;
        }
    });
    console.log('Wanna load duels ? : ', (amountOfDuelsToPlay - curAmountOfDuels));
    initialUpdate = false;
    loadDuelsToPlay( amountOfDuelsToPlay );
}

function saveDuel() {
    var duelId = $(".duelTemplateWrapper").data("currduelid");
    var sliderScore = $(".sliderr").slider("value");
    
    $.ajax({
        "url": "/musicLadderAPI/duel/" + duelId + "/" + Math.abs(0 - sliderScore) + "/" + Math.abs(sliderScore - 10 ),
        "type": "POST",
        "headers": {"Content-Type": "application/json"},
        "data": JSON.stringify({}),
        "success": evaluateDuelSave
    });
}

//Save duel functionality ***END***
//Duels to play functionality ***START***
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

function displayDuelsToPlayFunc( data ) {
    console.log( 'displayDuelsToPlay triggered');
    var source, template, context, html, elements = 0;

    var style = "border: 1px solid black;";
    var style2 = "border: 1px solid black;font-weight: 700;";

    $.each(data, function (index, curDuel) {
        elements++;
        if (index === 0) {
            loadPredictions(curDuel.song1BeforeMatchRating, curDuel.song2BeforeMatchRating, function (predictions) {
                $(".duelTemplateWrapper").remove();
                source = $("#current-duel-template").html();
                template = Handlebars.compile( source );

                context = {
                    duelId: curDuel.duelID,
                    song1Name: curDuel.song1Name,
                    song1Rating: curDuel.song1BeforeMatchRating,
                    song2Name: curDuel.song2Name,
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
        
        var exists = false;
        
        $('.duelToPlay').each(function(i, obj) {

            if ( $(obj).data('duelid') === curDuel.duelID ) {
                console.log('I exist', curDuel.duelID);
                exists = true;
                return false;
            }
        });
            
        if( !exists ) {
            console.log('I DO NOT exist', curDuel.duelID);
            
            source = $("#duels-to-play-row-template").html();
            template = Handlebars.compile(source);

            context = {
                duelID: curDuel.duelID,
                style: style,
                song1Name: curDuel.song1Name,
                style2: style2,
                song2Name: curDuel.song2Name
            };
            html = template(context);
            
            $(html).appendTo("#duelsToPlayList tbody");
        }
    });
    
    if( elements < 5 ) {
        console.log('I need to add a new one');
        generateDuels( 5 - elements );
    }
    
    
}


function loadDuelsToPlay(amount) {
    $.ajax({
        "url": "/musicLadderAPI/all/duelsToPlay/" + amount,
        "type": "GET",
        "data": {},
        "success": displayDuelsToPlayFunc
    });
}
//Duels to play functionality ***END***
//Played Duels functionality ***START***
function displayPlayedDuels( data ) {
    var style = "border: 1px solid black;";
    var style2 = "border: 1px solid black;font-weight: 700;";
    var song1Points, song1Update, song2Points, song2Update, style3, style4;
    $("#playedDuelsList tbody").empty();
    $.each(data, function (curDuelID, curDuel) {
        
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
            duelId: curDuel.duelID,
            style: style,
            song1Name: curDuel.song1Name,
            style3: style3,
            song1Update: song1Update,
            song1Rating: parseFloat(Math.round(curDuel.song1AfterMatchRating * 100) / 100).toFixed(2),
            song1Score: curDuel.song1Score,
            style2: style2,
            song2Score: curDuel.song2Score,
            style4: style4,
            song2Rating: parseFloat(Math.round(curDuel.song2AfterMatchRating * 100) / 100).toFixed(2),
            song2Update: song2Update,
            song2Name: curDuel.song2Name

        };
        var html = template(context);

        $("#playedDuelsList tbody").append(html);
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
//Played Duels functionality ***END***
//Songs functionality ***START***
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

function loadSongs( ladderId ) {
    $.ajax({
        'url': '/musicLadderAPI/all/songs/' + ladderId,
        'type': 'GET',
        'success': displaySongs
    });
}
//Songs functionality ***END***

function load() {
    //Load pre-generated data
    loadSongs( 1 );
    loadPlayedDuels( 15 );
    loadDuelsToPlay( 5 );
    
    //ok we are deleting the one which was inserted
    //now,we should ask the db if it has more stuff to give me
    //then load them, look through what you have on the page
    //  if they match, drop them
    //  if they do not match -add them
    //if the final amount is lower than 5 (the value on like 2,3)
    //  generate new duel
    //get the id when it returns
    //load it, add it to the others
    //done

    $saveDuelButton = $("#saveDuel");
    $slider = $(".sliderr");

    $saveDuelButton.on("click", saveDuel);
}

$(window).ready(load);