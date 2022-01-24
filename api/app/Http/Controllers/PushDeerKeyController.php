<?php

namespace App\Http\Controllers;

use App\Models\PushDeerKey;
use Illuminate\Http\Request;
use Illuminate\Support\Str;

class PushDeerKeyController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function list()
    {
        $pd_keys = PushDeerKey::where('uid', $_SESSION['uid'])->get(['id','name', 'uid', 'key','created_at']);
        return http_result(['keys' => $pd_keys]);
    }

    public function gen(Request $request)
    {
        $uid = $_SESSION['uid'];
        if (strlen($uid) < 1) {
            return send_error('uid错误', ErrorCode('ARGS'));
        }

        $the_key = [];
        $the_key['name'] = 'Key'.Str::random(8);
        $the_key['uid'] = $uid;
        $the_key['key'] = 'PDU'.$uid.'T'.Str::random(32);

        // $pd_key = PushDeerKey::updateOrCreate($the_key, ['uid']);
        $pd_key = PushDeerKey::create($the_key);
        return $this->list();
    }

    public function rename(Request $request)
    {
        $validated = $request->validate(
            [
                'id' => 'integer|required',
                'name' =>'string|required'
            ]
        );

        if ($pd_key = PushDeerKey::where('id', $validated['id'])->get(['id', 'name','uid', 'key','created_at'])->first()) {
            if ($pd_key->uid == $_SESSION['uid']) {
                $pd_key->name = $validated['name'];
                $pd_key->save();
                return http_result(['message'=>'done']);
            }
        }

        send_error('Key不存在或已删除', ErrorCode('ARGS'));
    }

    public function regen(Request $request)
    {
        $validated = $request->validate(
            [
                'id' => 'integer|required',
            ]
        );

        if ($pd_key = PushDeerKey::where('id', $validated['id'])->get(['id', 'name','uid', 'key','created_at'])->first()) {
            if ($pd_key->uid == $_SESSION['uid']) {
                $pd_key->key = 'PDU'.$pd_key->uid.'T'.Str::random(32);
                $pd_key->save();
                return http_result(['message'=>'done']);
            }
        }

        send_error('Key不存在或已删除', ErrorCode('ARGS'));
    }

    public function remove(Request $request)
    {
        $validated = $request->validate(
            [
                'id' => 'integer|required',
            ]
        );

        if ($pd_key = PushDeerKey::where('id', $validated['id'])->get(['id','name', 'uid', 'key','created_at'])->first()) {
            if ($pd_key->uid == $_SESSION['uid']) {
                $pd_key->delete();
                return http_result(['message'=>'done']);
            }
        }

        send_error('Key不存在或已删除', ErrorCode('ARGS'));
    }
}
