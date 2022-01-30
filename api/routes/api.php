<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });

// 游客权限的操作

// 假登入，用于测试使用
Route::any('/login/fake', 'App\Http\Controllers\PushDeerUserController@fakeLogin');

// 通过 apple 返回的 idtoken 登入
Route::post('/login/idtoken', 'App\Http\Controllers\PushDeerUserController@login');

// 通过 微信客户端返回的 code 登入
Route::post('/login/wecode', 'App\Http\Controllers\PushDeerUserController@wechatLogin');

// 推送消息
Route::middleware('json.request')->any('/message/push', 'App\Http\Controllers\PushDeerMessageController@push');


// 自动登入，适用于通过 token 进行操作的接口
Route::middleware('auto.login')->group(function () {
    Route::middleware('auth.member')->group(function () {
        // 设备列表
        Route::post('/device/list', 'App\Http\Controllers\PushDeerDeviceController@list');
        // 注册设备
        Route::post('/device/reg', 'App\Http\Controllers\PushDeerDeviceController@reg');
        // 重命名设备
        Route::post('/device/rename', 'App\Http\Controllers\PushDeerDeviceController@rename');
        // 删除设备
        Route::post('/device/remove', 'App\Http\Controllers\PushDeerDeviceController@remove');

        // key列表
        Route::post('/key/list', 'App\Http\Controllers\PushDeerKeyController@list');
        // 生成一个新key
        Route::post('/key/gen', 'App\Http\Controllers\PushDeerKeyController@gen');
        // 重置一个key
        Route::post('/key/regen', 'App\Http\Controllers\PushDeerKeyController@regen');
        // 重命名key
        Route::post('/key/rename', 'App\Http\Controllers\PushDeerKeyController@rename');
        // 删除一个key
        Route::post('/key/remove', 'App\Http\Controllers\PushDeerKeyController@remove');

        // 消息列表
        Route::post('/message/list', 'App\Http\Controllers\PushDeerMessageController@list');
        // 删除消息
        Route::post('/message/remove', 'App\Http\Controllers\PushDeerMessageController@remove');


        Route::post('/user/info', 'App\Http\Controllers\PushDeerUserController@info');

        Route::post('/user/merge', 'App\Http\Controllers\PushDeerUserController@merge');
    });
});
