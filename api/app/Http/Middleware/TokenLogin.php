<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;

class TokenLogin
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle(Request $request, Closure $next)
    {
        // 拦截 token
        $token = $request->input('token');
        if (strlen($token) > 0) {
            session_id($token);
            session_start();
        }

        return $next($request);
    }
}
