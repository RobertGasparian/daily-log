 - "The main activity is com.robgasp.dailylog.MainActivity.kt."
 - "Navigation is handled in file com.robgasp.dailylog.navigation.Navigation.kt"
Project architecture:
 - "We are using clean architecture but in single module, so please separate code in different packages"
 - "Always follow official architecture recommendations, including use of a layered architecture. Use a unidirectional data flow (UDF), ViewModels, lifecycle-aware UI state collection, and other recommendations."
Preferred libraries: 
 - "Prioritise Kotlin based libraries and use Kotlin language features/classes instead of Java."
 - "Navigation is handled by Navigation 3 library"
 - "DI is handled by Hilt"
 - "Database is handled by Room"
 - "For codestyle rules we are using org.jlleitschuh.gradle.ktlint and the rules are in .editorconfig file"