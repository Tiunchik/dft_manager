package solutions.dft.repository

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import solutions.dft.sql
import java.time.LocalDateTime
import java.util.*

/**
 * Группировка - UUID
 * КАКОЙ ТАСКА
 * КЕМ ИЗМЕНЕННО
 * ВРЕМЯ ИЗМЕНЕНИЯ ------------------
 *
 * КАКОЕ ПОЛЕ ИЗМЕНЕНО
 * ПРОШЛОЕ ЗНАЧЕНИЕ
 * НОВОЕ ЗНАЧЕНИЕ.
 */

@Serializable class TaskChangesCommit(
    @Contextual var uuid: UUID,
    @Contextual var createdAt: LocalDateTime,
    var creatorId: Long = -1,
    var taskCode: String = "",
) {
    lateinit var changes: List<TaskChangeEvent>

    companion object {
        fun from(rs: ResultRow): TaskChangesCommit = TaskChangesCommit(
            uuid = rs[TaskChangesCommitTable.id].value,
            createdAt = rs[TaskChangesCommitTable.createdAt],
            creatorId = rs[TaskChangesCommitTable.creatorId],
            taskCode = rs[TaskChangesCommitTable.taskCode].value,
        )
    }
}

@Serializable data class TaskChangeEvent(
    val fieldName: String,
    val oldValue: String,
    val newValue: String,
    @Contextual val commitUUID: UUID
) {
    companion object {
        fun from(rs: ResultRow) = TaskChangeEvent(
            fieldName = rs[TaskChangeEventTable.fieldName],
            oldValue = rs[TaskChangeEventTable.oldValue],
            newValue = rs[TaskChangeEventTable.newValue],
            commitUUID = rs[TaskChangeEventTable.commitUUID].value,
        )
    }
}

/** Имеет поле uuid от Родителя(UUIDTable) */
object TaskChangesCommitTable : UUIDTable("tasks-change-commits", "uuid") {
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val creatorId = long("creator_id")
    val taskCode = reference("task_code", TaskTable)
}

object TaskChangeEventTable : Table("tasks-change-events") {
    val fieldName = text("field_name")
    val oldValue = text("old_value")
    val newValue = text("new_value")
    val commitUUID = reference("commit_uuid", TaskChangesCommitTable.id)
}

/** preset queries */

suspend fun TaskChangesCommitTable.selectAllForTask(taskCode: String, commitUUID: UUID? = null): List<TaskChangesCommit> = sql {
    var where = TaskChangesCommitTable.taskCode eq taskCode
    if (commitUUID != null) where = where and (TaskChangesCommitTable.id eq commitUUID)

    val commits = TaskChangesCommitTable.select(where).map(TaskChangesCommit::from)
    val uuidAndEventsMap = TaskChangeEventTable
        .select(TaskChangeEventTable.commitUUID inList (commits.map(TaskChangesCommit::uuid)))
        .map(TaskChangeEvent::from)
        .groupBy(TaskChangeEvent::commitUUID)

    return@sql commits.onEach { it.changes = uuidAndEventsMap.getOrDefault(it.uuid, emptyList()) }
}

/** Entities */




