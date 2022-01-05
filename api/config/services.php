<?php

return [

    /*
    |--------------------------------------------------------------------------
    | Third Party Services
    |--------------------------------------------------------------------------
    |
    | This file is for storing the credentials for third party services such
    | as Mailgun, Postmark, AWS and more. This file provides the de facto
    | location for this type of information, allowing packages to have
    | a conventional file to locate the various service credentials.
    |
    */

    'mailgun' => [
        'domain' => env('MAILGUN_DOMAIN'),
        'secret' => env('MAILGUN_SECRET'),
        'endpoint' => env('MAILGUN_ENDPOINT', 'api.mailgun.net'),
    ],

    'postmark' => [
        'token' => env('POSTMARK_TOKEN'),
    ],

    'ses' => [
        'key' => env('AWS_ACCESS_KEY_ID'),
        'secret' => env('AWS_SECRET_ACCESS_KEY'),
        'region' => env('AWS_DEFAULT_REGION', 'us-east-1'),
    ],

    'go_push' => [
        'address' => env('GO_PUSH_ADDRESS', '127.0.0.1'),
        'ios_port' => env('GO_PUSH_IOS_PORT', 8888),
        'ios_topic' => env('GO_PUSH_IOS_TOPIC', 'com.pushdeer.app.ios'),
        'ios_clip_port' => env('GO_PUSH_IOS_CLIP_PORT', 8889),
        'ios_clip_topic' => env('GO_PUSH_IOS_CLIP_TOPIC', 'com.pushdeer.app.ios.Clip')
    ]

];
