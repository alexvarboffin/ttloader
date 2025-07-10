package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.artifacts.dependencies.ProjectDependencyInternal;
import org.gradle.api.internal.artifacts.DefaultProjectDependencyFactory;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.catalog.DelegatingProjectDependency;
import org.gradle.api.internal.catalog.TypeSafeProjectDependencyFactory;
import javax.inject.Inject;

@NonNullApi
public class TtloaderProjectDependency extends DelegatingProjectDependency {

    @Inject
    public TtloaderProjectDependency(TypeSafeProjectDependencyFactory factory, ProjectDependencyInternal delegate) {
        super(factory, delegate);
    }

    /**
     * Creates a project dependency on the project at path ":Desktop"
     */
    public DesktopProjectDependency getDesktop() { return new DesktopProjectDependency(getFactory(), create(":Desktop")); }

    /**
     * Creates a project dependency on the project at path ":app"
     */
    public AppProjectDependency getApp() { return new AppProjectDependency(getFactory(), create(":app")); }

    /**
     * Creates a project dependency on the project at path ":features"
     */
    public FeaturesProjectDependency getFeatures() { return new FeaturesProjectDependency(getFactory(), create(":features")); }

    /**
     * Creates a project dependency on the project at path ":intentresolver"
     */
    public IntentresolverProjectDependency getIntentresolver() { return new IntentresolverProjectDependency(getFactory(), create(":intentresolver")); }

    /**
     * Creates a project dependency on the project at path ":service"
     */
    public ServiceProjectDependency getService() { return new ServiceProjectDependency(getFactory(), create(":service")); }

    /**
     * Creates a project dependency on the project at path ":shared"
     */
    public SharedProjectDependency getShared() { return new SharedProjectDependency(getFactory(), create(":shared")); }

    /**
     * Creates a project dependency on the project at path ":threader"
     */
    public ThreaderProjectDependency getThreader() { return new ThreaderProjectDependency(getFactory(), create(":threader")); }

}
