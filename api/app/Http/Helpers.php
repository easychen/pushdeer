<?php
use Rookiejin\Xmpush\Builder;
use Rookiejin\Xmpush\HttpBase;
use Rookiejin\Xmpush\Sender;
use Rookiejin\Xmpush\Constants;
use Rookiejin\Xmpush\Stats;
use Rookiejin\Xmpush\Tracer;
use Rookiejin\Xmpush\Feedback;
use Rookiejin\Xmpush\DevTools;
use Rookiejin\Xmpush\Subscription;
use Rookiejin\Xmpush\TargetedMessage;

function getUserDataFromIdentityToken($idToken)
{
    $appleSignInPayload = \AppleSignIn\ASDecoder::getAppleSignInPayload($idToken);
    return [ 'email' => $appleSignInPayload->getEmail() , 'uid' => $appleSignInPayload->getUser() ];
}

function http_result($content, $code=0)
{
    return ['code'=>$code, 'content'=>$content];
}

function send_error($error, $code = '9999')
{
    return response()->json(['code'=>$code, 'error'=>$error]);
    // return response()->json(http_result($msg, $code, $msg));
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

function android_sender()
{
    if (!isset($GLOBALS['PD_ANDROID_SENDER'])) {
        Constants::setPackage(env('ANDROID_PACKAGE', 'com.pushdeer.app.quick.v2'));
        Constants::setSecret(env('MIPUSH_SECRET', 'NONE'));

        $GLOBALS['PD_ANDROID_SENDER'] = new Sender();
    }
    return $GLOBALS['PD_ANDROID_SENDER'];
}

function android_send($is_clip, $device_token, $text, $desp = '', $dev = true)
{
    if (strlen($desp) < 1) {
        $desp = $text;
        $text = 'PushDeer';
    }

    $message1 = new Builder();
    $message1->title($text);  // 通知栏的title
    $message1->description($desp); // 通知栏的descption
    $message1->passThrough(0);  // 这是一条通知栏消息，如果需要透传，把这个参数设置成1,同时去掉title和descption两个参数
    // $message1->payload($payload); // 携带的数据，点击后将会通过客户端的receiver中的onReceiveMessage方法传入。
    $message1->extra(Builder::notifyForeground, 1); // 应用在前台是否展示通知，如果不希望应用在前台时候弹出通知，则设置这个参数为0
    $message1->extra(Builder::notifyEffect, 1);
    $message1->notifyType(4);
    $message1->notifyId(rand(1, 100)); // 通知类型。同样的类型的通知会互相覆盖，不同类型可以在通知栏并存
    $message1->build();

    $sender = android_sender();
    return $sender->send($message1, $device_token)->getRaw();
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

    $port = intval($is_clip) == 1 ? config('services.go_push.ios_clip_port') : config('services.go_push.ios_port');
    $topic = intval($is_clip) == 1 ? config('services.go_push.ios_clip_topic') : config('services.go_push.ios_topic');
    $notification->topic = $topic;
    $notification->sound = ['volume'=>2.0];

    $json = ['notifications'=>[$notification]];
    $client = new GuzzleHttp\Client();
    $response = $client->post('http://'.config('services.go_push.address').':'. $port .'/api/push', [
    GuzzleHttp\RequestOptions::JSON => $json
    ]);
    $ret = $response->getBody()->getContents();
    error_log('push error'. $ret);
    return $ret;
}
