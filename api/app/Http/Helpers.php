<?php
use AppleSignIn\ASDecoder;

function getUserDataFromIdentityToken($idToken)
{
    $appleSignInPayload = ASDecoder::getAppleSignInPayload($idToken);
    return [ 'email' => $appleSignInPayload->getEmail() , 'uid' => $appleSignInPayload->getUser() ];
}

function http_result($content, $code=0, $error='')
{
    return ['code'=>$code, 'content'=>$content,'error'=>$error];
}

function send_error($msg, $code = '9999')
{
    return response()->json(http_result($msg, $code, $msg));
}

function uid()
{
    return isset($_SESSION) && $_SESSION['uid'] ? $_SESSION['uid'] : false;
}

function ErrorCode($code)
{
    $ret = 80999;

    switch ($code) {
        case 'AUTH':
            $ret = 80403;
            break;

        case 'ARGS':
            $ret = 80501;
            break;

        case 'REMOTE':
            $ret = 80502;
            break;

        default:
            $ret = 80999;
    }

    return $ret;
}

function ios_send($is_clip, $device_token, $text, $desp = '', $dev = true)
{
    $notification = new stdClass();
    $notification->tokens = [ $device_token ];
    $notification->platform = 1;
    if (strlen($desp) > 1) {
        $notification->title = $text;
        $notification->message = $desp;
    } else {
        $notification->message = $text;
    }

    if ($dev) {
        $notification->development = true;
    } else {
        $notification->production = true;
    }

    $port = intval($is_clip) == 1 ? 8889 : 8888;
    $topic = intval($is_clip) == 1 ? 'com.pushdeer.app.ios.Clip' : 'com.pushdeer.app.ios';
    $notification->topic = $topic;
    $notification->sound = ['volume'=>2.0];

    $json = ['notifications'=>[$notification]];
    $client = new GuzzleHttp\Client();
    $response = $client->post('http://127.0.0.1:'. $port .'/api/push', [
    GuzzleHttp\RequestOptions::JSON => $json
    ]);
    $ret = $response->getBody()->getContents();
    error_log('push error'. $ret);
    return $ret;
    ;
}
