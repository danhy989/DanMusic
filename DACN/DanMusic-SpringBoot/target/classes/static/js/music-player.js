
var ACTIVE_COLOR_ICON = 'rgb(0, 92, 200)';
var INACTIVE_COLOR_ICON = 'rgb(255, 255, 255)';
var VOLUME_DEFAULT = 50;
var TRACK_PROGRESS_MS_DEFAULT = 0;
var TOTAL_ALBUMS_TRACK_NEW_RELEASES_DEFAULT = 50;
var COUNTRY_ALBUMS_TRACK_NEW_RELEASES_DEFAULT = 'VN';
var OFFSET_POSITION_DEFAULT = 0;
var COLORS = ['#001f3f', '#0074D9', '#7FDBFF', '##39CCCC', '#3D9970', '#2ECC40', '#01FF70',
    '#FFDC00', '#FF851B', '#FF4136', '#85144b', '#F012BE', '#B10DC9', '#AAAAAA', '#DDDDDD'];

$(document).ready(function () {

    var token = localStorage.getItem('access_token');

    if(token == undefined || token == null){
        $('#myModal').modal('show');
    }

    localStorage.setItem('progress_ms', TRACK_PROGRESS_MS_DEFAULT);
    localStorage.setItem('isPlaying',false);

    var device_id = localStorage.getItem("device_id");

    setSongsContentPage(COUNTRY_ALBUMS_TRACK_NEW_RELEASES_DEFAULT, TOTAL_ALBUMS_TRACK_NEW_RELEASES_DEFAULT, OFFSET_POSITION_DEFAULT);
    setAlbumsContentPage(COUNTRY_ALBUMS_TRACK_NEW_RELEASES_DEFAULT, TOTAL_ALBUMS_TRACK_NEW_RELEASES_DEFAULT, OFFSET_POSITION_DEFAULT);
    setTrackPlayingContentPage();


    getFollowedArtists();
    getUsersTopArtists('artists', 'medium_term', 5);



    $("#seekbar").change(function () {
        if (localStorage.getItem("isPlaying") == "true") {
            var spotify_uri = localStorage.getItem('track-is-playing-uri');
            fetch(`https://api.spotify.com/v1/me/player/play?device_id=${device_id}`, {
                method: 'PUT',
                body: JSON.stringify({
                    uris: [spotify_uri],
                    position_ms: Number($("#seekbar").val())
                }),
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
            });
        }
    })

    $("#volumebar").change(function () {
        var volumeValue = Math.floor(Number($("#volumebar").val()) * 100);
        localStorage.setItem('volumeValue', volumeValue);

        if (localStorage.getItem("isvolumeoff") == "false") {
            fetch(`	https://api.spotify.com/v1/me/player/volume?volume_percent=${volumeValue}&device_id=${device_id}`, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
            });
        }
    })



    $('#ui-menu .row-menu')
        .off('click.menuItemClick')
        .on('click.menuItemClick', function () {
            getTrackRecentPlayed();

            let self = $(this);
            const showClass = 'show';
            const uiPageId = self.data('ui-page');

            //Remove and add show class to dynamic-content page
            $('.ui-page').removeClass(showClass);
            $(uiPageId).addClass(showClass);

            //Remove and add active class to ui-menu
            $('.row-menu').removeClass('active');
            $(self).addClass('active');


        });

    $('#ui-content-page-type .typeItem')
        .off('click.typeItemClick')
        .on('click.typeItemClick', function () {
            let self = $(this);
            const showClass = 'show';
            const uiPageId = self.data('ui-page');

            //Remove and add show class to dynamic-content page
            $('.ui-content-page-body-music').removeClass(showClass);
            $(uiPageId).addClass(showClass);

            $('.typeItem').removeClass('active');
            $(self).addClass('active');

            if (uiPageId == "#id-content-page-music-songs") {
                setSongsContentPage(COUNTRY_ALBUMS_TRACK_NEW_RELEASES_DEFAULT, TOTAL_ALBUMS_TRACK_NEW_RELEASES_DEFAULT, OFFSET_POSITION_DEFAULT);
            }
        })

    $('#search-input').keyup(delay(function (e) {
        if(this.value.trim()!== '' && this.value.trim()!==null){
            $('.ui-page').removeClass('show');
            $('#search-result-page').addClass('show');
            getSearching(this.value);
        }else{
            $('.ui-page').removeClass('show');
            $('#musicPage').addClass('show');
        }
    }, 500));


    $("#spotifyAuthorization").bind("click", function() {

        var spotifyAuthorizeUrl = 'https://accounts.spotify.com/authorize';
        var spotifyRedirectUrl = 'http://localhost:8080/token/callback';
        var spotifyClientId = 'a0a76faa34084bcbb1c0180760beda39';
        var spotifyScope = 'streaming%20user-read-email%20user-read-private%20user-read-currently-playing%20user-read-playback-state%20 user-modify-playback-state%20user-follow-read%20user-top-read%20user-read-recently-played';
        var spotifyResponseType = 'code';

        var generateRandomString = function(length) {
            var text = '';
            var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

            for (var i = 0; i < length; i++) {
                text += possible.charAt(Math.floor(Math.random() * possible.length));
            }
            return text;
        };
        var state = generateRandomString(16);

        var url = spotifyAuthorizeUrl+
            "?client_id="+spotifyClientId+
            "&response_type="+spotifyResponseType+
            "&redirect_uri="+spotifyRedirectUrl+
            "&scope="+spotifyScope+
            "&state="+state;
        document.cookie = "spotify_auth_state="+state;
        $("#spotifyAuthorization").attr("href",url);
        // console.log(url);
        // console.log(state);
    });
});


