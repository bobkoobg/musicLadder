console.log('hi musicLadderIndex.js');

var $songList;

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
        console.log("k is ", k, " v is : ", v );
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
    console.log('hi loadSongs');
    //Send the AJAX call to the server
    $.ajax( {
        //The URL to process the request
        'url' : '/musicladderapi',
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
}

load();
