package com.pushdeer.os.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wh.common.typeExt.toTimestamp

@Entity
class LogDog {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
    var level:String = "d"
    var entity:String = ""
    var event:String = ""
    var log:String = ""
    var timestamp:Long = System.currentTimeMillis()

    override fun toString(): String {
        return "id:$id\n" +
                "level:$level\n" +
                "entity:$entity\n" +
                "event:$event\n" +
                "log:$log\n" +
                "time:${timestamp.toTimestamp()}"
    }

//    @Composable
//    fun resolve(
//        d: @Composable () -> Unit,
//        e: @Composable () -> Unit,
//        i: @Composable () -> Unit,
//        w: @Composable () -> Unit
//    ) {
//        when(this.level){
//            "d" -> d()
//            "e" -> e()
//            "i" -> i()
//            "w" -> w()
//        }
//
//    }

    companion object{
        fun logd(entity: String,event:String,log:String): LogDog {
            return LogDog().apply {
                level = "d"
                this.entity = entity
                this.event = event
                this.log = log
            }
        }

        fun loge(entity: String,event:String,log:String): LogDog {
            return LogDog().apply {
                level = "e"
                this.entity = entity
                this.event = event
                this.log = log
            }
        }

        fun logi(entity: String,event:String,log:String): LogDog {
            return LogDog().apply {
                level = "i"
                this.entity = entity
                this.event = event
                this.log = log
            }
        }

        fun logw(entity: String,event:String,log:String): LogDog {
            return LogDog().apply {
                level = "w"
                this.entity = entity
                this.event = event
                this.log = log
            }
        }
    }
}