function delay(callback, ms) {
    var timer = 0;
    return function() {
        var context = this, args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            callback.apply(context, args);
        }, ms || 0);
    };
}

window.setInterval(function () {
    if(localStorage.getItem('authentication-dialog-show')=='true'){
        $('#myModal').modal('show');
    }else{
        $('#myModal').modal('hide');
    }

    var isPlaying = localStorage.getItem('isPlaying') || "false";
    if (isPlaying === "true") {
        var token = localStorage.getItem('access_token');
        fetch('https://api.spotify.com/v1/me/player/currently-playing', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
        })
            .then(
                function (response) {
                    if (response.status !== 200) {
                        console.log('Looks like there was a problem. Status Code: ' +
                            response.status);
                        return;
                    }

                    // Examine the text in the response
                    response.json().then(function (data) {
                        if (data.is_playing == true && data.actions.disallows.resuming == true) {
                            $("#ui-track-position-time").html(convertTimeToString(data.progress_ms));

                            $("#seekbar").attr('value', data.progress_ms);

                            // console.log('progress_ms',data.progress_ms);
                            // console.log('value',$("#seekbar").attr('value'));
                            // console.log('max',$("#seekbar").attr('max'));

                            localStorage.setItem('progress_ms', data.progress_ms);
                        }

                    });
                }
            )
            .catch(function (err) {
                console.log('Fetch Error :-S', err);
            });
    }
}, 500)



function pad(n) {
    return (n < 10) ? ("0" + n) : n;
}

function convertTimeToString(second) {
    second = second / 1000;
    var numberOfMinutes = Math.floor(((second % 86400) % 3600) / 60);
    var numberOfSeconds = pad(Math.floor(((second % 86400) % 3600) % 60));
    return numberOfMinutes + ":" + numberOfSeconds;
}

