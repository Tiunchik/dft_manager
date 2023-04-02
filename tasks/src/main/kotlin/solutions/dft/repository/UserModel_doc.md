TODO - impl - extension property for entityId -> userId & user(full model)
-- for init from DB
-- for init from External source(HTTP, Kafka, ...)
TODO - impl - extension property for list -> get comments<T : Iterable>()
* T : Iterable - easy map to: List, Set, Queue, Map(List<Pair>)
-- for init from DB
-- for init from External source(HTTP, Kafka, ...)
TODO ideas:
- может быть нас спасут просто by lazy или кастомные Делегаты???
- ? сделать кастомный lateinit для primitive - хотя сомнительно
/**
* impl note:
* in sql -> use other table relationship with many-to-many
* in equals&hashCode -> ignore field
* in toString -> ignore field because recursion and risk of reference-cycle(this-child-this)
* ? maybe good idea - make it extension field in code?
* in code -> suspend extension Property with cache in model like _linkedTasks {get} from C#
  */
  var linkedTasks: MutableList<Task> = mutableListOf()
  var attachedFiles: MutableList<Any> = mutableListOf() // todo - attached files
  var comments: MutableList<Any> = mutableListOf() // todo - impl model Comment
  var changeHistory: MutableList<Long> = mutableListOf() // todo - impl model TaskChangeEvent
  /**
* impl note:
* in code -> suspend extension Property with cache in model like _linkedTasks {get} from C#
  */
  // todo - relationships
  // ? comments
  // ? tags [project, release, etc]
  // ? Type [feature, bug, ...]
  // ? клонировано от
  // ? блокирующую другие таски