/*
 * Copyright 2023 Pyrrha Wills
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gay.pyrrha.demontech

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import java.lang.System.getenv
import java.util.*

private const val RUN_NUMBER_VAR = "GITHUB_RUN_NUMBER"

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

val isCI
    get(): Boolean = getenv("CI") != null

val buildNumber
    get(): String = if (isCI && getenv(RUN_NUMBER_VAR) != null) {
        getenv(RUN_NUMBER_VAR)
    } else {
        "local"
    }

val Project.buildInfo
    get(): String = if(buildNumber == "local")
        libs.findVersion("minecraft").get().requiredVersion
    else
        "build.$buildNumber-${libs.findVersion("minecraft").get().requiredVersion}"

fun String?.base64Decode(): String? =
    if (this != null) String(Base64.getDecoder().decode(this)) else null