window.onSpotifyWebPlaybackSDKReady = () => {
    var token = localStorage.getItem("access_token");
    const player = new Spotify.Player({
        name: 'Web Playback SDK Quick Start Player',
        getOAuthToken: cb => { cb(token); }
});

    // Error handling
    player.addListener('initialization_error', ({ message }) => { console.error(message); });
    player.addListener('authentication_error', ({ message }) => {

    var token = JSON.parse(localStorage.getItem("token"));
    console.log('first',token);
    if(undefined !== token && null !== token){
        // request to new token by refresh token
        var url = "http://localhost:8080/token/refresh";
        var token = JSON.parse(localStorage.getItem("token"));
        $.ajax({
            url: url,
            type: "PUT",
            data: JSON.stringify(token),
            dataType: "json",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            success: function (data) {
                localStorage.setItem('token',JSON.stringify(data));
                localStorage.setItem('access_token',data.access_token);
            }
        });
    }else{
        localStorage.setItem('authentication-dialog-show',true);
    }
});

    player.addListener('account_error', ({ message }) => { console.error(message); });
    player.addListener('playback_error', ({ message }) => { console.error(message); });


    // Playback status updates
    player.addListener('player_state_changed', state => {
        console.log(state);
    if (state.duration - state.position < 1000 && state.track_window.next_tracks.length > 0) {
        var tr_active = document.getElementsByClassName('ui_playings_page_tr active')[0];
        var tr_active_num = $(tr_active).children('th').html();
        playAlbumSpotify(state.track_window.current_track.album.uri, tr_active_num);
    }
    if (state.paused == false) {
        //start music

        if (state.position == 0) {
            $("#ui-track-duration-time").html(convertTimeToString(state.duration));
            $("#ui-bar-album-image").attr("src", state.track_window.current_track.album.images[2].url);
            $("#ui-bar-title").html(state.track_window.current_track.name);
            $("#ui-bar-artist").html(state.track_window.current_track.artists[0].name);
            $('#seekbar').attr('max', state.duration);

            var random_color = COLORS[Math.floor(Math.random() * COLORS.length)];
            $('#ui-music-player-navbar').attr('style', 'background-color: ' + random_color + ' !important');

            localStorage.setItem('track-is-playing-uri', state.track_window.current_track.uri);
        }
        localStorage.setItem("isPlaying", true);
        document.getElementById("playPausebutton").className = "fas fa-pause-circle";

        var current_show_element = document.getElementsByClassName('ui-page show');
        var id_current_show_element = $(current_show_element).attr("id");
        switch (id_current_show_element) {
            case 'nowPlayingPage':
                $('#ui-table-content-playing-list tr').each(function () {
                    var trackUri = $(this).find("td").eq(4).html();
                    if (trackUri.trim() === state.track_window.current_track.uri.trim()) {
                        $('.ui_playings_page_tr').removeClass('active');
                        $(this).addClass('active');
                    }
                });
                break;
            case 'recentPlaysPage':

                break;
            default:
                break;
        }

    } else {
        if (state.paused == true) {
            //pause music
            localStorage.setItem("isPlaying", false);
            console.log(state.position);
            document.getElementById("playPausebutton").className = "fas fa-play";
        }
    }
});

    // Ready
    player.addListener('ready', ({ device_id }) => {
        console.log('Ready with Device ID', device_id);
    localStorage.setItem("device_id", device_id);
    localStorage.setItem('authentication-dialog-show',false);
});

    // Not Ready
    player.addListener('not_ready', ({ device_id }) => {
        console.log('Device ID has gone offline', device_id);
});

    //Set volume
    player.setVolume(VOLUME_DEFAULT / 100).then(() => {
        console.log('Volume updated!');
    localStorage.setItem('volumeValue', VOLUME_DEFAULT);
    localStorage.setItem('isvolumeoff', false);
});



    // Connect to the player!
    player.connect();


}

function playPauseAction(iconID) {

    //play stop resume music

    //var spotify_uri = "spotify:track:7xGfFoTpQ2E7fRF5lN10tr";

    var spotify_uri = localStorage.getItem('track-is-playing-uri');
    console.log(spotify_uri);
    //change icon
    if (document.getElementById(iconID).className == "fas fa-play") {

        if ($("#seekbar").val() != localStorage.getItem("progress_ms")) {
            console.log('seekbar value', $("#seekbar").val());
            localStorage.setItem("progress_ms", $("#seekbar").val());
        }

        var progress_ms = Number(localStorage.getItem("progress_ms")) || 0;
        console.log(progress_ms);

        playTrackSpotify(spotify_uri, progress_ms);


    } else {
        pause();

    }
}

function muteOnmuteAction(iconID) {
    const token = localStorage.getItem('access_token');

    var device_id = localStorage.getItem("device_id");
    var volumeValue = Number(localStorage.getItem("volumeValue"));
    if (document.getElementById(iconID).className == "fa fa-volume-up") {
        //Mute action
        volumeValue = 0;
        localStorage.setItem('isvolumeoff', true);

        document.getElementById(iconID).className = "fas fa-volume-mute";
    } else {
        //Unmute action
        localStorage.setItem('isvolumeoff', false);

        document.getElementById(iconID).className = "fa fa-volume-up";
    }
    fetch(`	https://api.spotify.com/v1/me/player/volume?volume_percent=${volumeValue}&device_id=${device_id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
    });
}

function playAlbumSpotify(uri, offset_position = 0, position_ms = 0) {
    var device_id = localStorage.getItem("device_id");
    const token = localStorage.getItem('access_token');
    fetch(`https://api.spotify.com/v1/me/player/play?device_id=${device_id}`, {
        method: 'PUT',
        body: JSON.stringify({
            context_uri: uri,
            offset: {
                position: offset_position
            },
            position_ms: position_ms
        }),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
    });
}

