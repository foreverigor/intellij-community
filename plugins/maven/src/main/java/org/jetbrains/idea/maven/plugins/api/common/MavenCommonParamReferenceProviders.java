/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.idea.maven.plugins.api.common;

import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.dom.model.MavenDomConfiguration;
import org.jetbrains.idea.maven.dom.references.MavenDependencyReferenceProvider;
import org.jetbrains.idea.maven.dom.references.MavenPathReferenceConverter;
import org.jetbrains.idea.maven.plugins.api.MavenParamReferenceProvider;

/**
 * @author Sergey Evdokimov
 */
public class MavenCommonParamReferenceProviders {

  private MavenCommonParamReferenceProviders() {
  }

  public static class FilePath implements MavenParamReferenceProvider {
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                 @NotNull MavenDomConfiguration domCfg,
                                                 @NotNull ProcessingContext context) {
      return MavenPathReferenceConverter.createReferences(domCfg, element, Condition.TRUE);
    }
  }

  public static class DirPath implements MavenParamReferenceProvider {
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                 @NotNull MavenDomConfiguration domCfg,
                                                 @NotNull ProcessingContext context) {
      return MavenPathReferenceConverter.createReferences(domCfg, element, FileReferenceSet.DIRECTORY_FILTER);
    }
  }

  public static class DependencyWithoutVersion extends MavenDependencyReferenceProvider {
    public DependencyWithoutVersion() {
      setCanHasVersion(false);
    }
  }

}
