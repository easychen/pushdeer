<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PushDeerKey extends Model
{
    use HasFactory;

    protected $fillable = [
        'uid',
        'key',
        'name',
    ];
}