function playTrackSpotify(uri, position_ms = 0) {
    var device_id = localStorage.getItem("device_id");
    const token = localStorage.getItem('access_token');
    fetch(`https://api.spotify.com/v1/me/player/play?device_id=${device_id}`, {
        method: 'PUT',
        body: JSON.stringify({
            uris: [uri],
            position_ms: position_ms
        }),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
    });
}

function addAnItemToTheEndOfTheUserCurrentQueue(uri) {
    var device_id = localStorage.getItem("device_id");
    const token = localStorage.getItem('access_token');
    fetch(`https://api.spotify.com/v1/me/player/queue?device_id=${device_id}&uri=${uri}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
    });
}

function pause() {
    var device_id = localStorage.getItem("device_id");
    const token = localStorage.getItem('access_token');
    fetch(`https://api.spotify.com/v1/me/player/pause?device_id=${device_id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
    });
}

function setAlbumsContentPage(country, limit, offset, url = null) {
    $('#id-content-page-music-albums .auto-grid').html('');
    const token = localStorage.getItem('access_token');
    if (url == null) {
        url = "https://api.spotify.com/v1/browse/new-releases?country=" + country + "&limit=" + limit + "&offset=" + offset;
    }
    $.ajax({
        url: url,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            const list = data.albums.items;

            var k = 1;
            for (var i = 0; i < list.length; i++) {

                var temp = '<li onclick="album_page_select_row(id)" id="album_page_id_li_' + k++ + '">' +
                    '<div>' +
                    '<img src="' + list[i].images[1].url + '" alt="">' +
                    '<h5>' + list[i].name + '</h5>';

                if (list[i].artists.length > 1) {
                    var artists = '';
                    list[i].artists.forEach(element => {
                        if (artists == '') {
                        artists = element.name;
                    } else {
                        artists = artists + ' & ' + element.name;
                    }
                });
                    temp = temp + '<p>' + artists + '<p>';
                } else {
                    temp = temp + '<p>' + list[i].artists[0].name + '<p>';
                }
                temp = temp +
                    '<p class="ui-album-page-li-id" hidden>' + list[i].id + '</p>' +
                    '</div>' + '</li>';
                $('#id-content-page-music-albums .auto-grid').append(temp);

            }
        }
    });
}

function album_page_select_row(id) {
    var li = document.getElementById(id);
    var div = $(li).children()[0];
    var album_id_dom = $(div).children()[4];
    var album_id_str = $(album_id_dom).html();
    setTrackPlayingContentPage(album_id_str);

    $('.ui-page').removeClass("show");
    $('#nowPlayingPage').addClass("show");

    var uri = `spotify:album:${album_id_str}`;
    playAlbumSpotify(uri);
}

