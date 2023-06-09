package solutions.dft

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import solutions.dft.config.KoinBeanConfig

@Module(includes = [KoinBeanConfig::class])
@ComponentScan
class KoinEntryPointModule