<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PushDeerMessage extends Model
{
    use HasFactory;

    protected $fillable = [
        'readkey',
        'pushkey_name',
        'text',
        'desp',
        'uid',
        'type',
    ];
}