function setTrackPlayingContentPage(id, market = null, url = null) {
    $('#ui-table-content-playing-list > tbody').html('');
    $('.ui-row-info-header-playing h1').html('');
    $('.ui-row-info-header-playing p').html('');

    const token = localStorage.getItem('access_token');
    if (url == null) {
        if (market != null) {
            url = `https://api.spotify.com/v1/albums/${id}?market=${market}`;
        } else {
            url = `https://api.spotify.com/v1/albums/${id}`;
        }
    }
    $.ajax({
        url: url,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            const name = data.name;
            const artists = () => {
                if (data.artists.length > 1) {
                    var artists = '';
                    data.artists.forEach(element => {
                        if (artists == '') {
                        artists = element.name;
                    } else {
                        artists = artists + ' & ' + element.name;
                    }
                });

                } else {
                    artists = data.artists[0].name;
                }
                return artists;
            }
            const imageAlbum = data.images[1].url?data.images[1].url:'';

            const uriAlbum = data.uri;

            var listTrack = () => {
                var listTrackOutCome = [];
                for (var i = 0; i < data.tracks.items.length; i++) {
                    var track = data.tracks.items[i];
                    var trackRow = {};
                    trackRow.num = (i + 1);
                    trackRow.name = track.name;
                    trackRow.duration = convertTimeToString(track.duration_ms);
                    trackRow.uri = track.uri;
                    listTrackOutCome.push(trackRow);
                }
                return listTrackOutCome;
            }

            listTrack().forEach(t => {
                var temp = `<tr onclick="playings_page_select_row(id,'album')"  id="playings_page_id_tr_${t.num}" class="ui_playings_page_tr"> 
                <th scope="row" class="num ui-bold">  ${t.num}  </th> 
                <td class="name ui-bold">  ${t.name}  </td> 
                <td><i class="fa fa-heart" aria-hidden="true"></i></td> 
                <td><i class="fas fa-ellipsis-h" aria-hidden="true"></i></td> 
                <td>  ${t.duration}  </td> 
                <td hidden>  ${t.uri}  </td> 
                </tr>`;
            $('#ui-table-content-playing-list > tbody').append(temp);
        })

            $('.ui-row-img-header-playing').attr("src", imageAlbum);
            $('#id-row-info-header-playing-album-name').html(name);
            $('#id-row-info-header-playing-album-artists').html(artists);
            $('#id-row-info-header-playing-album-uri').html(uriAlbum);
        }
    });
}

function playings_page_select_row(id, type) {
    var row_element = document.getElementById(id);
    var offset_position = $(row_element).children('th');
    var offset_position_num = $(offset_position).html();
    var uri = $('#id-row-info-header-playing-album-uri').html();

    var urlTrack = $(row_element).children('td')[4];
    var urlTrack_str = $(urlTrack).html();
    if (type == 'track') {
        //$('.ui_recent_page_tr').removeClass('active');
        console.log(urlTrack_str);
        $('.ui_result_page_tr').removeClass('active');
        $(row_element).addClass('active');
        playTrackSpotify(urlTrack_str.trim());
    }
    if (type == 'album') {
        playAlbumSpotify(uri, Number(offset_position_num) - 1);
    }
}

function setSongsContentPage(country, limit, offset, url = null) {
    $('#ui-table-content-songs-list > tbody').html('');
    const token = localStorage.getItem('access_token');
    if (url == null) {
        url = "https://api.spotify.com/v1/browse/new-releases?country=" + country + "&limit=" + limit + "&offset=" + offset;
    }
    $.ajax({
        url: url,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            const list = data.albums.items;

            var k = 1;
            for (var i = 0; i < list.length; i++) {
                if (list[i].album_type == "single") {

                    var temp = '<tr onclick="songs_page_select_row(id)" class="songs_page_ui_tr" id="songs_page_id_tr_' + k + '">' +
                        '<th scope="row" class="num ui-bold">' + k++ + '</th>' +
                        '<td class="name ui-bold">' + list[i].name + '</td>' +
                        '<td><i class="fa fa-heart" aria-hidden="true"></i></td>' +
                        '<td><i class="fas fa-ellipsis-h" aria-hidden="true"></i></td>';

                    if (list[i].artists.length > 1) {
                        var artists = '';
                        list[i].artists.forEach(element => {
                            if (artists == '') {
                            artists = element.name;
                        } else {
                            artists = artists + ' & ' + element.name;
                        }
                    });
                        temp = temp + '<td>' + artists + '<td>';
                    } else {
                        temp = temp + '<td>' + list[i].artists[0].name + '<td>';
                    }

                    temp = temp +
                        '<td>' + list[i].release_date + '</td>' +
                        '<td hidden>' + list[i].uri + '</td>' +
                        '</tr>';

                    $('#ui-table-content-songs-list > tbody').append(temp);
                }
            }
        }
    });
}

function songs_page_select_row(id) {
    var tr = document.getElementById(id);

    $('.songs_page_ui_tr').removeClass('active');
    $(tr).addClass('active');

    var spotify_uri_dom = $(tr).children()[7];
    var spotify_uri = $(spotify_uri_dom).html();

    $('.ui-page').removeClass("show");
    $('#nowPlayingPage').addClass("show");

    var uri_id = spotify_uri.split(":")[2];

    setTrackPlayingContentPage(uri_id);
    playAlbumSpotify(spotify_uri);
}

