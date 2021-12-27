## ① 模拟登入
curl "http://127.0.0.1:8800/login/fake" \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ② 获取当前用户数据
curl -X "POST" "http://127.0.0.1:8800/user/info" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ③ 注册设备
curl -X "POST" "http://127.0.0.1:8800/device/reg" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ④ 设备列表
curl -X "POST" "http://127.0.0.1:8800/device/list" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑤ 删除已注册的设备
curl -X "POST" "http://127.0.0.1:8800/device/remove" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑤ 重命名设备
curl -X "POST" "http://127.0.0.1:8800/device/rename" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑥ 生成一个新key
curl -X "POST" "http://127.0.0.1:8800/key/gen" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑦ 重置一个key 
curl -X "POST" "http://127.0.0.1:8800/key/regen" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑦ 重命名key
curl -X "POST" "http://127.0.0.1:8800/key/rename" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑧ key 列表
curl -X "POST" "http://127.0.0.1:8800/key/list" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑨ 删除一个key
curl -X "POST" "http://127.0.0.1:8800/key/remove" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑩推送消息
curl -X "POST" "http://127.0.0.1:8800/message/push" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑩① 消息列表
curl -X "POST" "http://127.0.0.1:8800/message/list" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'

## ⑩② 删除消息
curl -X "POST" "http://127.0.0.1:8800/message/remove" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8' \
     -H 'Cookie: PHPSESSID=8d57dae1ca8502c784697bb4177bbdcd'
