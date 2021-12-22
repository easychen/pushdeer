# GET /login/fake

+ Request

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 59

    + Body

            {"code":0,"content":{"token":"bdb7d5609ec03ef6168a174192ea3c9d"}}


# POST /user/info

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 57

    + Body

            {"uid":1,"name":"easychen","email":"easychen@gmail.com","level":1}


# POST /device/reg

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d&name=Easy%E7%9A%84iPad&device_id=device-token&is_clip=0

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 58

    + Body

            {"code":0,"content":{"devices":[{"id":2,"uid":"1","name":"Easy\u7684iPad","type":"all","device_id":"device-token","is_clip":0}]}}


# POST /device/list

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 59

    + Body

            {"code":0,"content":{"devices":[{"id":1,"uid":"1","name":"Easy\u7684iPad","type":"all","device_id":"device-token","is_clip":0}]}}


# POST /device/remove

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d&id=1

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 58

    + Body

            {"code":0,"content":{"message":"done"}}


# POST /key/gen

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 59

    + Body

            {"code":0,"content":{"keys":[{"id":2,"uid":"1","key":"PDU1TTnEZKlRVODU9GkFmtHwaIraV5twUPQbA"}]}}


# POST /key/regen

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d&id=1

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 59

    + Body

            {"code":0,"content":{"message":"done"}}


# POST /key/list

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 59

    + Body

            {"code":0,"content":{"keys":[{"id":2,"uid":"1","key":"PDU1TTnEZKlRVODU9GkFmtHwaIraV5twUPQbA"}]}}


# POST /key/remove

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d&id=1

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 58

    + Body

            {"code":0,"content":{"message":"done"}}


# POST /message/push

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            pushkey=PDU1TTnEZKlRVODU9GkFmtHwaIraV5twUPQbA&text=%E8%BF%99%E6%98%AF%E4%BB%80%E4%B9%88%E5%91%801111

+ Response 200 (application/json)

    + Headers

            Transfer-Encoding: chunked
            X-RateLimit-Remaining: 58
            X-RateLimit-Limit: 60
            Cache-Control: no-cache, private

    + Body

            {"result":["{\"counts\":1,\"logs\":[],\"success\":\"ok\"}"]}


# POST /message/list

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 58

    + Body

            {"code":0,"content":{"messages":[{"id":3,"uid":"1","text":"\u8fd9\u662f\u4ec0\u4e48\u54401111","desp":"","type":"markdown","created_at":"2021-12-22T12:09:46.000000Z"},{"id":2,"uid":"1","text":"\u8fd9\u662f\u4ec0\u4e48\u5440234","desp":"","type":"markdown","created_at":"2021-12-22T12:08:32.000000Z"}]}}


# POST /message/remove

+ Request (application/x-www-form-urlencoded; charset=utf-8)


    + Body

            token=bdb7d5609ec03ef6168a174192ea3c9d&id=1

+ Response 200 (application/json)

    + Headers

            Pragma: no-cache
            Set-Cookie: PHPSESSID=bdb7d5609ec03ef6168a174192ea3c9d; path=/
            Transfer-Encoding: chunked
            Expires: Thu, 19 Nov 1981 08:52:00 GMT
            X-RateLimit-Limit: 60
            Cache-Control: no-store, no-cache, must-revalidate, no-cache, private
            X-RateLimit-Remaining: 59

    + Body

            {"code":0,"content":{"message":"done"}}


