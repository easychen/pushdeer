<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PushDeerUser extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'email',
        'apple_id',
        'wechat_id',
        'level',
    ];
}
