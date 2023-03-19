package solutions.dft.schema

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.time
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import solutions.dft.schema.Tasks.default
import solutions.dft.schema.Tasks.nullable
import solutions.dft.schema.Tasks.uniqueIndex
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
 *  var coments: MutableList<Any> = mutableListOf() // todo - impl model Comment
 *  var changeHistory: MutableList<Any> = mutableListOf() // todo - impl model TaskChangeEvent
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

/**
 * поля что по бизнес требования NOT NULL обозначаются так:
 * примитивы -> default value
 * объекты -> lateinit var - сохранение в БД будет когда value уже есть
 *
 */
@Serializable
class Task {
    var code: String = ""
    var title: String = ""
    var content: String = ""
    var creatorId: Long = -1 // lateinit
    var checkerId: Long? = null
    var executorId: Long? = null
    @Contextual var deadlineAt: LocalDateTime? = null
    var statusId: Long = -1 // lateinit
    var priority: TaskPriority = TaskPriority.MEDIUM
    @Contextual var createdAt: LocalDateTime = LocalDateTime.now()
    @Contextual var updatedAt: LocalDateTime = LocalDateTime.now()
}


object Tasks : IdTable<String>() {
    val code = varchar("code", 10).uniqueIndex()
    val title = text("title").default("")
    val content = text("content").default("")
    val creatorId = long("creator_id")
    val checkerId = long("checker_id").nullable()
    val executorId = long("executor_id").nullable()
    val deadlineAt = datetime("deadline_at").nullable()
    val statusId = long("status_id")
    val priority = enumeration<TaskPriority>("priority")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

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
    val code: String by Tasks.code
    val title: String by Tasks.title
    val content: String by Tasks.content
    val creatorId: Long by Tasks.creatorId
    val checkerId: Long? by Tasks.checkerId
    val executorId: Long? by Tasks.executorId
    val deadlineAt: LocalDateTime? by Tasks.deadlineAt
    val statusId: Long by Tasks.statusId
    val priority : TaskPriority by Tasks.priority
    val createdAt : LocalDateTime by Tasks.createdAt
    val updatedAt : LocalDateTime by Tasks.updatedAt

    companion object : EntityClass<String, TaskEntity>(Tasks)
}

@Mapper // code-generation
interface TaskConverter {

    //    @Mapping(source = "deadlineAt", target = "deadlineAt")
    fun convertToDto(taskEntity: TaskEntity): Task

    @InheritInverseConfiguration
    fun convertToModel(task: Task): TaskEntity

}

class TY(init: TY.() -> Unit) {
    lateinit var name: String
    var email: String = ""

    init {
        init.invoke(this)
    }
}

fun run() {
    TY {
        name = "aaa"
        email = "bbb"
    }
}

/**
 * Create
 * Get
 * GetAll
 * Update
 * Delete
 */
class TaskRepo {
    suspend fun <T> dbQuery(log: Boolean = false, block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) {
            addLogger(StdOutSqlLogger)
            if (log) transaction { addLogger(StdOutSqlLogger) }
            block()
        }

    suspend fun getAllTasks(): Iterable<TaskEntity> = dbQuery {
        TaskEntity.all()
//       val i = TaskEntity.searchQuery(Tasks.code eq "")
//        null;

//        Tasks.selectAll()
//            .map {
//            Task(
//                it[Tasks.ID],
//                it[Tasks.title]
//            )
//        }
    }
}

