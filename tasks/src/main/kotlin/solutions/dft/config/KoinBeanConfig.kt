package solutions.dft.config

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import solutions.dft.repository.TaskConverter

@Module
class KoinBeanConfig {

    @Single fun taskConverter(): TaskConverter = Mappers.getMapper(TaskConverter::class.java)

}