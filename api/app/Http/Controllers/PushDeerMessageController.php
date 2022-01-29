<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Str;
use App\Models\PushDeerUser;
use App\Models\PushDeerKey;
use App\Models\PushDeerDevice;
use App\Models\PushDeerMessage as Message;
use App\Models\PushDeerMessage;

class PushDeerMessageController extends Controller
{
    public function list(Request $request)
    {
        $validated = $request->validate(
            [
                'limit' => 'integer|nullable',
                'since_id' => 'integer|nullable',
            ]
        );

        $limit = !isset($validated['limit']) ? 10 : intval($validated['limit']);

        if ($limit > 100) {
            $limit = 100;
        }

        if (isset($validated['since_id']) && intval($validated['since_id']) > 0) {
            $pd_sql = Message::where('uid', $_SESSION['uid'])->where('id', '>', intval($validated['since_id']));
        } else {
            $pd_sql = Message::where('uid', $_SESSION['uid']);
        }

        $pd_messages = $pd_sql->orderBy('id', 'DESC')->offset(0)->limit($limit)->get(['id', 'uid', 'text', 'desp', 'type','pushkey_name','created_at']);



        return http_result(['messages' => $pd_messages]);
    }

    //
    public function push(Request $request)
    {
        $validated = $request->validate(
            [
                'pushkey' => 'string|required',
                'text' => 'string|required',
                'desp' => 'string|nullable',
                'type' => 'string|nullable',
            ]
        );

        if (!isset($validated['desp'])) {
            $validated['desp'] = '';
        }

        if (!isset($validated['type'])) {
            $validated['type'] = 'markdown';
        }

        $key = PushDeerKey::where('key', $validated['pushkey'])->get()->first();

        $result = [];

        if ($key) {
            $readkey = Str::random(32);
            $the_message = [];
            $the_message['uid'] = $key->uid;
            $the_message['text'] = $validated['text'];
            $the_message['desp'] = $validated['desp'];
            $the_message['type'] = $validated['type'];
            $the_message['readkey'] = $readkey;
            $the_message['pushkey_name'] = $key->name;
            $pd_message = Message::create($the_message);

            if ($devices = PushDeerDevice::where('uid', $key->uid)->get()) {
                foreach ($devices as $device) {
                    if ($device) {
                        $func_name = $device['type'].'_send';
                        if (function_exists($func_name)) {
                            $result[] = $func_name($device->is_clip, $device->device_id, $validated['text'], '', env('APP_DEBUG'));
                        }
                    }
                }
            } else {
                return send_error('没有可用的设备，请先注册', ErrorCode('ARGS'));
            }
        }

        return http_result(['result'=>$result]);
    }



    public function remove(Request $request)
    {
        $validated = $request->validate(
            [
                'id' => 'integer|required',
            ]
        );

        if ($pd_message = PushDeerMessage::where('id', $validated['id'])->get(['id', 'uid', 'text', 'desp', 'type','created_at'])->first()) {
            if ($pd_message->uid == $_SESSION['uid']) {
                $pd_message->delete();
                return http_result(['message'=>'done']);
            }
        }

        return send_error('消息不存在或已删除', ErrorCode('ARGS'));
    }
}
