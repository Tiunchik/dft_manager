package solutions.dft.schema

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime


enum class TaskPriority { THE_LOWEST, LOWER, MEDIUM, HIGHER, THE_HIGHEST }

/**
 * TODO - impl - extension property for entityId -> userId & user(full model)
 *      -- for init from DB
 *      -- for init from External source(HTTP, Kafka, ...)
 * TODO - impl - extension property for list -> get comments<T : Iterable>()
 *      * T : Iterable - easy map to: List, Set, Queue, Map(List<Pair>)
 *      -- for init from DB
 *      -- for init from External source(HTTP, Kafka, ...)
 *
 * TODO ideas:
 *      - может быть нас спасут просто by lazy или кастомные Делегаты???
 *      - ? сделать кастомный lateinit для primitive - хотя сомнительно
 *
 *  /**
 *   * impl note:
 *   * in sql -> use other table relationship with many-to-many
 *   * in equals&hashCode -> ignore field
 *   * in toString -> ignore field because recursion and risk of reference-cycle(this-child-this)
 *   * ? maybe good idea - make it extension field in code?
 *   * in code -> suspend extension Property with cache in model like _linkedTasks {get} from C#
 *   */
 *  var linkedTasks: MutableList<Task> = mutableListOf()
 *  var attachedFiles: MutableList<Any> = mutableListOf() // todo - attached files
 *  var comments: MutableList<Any> = mutableListOf() // todo - impl model Comment
 *  var changeHistory: MutableList<Long> = mutableListOf() // todo - impl model TaskChangeEvent
 *
 *  /**
 *   * impl note:
 *   * in code -> suspend extension Property with cache in model like _linkedTasks {get} from C#
 *   */
 *  // todo - relationships
 *  // ? comments
 *  // ? tags [project, release, etc]
 *  // ? Type [feature, bug, ...]
 *  // ? клонировано от
 *  // ? блокирующую другие таски
 *
 */
class TODOForTask

@Serializable data class TaskCreate(
    var title: String?,
    var content: String?,
    var creatorId: Long?,
    var checkerId: Long?,
    var executorId: Long?,
    @Contextual var deadlineAt: LocalDateTime?,
    var statusId: Long?,
    var priority: TaskPriority?,
)
@Serializable data class TaskUpdate(
    var title: String? = null,
    var content: String? = null,
//    var creatorId: Long?,
    var checkerId: Long? = null,
    var executorId: Long? = null,
    @Contextual var deadlineAt: LocalDateTime? = null,
    var statusId: Long? = null,
    var priority: TaskPriority? = null,
)

/**
 * поля что по бизнес требования NOT NULL обозначаются так:
 * примитивы -> default value
 * объекты -> lateinit var - сохранение в БД будет когда value уже есть
 *
 */
@Serializable
class Task {
    var code: String = "" // final
    var title: String = ""
    var content: String = ""
    var creatorId: Long = -1 // lateinit final
    var checkerId: Long? = null
    var executorId: Long? = null
    @Contextual var deadlineAt: LocalDateTime? = null
    var statusId: Long = -1 // lateinit
    var priority: TaskPriority = TaskPriority.MEDIUM
    @Contextual var createdAt: LocalDateTime = LocalDateTime.now() // final
    @Contextual var updatedAt: LocalDateTime = LocalDateTime.now()
}

object Tasks : IdTable<String>() {
    val code = varchar("code", 10).uniqueIndex("tasks_code_unique")
    val title = text("title").default("")
    val content = text("content").default("")
    val creatorId = long("creator_id")
    val checkerId = long("checker_id").nullable()
    val executorId = long("executor_id").nullable()
    val deadlineAt = datetime("deadline_at").nullable()
    val statusId = long("status_id")
    val priority = enumerationByName<TaskPriority>("priority", 16).default(TaskPriority.MEDIUM)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey = PrimaryKey(id)
}


/**
 * Entity из коробки может:
 * new(insert)
 * get(select * where id=value OR THROW EXCEPTION if not found)
 * find(select * where $predicate): T
 * findById(select * where id=value OR null): T
 * all(select *)
 * count(count *)
 * reload(???)
 * searchQuery(select * where $predicate): Query
 */
class TaskEntity(id: EntityID<String>) : Entity<String>(id) {
    var code: String by Tasks.code
    var title: String by Tasks.title
    var content: String by Tasks.content
    var creatorId: Long by Tasks.creatorId
    var checkerId: Long? by Tasks.checkerId
    var executorId: Long? by Tasks.executorId
    var deadlineAt: LocalDateTime? by Tasks.deadlineAt
    var statusId: Long by Tasks.statusId
    var priority: TaskPriority by Tasks.priority
    var createdAt: LocalDateTime by Tasks.createdAt
    var updatedAt: LocalDateTime by Tasks.updatedAt

    companion object : EntityClass<String, TaskEntity>(Tasks)
}


/**
 * Create
 * Get
 * GetAll
 * Update
 * Delete
 */
class TaskRepo

suspend fun <T> sql(log: Boolean = false, block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) {
        if (log) transaction { addLogger(StdOutSqlLogger) }
        block()
    }

