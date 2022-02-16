<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>PushDeer</title>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.19/tailwind.min.css">
</head>
<body class="p-5 mx-auto w-3/4">
<div>感谢使用自架版PushDeer</div>
<div>请用 ios14+ 系统摄像头扫码<br/>然后输入以下URL进行测试<br/><div id="url" class="bg-yellow-600 text-white p-2 my-2 rounded inline-block"></div></div>
<img src="/code.png" alt="clip code">

<script>
    const hostname = window.location.hostname;
    let urlinfo = window.origin;
    if( hostname == 'localhost' || hostname == '127.0.0.1' ) urlinfo = '您使用的是本机专用地址，请使用局域网或者公网地址测试';
    window.document.querySelector("#url").innerHTML=urlinfo;
</script>
</body>
</html>
