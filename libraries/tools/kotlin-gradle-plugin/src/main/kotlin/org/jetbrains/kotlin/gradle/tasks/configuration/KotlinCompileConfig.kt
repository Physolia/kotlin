/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.tasks.configuration

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Attribute
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.internal.transforms.ClasspathEntrySnapshotTransform
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.KotlinCompileOptionsImpl
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinWithJavaCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.KotlinCompilationData
import org.jetbrains.kotlin.gradle.plugin.registerSubpluginOptionsAsInputs
import org.jetbrains.kotlin.gradle.tasks.CompilerPluginOptions
import org.jetbrains.kotlin.gradle.tasks.KOTLIN_BUILD_DIR_NAME
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.utils.property

internal typealias KotlinCompileConfig = BaseKotlinCompileConfig<KotlinCompile>

internal open class BaseKotlinCompileConfig<TASK : KotlinCompile> : AbstractKotlinCompileConfig<TASK> {

    constructor(compilation: KotlinCompilationData<*>, taskName: String = compilation.compileKotlinTaskName) : super(compilation, taskName) {
        val javaTaskProvider = when (compilation) {
            is KotlinJvmCompilation -> compilation.compileJavaTaskProvider
            is KotlinJvmAndroidCompilation -> compilation.compileJavaTaskProvider
            is KotlinWithJavaCompilation<*> -> compilation.compileJavaTaskProvider
            else -> null
        }

        javaTaskProvider?.let {
            associatedJavaCompileTaskTargetCompatibility.value(javaTaskProvider.map { it.targetCompatibility })
            associatedJavaCompileTaskSources.from(javaTaskProvider.map { it.source })
            associatedJavaCompileTaskName.value(javaTaskProvider.name)
        }
        moduleName.value(providers.provider {
            (compilation.kotlinOptions as? KotlinJvmOptions)?.moduleName ?: parentKotlinOptions.orNull?.moduleName ?: compilation.moduleName
        })
    }

    constructor(project: Project, configuration: KotlinCompileOptionsImpl, ext: KotlinTopLevelExtension) : super(
        project, configuration, ext
    ) {
        parentKotlinOptions.set(configuration.parentKotlinOptions)
        additionalPluginOptions.set(configuration.additionalPluginOptions.map { externalOptions ->
            externalOptions.map { CompilerPluginOptions(it) }
        })
    }

    companion object {
        private const val TRANSFORMS_REGISTERED = "_kgp_internal_kotlin_compile_transforms_registered"

        val ARTIFACT_TYPE_ATTRIBUTE: Attribute<String> = Attribute.of("artifactType", String::class.java)
        private const val DIRECTORY_ARTIFACT_TYPE = "directory"
        private const val JAR_ARTIFACT_TYPE = "jar"
        const val CLASSPATH_ENTRY_SNAPSHOT_ARTIFACT_TYPE = "classpath-entry-snapshot"
    }

    private val classpathSnapshotConfiguration: Configuration?

    init {
        /**
         * Prepares for configuration of the task. This method must be called during build configuration, not during task configuration
         * (which typically happens after build configuration). The reason is that some actions must be performed early (e.g., creating
         * configurations should be done early to avoid issues with composite builds (https://issuetracker.google.com/183952598)).
         */
        classpathSnapshotConfiguration = if (propertiesProvider.useClasspathSnapshot) {
            registerTransformsOnce(project)
            project.configurations.detachedConfiguration(project.dependencies.create(classpath))
        } else null
    }

