<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;

class InjectSessionFromToken
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
        // Read the token from the request input (e.g., query parameter or JSON body)
        $token = $request->input('token');

        // If a token is present, inject it as a session cookie into the request
        if ($token && is_string($token)) {
            // Get the configured session cookie name (e.g., 'laravel_session')
            $sessionCookieName = config('session.cookie');

            // Add the token as a cookie to the request object.
            // The StartSession middleware will automatically pick this up.
            $request->cookies->set($sessionCookieName, $token);
        }

        return $next($request);
    }
}
