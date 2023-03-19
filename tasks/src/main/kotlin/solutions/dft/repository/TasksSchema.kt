package solutions.dft.temp

import kotlinx.coroutines.Dispatchers import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single
import java.time.LocalDateTime


enum class TaskPriority { TH_LOWEST, LOWER, NORMAL, HIGHER, THE_HIGHEST }

// todo - Иерархия, как реализовать?
/**
 * поля что по бизнес требования NOT NULL обозначаются так:
 * примитивы -> default value
 * объекты -> lateinit var
 */
class Task() {
    var id: Long = -1
    var code: String = ""
    var title: String = ""
    var body: String = ""
    lateinit var creator: Any
    lateinit var owner: Any
    var createAt: LocalDateTime = LocalDateTime.now()
    var deadLineAt: LocalDateTime = LocalDateTime.MAX
    var status: String = ""
    var priority: TaskPriority = TaskPriority.NORMAL

    /**
     * impl note:
     * in sql -> use other table relationship with many-to-many
     * in equals&hashCode -> ignore field
     * in toString -> ignore field because recursion and risk of reference-cycle(this-child-this)
     * ? maybe good idea - make it extension field in code?
     */
    var linkedTasks: MutableList<Task> = mutableListOf()

    var coments: MutableList<Any> = mutableListOf()
    var changeHistory: MutableList<Any> = mutableListOf()
    var createdAt: LocalDateTime = LocalDateTime.now()
    var updatedAt: LocalDateTime = LocalDateTime.now()


    // ? comments
    // ? tags [project, release, etc]
}

object Tasks : IdTable<Long>() {
    val ID = long("id").uniqueIndex().autoIncrement()
    val title = text("title")

    override val id: Column<EntityID<Long>> = ID.entityId()
    override val primaryKey = PrimaryKey(id)
}


// entity
class TaskEntity(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, TaskEntity>(Tasks)

    var sequelId by Tasks.id
    var title by Tasks.title
}

fun runTest() {
    transaction {
    }
    TaskEntity.new { title = "abvng" }.title
}


// =======================
//object FooTable : IdTable<String>() {
//    val myIdColumn = varchar("my_id_column", 50).uniqueIndex()
//    override val id: Column<EntityID<String>> = myIdColumn.entityId()
//}
//
//
//
//class FooEntity(id: String) : Entity<String>(id) {
//    companion object : EntityClass<String, FooEntity>(FooTable)
//
//}

@Single
class TaskRepo {
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    //    fun createTask(task: Task) : Task {
//
//    }
    suspend fun getAllTasks(): List<Task> = dbQuery {
//        val t = TaskEntity(EntityID(1, Tasks.id))
        val te = TaskEntity
        te.new {

        }
//        Tasks.selectAll().map { Task(it[Tasks.ID], it[Tasks.title]) }
        Tasks.selectAll().map { Task() }
    }
}

