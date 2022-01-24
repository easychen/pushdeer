<?php

namespace App\Http\Controllers;

use App\Models\PushDeerDevice;
use Illuminate\Http\Request;

class PushDeerDeviceController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function list()
    {
        $pd_devices = PushDeerDevice::where('uid', $_SESSION['uid'])->get(['id', 'uid', 'name', 'type', 'device_id', 'is_clip']);
        return http_result(['devices' => $pd_devices]);
    }

    public function reg(Request $request)
    {
        $validated = $request->validate(
            [
                'name' => 'string|required',
                'device_id' => 'string|required',
                'is_clip' => 'integer|nullable',
                'type' => 'string|nullable',
            ]
        );

        $uid = $_SESSION['uid'];
        if (strlen($uid) < 1) {
            return send_error('uid错误', ErrorCode('ARGS'));
        }

        $type = 'ios';
        if (isset($validated['type']) && strlen($validated['type']) > 0) {
            $type = trim($validated['type']);
        }

        $the_device = [];
        $the_device['uid'] = $uid;
        $the_device['name'] = $validated['name'];
        $the_device['type'] = $type;
        $the_device['is_clip'] = intval($validated['is_clip']);
        $the_device['device_id'] = $validated['device_id'];


        $pd_device = PushDeerDevice::updateOrCreate($the_device, ['uid','device_id']);
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

        if ($pd_device = PushDeerDevice::where('id', $validated['id'])->get(['id', 'uid', 'name', 'type', 'device_id', 'is_clip'])->first()) {
            if ($pd_device->uid == $_SESSION['uid']) {
                $pd_device->name = $validated['name'];
                $pd_device->save();
                return http_result(['message'=>'done']);
            }
        }

        return send_error('设备不存在或已注销', ErrorCode('ARGS'));
    }

    public function remove(Request $request)
    {
        $validated = $request->validate(
            [
                'id' => 'integer|required',
            ]
        );

        if ($pd_device = PushDeerDevice::where('id', $validated['id'])->get(['id', 'uid', 'name', 'type', 'device_id', 'is_clip'])->first()) {
            if ($pd_device->uid == $_SESSION['uid']) {
                $pd_device->delete();
                return http_result(['message'=>'done']);
            }
        }

        return send_error('设备不存在或已注销', ErrorCode('ARGS'));
    }
}
