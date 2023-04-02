package solutions.dft.service

import org.jetbrains.exposed.sql.insert
import org.koin.core.annotation.Single
import solutions.dft.repository.TaskChangeEventTable
import solutions.dft.repository.TaskChangesCommitTable
import solutions.dft.repository.TaskEntity
import solutions.dft.repository.TaskUpdate
import solutions.dft.sql
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KMutableProperty0

@Single
class TaskService {
    suspend fun updateTask(taskCode: String, updaterId: Long, taskEntity: TaskEntity, updateDTO: TaskUpdate) {
        sql {
            val commitUUID = TaskChangesCommitTable.insert {
                it[creatorId] = updaterId
                it[this.taskCode] = taskCode
            }[TaskChangesCommitTable.id].value

            updateTaskFieldIfNotNull(updateDTO.title, taskEntity::title, commitUUID)
            updateTaskFieldIfNotNull(updateDTO.content, taskEntity::content, commitUUID)
            updateTaskFieldIfNotNull(updateDTO.checkerId, taskEntity::checkerId, commitUUID)
            updateTaskFieldIfNotNull(updateDTO.executorId, taskEntity::executorId, commitUUID)
            updateTaskFieldIfNotNull(updateDTO.deadlineAt, taskEntity::deadlineAt, commitUUID)
            updateTaskFieldIfNotNull(updateDTO.statusId, taskEntity::statusId, commitUUID)
            updateTaskFieldIfNotNull(updateDTO.priority, taskEntity::priority, commitUUID)
            taskEntity.updatedAt = LocalDateTime.now()
        }
    }

    private inline fun <reified T> updateTaskFieldIfNotNull(newValue: T?, entityProperty: KMutableProperty0<T>, commitUUID: UUID) {
        if (newValue == null) return
        val oldValue = entityProperty.get()
        if (newValue == oldValue) return

        entityProperty.set(newValue) // set for Exposed Entity
        TaskChangeEventTable.insert {
            it[this.fieldName] = entityProperty.name
            it[this.oldValue] = oldValue.toString()
            it[this.newValue] = newValue.toString()
            it[this.commitUUID] = commitUUID // сетаем value для связи one-to-many commit-changeEvent
        }
    }
}