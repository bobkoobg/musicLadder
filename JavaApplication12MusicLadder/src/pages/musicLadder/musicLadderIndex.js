console.log('hi');

function loadSongs() {
    console.log('hi loadSongs');
    //Send the AJAX call to the server
    $.ajax( {
        //The URL to process the request
        'url' : 'page.php',
        //The type of request, also known as the "method" in HTML forms
        //Can be 'GET' or 'POST'
        'type' : 'GET',
        //Any post-data/get-data parameters
        //This is optional
        'data' : {
          'paramater1' : 'value',
          'parameter2' : 'another value'
        },
        //The response from the server
        'success' : function(data) {
        //You can use any jQuery/JavaScript here!!!
            console.log('Return data : ' + data);
        }
    } );
}

loadSongs();
