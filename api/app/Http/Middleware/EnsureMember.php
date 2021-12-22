<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;

class EnsureMember
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
        if (!isset($_SESSION) || !isset($_SESSION['level'])  || $_SESSION['level'] < 1) {
            return send_error("当前用户没有足够的权限访问此接口", ErrorCode('AUTH'));
        }

        return $next($request);
    }
}
