console.log('hi musicLadderIndex.js');

var $songList;
var $slider;

function evaluateRating( currentRating, formerRating ) {
    if ( currentRating > formerRating ) {
        return "#00ff00";
    } else if ( currentRating < formerRating ) {
        return "#ff0000";
    } else {
        return "#ffff00";
    }
}

function displaySongs( data ) {
    $.each(data, function(k, v) {
        var style = "style=\"border: 1px solid black;\"";
        var style2 = "style=\"border: 1px solid black;width: 20px;background-color:"+evaluateRating(v.currentRating, v.formerRating)+"\"";
        var content = "<tr>"
                + "<td " + style + ">" + ( k+1 ) + "</td>" 
                + "<td " + style + ">" + v.name + "</td>"
                + "<td " + style + ">" + ( v.wins + v.draws + v.loses ) + "</td>"
                + "<td " + style + ">" + v.wins + "</td>"
                + "<td " + style + ">" + v.draws + "</td>"
                + "<td " + style + ">" + v.loses + "</td>"
                + "<td " + style2 + "></td>"
                + "<td " + style + ">" + v.currentRating + "</td>"
                + "</tr>";
        $songList.append()
        $("#songsList tbody").append( content );
    });
}

function loadSongs() {
    //Send the AJAX call to the server
    $.ajax( {
        //The URL to process the request
        'url' : '/musicLadderapi',
        //The type of request, also known as the "method" in HTML forms
        //Can be 'GET' or 'POST'
        'type' : 'GET',
        //Any post-data/get-data parameters
        //This is optional
        'data' : {
//          'paramater1' : 'value',
//          'parameter2' : 'another value'
        },
        //The response from the server
        'success' : displaySongs
    } );
}


function load() {
    loadSongs();
    
    $songList = $("#songsList");
    $( ".sliderr" ).slider({
        value: 5,
        min: 1,
        max: 10,
        step: 1
    })
    .each(function() {

        // Add labels to slider whose values 
        // are specified by min, max

        // Get the options for this slider (specified above)
        var opt = $(this).data().uiSlider.options;

        // Get the number of possible values
        var vals = opt.max - opt.min;

        // Position the labels
        for (var i = 0; i <= vals; i++) {

            // Create a new element and position it with percentages
            var el = $("<label style=\"position: absolute;width: 20px;margin-top: 20px;margin-left: -10px;text-align: center;\">" + (i + opt.min) + "</label>").css("left", (i/vals*100) + "%");

            // Add the element inside #slider
            $(".sliderr").append(el);

        }

    });
}

$( document ).ready( load );