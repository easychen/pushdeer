<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePushDeerMessagesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('push_deer_messages', function (Blueprint $table) {
            $table->id();
            $table->string('uid');
            $table->mediumText('text');
            $table->longText('desp');
            $table->string('type')->default('markdown');
            $table->string('readkey');
            $table->string('url')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('push_deer_messages');
    }
}
