/*
 * Copyright (C) 2012 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.swift.generator;

import com.google.common.base.Preconditions;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SwiftGeneratorConfig
{
    private final URI inputBase;
    private final Set<URI> inputs;
    private final File outputFolder;
    private final String overridePackage;
    private final String defaultPackage;
    private final boolean addThriftExceptions;
    private final boolean generateIncludedCode;
    private final String codeFlavor;

    private SwiftGeneratorConfig(final URI inputBase,
                                 final Set<URI> inputs,
                                 final File outputFolder,
                                 final String overridePackage,
                                 final String defaultPackage,
                                 final boolean addThriftExceptions,
                                 final boolean generateIncludedCode,
                                 final String codeFlavor)
    {
        this.inputBase = inputBase;
        this.inputs = inputs;
        this.outputFolder = outputFolder;
        this.overridePackage = overridePackage;
        this.defaultPackage = defaultPackage;
        this.addThriftExceptions = addThriftExceptions;
        this.generateIncludedCode = generateIncludedCode;
        this.codeFlavor = codeFlavor;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Returns the input base URI to load Thrift IDL files.
     */
    public URI getInputBase()
    {
        return inputBase;
    }

    /**
     * Returns an array of input URIs to read. If any of the URIs is not absolute, an input base must be set.
     */
    public Set<URI> getInputs()
    {
        return inputs;
    }

    /**
     * Returns the output folder which will contain the generated sources.
     */
    public File getOutputFolder()
    {
        return outputFolder;
    }

    /**
     * If non-null, overrides the java namespace definitions in the IDL files.
     */
    public String getOverridePackage()
    {
        return overridePackage;
    }

    /**
     * If no namespace was set in the Thrift IDL file, fall back to this package.
     */
    public String getDefaultPackage()
    {
        return defaultPackage;
    }

    /**
     * If true, adds {@link org.apache.thrift.TException} to the method signature
     * of all generated service methods.
     */
    public boolean isAddThriftExceptions()
    {
        return addThriftExceptions;
    }

    /**
     * If true, generate code for all included Thrift IDLs instead of just referring to
     * them.
     */
    public boolean isGenerateIncludedCode()
    {
        return generateIncludedCode;
    }

    /**
     * The template to use for generating source code.
     */
    public String getCodeFlavor()
    {
        return codeFlavor;
    }

    public static class Builder
    {
        private URI inputBase = null;
        private final Set<URI> inputs = new HashSet<>();
        private File outputFolder = null;
        private String overridePackage = null;
        private String defaultPackage = null;
        private boolean addThriftExceptions = true;
        private boolean generateIncludedCode = false;
        private String codeFlavor = null;

        private Builder()
        {
        }

        public SwiftGeneratorConfig build()
        {
            Preconditions.checkState(outputFolder != null, "output folder must be set!");
            Preconditions.checkState(inputs.size() > 0, "no input files given!");

            Preconditions.checkState(inputBase != null, "input base uri must be set to load includes!");
            Preconditions.checkState(codeFlavor != null, "no code flavor selected!");

            return new SwiftGeneratorConfig(
                inputBase,
                inputs,
                outputFolder,
                overridePackage,
                defaultPackage,
                addThriftExceptions,
                generateIncludedCode,
                codeFlavor);
        }

        public Builder inputBase(final URI inputBase)
        {
            this.inputBase = inputBase;
            return this;
        }

        public Builder outputFolder(final File outputFolder)
        {
            this.outputFolder = outputFolder;
            return this;
        }

        public Builder addInputs(final URI ... inputs)
        {
            return addInputs(Arrays.asList(inputs));
        }

        public Builder addInputs(final Iterable<URI> inputs)
        {
            for (URI input : inputs) {
                this.inputs.add(input);
            }

            return this;
        }

        public Builder overridePackage(final String overridePackage)
        {
            this.overridePackage = overridePackage;
            return this;
        }

        public Builder defaultPackage(final String defaultPackage)
        {
            this.defaultPackage = defaultPackage;
            return this;
        }

        public Builder clearAddThriftExceptions()
        {
            this.addThriftExceptions = false;
            return this;
        }

        public Builder addThriftExceptions(final boolean addThriftExceptions)
        {
            this.addThriftExceptions = addThriftExceptions;
            return this;
        }

        public Builder generateIncludedCode(final boolean generateIncludedCode)
        {
            this.generateIncludedCode = generateIncludedCode;
            return this;
        }

        public Builder setGenerateIncludedCode()
        {
            this.generateIncludedCode = true;
            return this;
        }

        public Builder codeFlavor(final String codeFlavor)
        {
            this.codeFlavor = codeFlavor;
            return this;
        }
    }
}