    private fun registerTransformsOnce(project: Project) {
        if (project.extensions.extraProperties.has(TRANSFORMS_REGISTERED)) {
            return
        }
        project.extensions.extraProperties[TRANSFORMS_REGISTERED] = true

        project.dependencies.registerTransform(ClasspathEntrySnapshotTransform::class.java) {
            it.from.attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_ARTIFACT_TYPE)
            it.to.attribute(ARTIFACT_TYPE_ATTRIBUTE, CLASSPATH_ENTRY_SNAPSHOT_ARTIFACT_TYPE)
        }
        project.dependencies.registerTransform(ClasspathEntrySnapshotTransform::class.java) {
            it.from.attribute(ARTIFACT_TYPE_ATTRIBUTE, DIRECTORY_ARTIFACT_TYPE)
            it.to.attribute(ARTIFACT_TYPE_ATTRIBUTE, CLASSPATH_ENTRY_SNAPSHOT_ARTIFACT_TYPE)
        }
    }

    override val incremental: Property<Boolean> = objectFactory.property(propertiesProvider.incrementalJvm ?: true)

    val associatedJavaCompileTaskTargetCompatibility: Property<String> = objectFactory.property<String>()

    val associatedJavaCompileTaskSources: ConfigurableFileCollection = objectFactory.fileCollection()

    val associatedJavaCompileTaskName: Property<String> = objectFactory.property<String>()

    internal val jvmTargetValidationMode: Property<PropertiesProvider.JvmTargetValidationMode> =
        objectFactory.property(propertiesProvider.jvmTargetValidationMode)

    open val classpathSnapshotDir: DirectoryProperty
        get() = objectFactory.directoryProperty().value(
            project.layout.buildDirectory.dir("$KOTLIN_BUILD_DIR_NAME/classpath-snapshot/${taskName}")
        )

    val classpathSnapshotFiles: FileCollection? by lazy {
        classpathSnapshotConfiguration ?: return@lazy null
        return@lazy classpathSnapshotConfiguration.incoming.artifactView {
            it.attributes.attribute(
                ARTIFACT_TYPE_ATTRIBUTE, CLASSPATH_ENTRY_SNAPSHOT_ARTIFACT_TYPE
            )
        }.files
    }

    val useClasspathSnapshot: Property<Boolean> = objectFactory.property(propertiesProvider.useClasspathSnapshot)

    val javaPackagePrefix: Property<String> = objectFactory.property()

    val usePreciseJavaTracking: Property<Boolean> = objectFactory.property(propertiesProvider.usePreciseJavaTracking ?: true)

    val useFir: Property<Boolean> = objectFactory.property(propertiesProvider.useFir)

    val parentKotlinOptions: Property<KotlinJvmOptions> = objectFactory.property()

    val additionalPluginOptions: ListProperty<CompilerPluginOptions> = objectFactory.listProperty(CompilerPluginOptions::class.java)

    fun addSubpluginOptions(pluginId: String, options: Provider<List<SubpluginOption>>) {
        additionalPluginOptions.add(options.map { subpluginOptions ->
            val compilerPluginOptions = CompilerPluginOptions()
            compilerPluginOptions.subpluginOptionsByPluginId[pluginId] = subpluginOptions.toMutableList()
            compilerPluginOptions
        })
    }

    override fun configureTask(taskProvider: TaskProvider<TASK>) {
        super.configureTask(taskProvider)

        taskProvider.configure { task ->
            registerSubpluginOptions(additionalPluginOptions, task.pluginOptions, task)
            task.associatedJavaCompileTaskTargetCompatibility.set(associatedJavaCompileTaskTargetCompatibility)
            task.associatedJavaCompileTaskSources.from(associatedJavaCompileTaskSources)
            task.associatedJavaCompileTaskName.set(associatedJavaCompileTaskName)
            task.jvmTargetValidationMode.set(jvmTargetValidationMode)
            task.parentKotlinOptionsImpl.set(parentKotlinOptions)

            task.classpathSnapshotProperties.useClasspathSnapshot.value(useClasspathSnapshot).disallowChanges()
            if (useClasspathSnapshot.getOrElse(false)) {
                task.classpathSnapshotProperties.classpathSnapshot.from(classpathSnapshotFiles).disallowChanges()
                task.classpathSnapshotProperties.classpathSnapshotDir.value(classpathSnapshotDir).disallowChanges()
            } else {
                task.classpathSnapshotProperties.classpath.from(task.project.provider { task.classpath })
            }
            task.javaPackagePrefix = javaPackagePrefix.orNull
            task.usePreciseJavaTracking = usePreciseJavaTracking.get()
            if (useFir.getOrElse(false)) {
                task.kotlinOptions.useFir = true
            }
        }
    }
}

internal fun registerSubpluginOptions(
    pluginOptions: ListProperty<CompilerPluginOptions>,
    taskPluginOptions: CompilerPluginOptions,
    task: Task
) {
    pluginOptions.get().forEach {
        it.subpluginOptionsByPluginId.forEach { (id, subpluginOptions) ->
            task.registerSubpluginOptionsAsInputs(id, subpluginOptions)
            for (option in subpluginOptions) {
                taskPluginOptions.addPluginArgument(id, option)
            }
        }
    }
}
