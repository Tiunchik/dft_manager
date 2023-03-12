group = "dft.solutions"
version = "1.0"

registerTaskForIncludedBuilds("build", "build")

fun registerTaskForIncludedBuilds(task: String, taskGroup: String) {
    tasks.register(task) {
        group = taskGroup

        gradle.includedBuilds
            .forEach {
                if (it.projectDir.startsWith(project.projectDir)) dependsOn(it.task(":$task"))
            }
    }
}