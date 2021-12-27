<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\PushDeerUser;

class PushDeerUserController extends Controller
{
    public function info(Request $request)
    {
        //return $_SESSION;
        return http_result(PushDeerUser::where('id', $_SESSION['uid'])->get());
    }

    public function fakeLogin(Request $request)
    {
        $info = [
            'uid' => 'theid999',
            'email' => 'easychen+new@gmail.com',
        ];

        if (isset($info['uid'])) {
            // 通过 uid 查询 user 表，如果存在自动登入，如果不存在自动注册并登入
            $pd_user = PushDeerUser::where('apple_id', $info['uid'])->get()->first();
            if (!$pd_user) {
                // 用户不存在，创建用户
                $the_user = [];
                $the_user['apple_id'] = $info['uid'];
                $the_user['email'] = $info['email'];
                $the_user['name'] = @reset(explode('@', $info['email']));
                $the_user['level'] = 1;

                $pd_user = PushDeerUser::create($the_user);
            }

            // 将数据写到session
            session_start();
            $_SESSION['uid'] = $pd_user['id'];
            $_SESSION['name'] = $pd_user['name'];
            $_SESSION['email'] = $pd_user['email'];
            $_SESSION['level'] = $pd_user['level'];

            session_regenerate_id(true);
            $token = session_id();
            return http_result(['token'=>$token]);
        }

        return send_error('id_token解析错误', ErrorCode('ARGS'));
    }

    //
    public function login(Request $request)
    {
        $validated = $request->validate(
            [
                'idToken' => 'string',
            ]
        );

        if (isset($validated['idToken'])) {
            // 解码并进行验证
            $info = getUserDataFromIdentityToken($validated['idToken']);
            if (isset($info['uid'])) {
                // 通过 uid 查询 user 表，如果存在自动登入，如果不存在自动注册并登入
                $pd_user = PushDeerUser::where('apple_id', $info['uid'])->get()->first();
                if (!$pd_user) {
                    // 用户不存在，创建用户
                    $the_user = [];
                    $the_user['apple_id'] = $info['uid'];
                    $the_user['email'] = $info['email'];
                    $the_user['name'] = @reset(explode('@', $info['email']));
                    $the_user['level'] = 1;

                    $pd_user = PushDeerUser::create($the_user);
                }

                // 将数据写到session
                session_start();
                $_SESSION['uid'] = $pd_user['id'];
                $_SESSION['name'] = $pd_user['name'];
                $_SESSION['email'] = $pd_user['email'];
                $_SESSION['level'] = $pd_user['level'];

                session_regenerate_id(true);
                $token = session_id();
                return http_result(['token'=>$token]);
            }
        }


        return send_error('id_token解析错误', ErrorCode('ARGS'));
    }
}
