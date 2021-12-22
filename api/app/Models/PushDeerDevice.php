<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PushDeerDevice extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'uid',
        'type',
        'device_id',
        'is_clip',
    ];
}