function stepBackwardAction() {
    const token = localStorage.getItem('access_token');
    const device_id = localStorage.getItem("device_id");

    console.log('stepback');
    var url = `https://api.spotify.com/v1/me/player/previous?device_id=${device_id}`;
    $.ajax({
        url: url,
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {

        }
    });
}

function stepForwardAction() {
    const token = localStorage.getItem('access_token');
    const device_id = localStorage.getItem("device_id");
    console.log('stepforward');
    var url = `https://api.spotify.com/v1/me/player/next?device_id=${device_id}`;
    $.ajax({
        url: url,
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {

        }
    });
}

function shuffleAction() {
    const token = localStorage.getItem('access_token');
    const device_id = localStorage.getItem("device_id");
    var state = false;
    if ($('#id-bar-shuffleIcon').css('color') == INACTIVE_COLOR_ICON) {
        state = true;
        $('#id-bar-shuffleIcon').css('color', ACTIVE_COLOR_ICON);
    } else {
        state = false;
        $('#id-bar-shuffleIcon').css('color', INACTIVE_COLOR_ICON);
    }
    $.ajax({
        url: `https://api.spotify.com/v1/me/player/shuffle?state=${state}&device_id=${device_id}`,
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {

        }
    });
}

function repeatAction() {
    const token = localStorage.getItem('access_token');
    const device_id = localStorage.getItem("device_id");
    var state = "off";
    if ($('#id-bar-repeatIcon').css('color') == INACTIVE_COLOR_ICON) {
        state = "track";
        $('#id-bar-repeatIcon').css('color', ACTIVE_COLOR_ICON);
    } else {
        state = "off";
        $('#id-bar-repeatIcon').css('color', INACTIVE_COLOR_ICON);
    }
    $.ajax({
        url: `https://api.spotify.com/v1/me/player/repeat?state=${state}&device_id=${device_id}`,
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {

        }
    });
}

function getFollowedArtists(type = 'artist', after = null, limit = null) {
    $('#id-followed-artists .auto-grid').html('');
    const token = localStorage.getItem('access_token');
    var url = `https://api.spotify.com/v1/me/following?type=${type}`;
    if (after != null) {
        url = url + `&after=${after}`;
    }
    if (limit != null) {
        url = url + `&limit=${limit}`;
    }

    $.ajax({
        url: url,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            var items = data.artists.items;
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                var imageUrl = item.images[1].url?item.images[1].url:'';
                var name = item.name;
                var id = item.id;
                $('#id-followed-artists .auto-grid').append(`<li onclick="artirst_page_select_row(id)" id="id_artirst_page_li_${i + 1}">
            <div>
              <img  src="${imageUrl}" alt="">
              <h5>${name}</h5>
              <p hidden>${id}</p>
            </div>
          </li>`);
            }
        }
    });
}

function getUsersTopArtists(type = 'artists', time_range = null, limit = null, offset = null) {
    $('#id-users-top-artists .auto-grid').html('');
    const token = localStorage.getItem('access_token');
    var url = `https://api.spotify.com/v1/me/top/${type}?`;
    if (time_range != null) {
        url = url + `&time_range=${time_range}`;
    }
    if (limit != null) {
        url = url + `&limit=${limit}`;
    }
    if (offset != null) {
        url = url + `&offset=${offset}`;
    }

    var bol = url.indexOf("?&");

    if (bol !== -1) {
        url.replace("?&", "?");
    }

    $.ajax({
        url: url,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            var items = data.items;
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                var imageUrl = item.images[1].url?item.images[1].url:'';
                var name = item.name;
                var id = item.id;
                $('#id-users-top-artists .auto-grid').append(`<li onclick="artirst_page_select_row(id)" id="id_artirst_hot_page_li_${i + 1}">
            <div>
              <img  src="${imageUrl}" alt="">
              <h5>${name}</h5>
              <p hidden>${id}</p>
            </div>
          </li>`);
            }
        }
    });
}

