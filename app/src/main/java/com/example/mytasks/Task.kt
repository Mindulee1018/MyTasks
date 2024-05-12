import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "priority") val priority: Priority,
    @ColumnInfo(name = "deadline") val deadline: Date,
    @ColumnInfo(name = "completed") val completed: Boolean = false
)

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}
