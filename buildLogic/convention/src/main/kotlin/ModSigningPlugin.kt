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

import gay.pyrrha.demontech.base64Decode
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.plugins.signing.SigningExtension

class ModSigningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("signing")
            }

            extensions.configure<SigningExtension> {
                sign(tasks.getByName("remapJar"))
                val signingKeyId: String? by project
                val signingKey: String? by project
                val signingPassword: String? by project
                useInMemoryPgpKeys(signingKeyId, signingKey.base64Decode(), signingPassword.base64Decode())

                isRequired = System.getenv("CI") != null && System.getenv("GITHUB_JOB") == "build_push"
            }
        }
    }
}