function setArtirstAlbumPage(id) {
    $('#ui-row-body-artirst-album-page .auto-grid').html('');
    $('.ui-row-body-artirst-album-page-artirst-related .auto-grid').html('');

    const token = localStorage.getItem('access_token');
    var url_artist_info = `https://api.spotify.com/v1/artists/${id}`;
    var url_artist_albums = `https://api.spotify.com/v1/artists/${id}/albums`;
    var url_artist_related = `	https://api.spotify.com/v1/artists/${id}/related-artists`;

    //set header
    $.ajax({
        url: url_artist_info,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            var imageUrl = data.images[1].url?data.images[1].url:'';
            var name = data.name;
            var id = data.id;
            var genres = () => {
                var rs = '';
                data.genres.forEach(element => {
                    rs += ' ' + element;
            });
                return rs;
            }
            var follower = data.followers.total;

            $('#id-row-img-header-artirst-album').attr('src', imageUrl);
            $('#id-header-artirst-album-name').html(name);
            $('#id-header-artirst-album-genres').html('Genres: ' + genres());
            $('#id-header-artirst-album-artirst-id').html(id);
            $('#id-header-artirst-album-follower').html('Following: ' + follower + ' people');
        }
    });

    $.ajax({
        url: url_artist_albums,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            var items = data.items;
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                var imageAlbum = item.images[1].url?item.images[1].url:'';
                var name = item.name;
                var release_date = item.release_date;
                var uri = item.uri;
                var id = item.id;

                $('.ui-row-body-artirst-album-page-albums .auto-grid').append(`<li onclick="artistalbumsSelected(id)" id="id-artist-albums-li-${i + 1}">
                <div>
                  <img src="${imageAlbum}" alt="">
                  <p>${release_date}</p>
                  <h5>${name}</h5>
                  <p hidden>${id}</p>
                  <p hidden>${uri}</p>
                </div>
              </li>`);
            }
        }
    });

    $.ajax({
        url: url_artist_related,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            var items = data.artists;
            console.log('items', items);
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                console.log('asdasda',item);
                var imageUrl = item.images[1].url?item.images[1].url:'';
                var name = item.name;
                var id = item.id;

                $('.ui-row-body-artirst-album-page-artirst-related .auto-grid').append(`<li onclick="artistRelatedSelected(id)" id="id-artist-Related-li-${i + 1}">
                <div>
                  <img src="${imageUrl}" alt="">
                  <h5>${name}</h5>
                  <p hidden>${id}</p>
                </div>
              </li>`);
            }
        }
    });
}

function artistalbumsSelected(id) {
    var li = document.getElementById(id);
    var div = $(li).children()[0];
    var id_albumSelected = $(div).children()[3];
    var id_albumSelected_str = $(id_albumSelected).html();
    var url_albumSelected = $(div).children()[4];
    var url_albumSelected_str = $(url_albumSelected).html();

    setTrackPlayingContentPage(id_albumSelected_str);


    $('.ui-page').removeClass("show");
    $('#nowPlayingPage').addClass("show");
    playAlbumSpotify(url_albumSelected_str);
}

function artistRelatedSelected(id) {
    var li = document.getElementById(id);
    var div = $(li).children()[0];
    var artirst_id = $(div).children()[2];
    var artirst_id_str = $(artirst_id).html();
    setArtirstAlbumPage(artirst_id_str);

    window.scrollTo(0, 0);
}

function artirst_page_select_row(id) {
    var li = document.getElementById(id);
    var div = $(li).children()[0];
    var artirst_id = $(div).children()[2];
    var artirst_id_str = $(artirst_id).html();
    console.log('abc');
    setArtirstAlbumPage(artirst_id_str);


    $('.ui-page').removeClass('show');
    $('#artirst-album-page').addClass('show');
}

function getTrackRecentPlayed(limit = null, before = null, after = null) {
    $('#id-recent-play-page-body-table > tbody').html('');

    var url = `https://api.spotify.com/v1/me/player/recently-played?type=track`;

    if (limit != null) {
        url = url + `&limit=${limit}`;
    }
    if (before != null) {
        url = url + `&before=${before}`;
    }
    if (after != null) {
        url = url + `&after=${after}`;
    }
    const token = localStorage.getItem('access_token');
    $.ajax({
        url: url,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            var items = data.items;
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                var track = item.track;
                var name = track.name;
                var durationTime = convertTimeToString(track.duration_ms);
                var uriTrack = track.uri;

                var temp = `<tr onclick="playings_page_select_row(id,'track')" id="id_recent_page_id_tr_${(i + 1)}" class="ui_recent_page_tr"> 
                    <th scope="row" class="num ui-bold">  ${(i + 1)}  </th> 
                    <td class="name ui-bold"> ${name}  </td> 
                    <td><i class="fa fa-heart" aria-hidden="true"></i></td> 
                    <td><i class="fas fa-ellipsis-h" aria-hidden="true"></i></td> 
                    <td>  ${durationTime}  </td> 
                    <td hidden>  ${uriTrack}  </td> 
                    </tr>;`;
                $('#id-recent-play-page-body-table > tbody').append(temp);
            }
        }
    });
}

