import org.gradle.api.JavaVersion

public object Build {
    public const val NAMESPACE: String = "com.example.instagramfeedpreview"

    public const val COMPILE_SDK: Int = 33
    public const val MIN_SDK: Int = 21
    public const val TARGET_SDK: Int = 33

    public val SOURCE_COMPATIBILITY: JavaVersion = JavaVersion.VERSION_17
    public val TARGET_COMPATIBILITY: JavaVersion = JavaVersion.VERSION_17
    public val JVM_TARGET: String = JavaVersion.VERSION_17.toString()
}
