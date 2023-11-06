import org.gradle.api.JavaVersion

object Build {
    const val NAMESPACE: String = "com.example.instagramfeedpreview"

    const val COMPILE_SDK: Int = 33
    const val MIN_SDK: Int = 21
    const val TARGET_SDK: Int = 33

    val SOURCE_COMPATIBILITY: JavaVersion = JavaVersion.VERSION_17
    val TARGET_COMPATIBILITY: JavaVersion = JavaVersion.VERSION_17
    val JVM_TARGET: String = JavaVersion.VERSION_17.toString()
}
