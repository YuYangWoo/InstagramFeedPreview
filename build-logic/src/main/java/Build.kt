import org.gradle.api.JavaVersion

object Build {
    const val COMPILE_SDK: Int = 33
    const val MIN_SDK: Int = 24
    const val TARGET_SDK: Int = 33

    val SOURCE_COMPATIBILITY: JavaVersion = JavaVersion.VERSION_17
    val TARGET_COMPATIBILITY: JavaVersion = JavaVersion.VERSION_17
    val JVM_TARGET: String = JavaVersion.VERSION_17.toString()
}
