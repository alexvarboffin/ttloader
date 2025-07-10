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
public class FeaturesProjectDependency extends DelegatingProjectDependency {

    @Inject
    public FeaturesProjectDependency(TypeSafeProjectDependencyFactory factory, ProjectDependencyInternal delegate) {
        super(factory, delegate);
    }

    /**
     * Creates a project dependency on the project at path ":features:permissionResolver"
     */
    public Features_PermissionResolverProjectDependency getPermissionResolver() { return new Features_PermissionResolverProjectDependency(getFactory(), create(":features:permissionResolver")); }

    /**
     * Creates a project dependency on the project at path ":features:ui"
     */
    public Features_UiProjectDependency getUi() { return new Features_UiProjectDependency(getFactory(), create(":features:ui")); }

    /**
     * Creates a project dependency on the project at path ":features:wads"
     */
    public Features_WadsProjectDependency getWads() { return new Features_WadsProjectDependency(getFactory(), create(":features:wads")); }

}
