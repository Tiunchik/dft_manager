//package solutions.dft.schema
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.serialization.Contextual
//import kotlinx.serialization.Serializable
//import org.jetbrains.exposed.dao.Entity
//import org.jetbrains.exposed.dao.EntityClass
//import org.jetbrains.exposed.dao.id.EntityID
//import org.jetbrains.exposed.dao.id.IdTable
//import org.jetbrains.exposed.sql.Column
//import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
//import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
//import org.jetbrains.exposed.sql.StdOutSqlLogger
//import org.jetbrains.exposed.sql.addLogger
//import org.jetbrains.exposed.sql.javatime.datetime
//import org.jetbrains.exposed.sql.javatime.time
//import org.jetbrains.exposed.sql.selectAll
//import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
//import org.jetbrains.exposed.sql.transactions.transaction
//import org.mapstruct.InheritInverseConfiguration
//import org.mapstruct.Mapper
//import org.mapstruct.Mapping
//import solutions.dft.schema.Tasks.default
//import solutions.dft.schema.Tasks.nullable
//import solutions.dft.schema.Tasks.uniqueIndex
//import java.time.LocalDateTime
//
//
//enum class TaskPriority { THE_LOWEST, LOWER, MEDIUM, HIGHER, THE_HIGHEST }
//
//
///**
// * поля что по бизнес требования NOT NULL обозначаются так:
// * примитивы -> default value
// * объекты -> lateinit var - сохранение будет когда value уже есть
// */
//@Serializable
//data class Task(
//    var code: String = "",
//    var title: String = "",
//    var content: String = "",
//    var creatorId: Long, // todo - impl model User
//    var checkerId: Long? = null, // todo - impl model User
//    var executorId: Long? = null, // todo - impl model User
//    @Contextual var deadlineAt: LocalDateTime? = null,
//) {
////    lateinit var creator: Any
////    var checker: Any? = null // todo - impl model User
////    var executor: Any? = null // todo - impl model User
////    var deadLineAt: LocalDateTime? = null
////    lateinit var status: Any // todo - impl model TaskStatus - в какой колонке это таска
////    var priority: TaskPriority = TaskPriority.MEDIUM
////
////    var createdAt: LocalDateTime = LocalDateTime.now()
////    var updatedAt: LocalDateTime = LocalDateTime.now()
////
////
////    /**
////     * impl note:
////     * in sql -> use other table relationship with many-to-many
////     * in equals&hashCode -> ignore field
////     * in toString -> ignore field because recursion and risk of reference-cycle(this-child-this)
////     * ? maybe good idea - make it extension field in code?
////     * in code -> suspend extension Property with cache in model like _linkedTasks {get} from C#
////     */
////    var linkedTasks: MutableList<Task> = mutableListOf()
//
//////    var attachedFiles: MutableList<Any> = mutableListOf() // todo - attached files
////
////    //    var coments: MutableList<Any> = mutableListOf() // todo - impl model Comment
//////    var changeHistory: MutableList<Any> = mutableListOf() // todo - impl model TaskChangeEvent
//
////    /**
////     * impl note:
////     * in code -> suspend extension Property with cache in model like _linkedTasks {get} from C#
////     */
////    // todo - relationships
////    // ? comments
////    // ? tags [project, release, etc]
////    // ? Type [feature, bug, ...]
////    // ? клонировано от
////    // ? блокирующую другие таски
////
////
////    var someProperty: Int = 0
////        get() = 1
////        set(value) {
////            field = value
////        }
//}
//
//suspend fun Task.relationships(): MutableList<Any> = mutableListOf()
//
//object Tasks : IdTable<String>() {
//    val code = varchar("code", 10).uniqueIndex()
//    val title = text("title").default("")
//    val content = text("content").default("")
//    val creatorId = long("creator_id")
//    val checkerId = long("checker_id").nullable()
//    val executorId = long("executor_id").nullable()
//    val deadlineAt = datetime("deadline_at").nullable()
//
//    override val id: Column<EntityID<String>> = code.entityId()
//    override val primaryKey = PrimaryKey(id)
//}
//
//
///**
// * new(insert)
// * get(select * where id=value OR THROW EXCEPTION if not found)
// * find(select * where $predicate): T
// * findById(select * where id=value OR null): T
// * all(select *)
// * count(count *)
// * reload(???)
// * searchQuery(select * where $predicate): Query
// */
//class TaskEntity(id: EntityID<String>) : Entity<String>(id) {
//    companion object : EntityClass<String, TaskEntity>(Tasks)
//
//    val code: String by Tasks.code
//    val title: String by Tasks.title
//    val content: String by Tasks.content
//    val creatorId: Long by Tasks.creatorId
//    val checkerId: Long? by Tasks.checkerId
//    val executorId: Long? by Tasks.executorId
//    val deadlineAt: LocalDateTime? by Tasks.deadlineAt
//}
//@Mapper // code-generation
//interface TaskConverter {
//
////    @Mapping(source = "deadlineAt", target = "deadlineAt")
//    fun convertToDto(taskEntity: TaskEntity) : Task
//
//    @InheritInverseConfiguration
//    fun convertToModel(task: Task) : TaskEntity
//
//}
//fun run() {
//    val o = transaction { "" }
//}
//
///**
// * Create
// * Get
// * GetAll
// * Update
// * Delete
// */
//class TaskRepo {
//    suspend fun <T> dbQuery(log: Boolean = false, block: suspend () -> T): T =
//        newSuspendedTransaction(Dispatchers.IO) {
//            addLogger(StdOutSqlLogger)
//            if (log) transaction { addLogger(StdOutSqlLogger) }
//            block()
//        }
//
//    suspend fun getAllTasks(): Iterable<TaskEntity> = dbQuery {
//        TaskEntity.all()
////       val i = TaskEntity.searchQuery(Tasks.code eq "")
////        null;
//
////        Tasks.selectAll()
////            .map {
////            Task(
////                it[Tasks.ID],
////                it[Tasks.title]
////            )
////        }
//    }
//}
//
