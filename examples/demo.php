<?php

// 调用函数
function pushdeer_send($text, $desp = '', $type='text', $key = '[PUSHKEY]')
{
    $postdata = http_build_query(array( 'text' => $text, 'desp' => $desp, 'type' => $type , 'pushkey' => $key ));
    $opts = array('http' =>
    array(
        'method'  => 'POST',
        'header'  => 'Content-type: application/x-www-form-urlencoded',
        'content' => $postdata));
    
    $context  = stream_context_create($opts);
    return $result = file_get_contents('https://api2.pushdeer.com/message/push', false, $context);
}



// 使用实例
print_r(pushdeer_send('服务器又宕机了主人', '', 'text', 'PDU...'));
