rootProject.name = "Free World"
//include(":app")

include(":desktop")
include(":android")
include(":html")
include(":utils")
include(":minicraft")
include(":tetris")
include(":dungeoncrawler")
include(":superjumper")

apply(from = "features/modules.gradle.kts")