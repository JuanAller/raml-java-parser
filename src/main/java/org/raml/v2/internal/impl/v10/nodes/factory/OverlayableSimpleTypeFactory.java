/*
 * Copyright 2013 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.v2.internal.impl.v10.nodes.factory;

import javax.annotation.Nonnull;

import org.raml.v2.internal.framework.grammar.rule.NodeFactory;
import org.raml.v2.internal.framework.nodes.KeyValueNodeImpl;
import org.raml.v2.internal.framework.nodes.Node;
import org.raml.v2.internal.framework.nodes.SimpleTypeNode;
import org.raml.v2.internal.framework.nodes.StringNodeImpl;
import org.raml.v2.internal.impl.commons.nodes.ObjectNodeImpl;
import org.raml.v2.internal.impl.commons.nodes.OverlayableNode;
import org.raml.v2.internal.impl.commons.nodes.OverlayableObjectNodeImpl;
import org.raml.v2.internal.impl.commons.nodes.OverlayableStringNode;


public class OverlayableSimpleTypeFactory implements NodeFactory
{

    private Boolean wrappInObject;

    public OverlayableSimpleTypeFactory(Boolean wrappInObject)
    {
        this.wrappInObject = wrappInObject;
    }

    @Override
    public Node create(@Nonnull Node currentNode, Object... args)
    {
        if (!(currentNode instanceof SimpleTypeNode))
        {
            throw new IllegalStateException("Factory incompatible with node of type " + currentNode.getClass().getSimpleName());
        }
        if (currentNode instanceof OverlayableNode)
        {
            return currentNode;
        }
        if (wrappInObject)
        {
            ObjectNodeImpl result = new OverlayableObjectNodeImpl();
            OverlayableStringNode overlayableStringNode = new OverlayableStringNode(((SimpleTypeNode) currentNode).getLiteralValue());
            KeyValueNodeImpl keyValueNode = new KeyValueNodeImpl(new StringNodeImpl("value"), overlayableStringNode);
            if (currentNode.getParent() != null)
            {
                keyValueNode.setStartPosition(currentNode.getParent().getStartPosition());
                keyValueNode.setEndPosition(currentNode.getParent().getEndPosition());
            }
            result.addChild(keyValueNode);
            return result;
        }
        else
        {
            return new OverlayableStringNode(((SimpleTypeNode) currentNode).getLiteralValue());
        }
    }
}
