package solutions.dft.repository

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.valiktor.functions.isNotNull
import solutions.dft.Validatable
import solutions.dft.validation
import java.time.LocalDateTime


enum class TaskPriority { THE_LOWEST, LOWER, MEDIUM, HIGHER, THE_HIGHEST }

@Serializable data class TaskCreate(
    val title: String? = null,
    val content: String? = null,
    val creatorId: Long? = null,
    val checkerId: Long? = null,
    val executorId: Long? = null,
    @Contextual val deadlineAt: LocalDateTime? = null,
    val statusId: Long? = null,
    val priority: TaskPriority? = null,
) : Validatable {
    override fun validate() {
        validation(this) {
            validate(TaskCreate::title).isNotNull()
            validate(TaskCreate::creatorId).isNotNull()
            validate(TaskCreate::statusId).isNotNull()
        }
    }
}

@Serializable data class TaskUpdate(
    val title: String? = null,
    val content: String? = null,
    val checkerId: Long? = null,
    val executorId: Long? = null,
    @Contextual val deadlineAt: LocalDateTime? = null,
    val statusId: Long? = null,
    val priority: TaskPriority? = null,
) : Validatable {
    override fun validate() {
        validation(this) {
            validate(TaskUpdate::title).isNotNull()
            validate(TaskUpdate::statusId).isNotNull()
            validate(TaskUpdate::priority).isNotNull()
        }
    }
}

@Serializable class Task {
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

object TaskTable : IdTable<String>("tasks") {
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
    var code: String by TaskTable.code
    var title: String by TaskTable.title
    var content: String by TaskTable.content
    var creatorId: Long by TaskTable.creatorId
    var checkerId: Long? by TaskTable.checkerId
    var executorId: Long? by TaskTable.executorId
    var deadlineAt: LocalDateTime? by TaskTable.deadlineAt
    var statusId: Long by TaskTable.statusId
    var priority: TaskPriority by TaskTable.priority
    var createdAt: LocalDateTime by TaskTable.createdAt
    var updatedAt: LocalDateTime by TaskTable.updatedAt

    companion object : EntityClass<String, TaskEntity>(TaskTable)
}

@Mapper // code-generation
interface TaskConverter {

    fun entityToModel(taskEntity: TaskEntity): Task

    @InheritInverseConfiguration
    @Mapping(
        target = "id", expression = "java(new org.jetbrains.exposed.dao.id.EntityID(" +
                "task.getCode(), solutions.dft.repository.TaskTable.INSTANCE))"
    )
    fun modelToEntityWithId(task: Task): TaskEntity

    @InheritInverseConfiguration
    @Mapping(target = "code", ignore = true)
    fun createDtoToTargetEntityNoId(task: TaskCreate, @MappingTarget entity: TaskEntity)

}