function getSearching(q){
    $('#id-search-result-body-album .auto-grid').html('');
    $('#id-search-result-body-artirs .auto-grid').html('');
    $('#id-search-result-body-songs tbody').html('');

    const token = localStorage.getItem('access_token');

    //fake data
    var type ="track,album,artist";
    var market = "VN";
    var limit = 10;
    var offset = 0;

    var url = `https://api.spotify.com/v1/search?q=${q}&type=${type}&market=${market}&limit=${limit}&offset=${offset}`;

    $.ajax({
        url: url,
        type: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            var albums = data.albums;
            var artists = data.artists;
            var tracks = data.tracks;

            //Set Albums
            if(albums.items.length > 0){
                $('#id-search-result-body-album').show();
                var items = albums.items;
                for(var i=0;i<items.length;i++){
                    var item = items[i];
                    var name = item.name;
                    var id = item.id;
                    var imageAlbum = item.images[1].url?item.images[1].url:'';
                    var uri = item.uri;
                    const artists = () => {
                        if (item.artists.length > 1) {
                            var artists = '';
                            item.artists.forEach(element => {
                                if (artists == '') {
                                artists = element.name;
                            } else {
                                artists = artists + ' & ' + element.name;
                            }
                        });

                        } else {
                            artists = item.artists[0].name;
                        }
                        return artists;
                    }

                    $('#id-search-result-body-album .auto-grid').append(
                        `<li onclick="artistalbumsSelected(id)" id="id-search-result-albums-li-${i + 1}">
                <div>
                  <img src="${imageAlbum}" alt="">
                  <h5>${name}</h5>
                  <p>${artists()}</p>
                  <p hidden>${id}</p>
                  <p hidden>${uri}</p>
                </div>
              </li>`);

                }
            }else{
                $('#id-search-result-body-album').hide();
            }

            //Set Artists
            if(artists.items.length > 0){
                $('#id-search-result-body-artirs').show();
                var items = artists.items;
                for(var i=0;i<items.length;i++){
                    var item = items[i];
                    var imageUrl = item.images[1].url?item.images[1].url:'';
                    var name = item.name;
                    var id = item.id;

                    $('.ui-row-body-artirst-album-page-artirst-related .auto-grid').append(`<li onclick="artirst_page_select_row(id)" id="id-artist-Related-li-${i + 1}">
                <div>
                  <img src="${imageUrl}" alt="">
                  <h5>${name}</h5>
                  <p hidden>${id}</p>
                </div>
              </li>`);
                }
            }else{
                $('#id-search-result-body-artirs').hide();
            }

            //Set Tracks
            if(tracks.items.length > 0){
                $('#id-search-result-body-songs').show();
                var items = tracks.items;
                for(var i=0;i<items.length;i++){
                    var item = items[i];
                    var name = item.name;
                    var durationTime = convertTimeToString(item.duration_ms);
                    var uriTrack = item.uri;

                    var temp = `<tr onclick="playings_page_select_row(id,'track')" id="id-search-result-body-songs-table_tr_${(i+1)}" class="ui_result_page_tr"> 
                    <th scope="row" class="num ui-bold">  ${(i + 1)}  </th> 
                    <td class="name ui-bold"> ${name}  </td> 
                    <td><i class="fa fa-heart" aria-hidden="true"></i></td> 
                    <td><i class="fas fa-ellipsis-h" aria-hidden="true"></i></td> 
                    <td>  ${durationTime}  </td> 
                    <td hidden>  ${uriTrack}  </td> 
                    </tr>`;
                    $('#id-search-result-body-songs-table > tbody').append(temp);
                }
            }else{
                $('#id-search-result-body-songs').hide();
            }
        }
    });
}

function getPlaylist(){
    const token = localStorage.getItem('access_token');
    var url = `	https://api.spotify.com/v1/me/playlists`;
    var limit = 10;
    var offset = 0;



}