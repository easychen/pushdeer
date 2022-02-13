<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Foundation\Testing\WithFaker;
use Illuminate\Testing\Fluent\AssertableJson;
use Tests\TestCase;

class ApiTest extends TestCase
{
    private $token;
    private static $key;
    /**
     * A basic feature test example.
     *
     * @return void
     */
    public function test_login_fake()
    {
        $response = $this->getJson('/login/fake');
        $response->assertStatus(200)->assertJson(['code'=>0]);
        $data = json_decode($response->getContent(), true);
        $this->token = $data['content']['token'];
        echo __LINE__ . " " . $this->token ."\r\n";
        // $response->dd();
    }

    public function test_user_info()
    {
        $response = $this->post('/user/info', ['token'=>$this->token]);
        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.id', 1)
            ->assertJsonPath('content.apple_id', 'theid999');
    }

    public function test_device_reg()
    {
        $response = $this->post('/device/reg', [
            'token'=>$this->token,
            'name'=>'Easy的iPad',
            'device_id'=>'device-token',
            'is_clip'=>0,
        ]);

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.devices.0.uid', '1')
            ->assertJsonPath('content.devices.0.device_id', 'device-token');
    }

    public function test_device_list()
    {
        $response = $this->post('/device/list', [
            'token'=>$this->token,
        ]);

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.devices.0.uid', '1')
            ->assertJsonPath('content.devices.0.device_id', 'device-token');
    }

    public function test_device_rename()
    {
        $response = $this->post('/device/rename', [
            'token'=>$this->token,
            'id' => 1,
            'name' => 'device-renamed',
        ]);

        // $response->dump();

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.message', 'done');

        $response = $this->post('/device/list', [
            'token'=>$this->token,
        ]);

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertSee('device-renamed');
    }

    public function test_key_gen()
    {
        $response = $this->post('/key/gen', [
            'token'=>$this->token,
        ]);

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            // ->assertJson('content.keys.0.id', 1)
            ->assertSee('PDU');

        $data = json_decode($response->getContent(), true);
        self::$key = $data['content']['keys'][0]['key'];
    }

    public function test_key_rename()
    {
        $response = $this->post('/key/rename', [
            'token'=>$this->token,
            'id' => 1,
            'name' => 'key-renamed',
        ]);

        // $response->dump();

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.message', 'done');

        $response = $this->post('/key/list', [
            'token'=>$this->token,
        ]);

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertSee('key-renamed');
    }

    public function test_message_push()
    {
        $response = $this->post('/message/push', [
            'pushkey'=>self::$key,
            'text' => 'tested',
            'type' => 'text',
        ]);

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertSee('success');
    }

    public function test_message_list()
    {
        $response = $this->post('/message/list', [
            'token'=>$this->token,
        ]);

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertSee('tested');
    }

    public function test_message_remove()
    {
        $response = $this->post('/message/remove', [
            'token'=>$this->token,
            'id' => 1,
        ]);

        // $response->dump();

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.message', 'done');
    }

    public function test_key_regen()
    {
        $response = $this->post('/key/regen', [
            'token'=>$this->token,
            'id' => 1,
        ]);

        // $response->dump();

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.message', 'done');
    }

    public function test_key_remove()
    {
        $response = $this->post('/key/remove', [
            'token'=>$this->token,
            'id' => 1,
        ]);

        // $response->dump();

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.message', 'done');
    }

    public function test_device_remove()
    {
        $response = $this->post('/device/remove', [
            'token'=>$this->token,
            'id' => 1,
        ]);

        // $response->dump();

        $response->assertStatus(200)
            ->assertJson(['code'=>0])
            ->assertJsonPath('content.message', 'done');
    }
}
